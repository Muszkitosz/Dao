package myboardgame.model;

/**
 * A java interface for the {@code Directions}.
 */
public interface Direction {
    /**
     * Returns the change of the row after moving.
     * @return the change of the row after moving
     */
    int getRowChange();

    /**
     * Returns the change of the column after moving.
     * @return the change of the column after moving
     */
    int getColChange();
}
