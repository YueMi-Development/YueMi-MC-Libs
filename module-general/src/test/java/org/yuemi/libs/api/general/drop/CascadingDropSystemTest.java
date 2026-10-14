package org.yuemi.libs.api.general.drop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.yuemi.libs.api.general.drop.cascade.CascadeDirection;
import org.yuemi.libs.api.general.drop.cascade.CascadingDropBuilder;
import org.yuemi.libs.api.general.drop.cascade.CascadingDropSystem;

public class CascadingDropSystemTest {

    private final org.yuemi.libs.api.general.random.RandomApi randomApi =
        org.yuemi.libs.api.general.random.RandomApi.getInstance();

    @Test
    public void testEmptySystemReturnsNull() {
        // Build a system with one rarity tier that has no drops
        var system = CascadingDropBuilder.<String>create(randomApi)
            .addRarity(0.5)
            .build();

        for (int i = 0; i < 100; i++) {
            assertNull(system.roll(), "Empty rarity tier should return null");
        }
    }

    @Test
    public void testSingleDropRespectsChance() {
        var system = CascadingDropBuilder.<String>create(randomApi)
            .addRarity(0.5)          // 50% to hit this tier
            .withDrop("item", 1.0)   // 100% of tier → always picked if tier hits
            .build();

        int hits = 0;
        int trials = 2000;
        for (int i = 0; i < trials; i++) {
            if (system.roll() != null) hits++;
        }
        // Should be roughly 50% ± 4%
        double ratio = (double) hits / trials;
        assertTrue(Math.abs(ratio - 0.5) < 0.04,
            "Expected ~50% hit rate, got " + (ratio * 100) + "%");
    }

    @Test
    public void testTwoRungsLowestCheckedFirst() {
        // Tier 0: always miss. Tier 1: always hit and return "rare".
        var system = CascadingDropBuilder.<String>create(randomApi)
            .addRarity(0.0)           // tier 0 — never hits
            .withDrop("common", 1.0)
            .addRarity(1.0)           // tier 1 — always hits
            .withDrop("rare", 1.0)
            .build();

        for (int i = 0; i < 100; i++) {
            assertEquals("rare", system.roll());
        }
    }

    @Test
    public void testCascadeItemSelectionWithinTier() {
        // Tier 0 hits 100%, within tier: 70% common, 30% rare
        var system = CascadingDropBuilder.<String>create(randomApi)
            .addRarity(1.0)
            .withDrop("common", 0.7)
            .withDrop("rare", 0.3)
            .build();

        int common = 0, rare = 0;
        int trials = 2000;
        for (int i = 0; i < trials; i++) {
            String result = system.roll();
            assertNotNull(result);
            if (result.equals("common")) common++;
            else if (result.equals("rare")) rare++;
            else fail("Unexpected item: " + result);
        }

        assertTrue(Math.abs((double) common / trials - 0.7) < 0.04,
            "common ratio was " + ((double) common / trials * 100) + "% expected ~70%");
        assertTrue(Math.abs((double) rare / trials - 0.3) < 0.04,
            "rare ratio was " + ((double) rare / trials * 100) + "% expected ~30%");
    }

    @Test
    public void testMustHaveSomeUsedWhenAllMiss() {
        // Tier 0 misses always, no fallback in tier 0's item selection
        var system = CascadingDropBuilder.<String>create(randomApi)
            .addRarity(0.0)
            .withDrop("common", 1.0)  // unreachable because tier 0 always misses
            .mustHaveSome(List.of("fallback_a", "fallback_b"))
            .build();

        Set<String> seen = new HashSet<>();
        for (int i = 0; i < 200; i++) {
            String result = system.roll();
            assertNotNull(result);
            assertTrue(!result.equals("common"),
                "common should never be returned since tier 0 always misses");
            seen.add(result);
        }
        // Should only see fallback items
        assertTrue(seen.stream().allMatch(s -> s.startsWith("fallback_")),
            "Should only see fallback items, got: " + seen);
    }

