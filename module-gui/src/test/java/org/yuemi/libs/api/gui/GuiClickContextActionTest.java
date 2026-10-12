package org.yuemi.libs.api.gui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuiClickContextActionTest {

    @Test
    public void testActionValuesExist() {
        GuiClickContext.Action[] actions = GuiClickContext.Action.values();
        assertTrue(actions.length >= 8, "Should have at least 8 actions");
    }

    @Test
    public void testValueOf() {
        assertEquals(GuiClickContext.Action.LEFT_CLICK, GuiClickContext.Action.valueOf("LEFT_CLICK"));
        assertEquals(GuiClickContext.Action.RIGHT_CLICK, GuiClickContext.Action.valueOf("RIGHT_CLICK"));
        assertEquals(GuiClickContext.Action.SHIFT_CLICK, GuiClickContext.Action.valueOf("SHIFT_CLICK"));
        assertEquals(GuiClickContext.Action.PICKUP, GuiClickContext.Action.valueOf("PICKUP"));
        assertEquals(GuiClickContext.Action.PLACE, GuiClickContext.Action.valueOf("PLACE"));
        assertEquals(GuiClickContext.Action.DRAG_PLACE, GuiClickContext.Action.valueOf("DRAG_PLACE"));
        assertEquals(GuiClickContext.Action.SWAP, GuiClickContext.Action.valueOf("SWAP"));
        assertEquals(GuiClickContext.Action.DROP_CURSOR, GuiClickContext.Action.valueOf("DROP_CURSOR"));
    }
}
