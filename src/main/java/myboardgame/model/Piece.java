package myboardgame.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * A class for representing Pieces.
 */
public class Piece {
    /**
     * The Type of the piece.
     */
    private final PieceType type;

    /**
     * The {@code Position} of the piece.
     */
    private final ObjectProperty<Position> position = new SimpleObjectProperty<>();

    /**
     * Constructs a {@code Piece} object.
     * @param type the {@code type} of the piece
     * @param position the {@code position} where you want to construct the piece
     */
    public Piece(PieceType type, Position position) {
        this.type = type;
        this.position.set(position);
    }

    /**
     * Returns the {@code type} of the {@code piece} as a {@code PieceType}.
     * @return the type of the piece
     */
    public PieceType getType() {
        return type;
    }

    /**
     * Returns the {@code Position} of the {@code piece} as a {@code Position} object.
     * @return the position of the piece
     */
    public Position getPosition() {
        return position.get();
    }

    /**
     * Moves the piece by the {@code Direction}.
     * @param direction is the direction you are moving towards
     */
    public void moveTo(Direction direction) {
        Position newPosition = position.get().moveTo(direction);
        position.set(newPosition);
    }

    /**
     * Returns the {@code Position} of the {@code piece} as a {@code Position Object Property}.
     * @return the position of the piece
     */
    public ObjectProperty<Position> positionProperty() {
        return position;
    }

    /**
     * Returns the string representation of this object.
     * @return the string representation of this position
     */
    public String toString() {
        return type.toString() + position.get().toString();
    }
}
