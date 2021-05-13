package myboardgame.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;

public class MyBoardGameModel {

    public enum Player{
        PLAYER_RED, PLAYER_BLUE;

        public Player alter() {
            return switch (this) {
                case PLAYER_RED -> PLAYER_BLUE;
                case PLAYER_BLUE -> PLAYER_RED;
            };
        }
    }

    private ReadOnlyObjectWrapper<Player> nextPlayer = new ReadOnlyObjectWrapper<>();

    /**
     * The size of the board.
     */
    public static int BOARD_SIZE = 4;

    private final Piece[] pieces;

    public static int Red[] = {0, 1, 2, 3};

    public static int Blue[] = {0, 1, 2, 3};

    /**
     * Creates a {@code MyBoardGameModel} object that corresponds to the original initial state of the game.
     */
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

    public record Move(DiskDirection direction, int steps) {
        public Move {
            if (steps <=0) {
                throw new IllegalArgumentException();
            }
        }
        public String toString() {
            return String.format("%s(%d)", direction, steps);
        }
    }

    public boolean isValidMove(int pieceNumber, DiskDirection direction) {

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
        /*
        Position next = getPiecePosition(pieceNumber);

        while (counter <= movelength) {
            if (getPieceNumber(next.moveTo(direction)).isEmpty()) {
                counter++;
                next = next.moveTo(direction);
            }
            else {return false;}
        }
        System.out.println(direction+":"+counter+"\n");
         */
        return true;
    }
    //for (int m = 1; m < BOARD_SIZE; m++) if előtt, ifben direction után m
    public Set<DiskDirection> getValidMoves(int pieceNumber) {
        EnumSet<DiskDirection> validMoves = EnumSet.noneOf(DiskDirection.class);
        for (var direction : DiskDirection.values()) {
                if (isValidMove(pieceNumber, direction)) {
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

    /**
     * {@return whether the puzzle is solved}
     * @param color the color of the PieceType
     */
    public boolean isGoal(PieceType color) {

        if (Arrays.stream(pieces).filter(p -> p.getType()==color).map(p -> p.getPosition().row()).distinct().count() == 1) {
            System.out.println("ggrow");
            return true;
        }
        if (Arrays.stream(pieces).filter(p -> p.getType()==color).map(p -> p.getPosition().col()).distinct().count() == 1) {
            System.out.println("ggcol");
            return true;
        }
        List<Position> positions = Arrays.stream(pieces).filter(p -> p.getType()==color).map(p -> p.getPosition()).collect(Collectors.toList());
        int minrow=Integer.MAX_VALUE;
        int maxrow=Integer.MIN_VALUE;
        int mincol=Integer.MAX_VALUE;
        int maxcol=Integer.MIN_VALUE;
        for (int i=0; i < positions.size(); i++) {
            if (positions.get(i).row() < minrow) {
                minrow = positions.get(i).row();
            }
            if (positions.get(i).row() > maxrow) {
                maxrow = positions.get(i).row();
            }
            if (positions.get(i).col() < mincol) {
                mincol = positions.get(i).col();
            }
            if (positions.get(i).col() > maxcol) {
                maxcol = positions.get(i).col();
            }
        }
        if (maxrow-minrow == 1 && maxcol-mincol == 1) {
            System.out.println("gg");
            return true;
        }
        List<Position> positions2 = Arrays.stream(pieces).filter(p -> p.getType()==color).map(p -> p.getPosition()).collect(Collectors.toList());
        List<Position> positions3 = Arrays.stream(pieces).filter(p -> p.getType()!=color).map(p -> p.getPosition()).collect(Collectors.toList());
        for (int i=0; i< positions2.size(); i++) {
                List<Position> validMoves = new ArrayList<>();
                for (var direction : DiskDirection.values()) {
                    Position newPosition = positions2.get(i).moveTo(direction);
                    if (isOnBoard(newPosition)) {
                        validMoves.add(newPosition);
                    }
                }
                for (int j=0; j < validMoves.size(); j++) {
                    if (positions3.containsAll(validMoves)) {
                        System.out.println("win "+color);
                        return true;
                    }
                }
            }
        return false;
    }


    public static void main(String[] args) {
        MyBoardGameModel model = new MyBoardGameModel();
        System.out.println(model);
    }

}
