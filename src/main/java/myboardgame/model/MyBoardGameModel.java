package myboardgame.model;

import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;

import java.util.*;

public class MyBoardGameModel {

    public static int BOARD_SIZE = 4;

    private final Piece[] pieces;

    public static int Red[] = {0, 1, 2, 3};

    public static int Blue[] = {0, 1, 2, 3};

    public MyBoardGameModel() {
        this(new Piece(PieceType.BLUE, new Position(0, 3)),
                new Piece(PieceType.BLUE, new Position(1, 2)),
                new Piece(PieceType.BLUE, new Position(2, 1)),
                new Piece(PieceType.BLUE, new Position(3, 0)),
                new Piece(PieceType.RED, new Position(0, 0)),
                new Piece(PieceType.RED, new Position(1, 1)),
                new Piece(PieceType.RED, new Position(2, 2)),
                new Piece(PieceType.RED, new Position(3, 3)));
    }

    public MyBoardGameModel(Piece... pieces) {
        checkPieces(pieces);
        this.pieces = pieces.clone();
    }

    private void checkPieces(Piece[] pieces) {
        var seen = new HashSet<Position>();
        for (var piece : pieces) {
            if (!isOnBoard(piece.getPosition()) || seen.contains(piece.getPosition())) {
                throw new IllegalArgumentException();
            }
            seen.add(piece.getPosition());
        }
    }

    public int getPieceCount() {
        return pieces.length;
    }

    public PieceType getPieceType(int pieceNumber) {
        return pieces[pieceNumber].getType();
    }

    public Position getPiecePosition(int pieceNumber) {
        return pieces[pieceNumber].getPosition();
    }

    public ObjectProperty<Position> positionProperty(int pieceNumber) {
        return pieces[pieceNumber].positionProperty();
    }

    public boolean isValidMove(int pieceNumber, DiskDirection direction, int movelength) {

        int counter = 0;

        if (pieceNumber < 0 || pieceNumber >= pieces.length) {
            throw new IllegalArgumentException();
        }
        Position newPosition = pieces[pieceNumber].getPosition().moveTo(direction);
        if (!isOnBoard(newPosition)) {
            return false;
        }
        for (var piece : pieces) {
            if (piece.getPosition().equals(newPosition)) {
                return false;
            }
        }

        Position next = getPiecePosition(pieceNumber);

        while (counter <= movelength) {
            if (getPieceNumber(next.moveTo(direction)).isEmpty()) {
                counter++;
                next = next.moveTo(direction);
            }
            else {return false;}
        }
        return true;
    }

    public Set<DiskDirection> getValidMoves(int pieceNumber) {
        EnumSet<DiskDirection> validMoves = EnumSet.noneOf(DiskDirection.class);
        for (var direction : DiskDirection.values()) {
            for (int m = 1; m < BOARD_SIZE; m++)
                if (isValidMove(pieceNumber, direction, m)) {
                    validMoves.add(direction);
                }
        }
        return validMoves;
    }

    public void move(int pieceNumber, DiskDirection direction) {
        pieces[pieceNumber].moveTo(direction);
    }

    public static boolean isOnBoard(Position position) {
        return 0 <= position.row() && position.row() < BOARD_SIZE
                && 0 <= position.col() && position.col() < BOARD_SIZE;
    }

    public List<Position> getPiecePositions() {
        List<Position> positions = new ArrayList<>(pieces.length);
        for (var piece : pieces) {
            positions.add(piece.getPosition());
        }
        return positions;
    }

    public OptionalInt getPieceNumber(Position position) {
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (var piece : pieces) {
            joiner.add(piece.toString());
        }
        return joiner.toString();
    }
    /*
    public boolean isGoal() {
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i].getType() == PieceType.BLUE && )
        }
    }
    */

    public static void main(String[] args) {
        MyBoardGameModel model = new MyBoardGameModel();
        System.out.println(model);
    }

}