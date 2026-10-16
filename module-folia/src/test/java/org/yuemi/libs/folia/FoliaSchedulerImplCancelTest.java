package org.yuemi.libs.folia;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.scheduler.BukkitTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FoliaSchedulerImplCancelTest {

    private final FoliaSchedulerImpl scheduler = new FoliaSchedulerImpl(null);

    @Test
    void cancelIsNoOpForNullHandle() {
        assertDoesNotThrow(() -> scheduler.cancel(null));
    }

    @Test
    void cancelCancelsFoliaScheduledTask() {
        ScheduledTask task = mock(ScheduledTask.class);

        scheduler.cancel(task);

        verify(task).cancel();
    }

    @Test
    void cancelCancelsBukkitTask() {
        BukkitTask task = mock(BukkitTask.class);

        scheduler.cancel(task);

        verify(task).cancel();
    }

    @Test
    void cancelIgnoresUnknownHandleTypes() {
        Object unknown = new Object();

        assertDoesNotThrow(() -> scheduler.cancel(unknown));
    }
}
