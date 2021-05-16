package myboardgame.model;

/**
 * Represents a 2D position.
 */
public record Position(int row, int col) {

    /**
     * Moves a {@code Piece} by {@code Direction}.
     * {@return the position whose vertical and horizontal distances from this position are
     * equal to the coordinate changes of the direction given}
     * @param direction a direction that specifies a change in the coordinates
     */
    public Position moveTo(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }

    /**
     * Returns the string representation of this object.
     * @return the string representation of this position
     */
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }

}