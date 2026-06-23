package org.yuemi.libs.plugin.gui;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.yuemi.libs.api.gui.AnvilInputBuilder;
import org.yuemi.libs.api.gui.ClosePolicy;
import org.yuemi.libs.api.gui.SignInputBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TextInputIntegrationTest {

    private GuiListener listener;
    private Player mockPlayer;
    private UUID playerUuid;
    private InventoryView mockView;
    private Inventory mockInventory;
    private Plugin mockPlugin;
    private Server mockServer;
    private BukkitScheduler mockScheduler;

    @BeforeEach
    public void setUp() {
        mockPlugin = mock(Plugin.class);
        listener = new GuiListener(mockPlugin);
        mockPlayer = mock(Player.class);
        playerUuid = UUID.randomUUID();
        when(mockPlayer.getUniqueId()).thenReturn(playerUuid);

        mockView = mock(InventoryView.class);
        when(mockView.getPlayer()).thenReturn(mockPlayer);

        mockInventory = mock(Inventory.class);
        when(mockInventory.getSize()).thenReturn(9);
        when(mockView.getTopInventory()).thenReturn(mockInventory);
        
        when(mockView.getInventory(anyInt())).thenAnswer(invocation -> {
            int slot = invocation.getArgument(0);
            if (slot >= 0 && slot < 9) {
                return mockInventory;
            }
            return null;
        });

        // Setup mock server for scheduler task scheduling
        mockServer = mock(Server.class);
        mockScheduler = mock(BukkitScheduler.class);
        when(mockServer.getScheduler()).thenReturn(mockScheduler);
        setBukkitServer(mockServer);
    }

    private void setBukkitServer(Server server) {
        try {
            java.lang.reflect.Field field = Bukkit.class.getDeclaredField("server");
            field.setAccessible(true);
            field.set(null, server);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
        // Reset the server to prevent leakage across tests
        setBukkitServer(null);
    }

    @Test
    public void testAnvilSubmitWithCharacterLimit() {
        AtomicReference<String> submittedText = new AtomicReference<>();
        AnvilInputBuilder builder = new AnvilInputBuilderImpl()
                .initialText("hello")
                .maxLength(5)
                .onSubmit((player, text) -> submittedText.set(text));

        AnvilInputHolder holder = new AnvilInputHolder((AnvilInputBuilderImpl) builder);
        when(mockInventory.getHolder()).thenReturn(holder);
        holder.setInventory(mockInventory);

        // Mock result item in slot 2 containing a 10 character name
        ItemStack mockItem = mock(ItemStack.class);
        ItemMeta mockMeta = mock(ItemMeta.class);
        when(mockItem.hasItemMeta()).thenReturn(true);
        when(mockItem.getItemMeta()).thenReturn(mockMeta);
        when(mockMeta.hasDisplayName()).thenReturn(true);
        when(mockMeta.getDisplayName()).thenReturn("superlongtext"); // 13 chars
        when(mockInventory.getItem(2)).thenReturn(mockItem);

        InventoryClickEvent clickEvent = new InventoryClickEvent(
                mockView,
                InventoryType.SlotType.RESULT,
                2,
                ClickType.LEFT,
                InventoryAction.PICKUP_ALL
        );
        clickEvent.setCancelled(false);

        // Click slot 2
        listener.onInventoryClick(clickEvent);

        assertTrue(clickEvent.isCancelled());
        assertEquals("super", submittedText.get()); // Truncated to 5
        verify(mockPlayer).closeInventory();
    }

    @Test
    public void testAnvilLockInputSlots() {
        AnvilInputBuilder builder = new AnvilInputBuilderImpl();
        AnvilInputHolder holder = new AnvilInputHolder((AnvilInputBuilderImpl) builder);
        when(mockInventory.getHolder()).thenReturn(holder);
        holder.setInventory(mockInventory);

        // Simulate click slot 0 (left slot)
        InventoryClickEvent clickEvent0 = new InventoryClickEvent(
                mockView,
                InventoryType.SlotType.CONTAINER,
                0,
                ClickType.LEFT,
                InventoryAction.PLACE_ALL
        );
        listener.onInventoryClick(clickEvent0);
        assertTrue(clickEvent0.isCancelled());

        // Simulate click slot 1 (right slot)
        InventoryClickEvent clickEvent1 = new InventoryClickEvent(
                mockView,
                InventoryType.SlotType.CONTAINER,
                1,
                ClickType.LEFT,
                InventoryAction.PLACE_ALL
        );
        listener.onInventoryClick(clickEvent1);
        assertTrue(clickEvent1.isCancelled());
    }

    @Test
    public void testAnvilCloseTriggersCallback() {
        AtomicReference<Boolean> closed = new AtomicReference<>(false);
        AnvilInputBuilder builder = new AnvilInputBuilderImpl()
                .onClose(player -> closed.set(true));

        AnvilInputHolder holder = new AnvilInputHolder((AnvilInputBuilderImpl) builder);
        when(mockInventory.getHolder()).thenReturn(holder);
        holder.setInventory(mockInventory);

        InventoryCloseEvent closeEvent = new InventoryCloseEvent(mockView);
        listener.onInventoryClose(closeEvent);

        assertTrue(closed.get());
    }

    @Test
    public void testAnvilClosePolicyReopen() {
        AnvilInputBuilder builder = new AnvilInputBuilderImpl()
                .closePolicy(ClosePolicy.REOPEN);

        AnvilInputHolder holder = new AnvilInputHolder((AnvilInputBuilderImpl) builder);
        when(mockInventory.getHolder()).thenReturn(holder);
        holder.setInventory(mockInventory);

        InventoryCloseEvent closeEvent = new InventoryCloseEvent(mockView);
        listener.onInventoryClose(closeEvent);

        // Verify task was scheduled to reopen
        verify(mockScheduler).runTask(eq(mockPlugin), any(Runnable.class));
    }

    @Test
    public void testSignSubmitAndRestore() {
        AtomicReference<String> submittedText = new AtomicReference<>();
        Location mockLocation = mock(Location.class);
        Block mockBlock = mock(Block.class);
        when(mockLocation.getBlock()).thenReturn(mockBlock);
        
        BlockState mockState = mock(BlockState.class);

        SignInputBuilderImpl.SignSession session = new SignInputBuilderImpl.SignSession(
                mockLocation,
                mockState,
                (player, text) -> submittedText.set(text),
                10,
                ClosePolicy.CLOSE,
                "-------------",
                mock(SignInputBuilderImpl.class)
        );

        SignInputBuilderImpl.ACTIVE_SESSIONS.put(mockPlayer, session);

        Block signBlock = mock(Block.class);
        String[] lines = new String[]{"-------------", "HelloWorldsLimit", "-------------", "description"};
        SignChangeEvent event = new SignChangeEvent(signBlock, mockPlayer, lines);

        listener.onSignChange(event);

        assertTrue(event.isCancelled());
        assertEquals("HelloWorld", submittedText.get()); // Truncated to 10 chars
        assertFalse(SignInputBuilderImpl.ACTIVE_SESSIONS.containsKey(mockPlayer));
        verify(mockBlock).setBlockData(any(), eq(false));
    }

    @Test
    public void testSignReopenIfCancelled() {
        SignInputBuilderImpl mockBuilder = mock(SignInputBuilderImpl.class);
        Location mockLocation = mock(Location.class);
        Block mockBlock = mock(Block.class);
        when(mockLocation.getBlock()).thenReturn(mockBlock);
        BlockState mockState = mock(BlockState.class);

        SignInputBuilderImpl.SignSession session = new SignInputBuilderImpl.SignSession(
                mockLocation,
                mockState,
                (player, text) -> {},
                10,
                ClosePolicy.REOPEN,
                "initialText",
                mockBuilder
        );

        SignInputBuilderImpl.ACTIVE_SESSIONS.put(mockPlayer, session);

        Block signBlock = mock(Block.class);
        // lines has "initialText" unchanged
        String[] lines = new String[]{"-------------", "initialText", "-------------", "description"};
        SignChangeEvent event = new SignChangeEvent(signBlock, mockPlayer, lines);

        listener.onSignChange(event);

        assertTrue(event.isCancelled());
        verify(mockScheduler).runTask(eq(mockPlugin), any(Runnable.class));
    }
}
