package org.yuemi.libs.api.general.random;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

/**
 * Utility API for random generation backed by {@link SecureRandom}
 * (hardware/OS-level entropy, not Java's pseudo-random {@link java.util.Random}).
 */
public class RandomApi {

    private static final RandomApi INSTANCE = new RandomApi();

    public static RandomApi getInstance() {
        return INSTANCE;
    }

    private final SecureRandom secureRandom;

    private RandomApi() {
        this.secureRandom = new SecureRandom();
    }

    /**
     * Returns the underlying {@link SecureRandom} instance.
     * Uses OS-level entropy (e.g. {@code /dev/urandom} on Linux/macOS).
     *
     * @return the secure random generator
     */
    @NotNull
    public SecureRandom secureRandom() {
        return secureRandom;
    }

    /**
     * Returns a random integer in {@code [0, bound)}.
     *
     * @param bound the upper bound (exclusive)
     * @return a random integer
     * @throws IllegalArgumentException if bound is not positive
     */
    public int nextInt(int bound) {
        return secureRandom.nextInt(bound);
    }

    /**
     * Returns a random double in {@code [0.0, 1.0)}.
     *
     * @return a random double
     */
    public double nextDouble() {
        return secureRandom.nextDouble();
    }

    /**
     * Returns whether a random roll succeeds given a probability.
     * Convenience for {@code nextDouble() < probability}.
     *
     * @param probability chance of success in {@code [0.0, 1.0]}
     * @return true if the roll succeeds
     */
    public boolean chance(double probability) {
        return secureRandom.nextDouble() < probability;
    }

    /**
     * Picks a random element from an array.
     *
     * @param array the array to pick from
     * @param <T>   the element type
     * @return a randomly chosen element, or null if array is empty
     */
    @Nullable
    public <T> T pick(T @NotNull [] array) {
        Objects.requireNonNull(array, "array");
        if (array.length == 0) {
            return null;
        }
        return array[secureRandom.nextInt(array.length)];
    }

    /**
     * Picks a random element from a list.
     *
     * @param list the list to pick from
     * @param <T>  the element type
     * @return a randomly chosen element, or null if list is empty
     */
    @Nullable
    public <T> T pick(@NotNull List<T> list) {
        Objects.requireNonNull(list, "list");
        if (list.isEmpty()) {
            return null;
        }
        return list.get(secureRandom.nextInt(list.size()));
    }
}
