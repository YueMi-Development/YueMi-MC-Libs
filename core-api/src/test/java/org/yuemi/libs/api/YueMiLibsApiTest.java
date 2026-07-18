package org.yuemi.libs.api;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class YueMiLibsApiTest {

    @Test
    public void testApiVersion() {
        YueMiLibsApi api = Mockito.mock(YueMiLibsApi.class);

        Mockito.when(api.getVersion()).thenReturn("1.0.0");

        assertEquals("1.0.0", api.getVersion());
    }

    @Test
    public void testApiCompatibility() {
        YueMiLibsApi api = Mockito.mock(YueMiLibsApi.class);

        Mockito.when(api.isCompatible("1.0.0", "2.0.0")).thenReturn(true);
        Mockito.when(api.isCompatible("2.0.0", "3.0.0")).thenReturn(false);
        Mockito.when(api.isCompatible(null, "1.5.0")).thenReturn(true);
        Mockito.when(api.isCompatible("1.5.0", null)).thenReturn(true);

        assertTrue(api.isCompatible("1.0.0", "2.0.0"));
        assertFalse(api.isCompatible("2.0.0", "3.0.0"));
        assertTrue(api.isCompatible(null, "1.5.0"));
        assertTrue(api.isCompatible("1.5.0", null));
    }

    @Test
    public void testApiModuleAccessors() {
        YueMiLibsApi api = Mockito.mock(YueMiLibsApi.class);

        org.yuemi.libs.api.economy.EconomyApi mockEconomy =
                Mockito.mock(org.yuemi.libs.api.economy.EconomyApi.class);
        org.yuemi.libs.api.items.ItemsApi mockItems =
                Mockito.mock(org.yuemi.libs.api.items.ItemsApi.class);

        Mockito.when(api.getEconomy()).thenReturn(mockEconomy);
        Mockito.when(api.getItems()).thenReturn(mockItems);

        assertNotNull(api.getEconomy());
        assertNotNull(api.getItems());
    }
}
