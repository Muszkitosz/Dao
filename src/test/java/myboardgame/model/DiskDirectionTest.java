package myboardgame.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DiskDirectionTest {

    @Test
    void of() {
        assertSame(DiskDirection.UP, DiskDirection.of(-1,0));
        assertSame(DiskDirection.RIGHT, DiskDirection.of(0, 1));
        assertSame(DiskDirection.DOWN, DiskDirection.of(1, 0));
        assertSame(DiskDirection.LEFT, DiskDirection.of(0, -1));
    }

    @Test
    void of_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> DiskDirection.of(0, 0));
    }
}
