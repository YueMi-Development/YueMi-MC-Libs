package org.yuemi.libs.api.general.compare;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CompareApiTest {

    private final CompareApi api = CompareApi.getInstance();

    @Test
    public void testCompareBothNull() {
        ItemComparison result = api.compare(null, null);
        assertTrue(result.areEqual());
        assertNull(result.differenceReason());
    }

    @Test
    public void testItemComparisonEqual() {
        ItemComparison result = ItemComparison.equal();
        assertTrue(result.areEqual());
        assertTrue(result.similar());
        assertTrue(result.sameMaterial());
        assertTrue(result.sameAmount());
        assertTrue(result.sameDurability());
        assertTrue(result.sameMeta());
        assertTrue(result.sameEnchantments());
        assertTrue(result.sameCustomModelData());
        assertNull(result.differenceReason());
    }

    @Test
    public void testItemComparisonSimilar() {
        ItemComparison result = ItemComparison.similar("Different enchantments");
        assertFalse(result.areEqual());
        assertTrue(result.similar());
        assertTrue(result.sameMaterial());
        assertTrue(result.sameAmount());
        assertTrue(result.sameDurability());
        assertTrue(result.sameMeta());
        assertTrue(result.sameEnchantments());
        assertTrue(result.sameCustomModelData());
        assertEquals("Different enchantments", result.differenceReason());
    }

    @Test
    public void testItemComparisonDifferent() {
        ItemComparison result = ItemComparison.different("Different material");
        assertFalse(result.areEqual());
        assertFalse(result.similar());
        assertFalse(result.sameMaterial());
        assertFalse(result.sameAmount());
        assertFalse(result.sameDurability());
        assertFalse(result.sameMeta());
        assertFalse(result.sameEnchantments());
        assertFalse(result.sameCustomModelData());
        assertEquals("Different material", result.differenceReason());
    }

    @Test
    public void testItemComparisonRecordAccessorMethods() {
        var result = ItemComparison.different("test reason");
        
        // Verify record accessor methods
        assertEquals(false, result.areEqual());
        assertEquals(false, result.similar());
        assertEquals(false, result.sameMaterial());
        assertEquals(false, result.sameAmount());
        assertEquals(false, result.sameDurability());
        assertEquals(false, result.sameMeta());
        assertEquals(false, result.sameEnchantments());
        assertEquals(false, result.sameCustomModelData());
        assertEquals("test reason", result.differenceReason());
    }
}
