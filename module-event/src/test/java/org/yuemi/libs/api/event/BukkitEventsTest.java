package org.yuemi.libs.api.event;

import org.bukkit.event.player.PlayerJoinEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.yuemi.libs.api.event.bukkit.BukkitEvents;
import org.yuemi.libs.api.event.bukkit.block.BlockEvents;
import org.yuemi.libs.api.event.bukkit.entity.EntityEvents;
import org.yuemi.libs.api.event.bukkit.player.PlayerEvents;

import static org.junit.jupiter.api.Assertions.*;

public class BukkitEventsTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testSubscribe() {
        BukkitEvents bukkitEvents = Mockito.mock(BukkitEvents.class);
        SubscriptionBuilder<PlayerJoinEvent> builder = Mockito.mock(SubscriptionBuilder.class);

        Mockito.when(bukkitEvents.subscribe(PlayerJoinEvent.class)).thenReturn(builder);

        assertSame(builder, bukkitEvents.subscribe(PlayerJoinEvent.class));
    }

    @Test
    public void testPlayerSubProvider() {
        BukkitEvents bukkitEvents = Mockito.mock(BukkitEvents.class);
        PlayerEvents mockPlayer = Mockito.mock(PlayerEvents.class);

        Mockito.when(bukkitEvents.player()).thenReturn(mockPlayer);

        assertSame(mockPlayer, bukkitEvents.player());
    }

    @Test
    public void testBlockSubProvider() {
        BukkitEvents bukkitEvents = Mockito.mock(BukkitEvents.class);
        BlockEvents mockBlock = Mockito.mock(BlockEvents.class);

        Mockito.when(bukkitEvents.block()).thenReturn(mockBlock);

        assertSame(mockBlock, bukkitEvents.block());
    }

    @Test
    public void testEntitySubProvider() {
        BukkitEvents bukkitEvents = Mockito.mock(BukkitEvents.class);
        EntityEvents mockEntity = Mockito.mock(EntityEvents.class);

        Mockito.when(bukkitEvents.entity()).thenReturn(mockEntity);

        assertSame(mockEntity, bukkitEvents.entity());
    }
}
