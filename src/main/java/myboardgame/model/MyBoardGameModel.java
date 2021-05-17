package myboardgame.model;

import javafx.beans.property.*;
import javafx.scene.paint.Color;
import org.tinylog.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representing the state of the game.
 */
public class MyBoardGameModel {

    /**
     * Enum representing the two players.
     */
    public enum Player{
        PLAYER_RED, PLAYER_BLUE;

        /**
         * Swaps the colors.
         * @return the other color
         */
        public Player alter() {
            return switch (this) {
                case PLAYER_RED -> PLAYER_BLUE;
                case PLAYER_BLUE -> PLAYER_RED;
            };
        }
    }

    /**
     * Indicates that whose turn it is.
     */
    private ReadOnlyObjectWrapper<Player> nextPlayer = new ReadOnlyObjectWrapper<>();

    /**
     * Returns the next player.
     * @return the next player
     */
    public ReadOnlyObjectProperty<Player> nextPlayerProperty() {return nextPlayer.getReadOnlyProperty();}

    /**
     * Indicates the total steps made.
     */
    private ReadOnlyIntegerWrapper totalSteps = new ReadOnlyIntegerWrapper();

    /**
     * Returns the total steps made.
     * @return the total steps made
     */
    public ReadOnlyIntegerProperty totalStepsProperty() {
        return totalSteps.getReadOnlyProperty();
    }

    /**
     * Returns the number of total steps.
     * @return the total steps made
     */
    public int getTotalSteps() {
        return totalSteps.get();
    }

    /**
     * The size of the board.
     */
    public static int BOARD_SIZE = 4;

    /**
     * An {@code array} that contains the {@code pieces}.
     */
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

    /**
     * Constructs a model by giving the {@code pieces} to a {@code list} one-by-one.
     * @param pieces the {@code list} that contains all {@code pieces}
     */
    public MyBoardGameModel(Piece... pieces) {
        checkPieces(pieces);
        this.pieces = pieces.clone();
    }

    /**
     * Checks if the positions of the {@code pieces} that you gave for the constructor are valid or not.
     * @param pieces an array which contains the {@code pieces}
     */
    private void checkPieces(Piece[] pieces) {
        var seen = new HashSet<Position>();
        for (var piece : pieces) {
            if (!isOnBoard(piece.getPosition()) || seen.contains(piece.getPosition())) {
                throw new IllegalArgumentException();
            }
            seen.add(piece.getPosition());
        }
    }

    /**
     * Returns the number of pieces on the board.
     * @return the number of pieces on the board
     */
    public int getPieceCount() {
        return pieces.length;
    }

    /**
     * Returns the type of a specific {@code piece}.
     * @param pieceNumber refers to a specific {@code piece}
     * @return the {@code PieceType} of a specific {@code piece}
     */
    public PieceType getPieceType(int pieceNumber) {
        return pieces[pieceNumber].getType();
    }

    /**
     * Returns the {@code position} of a specific {@code piece}.
     * @param pieceNumber refers to a specific {@code piece}
     * @return the {@code position} of a specific {@code piece}
     */
    public Position getPiecePosition(int pieceNumber) {
        return pieces[pieceNumber].getPosition();
    }

    /**
     * Returns a {@code positionProperty} specified to a {@code piece}.
     * @param pieceNumber specifies a {@code piece}
     * @return a {@code positionProperty} specified to a {@code piece}
     */
    public ObjectProperty<Position> positionProperty(int pieceNumber) {
        return pieces[pieceNumber].positionProperty();
    }

    /**
     * Adds the available positions to a list specified to a start position.
     * @param start is the position of a {@code piece} which we want to move
     * @param direction is the direction we want to move towards
     * @param results is a list which contains the number of steps available towards a specified direction
     */
    private void addValidPositionsOnDirection (Position start , DiskDirection direction, List<Position> results) {
        Position next = start.moveTo(direction);
            if (isOnBoard(next) && !isOccupiedTile(next)) {
                results.add(next);
                addValidPositionsOnDirection(next, direction, results);
            }
    }

    /**
     * Returns true if a {@code position} is occupied, else returns false.
     * @param pos is a {@code position} on the board
     * @return if a tile is occupied or not
     */
    public boolean isOccupiedTile (Position pos) {
        for (var piece : pieces) {
            if (piece.getPosition().row() == pos.row() && piece.getPosition().col() == pos.col()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the valid move for the current game state.
     * @param pieceNumber refers to a specific {@code piece}
     * @return the valid moves in a {@code list}
     */
    public List<Position> getValidMoves(int pieceNumber) {
        List<Position> validMoves = new ArrayList<Position>();
        PieceType pieceType = pieces[pieceNumber].getType();
        Player player = pieceType == PieceType.RED ? Player.PLAYER_RED : Player.PLAYER_BLUE;
        if (player != nextPlayer.get()) {
            return validMoves;
        }
        for (var direction : DiskDirection.values()) {
                addValidPositionsOnDirection(pieces[pieceNumber].getPosition(), direction, validMoves);
        }
        return validMoves;
    }

    /**
     * Moves a {@code piece} to a destination.
     * @param startpos is the position of the current {@code piece}
     * @param direction is the way you want to move the {@code piece}
     * @param destination is the position where you want to move the {@code piece}
     */
    public void move(Position startpos, DiskDirection direction, Position destination) {
        var pieceNumber = getPieceNumber(startpos).getAsInt();
        while (pieces[pieceNumber].getPosition().row() != destination.row() || pieces[pieceNumber].getPosition().col() != destination.col()) {
            pieces[pieceNumber].moveTo(direction);
        }
            totalSteps.set(totalSteps.get() + 1);
            nextPlayer.set(nextPlayer.get().alter());
    }

    /**
     * Checks if a {@code position} is on the board or not.
     * @param position refers to a {@code position}
     * @return true if the {@code position} is on the board, otherwise false
     */
    public static boolean isOnBoard(Position position) {
        return 0 <= position.row() && position.row() < BOARD_SIZE
                && 0 <= position.col() && position.col() < BOARD_SIZE;
    }

    /**
     * Returns if a position contains a {@code piece} or not
     * @param position is the {@code position} what we are checking in each cycle
     * @return an {@code OptionalInt} if a position contains a {@code piece}
     */
    public OptionalInt getPieceNumber(Position position) {
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    /**
     * Returns a {@code list} which contains all the {@code piece} {@code positions} for the player in turn.
     * @return the positions of the {@code pieces} specified for the player in turn
     */
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

    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (var piece : pieces) {
            joiner.add(piece.toString());
        }
        return joiner.toString();
    }

    public static void main(String[] args) {
        MyBoardGameModel model = new MyBoardGameModel();
        System.out.println(model);
    }

}
