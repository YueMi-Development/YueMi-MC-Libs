package org.yuemi.libs.api.gui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SlotBehaviorTest {

    @Test
    public void testAllBehavior() {
        SlotBehavior behavior = SlotBehavior.ALL;
        assertTrue(behavior.allowPickup());
        assertTrue(behavior.allowPlace());
        assertTrue(behavior.allowShift());
        assertTrue(behavior.allowDrag());
    }

    @Test
    public void testNoneBehavior() {
        SlotBehavior behavior = SlotBehavior.NONE;
        assertFalse(behavior.allowPickup());
        assertFalse(behavior.allowPlace());
        assertFalse(behavior.allowShift());
        assertFalse(behavior.allowDrag());
    }

    @Test
    public void testDisplayBehavior() {
        SlotBehavior behavior = SlotBehavior.DISPLAY;
        assertFalse(behavior.allowPickup());
        assertFalse(behavior.allowPlace());
        assertFalse(behavior.allowShift());
        assertFalse(behavior.allowDrag());
    }

    @Test
    public void testContainerBehavior() {
        SlotBehavior behavior = SlotBehavior.CONTAINER;
        assertTrue(behavior.allowPickup());
        assertTrue(behavior.allowPlace());
        assertTrue(behavior.allowShift());
        assertTrue(behavior.allowDrag());
    }

    @Test
    public void testOutputBehavior() {
        SlotBehavior behavior = SlotBehavior.output();
        assertTrue(behavior.allowPickup());
        assertFalse(behavior.allowPlace());
        assertFalse(behavior.allowShift());
        assertFalse(behavior.allowDrag());
    }

    @Test
    public void testInputBehavior() {
        SlotBehavior behavior = SlotBehavior.input();
        assertFalse(behavior.allowPickup());
        assertTrue(behavior.allowPlace());
        assertTrue(behavior.allowShift());
        assertTrue(behavior.allowDrag());
    }

    @Test
    public void testBuilder() {
        SlotBehavior behavior = SlotBehavior.builder()
                .allowPickup(true)
                .allowPlace(false)
                .allowShift(true)
                .allowDrag(false)
                .build();

        assertTrue(behavior.allowPickup());
        assertFalse(behavior.allowPlace());
        assertTrue(behavior.allowShift());
        assertFalse(behavior.allowDrag());
    }

    @Test
    public void testBuilderDefaultAllTrue() {
        SlotBehavior behavior = SlotBehavior.builder().build();
        assertTrue(behavior.allowPickup());
        assertTrue(behavior.allowPlace());
        assertTrue(behavior.allowShift());
        assertTrue(behavior.allowDrag());
    }

    @Test
    public void testReadOnlyEqualsDisplay() {
        assertEquals(SlotBehavior.DISPLAY, SlotBehavior.READ_ONLY);
    }

    @Test
    public void testEquality() {
        SlotBehavior a = new SlotBehavior(true, false, true, false);
        SlotBehavior b = new SlotBehavior(true, false, true, false);
        SlotBehavior c = new SlotBehavior(true, true, true, true);

        assertEquals(a, b);
        assertNotEquals(a, c);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
