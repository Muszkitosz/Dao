package myboardgame.helper;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing a match.
 */
@Data
@NoArgsConstructor
public class User {

    /**
     * The name of the player, which will be the winner's name.
     */
    private String userName;

    /**
     * The total steps made during the game.
     */
    private int totalSteps;

    /**
     * The date when the game was played
     */
    private String issueDate;

    /**
     * Indicates a player with specified information.
     * @param userName is the name of the player
     * @param totalSteps is a number which indicates the total steps made during a match
     * @param issueDate is the date when a match was played
     */
    public User(String userName, int totalSteps, String issueDate) {
        this.userName = userName;
        this.totalSteps = totalSteps;
        this.issueDate = issueDate;
    }



}
