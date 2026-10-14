package org.yuemi.libs.api.general.random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RandomApiTest {

    private final RandomApi api = RandomApi.getInstance();

    @Test
    public void testSecureRandomNotNull() {
        assertNotNull(api.secureRandom());
        assertTrue(api.secureRandom() instanceof SecureRandom);
    }

    @Test
    public void testNextIntBoundPositive() {
        for (int bound : new int[]{1, 2, 5, 10, 100}) {
            for (int i = 0; i < 1000; i++) {
                int result = api.nextInt(bound);
                assertTrue(result >= 0 && result < bound,
                    "nextInt(" + bound + ") returned " + result);
            }
        }
    }

    @Test
    public void testNextIntBoundIllegal() {
        assertThrows(IllegalArgumentException.class, () -> api.nextInt(0));
        assertThrows(IllegalArgumentException.class, () -> api.nextInt(-1));
    }

    @Test
    public void testNextDoubleRange() {
        for (int i = 0; i < 1000; i++) {
            double result = api.nextDouble();
            assertTrue(result >= 0.0 && result < 1.0, "nextDouble() returned " + result);
        }
    }

    @Test
    public void testChanceZero() {
        for (int i = 0; i < 100; i++) {
            assertFalse(api.chance(0.0), "chance(0.0) should always be false");
        }
    }

    @Test
    public void testChanceOne() {
        for (int i = 0; i < 100; i++) {
            assertTrue(api.chance(1.0), "chance(1.0) should always be true");
        }
    }

    @Test
    public void testChanceMonteCarlo() {
        int trials = 10000;
        int successes = 0;
        for (int i = 0; i < trials; i++) {
            if (api.chance(0.5)) successes++;
        }
        // Allow 3% tolerance
        double observed = (double) successes / trials;
        assertTrue(Math.abs(observed - 0.5) < 0.03,
            "Expected ~50% success rate, got " + (observed * 100) + "%");
    }

    @Test
    public void testPickArrayNull() {
        assertThrows(NullPointerException.class, () -> api.pick((String[]) null));
    }

    @Test
    public void testPickArrayEmpty() {
        String[] empty = new String[0];
        assertNull(api.pick(empty));
    }

    @Test
    public void testPickArrayReturnsFromArray() {
        Integer[] arr = {1, 2, 3, 4, 5};
        for (int i = 0; i < 100; i++) {
            Integer picked = api.pick(arr);
            assertNotNull(picked);
            assertTrue(picked >= 1 && picked <= 5);
        }
    }

    @Test
    public void testPickListNull() {
        assertThrows(NullPointerException.class, () -> api.pick((List<String>) null));
    }

    @Test
    public void testPickListEmpty() {
        assertNull(api.pick(List.of()));
    }

    @Test
    public void testPickListReturnsFromList() {
        List<String> list = List.of("a", "b", "c");
        for (int i = 0; i < 100; i++) {
            String picked = api.pick(list);
            assertNotNull(picked);
            assertTrue(list.contains(picked));
        }
    }

    @Test
    public void testPickListUniformity() {
        List<String> list = List.of("x", "y", "z");
        int[] counts = {0, 0, 0};
        int trials = 9000;
        for (int i = 0; i < trials; i++) {
            String picked = api.pick(list);
            int idx = list.indexOf(picked);
            counts[idx]++;
        }
        // Each should appear roughly 33% of the time (allow 5% tolerance)
        for (int i = 0; i < 3; i++) {
            double ratio = (double) counts[i] / trials;
            assertTrue(Math.abs(ratio - 1.0 / 3.0) < 0.05,
                "Element " + i + " appeared " + (ratio * 100) + "% (expected ~33%)");
        }
    }

    @Test
    public void testInstanceReturnsSameInstance() {
        assertSame(RandomApi.getInstance(), RandomApi.getInstance());
    }
}
