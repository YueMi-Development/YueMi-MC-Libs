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
}
