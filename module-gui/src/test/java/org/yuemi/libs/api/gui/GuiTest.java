package org.yuemi.libs.api.gui;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GuiTest {

    @Test
    public void testGuiTitleAndRows() {
        Gui gui = Mockito.mock(Gui.class);

        Mockito.when(gui.getTitle()).thenReturn("Test GUI");
        Mockito.when(gui.getRows()).thenReturn(3);

        assertEquals("Test GUI", gui.getTitle());
        assertEquals(3, gui.getRows());
    }

    @Test
    public void testManageLayers() {
        Gui gui = Mockito.mock(Gui.class);
        GuiLayer layer1 = Mockito.mock(GuiLayer.class);
        GuiLayer layer2 = Mockito.mock(GuiLayer.class);

        Mockito.when(gui.getLayers()).thenReturn(Arrays.asList(layer1, layer2));

        assertEquals(2, gui.getLayers().size());
    }

    @Test
    public void testClosePolicy() {
        Gui gui = Mockito.mock(Gui.class);

        Mockito.when(gui.getClosePolicy()).thenReturn(ClosePolicy.CLOSE);
        gui.setClosePolicy(ClosePolicy.CLOSE);

        assertEquals(ClosePolicy.CLOSE, gui.getClosePolicy());
        Mockito.verify(gui).setClosePolicy(ClosePolicy.CLOSE);
    }

    @Test
    public void testUpdateTitle() {
        Gui gui = Mockito.mock(Gui.class);
        org.bukkit.entity.Player player = Mockito.mock(org.bukkit.entity.Player.class);

        gui.updateTitle(player, "New Title");
        Mockito.verify(gui).updateTitle(player, "New Title");
    }
}
