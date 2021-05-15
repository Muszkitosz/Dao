package myboardgame.model;

import javafx.beans.property.*;
import javafx.scene.paint.Color;
import org.tinylog.Logger;

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

    private ReadOnlyIntegerWrapper totalSteps = new ReadOnlyIntegerWrapper();

    public ReadOnlyIntegerProperty totalStepsProperty() {
        return totalSteps.getReadOnlyProperty();
    }

    public int getTotalSteps() {
        return totalSteps.get();
    }

    /**
     * The size of the board.
     */
    public static int BOARD_SIZE = 4;

    private final Piece[] pieces;

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
        totalSteps.set(0);
        nextPlayer.set(Player.PLAYER_RED);
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

    public boolean isValidMove(int pieceNumber, DiskDirection direction) {


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

    public void addValidPositionsOnDirection (Position start , DiskDirection direction, List<Position> results) {
        Position next = start.moveTo(direction);
            if (isOnBoard(next) && !isOccupiedTile(next)) {
                results.add(next);
                addValidPositionsOnDirection(next, direction, results);
            }
    }

    public boolean isOccupiedTile (Position pos) {
        for (var piece : pieces) {
            if (piece.getPosition().row() == pos.row() && piece.getPosition().col() == pos.col()) {
                return true;
            }
        }
        return false;
    }

    public List<Position> getValidMoves(int pieceNumber) {
        List<Position> validMoves = new ArrayList<Position>();
        PieceType pieceType = pieces[pieceNumber].getType();
        Player player = pieceType == PieceType.RED ? Player.PLAYER_RED : Player.PLAYER_BLUE;
        if (player != nextPlayer.get()) {
            return validMoves;
        }
        for (var direction : DiskDirection.values()) {
                /*if (isValidMove(pieceNumber, direction)) {
                    validMoves.add(direction);
                } */
                addValidPositionsOnDirection(pieces[pieceNumber].getPosition(), direction, validMoves);
        }
        return validMoves;
    }

    public void move(Position startpos, DiskDirection direction, Position destination) {
        var pieceNumber = getPieceNumber(startpos).getAsInt();
        while (pieces[pieceNumber].getPosition().row() != destination.row() || pieces[pieceNumber].getPosition().col() != destination.col()) {
            pieces[pieceNumber].moveTo(direction);
        }
            totalSteps.set(totalSteps.get() + 1);
            nextPlayer.set(nextPlayer.get().alter());
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

    public List<Position> getPlayerPositions() {
        List<Position> positions = new ArrayList<>();
        PieceType pieceType = nextPlayer.get() == Player.PLAYER_RED ? PieceType.RED : PieceType.BLUE;
        for (var piece : pieces) {
            if (piece.getType() == pieceType) {
                positions.add(piece.getPosition());
            }
        }
        return positions;
    }

    /**
     * {@return whether the puzzle is solved}
     * @param color the color of the PieceType
     */
    public boolean isGoal(PieceType color) {

        if (Arrays.stream(pieces).filter(p -> p.getType()==color).map(p -> p.getPosition().row()).distinct().count() == 1) {
            Logger.debug("All "+color+" pieces are in the same row, gg!");
            return true;
        }
        if (Arrays.stream(pieces).filter(p -> p.getType()==color).map(p -> p.getPosition().col()).distinct().count() == 1) {
            Logger.debug("All "+color+" pieces are in the same column, gg!");
            return true;
        }
        List<Position> positions = Arrays.stream(pieces).filter(p -> p.getType()==color).map(p -> p.getPosition()).collect(Collectors.toList());
        int minrow=Integer.MAX_VALUE;
        int maxrow=Integer.MIN_VALUE;
        int mincol=Integer.MAX_VALUE;
        int maxcol=Integer.MIN_VALUE;
        int counter = 0;
        for (int i=0; i < positions.size(); i++) {
            if (positions.get(i).row() == 0 && positions.get(i).col() == 0) {
                counter++;
            }
            if (positions.get(i).row() == BOARD_SIZE-1 && positions.get(i).col() == 0) {
                counter++;
            }
            if (positions.get(i).row() == 0 && positions.get(i).col() == BOARD_SIZE-1) {
                counter++;
            }
            if (positions.get(i).row() == BOARD_SIZE-1 && positions.get(i).col() == BOARD_SIZE-1) {
                counter++;
            }
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
        if (counter == 4) {
            Logger.debug("Each "+color+" pieces are in different corners, gg!");
            return true;
        }
        if (maxrow-minrow == 1 && maxcol-mincol == 1) {
            Logger.debug("The "+color+" pieces are forming a 2 by 2 square shape, gg!");
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
                        Logger.debug("One of the "+color+" pieces is cornered by the other player, which is not allowed so gg for the Player_"+color+"!");
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
