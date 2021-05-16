package myboardgame.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import myboardgame.helper.User;
import myboardgame.helper.UserDao;
import org.tinylog.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;


public class LeaderBoardController {

    UserDao userDao = new UserDao();

    @FXML
    private GridPane leaderBoard;

    @FXML
    void initialize() {
        addToLeaderboard();
    }

    @FXML
    private void switchBackToMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/first.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
        Logger.debug("Click on Back");
        Logger.info("Switching back to Main menu...");
    }

    @FXML
    private void resetLeaderboard(ActionEvent event) throws IOException {
        userDao.resetUsers();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/leaderboard.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
        Logger.debug("Click on Reset Leaderboard");
        Logger.info("Reseting Leaderboard...");
    }

    private void addToLeaderboard() {
        List<User> topUsers = userDao.getTopUsers();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

        for (int i=1; i < 16; i++) {

            if (i-1 < userDao.getTopUsers().size()) {
                leaderBoard.add(new Label(topUsers.get(i-1).getUserName()), 0, i);
                leaderBoard.add(new Label(Integer.toString(topUsers.get(i-1).getTotalSteps())),1, i);
                leaderBoard.add(new Label(ZonedDateTime.now().format(formatter)), 2, i);
            }
        }
    }
}
