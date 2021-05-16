package myboardgame.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DiskDirectionTest {

    @Test
    void of() {
        assertSame(DiskDirection.UP_LEFT,DiskDirection.of(-1,-1));
        assertSame(DiskDirection.UP, DiskDirection.of(-1,0));
        assertSame(DiskDirection.UP_RIGHT,DiskDirection.of(-1,1));
        assertSame(DiskDirection.RIGHT, DiskDirection.of(0, 1));
        assertSame(DiskDirection.DOWN_RIGHT,DiskDirection.of(1,1));
        assertSame(DiskDirection.DOWN, DiskDirection.of(1, 0));
        assertSame(DiskDirection.DOWN_LEFT,DiskDirection.of(1,-1));
        assertSame(DiskDirection.LEFT, DiskDirection.of(0, -1));
    }

    @Test
    void of_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> DiskDirection.of(0, 0));
    }
}
