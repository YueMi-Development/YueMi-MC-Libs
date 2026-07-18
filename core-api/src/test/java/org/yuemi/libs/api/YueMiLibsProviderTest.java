package org.yuemi.libs.api;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class YueMiLibsProviderTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<YueMiLibsProvider> constructor = YueMiLibsProvider.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test
    public void testGetApiMethodExists() {
        assertDoesNotThrow(() -> YueMiLibsProvider.class.getMethod("getApi"));
    }
}
