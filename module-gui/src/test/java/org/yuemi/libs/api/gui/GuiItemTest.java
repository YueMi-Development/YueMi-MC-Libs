package org.yuemi.libs.api.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class GuiItemTest {

    @Test
    public void testGetItemStack() {
        GuiItem item = Mockito.mock(GuiItem.class);
        Player mockPlayer = Mockito.mock(Player.class);
        ItemStack mockStack = Mockito.mock(ItemStack.class);

        Mockito.when(item.getItemStack(mockPlayer)).thenReturn(mockStack);

        assertSame(mockStack, item.getItemStack(mockPlayer));
    }

    @Test
    public void testShouldRender() {
        GuiItem item = Mockito.mock(GuiItem.class);
        Player mockPlayer = Mockito.mock(Player.class);

        Mockito.when(item.shouldRender(mockPlayer)).thenReturn(true);

        assertTrue(item.shouldRender(mockPlayer));
    }

    @Test
    public void testStaticBuilderThrowsWithoutProvider() {
        // GuiProvider is not registered in this test, so builder() should throw
        assertThrows(IllegalStateException.class, GuiItem::builder);
    }

    @Test
    public void testDefaultBehaviorIsAll() {
        // Use a real anonymous GuiItem instead of mock to test default method
        GuiItem item = new GuiItem() {
            @Override public org.bukkit.inventory.ItemStack getItemStack(Player player) { return null; }
            @Override public boolean shouldRender(Player player) { return false; }
            @Override public void onClick(GuiClickContext context) {}
        };
        assertEquals(SlotBehavior.ALL, item.behavior());
    }

    @Test
    public void testDefaultBehaviorAllowsAll() {
        GuiItem item = new GuiItem() {
            @Override public org.bukkit.inventory.ItemStack getItemStack(Player player) { return null; }
            @Override public boolean shouldRender(Player player) { return false; }
            @Override public void onClick(GuiClickContext context) {}
        };
        SlotBehavior behavior = item.behavior();
        assertTrue(behavior.allowPickup());
        assertTrue(behavior.allowPlace());
        assertTrue(behavior.allowShift());
        assertTrue(behavior.allowDrag());
    }
}
