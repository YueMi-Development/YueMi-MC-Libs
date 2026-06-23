package org.yuemi.libs.api.economy;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Collection;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

public class EconomyApiTest {

    @Test
    public void testEconomyApiMocking() {
        EconomyApi mockApi = Mockito.mock(EconomyApi.class);
        EconomyProvider mockProvider = Mockito.mock(EconomyProvider.class);

        Mockito.when(mockProvider.getName()).thenReturn("MockEcon");
        Mockito.when(mockProvider.isAvailable()).thenReturn(true);
        Mockito.when(mockApi.getActiveProvider()).thenReturn(mockProvider);
        Mockito.when(mockApi.getProviders()).thenReturn(Collections.singletonList(mockProvider));

        assertEquals("MockEcon", mockApi.getActiveProvider().getName());
        assertTrue(mockApi.getActiveProvider().isAvailable());
        assertEquals(1, mockApi.getProviders().size());
    }
}
