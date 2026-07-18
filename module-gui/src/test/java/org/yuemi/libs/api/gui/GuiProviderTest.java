package org.yuemi.libs.api.gui;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import java.lang.reflect.Modifier;
import static org.junit.jupiter.api.Assertions.*;

public class GuiProviderTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<GuiProvider> constructor = GuiProvider.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test
    public void testRegisterAndGetApi() {
        GuiApi mockApi = org.mockito.Mockito.mock(GuiApi.class);

        GuiProvider.register(mockApi);
        GuiApi retrieved = GuiProvider.getApi();

        assertSame(mockApi, retrieved);

        // Clean up
        GuiProvider.register(null);
    }

    @Test
    public void testGetApiReturnsNullByDefault() {
        GuiProvider.register(null);
        assertNull(GuiProvider.getApi());
    }
}
