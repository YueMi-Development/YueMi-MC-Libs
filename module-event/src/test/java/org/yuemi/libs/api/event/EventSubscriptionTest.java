package org.yuemi.libs.api.event;

import org.bukkit.event.player.PlayerJoinEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class EventSubscriptionTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testSubscriptionLifecycle() {
        EventSubscription<PlayerJoinEvent> subscription = Mockito.mock(EventSubscription.class);

        Mockito.when(subscription.getEventClass()).thenReturn(PlayerJoinEvent.class);
        Mockito.when(subscription.isActive()).thenReturn(true);

        assertSame(PlayerJoinEvent.class, subscription.getEventClass());
        assertTrue(subscription.isActive());

        subscription.unsubscribe();
        Mockito.when(subscription.isActive()).thenReturn(false);

        assertFalse(subscription.isActive());
    }
}
