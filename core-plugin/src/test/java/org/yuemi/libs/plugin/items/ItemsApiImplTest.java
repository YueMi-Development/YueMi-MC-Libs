package org.yuemi.libs.plugin.items;

import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.yuemi.libs.api.items.ItemProvider;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ItemsApiImplTest {

    private ItemsApiImpl itemsApi;
    private ItemProvider mockMinecraftProvider;
    private ItemProvider mockCustomProvider;
    private ItemStack mockItemStack;

    @BeforeEach
    public void setUp() {
        itemsApi = new ItemsApiImpl();
        mockMinecraftProvider = mock(ItemProvider.class);
        mockCustomProvider = mock(ItemProvider.class);
        mockItemStack = mock(ItemStack.class);

        when(mockMinecraftProvider.isAvailable()).thenReturn(true);
        when(mockCustomProvider.isAvailable()).thenReturn(true);

        itemsApi.registerProvider("minecraft", mockMinecraftProvider);
        itemsApi.registerProvider("custom", mockCustomProvider);
    }

    @Test
    public void testKeyWithExplicitNamespace() {
        itemsApi.getItem("custom:sword", 5);
        verify(mockCustomProvider).getItem(eq("sword"), eq(5));
    }

    @Test
    public void testKeyWithImplicitNamespaceDefaultsToMinecraft() {
        itemsApi.getItem("stone", 1);
        verify(mockMinecraftProvider).getItem(eq("stone"), eq(1));
    }

    @Test
    public void testUnavailableProviderReturnsNull() {
        when(mockCustomProvider.isAvailable()).thenReturn(false);
        ItemStack result = itemsApi.getItem("custom:sword", 1);
        assertNull(result);
    }
}
