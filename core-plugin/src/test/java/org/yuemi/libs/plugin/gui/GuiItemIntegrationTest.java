package org.yuemi.libs.plugin.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.yuemi.libs.api.gui.GuiItem;
import org.yuemi.libs.api.items.ItemsApi;
import org.yuemi.libs.api.items.ItemsApiProvider;
import static org.junit.jupiter.api.Assertions.*;

public class GuiItemIntegrationTest {

    private ItemsApi mockItemsApi;
    private Player mockPlayer;

    @BeforeEach
    public void setUp() {
        mockItemsApi = Mockito.mock(ItemsApi.class);
        mockPlayer = Mockito.mock(Player.class);
        ItemsApiProvider.register(mockItemsApi);
    }

    @AfterEach
    public void tearDown() {
        ItemsApiProvider.register(null);
    }

    @Test
    public void testItemWithStaticKey() {
        ItemStack testItem = Mockito.mock(ItemStack.class);
        Mockito.when(testItem.getType()).thenReturn(Material.DIAMOND_SWORD);
        Mockito.when(mockItemsApi.getItem("minecraft:diamond_sword", 1)).thenReturn(testItem);

        GuiItem guiItem = new GuiItemImpl.BuilderImpl()
                .item("minecraft:diamond_sword")
                .build();

        ItemStack resolved = guiItem.getItemStack(mockPlayer);
        assertNotNull(resolved);
        assertEquals(Material.DIAMOND_SWORD, resolved.getType());
        Mockito.verify(mockItemsApi).getItem("minecraft:diamond_sword", 1);
    }

    @Test
    public void testItemWithStaticKeyAndAmount() {
        ItemStack testItem = Mockito.mock(ItemStack.class);
        Mockito.when(testItem.getType()).thenReturn(Material.APPLE);
        Mockito.when(testItem.getAmount()).thenReturn(5);
        Mockito.when(mockItemsApi.getItem("minecraft:apple", 5)).thenReturn(testItem);

        GuiItem guiItem = new GuiItemImpl.BuilderImpl()
                .item("minecraft:apple", 5)
                .build();

        ItemStack resolved = guiItem.getItemStack(mockPlayer);
        assertNotNull(resolved);
        assertEquals(Material.APPLE, resolved.getType());
        assertEquals(5, resolved.getAmount());
        Mockito.verify(mockItemsApi).getItem("minecraft:apple", 5);
    }

    @Test
    public void testItemWithDynamicKey() {
        ItemStack testItem = Mockito.mock(ItemStack.class);
        Mockito.when(testItem.getType()).thenReturn(Material.GOLDEN_APPLE);
        Mockito.when(mockItemsApi.getItem("minecraft:golden_apple", 1)).thenReturn(testItem);

        GuiItem guiItem = new GuiItemImpl.BuilderImpl()
                .itemKey(player -> "minecraft:golden_apple")
                .build();

        ItemStack resolved = guiItem.getItemStack(mockPlayer);
        assertNotNull(resolved);
        assertEquals(Material.GOLDEN_APPLE, resolved.getType());
        Mockito.verify(mockItemsApi).getItem("minecraft:golden_apple", 1);
    }

    @Test
    public void testItemWithDynamicKeyAndAmount() {
        ItemStack testItem = Mockito.mock(ItemStack.class);
        Mockito.when(testItem.getType()).thenReturn(Material.COOKED_BEEF);
        Mockito.when(testItem.getAmount()).thenReturn(3);
        Mockito.when(mockItemsApi.getItem("minecraft:cooked_beef", 3)).thenReturn(testItem);

        GuiItem guiItem = new GuiItemImpl.BuilderImpl()
                .itemKey(player -> "minecraft:cooked_beef", 3)
                .build();

        ItemStack resolved = guiItem.getItemStack(mockPlayer);
        assertNotNull(resolved);
        assertEquals(Material.COOKED_BEEF, resolved.getType());
        assertEquals(3, resolved.getAmount());
        Mockito.verify(mockItemsApi).getItem("minecraft:cooked_beef", 3);
    }

    @Test
    public void testItemKeyWithoutProviderThrows() {
        ItemsApiProvider.register(null);

        GuiItem guiItem = new GuiItemImpl.BuilderImpl()
                .item("minecraft:stone")
                .build();

        assertThrows(IllegalStateException.class, () -> {
            guiItem.getItemStack(mockPlayer);
        });
    }
}