    @Test
    public void testMustHaveSomeNotUsedOnSuccessfulTier() {
        // Tier 0 hits 100%, returns "common"
        var system = CascadingDropBuilder.<String>create(randomApi)
            .addRarity(1.0)
            .withDrop("common", 1.0)
            .mustHaveSome(List.of("fallback"))
            .build();

        for (int i = 0; i < 100; i++) {
            assertEquals("common", system.roll(), "fallback should never be used");
        }
    }

    @Test
    public void testMultipleRungsStopAtFirstHit() {
        // Tier 0: 50%, tier 1: always hit
        var system = CascadingDropBuilder.<String>create(randomApi)
            .addRarity(0.5)
            .withDrop("tier0_item", 1.0)
            .addRarity(1.0)
            .withDrop("tier1_item", 1.0)
            .build();

        int tier0 = 0, tier1 = 0;
        int trials = 4000;
        for (int i = 0; i < trials; i++) {
            String result = system.roll();
            assertNotNull(result);
            if (result.equals("tier0_item")) tier0++;
            else if (result.equals("tier1_item")) tier1++;
        }

        // Tier 0 should be hit ~50% of the time
        double tier0Ratio = (double) tier0 / trials;
        assertTrue(Math.abs(tier0Ratio - 0.5) < 0.04,
            "tier0 should be ~50%, got " + (tier0Ratio * 100) + "%");
        // Tier 1 should be the rest
        assertEquals(trials - tier0, tier1);
    }

    @Test
    public void testRarityCount() {
        var system = CascadingDropBuilder.<String>create(randomApi)
            .addRarity(0.1)
            .withDrop("a", 1.0)
            .addRarity(0.2)
            .withDrop("b", 1.0)
            .addRarity(0.3)
            .withDrop("c", 1.0)
            .build();

        assertEquals(3, system.rarityCount());
    }

    @Test
    public void testDropsForRarity() {
        var system = CascadingDropBuilder.<String>create(randomApi)
            .addRarity(0.1)
            .withDrop("a", 0.5)
            .withDrop("b", 0.5)
            .addRarity(0.2)
            .withDrop("c", 1.0)
            .build();

        var tier0 = system.dropsForRarity(0);
        assertEquals(2, tier0.size());

        var tier1 = system.dropsForRarity(1);
        assertEquals(1, tier1.size());
        assertEquals("c", tier1.get(0).item());
    }

    @Test
    public void testDropsForRarityOutOfBounds() {
        var system = CascadingDropBuilder.<String>create(randomApi)
            .addRarity(0.1)
            .withDrop("a", 1.0)
            .build();

        assertThrows(IndexOutOfBoundsException.class, () -> system.dropsForRarity(1));
        assertThrows(IndexOutOfBoundsException.class, () -> system.dropsForRarity(-1));
    }

    @Test
    public void testCumulativeLessThanOneMeansNoDropInTier() {
        // Tier hits 100%, but only 50% chance to pick an item within the tier
        var system = CascadingDropBuilder.<String>create(randomApi)
            .addRarity(1.0)
            .withDrop("maybe", 0.5)   // 50% within tier
            // 50% no-drop within tier — should cascade to fallback
            .mustHaveSome(List.of("fallback"))
            .build();

        int maybe = 0, fallback = 0;
        int trials = 2000;
        for (int i = 0; i < trials; i++) {
            String result = system.roll();
            assertNotNull(result);
            if (result.equals("maybe")) maybe++;
            else if (result.equals("fallback")) fallback++;
        }

        // maybe ~50%, fallback ~50%
        double maybeRatio = (double) maybe / trials;
        assertTrue(Math.abs(maybeRatio - 0.5) < 0.04,
            "maybe should be ~50%, got " + (maybeRatio * 100) + "%");
        assertEquals(trials - maybe, fallback);
    }

    @Test
    public void testDropItemValidation() {
        assertThrows(IllegalArgumentException.class, () ->
            new DropItem<String>("test", 1.5));
        assertThrows(IllegalArgumentException.class, () ->
            new DropItem<String>("test", -0.1));
        assertThrows(IllegalArgumentException.class, () ->
            new DropItem<String>(null, 0.5));
    }

    @Test
    public void testAddRarityValidation() {
        var builder = CascadingDropBuilder.<String>create(randomApi);
        assertThrows(IllegalArgumentException.class, () -> builder.addRarity(1.5));
        assertThrows(IllegalArgumentException.class, () -> builder.addRarity(-0.1));
    }

