package org.yuemi.libs.api.gui;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GuiLayerTest {

    @Test
    public void testLayerIdAndPriority() {
        GuiLayer layer = Mockito.mock(GuiLayer.class);

        Mockito.when(layer.getId()).thenReturn("test-layer");
        Mockito.when(layer.getPriority()).thenReturn(10);

        assertEquals("test-layer", layer.getId());
        assertEquals(10, layer.getPriority());
    }

    @Test
    public void testSetAndGetItem() {
        GuiLayer layer = Mockito.mock(GuiLayer.class);
        GuiItem mockItem = Mockito.mock(GuiItem.class);
        int slot = 5;

        Mockito.when(layer.getItem(slot)).thenReturn(mockItem);

        assertSame(mockItem, layer.getItem(slot));
    }

    @Test
    public void testGetItems() {
        GuiLayer layer = Mockito.mock(GuiLayer.class);
        Map<Integer, GuiItem> items = new HashMap<>();
        items.put(0, Mockito.mock(GuiItem.class));
        items.put(1, Mockito.mock(GuiItem.class));

        Mockito.when(layer.getItems()).thenReturn(items);

        assertEquals(2, layer.getItems().size());
    }
}
