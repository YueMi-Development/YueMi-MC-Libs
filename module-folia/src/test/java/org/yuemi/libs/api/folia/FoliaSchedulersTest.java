package org.yuemi.libs.api.folia;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FoliaSchedulersTest {

    @Test
    public void testGetThrowsWhenNotInitialized() {
        assertThrows(IllegalStateException.class, FoliaSchedulers::get);
    }
}