    @Test
    public void testWithDropBeforeAddRarity() {
        var builder = CascadingDropBuilder.<String>create(randomApi);
        assertThrows(IllegalStateException.class, () -> builder.withDrop("test", 0.5));
    }

    @Test
    public void testMustHaveSomeEmptyList() {
        // Empty mustHaveSome means roll() returns null when all miss
        var system = CascadingDropBuilder.<String>create(randomApi)
            .addRarity(0.0)
            .withDrop("unreachable", 1.0)
            .mustHaveSome(List.of())
            .build();

        for (int i = 0; i < 100; i++) {
            assertNull(system.roll());
        }
    }

    /**
     * Example: 3-tier loot table (Common, Rare, Legendary).
     * Tier 0 (Common) — 50% chance (checked first).
     * Tier 1 (Rare)   — 20% chance (only reached if Common missed).
     * Tier 2 (Legendary) — 5% chance (only reached if Common AND Rare missed).
     * No fallback — null if every tier misses.
     *
     * Effective probabilities across all rolls:
     *   Common     = 50%
     *   Rare       = (1-0.50) * 0.20 = 10%
     *   Legendary  = (1-0.50) * (1-0.20) * 0.05 = 2%
     *   null       = (1-0.50) * (1-0.20) * (1-0.05) = 38%
     */
    @Test
    public void testThreeTierLootTable() {
        var system = CascadingDropBuilder.<String>create(randomApi)
            // Tier 0 — Common (50%)
            .addRarity(0.50)
            .withDrop("COBBLESTONE", 0.60)
            .withDrop("DIRT",         0.30)
            .withDrop("GRAVEL",        0.10)
            // Tier 1 — Rare (20% of remaining 50% = 10% overall)
            .addRarity(0.20)
            .withDrop("IRON_INGOT",    0.50)
            .withDrop("GOLD_INGOT",    0.35)
            .withDrop("LAVA_BUCKET",   0.15)
            // Tier 2 — Legendary (5% of remaining 10% = 2% overall)
            .addRarity(0.05)
            .withDrop("DIAMOND",                0.60)
            .withDrop("NETHERITE_INGOT",        0.30)
            .withDrop("ENCHANTED_GOLDEN_APPLE", 0.10)
            .build();

        int common = 0, rare = 0, legendary = 0, nullCount = 0;
        int cobble = 0, dirt = 0, gravel = 0;
        int iron = 0, gold = 0, lava = 0;
        int dia = 0, netherite = 0, egapple = 0;

        int trials = 50000;

        for (int i = 0; i < trials; i++) {
            String result = system.roll();
            if (result == null) {
                nullCount++;
                continue;
            }
            switch (result) {
                case "COBBLESTONE" -> { common++; cobble++; }
                case "DIRT"        -> { common++; dirt++; }
                case "GRAVEL"      -> { common++; gravel++; }
                case "IRON_INGOT"  -> { rare++; iron++; }
                case "GOLD_INGOT"  -> { rare++; gold++; }
                case "LAVA_BUCKET" -> { rare++; lava++; }
                case "DIAMOND"                 -> { legendary++; dia++; }
                case "NETHERITE_INGOT"         -> { legendary++; netherite++; }
                case "ENCHANTED_GOLDEN_APPLE"  -> { legendary++; egapple++; }
                default -> fail("Unexpected item: " + result);
            }
        }

        // --- Tier-level assertions ---
        // Common ~50%
        assertRatio("Common", common, trials, 0.50, 0.015);
        // Rare ~10% (0.50 miss * 0.20 hit)
        assertRatio("Rare", rare, trials, 0.10, 0.015);
        // Legendary ~2% (0.50 miss * 0.80 miss * 0.05 hit)
        assertRatio("Legendary", legendary, trials, 0.02, 0.008);
        // Null ~38%
        assertRatio("Null", nullCount, trials, 0.38, 0.015);

        // --- Within-tier item distribution ---
        // Common tier: 60/30/10
        assertRatio("COBBLESTONE (within Common)", cobble, common, 0.60, 0.03);
        assertRatio("DIRT         (within Common)", dirt,    common, 0.30, 0.03);
        assertRatio("GRAVEL       (within Common)", gravel,  common, 0.10, 0.03);

        // Rare tier: 50/35/15
        assertRatio("IRON_INGOT  (within Rare)", iron, rare, 0.50, 0.03);
        assertRatio("GOLD_INGOT  (within Rare)", gold, rare, 0.35, 0.03);
        assertRatio("LAVA_BUCKET (within Rare)", lava, rare, 0.15, 0.03);

        // Legendary tier: 60/30/10
        assertRatio("DIAMOND        (within Legendary)", dia,       legendary, 0.60, 0.05);
        assertRatio("NETHERITE      (within Legendary)", netherite, legendary, 0.30, 0.05);
        assertRatio("EG_APPLE       (within Legendary)", egapple,   legendary, 0.10, 0.05);

        assertEquals(trials, common + rare + legendary + nullCount,
            "All categories must sum to total trials");
    }

