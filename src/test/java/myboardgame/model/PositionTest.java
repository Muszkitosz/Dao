package myboardgame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
    Position position;

    void assertPosition(int expectedRow, int expectedCol, Position position) {
        assertAll("position",
                () -> assertEquals(expectedRow, position.row()),
                () -> assertEquals(expectedCol, position.col()));
    }

    @BeforeEach
    void init() {
        position = new Position(2, 2);
    }

    @Test
    void moveTo() {
        assertPosition(1, 1, position.moveTo(DiskDirection.UP_LEFT));
        assertPosition(1, 2, position.moveTo(DiskDirection.UP));
        assertPosition(1, 3, position.moveTo(DiskDirection.UP_RIGHT));
        assertPosition(2, 3, position.moveTo(DiskDirection.RIGHT));
        assertPosition(3, 3, position.moveTo(DiskDirection.DOWN_RIGHT));
        assertPosition(3, 2, position.moveTo(DiskDirection.DOWN));
        assertPosition(3, 1, position.moveTo(DiskDirection.DOWN_LEFT));
        assertPosition(2, 1, position.moveTo(DiskDirection.LEFT));
    }

    @Test
    void testToString() {
        assertEquals("(2,2)", position.toString());
    }
}
