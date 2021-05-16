package myboardgame.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PieceTest {
    Piece redPiece = new Piece(PieceType.RED, new Position(1,1));
    Piece bluePiece = new Piece(PieceType.BLUE, new Position(2,2));

    @Test
    void moveTo() {
        redPiece.moveTo(DiskDirection.UP);
        assertNotEquals(new Position(2,1),redPiece.getPosition());
        assertEquals(new Position(0,1),redPiece.getPosition());
    }

    @Test
    void testToString() {
        assertEquals("RED(1,1)",redPiece.toString());
        assertEquals("BLUE(2,2)",bluePiece.toString());
    }
}
