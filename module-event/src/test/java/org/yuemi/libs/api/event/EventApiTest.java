package org.yuemi.libs.api.event;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class EventApiTest {

    @Test
    public void testRegisterAndGetProvider() {
        EventApi api = Mockito.mock(EventApi.class);
        EventProvider mockProvider = Mockito.mock(EventProvider.class);

        Mockito.when(api.getProvider("test")).thenReturn(mockProvider);

        assertSame(mockProvider, api.getProvider("test"));
    }

    @Test
    public void testGetProviderReturnsNullForUnknown() {
        EventApi api = Mockito.mock(EventApi.class);

        Mockito.when(api.getProvider("unknown")).thenReturn(null);

        assertNull(api.getProvider("unknown"));
    }

    @Test
    public void testRegisterProvider() {
        EventApi api = Mockito.mock(EventApi.class);
        EventProvider mockProvider = Mockito.mock(EventProvider.class);

        api.registerProvider("custom", mockProvider);
        Mockito.when(api.getProvider("custom")).thenReturn(mockProvider);

        assertSame(mockProvider, api.getProvider("custom"));
    }
}
