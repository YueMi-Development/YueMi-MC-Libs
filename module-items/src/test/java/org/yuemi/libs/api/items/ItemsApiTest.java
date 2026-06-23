package org.yuemi.libs.api.items;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

public class ItemsApiTest {

    @Test
    public void testItemsApiMocking() {
        ItemsApi mockApi = Mockito.mock(ItemsApi.class);
        ItemProvider mockProvider = Mockito.mock(ItemProvider.class);

        Mockito.when(mockProvider.getName()).thenReturn("MockItems");
        Mockito.when(mockProvider.isAvailable()).thenReturn(false);

        assertEquals("MockItems", mockProvider.getName());
        assertFalse(mockProvider.isAvailable());
    }
}
