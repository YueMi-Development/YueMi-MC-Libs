package org.yuemi.libs.api.folia;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Cross-platform scheduler interface that works on both Paper and Folia.
 * <p>
 * On Folia, tasks are routed to the correct region thread automatically.
 * On Paper, they execute on the server thread with equivalent semantics.
 */
public interface FoliaScheduler {

    /**
     * Returns the underlying server.
     *
     * @return the server instance
     */
    @NotNull Server getServer();

    /**
     * Executes a task on the global region (Folia) or main thread (Paper).
     * Use for tasks that are not tied to any specific location or entity.
     *
     * @param plugin the owning plugin
     * @param task the task to execute
     */
    void executeGlobal(@NotNull JavaPlugin plugin, @NotNull Runnable task);

    /**
     * Runs a task after a delay on the global region (Folia) or main thread (Paper).
     *
     * @param plugin the owning plugin
     * @param task the task to execute
     * @param delayTicks delay in ticks (20 ticks = 1 second)
     */
    void runGlobalDelayed(@NotNull JavaPlugin plugin, @NotNull Runnable task, long delayTicks);

    /**
     * Runs a repeating task on the global region (Folia) or main thread (Paper).
     *
     * @param plugin the owning plugin
     * @param task the task to execute
     * @param delayTicks initial delay in ticks
     * @param periodTicks period in ticks
     * @return the scheduled task, or null if cancelled
     */
    @Nullable Object runGlobalTimer(@NotNull JavaPlugin plugin, @NotNull Runnable task, long delayTicks, long periodTicks);

    /**
     * Executes a task at a specific location's region (Folia) or main thread (Paper).
     * Use for block operations at a known location.
     *
     * @param plugin the owning plugin
     * @param location the location whose region should execute the task
     * @param task the task to execute
     */
    void executeRegion(@NotNull JavaPlugin plugin, @NotNull Location location, @NotNull Runnable task);

    /**
     * Runs a task after a delay at a specific location's region (Folia) or main thread (Paper).
     *
     * @param plugin the owning plugin
     * @param location the location whose region should execute the task
     * @param task the task to execute
     * @param delayTicks delay in ticks
     */
    void runRegionDelayed(@NotNull JavaPlugin plugin, @NotNull Location location, @NotNull Runnable task, long delayTicks);

    /**
     * Executes a task asynchronously (no world/entity access).
     * On Folia, runs on a dedicated async thread. On Paper, runs on a Bukkit async thread.
     *
     * @param plugin the owning plugin
     * @param task the task to execute
     */
    void executeAsync(@NotNull JavaPlugin plugin, @NotNull Runnable task);

    /**
     * Runs a task asynchronously after a delay.
     *
     * @param plugin the owning plugin
     * @param task the task to execute
     * @param delayTicks delay in ticks
     */
    void runAsyncDelayed(@NotNull JavaPlugin plugin, @NotNull Runnable task, long delayTicks);

    /**
     * Runs a task at a fixed rate asynchronously.
     *
     * @param plugin the owning plugin
     * @param task the task to execute
     * @param initialDelayTicks initial delay in ticks
     * @param periodTicks period in ticks
     */
    void runAsyncTimer(@NotNull JavaPlugin plugin, @NotNull Runnable task, long initialDelayTicks, long periodTicks);

    /**
     * Executes a task on the entity's current region (Folia) or main thread (Paper).
     * The task follows the entity if it moves across region boundaries.
     *
     * @param plugin the owning plugin
     * @param entity the entity to execute on
     * @param task the task to execute
     */
    void executeEntity(@NotNull JavaPlugin plugin, @NotNull Entity entity, @NotNull Runnable task);

    /**
     * Runs a task after a delay on the entity's current region.
     *
     * @param plugin the owning plugin
     * @param entity the entity
     * @param task the task to execute
     * @param delayTicks delay in ticks
     * @param timeoutTicks timeout in ticks (0 = no timeout); only honoured on the
     *                     Paper fallback path, since Folia signals entity removal
     *                     through the retired callback
     */
    void runEntityDelayed(@NotNull JavaPlugin plugin, @NotNull Entity entity, @NotNull Runnable task, long delayTicks, long timeoutTicks);

    /**
     * Runs a repeating task on the entity's current region, following the entity
     * across region boundaries.
     *
     * @param plugin the owning plugin
     * @param entity the entity
     * @param task the task to execute
     * @param delayTicks initial delay in ticks
     * @param periodTicks period in ticks
     * @return the scheduled task, or null if the entity is already retired
     */
    @Nullable Object runEntityTimer(@NotNull JavaPlugin plugin, @NotNull Entity entity, @NotNull Runnable task, long delayTicks, long periodTicks);

    /**
     * Cancels a single task previously returned by one of the {@code run*Timer} methods.
     * Accepts the opaque handle as returned; safe to call with {@code null}.
     *
     * @param task the handle returned when the task was scheduled
     */
    void cancel(@Nullable Object task);

    /**
     * Cancels all tasks owned by the given plugin.
     *
     * @param plugin the plugin
     */
    void cancelAll(@NotNull JavaPlugin plugin);

}
