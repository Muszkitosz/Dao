package myboardgame.helper;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class User {

    private String userName;

    private int totalSteps;

    private String issueDate;

    public User(String userName, int totalSteps, String issueDate) {
        this.userName = userName;
        this.totalSteps = totalSteps;
        this.issueDate = issueDate;
    }



}
