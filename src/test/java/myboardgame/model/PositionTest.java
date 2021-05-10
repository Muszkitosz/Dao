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
        position = new Position(0, 0);
    }

    @Test
    void moveTo() {
        assertPosition(-1, 0, position.moveTo(DiskDirection.UP));
        assertPosition(0, 1, position.moveTo(DiskDirection.RIGHT));
        assertPosition(1, 0, position.moveTo(DiskDirection.DOWN));
        assertPosition(0, -1, position.moveTo(DiskDirection.LEFT));
    }

    @Test
    void testToString() {
        assertEquals("(0,0)", position.toString());
    }
}