    /**
     * Same loot table as testThreeTierLootTable but with HIGH_TO_LOW direction.
     * Tier 2 (Legendary) is checked first, so it gets ~5% of all rolls.
     * Tier 1 (Rare)    — reached only if Legendary missed → ~9.5% of all rolls.
     * Tier 0 (Common)  — reached only if Legendary AND Rare missed → ~47.5% of all rolls.
     * Null             — ~38% (same as before, since all must miss)
     */
    @Test
    public void testHighToLowDirection() {
        var system = CascadingDropBuilder.<String>create(randomApi)
            .direction(CascadeDirection.HIGH_TO_LOW)
            // Tier 0 — Common (50%) — now checked LAST
            .addRarity(0.50)
            .withDrop("COBBLESTONE", 0.60)
            .withDrop("DIRT",         0.30)
            .withDrop("GRAVEL",       0.10)
            // Tier 1 — Rare (20%) — now checked second
            .addRarity(0.20)
            .withDrop("IRON_INGOT",   0.50)
            .withDrop("GOLD_INGOT",   0.35)
            .withDrop("LAVA_BUCKET",  0.15)
            // Tier 2 — Legendary (5%) — now checked FIRST
            .addRarity(0.05)
            .withDrop("DIAMOND",                0.60)
            .withDrop("NETHERITE_INGOT",        0.30)
            .withDrop("ENCHANTED_GOLDEN_APPLE", 0.10)
            .build();

        assertEquals(CascadeDirection.HIGH_TO_LOW, system.direction());

        int common = 0, rare = 0, legendary = 0, nullCount = 0;
        int trials = 50000;

        for (int i = 0; i < trials; i++) {
            String result = system.roll();
            if (result == null) {
                nullCount++;
                continue;
            }
            if (result.equals("COBBLESTONE") || result.equals("DIRT") || result.equals("GRAVEL")) {
                common++;
            } else if (result.equals("IRON_INGOT") || result.equals("GOLD_INGOT") || result.equals("LAVA_BUCKET")) {
                rare++;
            } else if (result.equals("DIAMOND") || result.equals("NETHERITE_INGOT") || result.equals("ENCHANTED_GOLDEN_APPLE")) {
                legendary++;
            }
        }

        // Legendary (now first) ~5%
        assertRatio("Legendary (HIGH_TO_LOW)", legendary, trials, 0.05, 0.012);
        // Rare (now second): 0.95 miss * 0.20 hit = 19% of remaining → ~19% overall
        assertRatio("Rare (HIGH_TO_LOW)", rare, trials, 0.19, 0.015);
        // Common (now last): 0.95 * 0.80 miss * 0.50 hit = 38% of remaining → ~38% overall
        assertRatio("Common (HIGH_TO_LOW)", common, trials, 0.38, 0.015);
        // Null ~38%
        assertRatio("Null (HIGH_TO_LOW)", nullCount, trials, 0.38, 0.015);

        assertEquals(trials, common + rare + legendary + nullCount);
    }

    private void assertRatio(String label, int observed, int total, double expected, double tolerance) {
        double ratio = (double) observed / total;
        assertTrue(Math.abs(ratio - expected) < tolerance,
            label + " should be ~" + (expected * 100) + "%, got " + (ratio * 100) + "%");
    }
}
