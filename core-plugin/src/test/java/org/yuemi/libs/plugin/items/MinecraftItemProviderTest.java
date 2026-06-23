package org.yuemi.libs.plugin.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MinecraftItemProviderTest {

    private MinecraftItemProvider provider;
    private ItemStack mockItem;
    private ItemMeta mockMeta;

    @BeforeEach
    public void setUp() {
        provider = spy(new MinecraftItemProvider(MatchMode.CUSTOM_MODEL_DATA));
        mockItem = mock(ItemStack.class);
        mockMeta = mock(ItemMeta.class);

        when(mockItem.hasItemMeta()).thenReturn(true);
        when(mockItem.getItemMeta()).thenReturn(mockMeta);
    }

    @Test
    public void testGetName() {
        assertEquals("Minecraft", provider.getName());
        assertTrue(provider.isAvailable());
    }

    @Test
    public void testGetItemCountWithMockedItems() {
        // Stub getItem to avoid calling Material.matchMaterial / Bukkit registries
        doReturn(mockItem).when(provider).getItem(eq("stone"), anyInt());

        org.bukkit.entity.Player mockPlayer = mock(org.bukkit.entity.Player.class);
        org.bukkit.inventory.PlayerInventory mockInventory = mock(org.bukkit.inventory.PlayerInventory.class);
        
        when(mockPlayer.getInventory()).thenReturn(mockInventory);
        
        // Setup inventory contents
        ItemStack matchedItem = mock(ItemStack.class);
        when(matchedItem.getType()).thenReturn(Material.STONE);
        when(matchedItem.getAmount()).thenReturn(10);
        when(matchedItem.hasItemMeta()).thenReturn(true);
        
        ItemMeta matchedMeta = mock(ItemMeta.class);
        when(matchedMeta.hasCustomModelData()).thenReturn(false);
        when(matchedItem.getItemMeta()).thenReturn(matchedMeta);

        when(mockItem.getType()).thenReturn(Material.STONE);

        when(mockInventory.getContents()).thenReturn(new ItemStack[]{matchedItem, null});

        int count = provider.getItemCount(mockPlayer, "stone");
        assertEquals(10, count);
    }
}
