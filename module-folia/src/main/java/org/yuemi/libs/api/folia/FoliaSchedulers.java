package org.yuemi.libs.api.folia;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Static accessor for the {@link FoliaScheduler}.
 * The actual implementation is provided by {@code module-folia}.
 */
@ApiStatus.Internal
public final class FoliaSchedulers {

    private static volatile FoliaScheduler instance;

    private FoliaSchedulers() {
    }

    /**
     * Sets the scheduler instance.
     * Called by the plugin on enable.
     *
     * @param scheduler the scheduler
     */
    public static void set(@NotNull FoliaScheduler scheduler) {
        instance = scheduler;
    }

    /**
     * Returns the current scheduler.
     *
     * @return the scheduler
     * @throws IllegalStateException if not initialized
     */
    @NotNull
    public static FoliaScheduler get() {
        FoliaScheduler result = instance;
        if (result == null) {
            throw new IllegalStateException("FoliaSchedulers not initialized. Call FoliaSchedulers.set() first.");
        }
        return result;
    }

    /**
     * Shortcut for {@code get().executeGlobal(plugin, task)}.
     */
    public static void executeGlobal(@NotNull JavaPlugin plugin, @NotNull Runnable task) {
        get().executeGlobal(plugin, task);
    }

    /**
     * Shortcut for {@code get().runGlobalDelayed(plugin, task, delay)}.
     */
    public static void runGlobalDelayed(@NotNull JavaPlugin plugin, @NotNull Runnable task, long delayTicks) {
        get().runGlobalDelayed(plugin, task, delayTicks);
    }

    /**
     * Shortcut for {@code get().runGlobalTimer(plugin, task, delay, period)}.
     */
    public static void runGlobalTimer(@NotNull JavaPlugin plugin, @NotNull Runnable task, long delayTicks, long periodTicks) {
        get().runGlobalTimer(plugin, task, delayTicks, periodTicks);
    }

    /**
     * Shortcut for {@code get().executeAsync(plugin, task)}.
     */
    public static void executeAsync(@NotNull JavaPlugin plugin, @NotNull Runnable task) {
        get().executeAsync(plugin, task);
    }

    /**
     * Shortcut for {@code get().runAsyncDelayed(plugin, task, delay)}.
     */
    public static void runAsyncDelayed(@NotNull JavaPlugin plugin, @NotNull Runnable task, long delayTicks) {
        get().runAsyncDelayed(plugin, task, delayTicks);
    }

    /**
     * Shortcut for {@code get().runAsyncTimer(plugin, task, initialDelay, period)}.
     */
    public static void runAsyncTimer(@NotNull JavaPlugin plugin, @NotNull Runnable task, long initialDelayTicks, long periodTicks) {
        get().runAsyncTimer(plugin, task, initialDelayTicks, periodTicks);
    }

}
