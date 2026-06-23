package org.yuemi.libs.plugin.gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.yuemi.libs.api.gui.GuiItem;
import org.yuemi.libs.api.gui.GuiLayer;
import static org.junit.jupiter.api.Assertions.*;

public class GuiLayerTest {

    private GuiLayer layer;
    private GuiItem mockItem;

    @BeforeEach
    public void setUp() {
        layer = new GuiLayerImpl("test-layer", 5);
        mockItem = Mockito.mock(GuiItem.class);
    }

    @Test
    public void testLayerMeta() {
        assertEquals("test-layer", layer.getId());
        assertEquals(5, layer.getPriority());
    }

    @Test
    public void testSetAndGetItem() {
        assertNull(layer.getItem(0));
        layer.setItem(0, mockItem);
        assertEquals(mockItem, layer.getItem(0));
        assertEquals(1, layer.getItems().size());
    }

    @Test
    public void testRemoveItem() {
        layer.setItem(13, mockItem);
        assertEquals(mockItem, layer.getItem(13));
        
        layer.removeItem(13);
        assertNull(layer.getItem(13));
        assertTrue(layer.getItems().isEmpty());
    }
}
