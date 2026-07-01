package org.yuemi.libs.plugin.event;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yuemi.libs.api.event.EventProvider;
import org.yuemi.libs.api.event.EventSubscription;
import org.yuemi.libs.api.event.bukkit.BukkitEvents;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class EventApiImplTest {

    private Plugin mockPlugin;
    private Server mockServer;
    private PluginManager mockPluginManager;
    private EventApiImpl eventApi;

    @BeforeEach
    public void setUp() {
        mockPlugin = mock(Plugin.class);
        mockServer = mock(Server.class);
        mockPluginManager = mock(PluginManager.class);

        when(mockServer.getPluginManager()).thenReturn(mockPluginManager);
        when(mockPlugin.getServer()).thenReturn(mockServer);

        setBukkitServer(mockServer);

        eventApi = new EventApiImpl(mockPlugin);
    }

    private void setBukkitServer(Server server) {
        try {
            java.lang.reflect.Field field = Bukkit.class.getDeclaredField("server");
            field.setAccessible(true);
            field.set(null, server);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
        setBukkitServer(null);
    }

    @Test
    public void testGetBukkitEvents() {
        BukkitEvents bukkitEvents = eventApi.bukkit();
        assertNotNull(bukkitEvents);
    }

    @Test
    public void testCustomProviderRegistration() {
        EventProvider mockProvider = new EventProvider() {};
        eventApi.registerProvider("mobs", mockProvider);

        assertEquals(mockProvider, eventApi.getProvider("mobs"));
        assertEquals(mockProvider, eventApi.getProvider("Mobs")); // case insensitivity
        assertNull(eventApi.getProvider("nonexistent"));
    }

    @Test
    public void testSubscribeCallsPluginManager() {
        AtomicBoolean triggered = new AtomicBoolean(false);
        EventSubscription<PlayerJoinEvent> subscription = eventApi.bukkit()
                .subscribe(PlayerJoinEvent.class)
                .priority(EventPriority.HIGHEST)
                .ignoreCancelled(true)
                .handler(event -> triggered.set(true));

        assertNotNull(subscription);
        assertEquals(PlayerJoinEvent.class, subscription.getEventClass());
        assertTrue(subscription.isActive());

        // Verify Bukkit registered it
        verify(mockPluginManager).registerEvent(
                eq(PlayerJoinEvent.class),
                any(),
                eq(EventPriority.HIGHEST),
                any(),
                eq(mockPlugin),
                eq(true)
        );
    }

    @Test
    public void testPlayerSubGroupShorthands() {
        EventSubscription<PlayerJoinEvent> joinSub = eventApi.bukkit().player().join(e -> {});
        assertNotNull(joinSub);

        // Verify Bukkit registered it
        verify(mockPluginManager).registerEvent(
                eq(PlayerJoinEvent.class),
                any(),
                eq(EventPriority.NORMAL),
                any(),
                eq(mockPlugin),
                eq(false)
        );
    }

    @Test
    public void testUnsubscribe() {
        EventSubscription<PlayerJoinEvent> subscription = eventApi.bukkit()
                .subscribe(PlayerJoinEvent.class)
                .handler(event -> {});

        assertTrue(subscription.isActive());
        subscription.unsubscribe();
        assertFalse(subscription.isActive());
    }

    @Test
    public void testDisableCleansUpSubscriptions() {
        EventSubscription<PlayerJoinEvent> subscription = eventApi.bukkit()
                .subscribe(PlayerJoinEvent.class)
                .handler(event -> {});

        assertTrue(subscription.isActive());
        eventApi.disable();
        assertFalse(subscription.isActive());
    }
}
