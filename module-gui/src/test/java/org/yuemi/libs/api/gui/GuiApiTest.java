package org.yuemi.libs.api.gui;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class GuiApiTest {

    @Test
    public void testCreateBuilder() {
        GuiApi api = Mockito.mock(GuiApi.class);
        GuiBuilder mockBuilder = Mockito.mock(GuiBuilder.class);

        Mockito.when(api.createBuilder()).thenReturn(mockBuilder);

        assertSame(mockBuilder, api.createBuilder());
    }

    @Test
    public void testCreateItemBuilder() {
        GuiApi api = Mockito.mock(GuiApi.class);
        GuiItem.Builder mockItemBuilder = Mockito.mock(GuiItem.Builder.class);

        Mockito.when(api.createItemBuilder()).thenReturn(mockItemBuilder);

        assertSame(mockItemBuilder, api.createItemBuilder());
    }

    @Test
    public void testCreateAnvilInputBuilder() {
        GuiApi api = Mockito.mock(GuiApi.class);
        AnvilInputBuilder mockBuilder = Mockito.mock(AnvilInputBuilder.class);

        Mockito.when(api.createAnvilInputBuilder()).thenReturn(mockBuilder);

        assertSame(mockBuilder, api.createAnvilInputBuilder());
    }

    @Test
    public void testCreateSignInputBuilder() {
        GuiApi api = Mockito.mock(GuiApi.class);
        SignInputBuilder mockBuilder = Mockito.mock(SignInputBuilder.class);

        Mockito.when(api.createSignInputBuilder()).thenReturn(mockBuilder);

        assertSame(mockBuilder, api.createSignInputBuilder());
    }
}
