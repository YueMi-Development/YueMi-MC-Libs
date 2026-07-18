package org.yuemi.libs.api.event;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;

import java.lang.reflect.Modifier;
import static org.junit.jupiter.api.Assertions.*;

public class EventApiProviderTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<EventApiProvider> constructor = EventApiProvider.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test
    public void testRegisterAndGetApi() {
        EventApi mockApi = Mockito.mock(EventApi.class);

        EventApiProvider.register(mockApi);
        EventApi retrieved = EventApiProvider.getApi();

        assertSame(mockApi, retrieved);

        // Clean up
        EventApiProvider.register(null);
    }

    @Test
    public void testGetApiReturnsNullByDefault() {
        EventApiProvider.register(null);
        assertNull(EventApiProvider.getApi());
    }
}
