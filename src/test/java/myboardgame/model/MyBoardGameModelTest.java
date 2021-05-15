package myboardgame.model;

import org.junit.jupiter.api.Test;

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
}
