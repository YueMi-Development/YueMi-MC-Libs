package org.yuemi.libs.api.event;

import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class SubscriptionBuilderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testBuilderChain() {
        SubscriptionBuilder<PlayerJoinEvent> builder = Mockito.mock(SubscriptionBuilder.class);
        EventSubscription<PlayerJoinEvent> subscription = Mockito.mock(EventSubscription.class);

        Mockito.when(builder.priority(EventPriority.HIGH)).thenReturn(builder);
        Mockito.when(builder.ignoreCancelled(true)).thenReturn(builder);
        Mockito.when(builder.handler(Mockito.any(Consumer.class))).thenReturn(subscription);

        SubscriptionBuilder<PlayerJoinEvent> result = builder.priority(EventPriority.HIGH);
        assertSame(builder, result);

        result = builder.ignoreCancelled(true);
        assertSame(builder, result);

        EventSubscription<PlayerJoinEvent> sub = builder.handler(event -> {});
        assertSame(subscription, sub);
    }
}
