package myboardgame.model;

import org.junit.jupiter.api.Test;

import java.util.OptionalInt;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MyBoardGameModelTest {

    MyBoardGameModel state1 = new MyBoardGameModel();

    MyBoardGameModel state2 = new MyBoardGameModel(new Piece(PieceType.BLUE, new Position(0, 0)),
            new Piece(PieceType.BLUE, new Position(1, 0)),
            new Piece(PieceType.BLUE, new Position(2, 0)),
            new Piece(PieceType.BLUE, new Position(3, 0)),
            new Piece(PieceType.RED, new Position(0, 1)),
            new Piece(PieceType.RED, new Position(1, 1)),
            new Piece(PieceType.RED, new Position(1, 2)),
            new Piece(PieceType.RED, new Position(3, 3))); // a column goal state for blue

    MyBoardGameModel state3 = new MyBoardGameModel(new Piece(PieceType.BLUE, new Position(0, 0)),
            new Piece(PieceType.BLUE, new Position(0, 1)),
            new Piece(PieceType.BLUE, new Position(0, 2)),
            new Piece(PieceType.BLUE, new Position(0, 3)),
            new Piece(PieceType.RED, new Position(2, 3)),
            new Piece(PieceType.RED, new Position(1, 1)),
            new Piece(PieceType.RED, new Position(2, 2)),
            new Piece(PieceType.RED, new Position(3, 3))); // a row goal state for blue

    MyBoardGameModel state4 = new MyBoardGameModel(new Piece(PieceType.BLUE, new Position(0, 0)),
            new Piece(PieceType.BLUE, new Position(0, 1)),
            new Piece(PieceType.BLUE, new Position(1, 0)),
            new Piece(PieceType.BLUE, new Position(1, 1)),
            new Piece(PieceType.RED, new Position(2, 3)),
            new Piece(PieceType.RED, new Position(2, 0)),
            new Piece(PieceType.RED, new Position(2, 2)),
            new Piece(PieceType.RED, new Position(3, 3))); // a 2x2 goal state for blue

    MyBoardGameModel state5 = new MyBoardGameModel(new Piece(PieceType.BLUE, new Position(0, 3)),
            new Piece(PieceType.BLUE, new Position(3, 1)),
            new Piece(PieceType.BLUE, new Position(2, 1)),
            new Piece(PieceType.BLUE, new Position(3, 0)),
            new Piece(PieceType.RED, new Position(0, 0)),
            new Piece(PieceType.RED, new Position(0, 2)),
            new Piece(PieceType.RED, new Position(1, 2)),
            new Piece(PieceType.RED, new Position(1, 3))); // goal state for blue, when 3 red cornered 1 blue

    MyBoardGameModel state6 = new MyBoardGameModel(new Piece(PieceType.BLUE, new Position(0, 0)),
            new Piece(PieceType.BLUE, new Position(3, 0)),
            new Piece(PieceType.BLUE, new Position(0, 3)),
            new Piece(PieceType.BLUE, new Position(3, 3)),
            new Piece(PieceType.RED, new Position(0, 1)),
            new Piece(PieceType.RED, new Position(1, 1)),
            new Piece(PieceType.RED, new Position(2, 2)),
            new Piece(PieceType.RED, new Position(2, 0))); // goal state for blue, when all disks are in a corner

    MyBoardGameModel state7 = new MyBoardGameModel(new Piece(PieceType.BLUE, new Position(0, 3)),
            new Piece(PieceType.BLUE, new Position(1, 3)),
            new Piece(PieceType.BLUE, new Position(0, 0)),
            new Piece(PieceType.BLUE, new Position(1, 0)),
            new Piece(PieceType.RED, new Position(0, 1)),
            new Piece(PieceType.RED, new Position(1, 1)),
            new Piece(PieceType.RED, new Position(2, 2)),
            new Piece(PieceType.RED, new Position(2, 0))); // a non-goal state

    @Test
    void isGoal() {
        assertFalse(state1.isGoal(PieceType.BLUE));
        assertFalse(state1.isGoal(PieceType.RED));
        assertTrue(state2.isGoal(PieceType.BLUE));
        assertFalse(state2.isGoal(PieceType.RED));
        assertTrue(state3.isGoal(PieceType.BLUE));
        assertFalse(state3.isGoal(PieceType.RED));
        assertTrue(state4.isGoal(PieceType.BLUE));
        assertFalse(state4.isGoal(PieceType.RED));
        assertTrue(state5.isGoal(PieceType.BLUE));
        assertFalse(state5.isGoal(PieceType.RED));
        assertTrue(state6.isGoal(PieceType.BLUE));
        assertFalse(state6.isGoal(PieceType.RED));
        assertFalse(state7.isGoal(PieceType.BLUE));
        assertFalse(state7.isGoal(PieceType.RED));
    }

    @Test
    void isOnBoard() {
        assertFalse(state6.isOnBoard(new Position(-1, -1)));
        assertTrue(state6.isOnBoard(new Position(1,1)));
    }

    @Test
    void testGetPieceCount() {
        assertEquals(8,state1.getPieceCount());
        assertEquals(state1.getPieceCount(),state2.getPieceCount());
        assertEquals(state1.getPieceCount(),state5.getPieceCount());
    }

    @Test
    void testGetPieceType() {
        assertEquals(PieceType.BLUE, state1.getPieceType(0));
        assertEquals(PieceType.BLUE, state1.getPieceType(1));
        assertEquals(PieceType.BLUE, state1.getPieceType(2));
        assertEquals(PieceType.BLUE, state1.getPieceType(3));
        assertEquals(PieceType.RED, state1.getPieceType(4));
        assertEquals(PieceType.RED, state1.getPieceType(5));
        assertEquals(PieceType.RED, state1.getPieceType(6));
        assertEquals(PieceType.RED, state1.getPieceType(7));
    }

    @Test
    void testGetPiecePosition() {
        assertEquals(new Position(0,3), state1.getPiecePosition(0));
        assertEquals(new Position(1,2), state1.getPiecePosition(1));
        assertEquals(new Position(2,1), state1.getPiecePosition(2));
        assertEquals(new Position(3,0), state1.getPiecePosition(3));
        assertEquals(new Position(0,0), state1.getPiecePosition(4));
        assertEquals(new Position(1,1), state1.getPiecePosition(5));
        assertEquals(new Position(2,2), state1.getPiecePosition(6));
        assertEquals(new Position(3,3), state1.getPiecePosition(7));
    }

    @Test
    void testIsOccupiedTile() {
        assertTrue(state1.isOccupiedTile(new Position(0,0)));
        assertFalse(state1.isOccupiedTile(new Position(1,0)));
    }

    @Test
    void testGetPieceNumber() {
        assertEquals(OptionalInt.of(0), state1.getPieceNumber(new Position(0,3)));
        assertEquals(OptionalInt.of(1), state1.getPieceNumber(new Position(1,2)));
        assertEquals(OptionalInt.of(2), state1.getPieceNumber(new Position(2,1)));
        assertEquals(OptionalInt.of(3), state1.getPieceNumber(new Position(3,0)));
        assertEquals(OptionalInt.of(4), state1.getPieceNumber(new Position(0,0)));
        assertEquals(OptionalInt.of(5), state1.getPieceNumber(new Position(1,1)));
        assertEquals(OptionalInt.of(6), state1.getPieceNumber(new Position(2,2)));
        assertEquals(OptionalInt.of(7), state1.getPieceNumber(new Position(3,3)));
        assertEquals(OptionalInt.empty(), state1.getPieceNumber(new Position(2,3)));
    }

    @Test
    void testGetPlayerPositions() {
        List<Position> playerPositions = new ArrayList<>();
        playerPositions.add(new Position(0,0));
        playerPositions.add(new Position(1,1));
        playerPositions.add(new Position(2,2));
        playerPositions.add(new Position(3,3));
        assertEquals(playerPositions, state1.getPlayerPositions());
    }

    @Test
    void testGetValidMoves() {
        List<Position> expectedValidMoves = new ArrayList<>();
        List<Position> realValidMoves = state1.getValidMoves(4);
        expectedValidMoves.add(new Position(0,1));
        expectedValidMoves.add(new Position(0,2));
        expectedValidMoves.add(new Position(1,0));
        expectedValidMoves.add(new Position(2,0));
        assertEquals(expectedValidMoves.size(), realValidMoves.size());
        assertTrue(realValidMoves.containsAll(expectedValidMoves));
    }

    @Test
    void testMoveUpLeft() {
        var oldPosition = state1.getPiecePosition(2);
        state1.move(oldPosition, DiskDirection.UP_LEFT, new Position(1,0));
        assertEquals(new Position(oldPosition.row()+DiskDirection.UP_LEFT.getRowChange(), oldPosition.col()+DiskDirection.UP_LEFT.getColChange()), state1.getPiecePosition(2));
    }

    @Test
    void testMoveUp() {
        var oldPosition = state1.getPiecePosition(1);
        state1.move(oldPosition, DiskDirection.UP, new Position(0,2));
        assertEquals(new Position(oldPosition.row()+DiskDirection.UP.getRowChange(), oldPosition.col()+DiskDirection.UP.getColChange()), state1.getPiecePosition(1));
    }

    @Test
    void testMoveUpRight() {
        var oldPosition = state1.getPiecePosition(5);
        state1.move(oldPosition, DiskDirection.UP_RIGHT, new Position(0,2));
        assertEquals(new Position(oldPosition.row()+DiskDirection.UP_RIGHT.getRowChange(), oldPosition.col()+DiskDirection.UP_RIGHT.getColChange()), state1.getPiecePosition(5));
    }

    @Test
    void testMoveRight() {
        var oldPosition = state1.getPiecePosition(1);
        state1.move(oldPosition, DiskDirection.RIGHT, new Position(1,3));
        assertEquals(new Position(oldPosition.row()+DiskDirection.RIGHT.getRowChange(), oldPosition.col()+DiskDirection.RIGHT.getColChange()), state1.getPiecePosition(1));
    }

    @Test
    void testMoveDownRight() {
        var oldPosition = state1.getPiecePosition(1);
        state1.move(oldPosition, DiskDirection.DOWN_RIGHT, new Position(2,3));
        assertEquals(new Position(oldPosition.row()+DiskDirection.DOWN_RIGHT.getRowChange(), oldPosition.col()+DiskDirection.DOWN_RIGHT.getColChange()), state1.getPiecePosition(1));
    }

    @Test
    void testMoveDown() {
        var oldPosition = state1.getPiecePosition(0);
        state1.move(oldPosition, DiskDirection.DOWN, new Position(1,3));
        assertEquals(new Position(oldPosition.row()+DiskDirection.DOWN.getRowChange(), oldPosition.col()+DiskDirection.DOWN.getColChange()), state1.getPiecePosition(0));
    }

    @Test
    void testMoveDownLeft() {
        var oldPosition = state1.getPiecePosition(5);
        state1.move(oldPosition, DiskDirection.DOWN_LEFT, new Position(2,0));
        assertEquals(new Position(oldPosition.row()+DiskDirection.DOWN_LEFT.getRowChange(), oldPosition.col()+DiskDirection.DOWN_LEFT.getColChange()), state1.getPiecePosition(5));
    }

    @Test
    void testMoveLeft() {
        var oldPosition = state1.getPiecePosition(5);
        state1.move(oldPosition, DiskDirection.LEFT, new Position(1,0));
        assertEquals(new Position(oldPosition.row()+DiskDirection.LEFT.getRowChange(), oldPosition.col()+DiskDirection.LEFT.getColChange()), state1.getPiecePosition(5));
    }

    @Test
    void testGetTotalSteps() {
        var oldPosition = state1.getPiecePosition(5);
        var oldPosition2 = state1.getPiecePosition(0);
        state1.move(oldPosition, DiskDirection.LEFT, new Position(1,0));
        state1.move(oldPosition2, DiskDirection.DOWN, new Position(1,3));
        assertEquals(2, state1.getTotalSteps());
    }

    @Test
    void testCheckPieces() {
        assertThrows(IllegalArgumentException.class, () -> {MyBoardGameModel state8 = new MyBoardGameModel( new Piece(PieceType.BLUE, new Position(0, 3)),
                new Piece(PieceType.BLUE, new Position(1, 3)),
                new Piece(PieceType.BLUE, new Position(0, 0)),
                new Piece(PieceType.BLUE, new Position(1, 0)),
                new Piece(PieceType.RED, new Position(0, 3)),
                new Piece(PieceType.RED, new Position(1, 1)),
                new Piece(PieceType.RED, new Position(2, 2)),
                new Piece(PieceType.RED, new Position(2, 0)));});
    }

    @Test
    void testToString() {
        assertEquals("[BLUE(0,3),BLUE(1,2),BLUE(2,1),BLUE(3,0),RED(0,0),RED(1,1),RED(2,2),RED(3,3)]",state1.toString());
    }
}
