package myboardgame.helper;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing a match.
 */
@Data
@NoArgsConstructor
public class Player {

    /**
     * The name of the player, which will be the winner's name.
     */
    private String playerName;

    /**
     * The total steps made during the game.
     */
    private int totalSteps;

    /**
     * The date when the game was played.
     */
    private String issueDate;

    /**
     * Indicates a player with specified information.
     * @param playerName is the name of the player
     * @param totalSteps is a number which indicates the total steps made during a match
     * @param issueDate is the date when a match was played
     */
    public Player(String playerName, int totalSteps, String issueDate) {
        this.playerName = playerName;
        this.totalSteps = totalSteps;
        this.issueDate = issueDate;
    }



}
