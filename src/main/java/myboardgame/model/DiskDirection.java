package myboardgame.model;

/**
 * Represents the eight main directions.
 */
public enum DiskDirection implements Direction {

    UP_LEFT(-1, -1),
    UP(-1, 0),
    UP_RIGHT(-1, 1),
    RIGHT(0, 1),
    DOWN_RIGHT(1, 1),
    DOWN(1, 0),
    DOWN_LEFT(1, -1),
    LEFT(0, -1);

    private final int rowChange;
    private final int colChange;

    DiskDirection(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    /**
     * {@return the change in the row coordinate when moving towards the direction}
     */
    public int getRowChange() {
        return rowChange;
    }

    /**
     * {@return the change in the column coordinate when moving towards the direction}
     */
    public int getColChange() {
        return colChange;
    }

    /**
     * {@return the direction that corresponds to the coordinate changes}
     * @param rowChange the change in the row coordinate
     * @param colChange the change in the column coordinate
     */
    public static DiskDirection of(int rowChange, int colChange) {
        /*for (var direction : values()) {
            if (direction.rowChange == rowChange && direction.colChange == colChange) {
                return direction;
            }
        }
        throw new IllegalArgumentException();*/
        if(rowChange == 0 && colChange > 0) return DiskDirection.RIGHT;
        if(rowChange > 0 && colChange > 0) return DiskDirection.DOWN_RIGHT;
        if(rowChange > 0 && colChange == 0) return DiskDirection.DOWN;
        if(rowChange > 0 && colChange < 0) return DiskDirection.DOWN_LEFT;
        if(rowChange == 0 && colChange < 0) return DiskDirection.LEFT;
        if(rowChange < 0 && colChange < 0) return DiskDirection.UP_LEFT;
        if(rowChange < 0 && colChange == 0) return DiskDirection.UP;
        if(rowChange < 0 && colChange > 0) return DiskDirection.UP_RIGHT;

        throw new IllegalArgumentException();
    }

}
