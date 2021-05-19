package myboardgame.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import myboardgame.helper.Player;
import myboardgame.helper.PlayerDao;
import org.tinylog.Logger;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;


public class LeaderBoardController {

    PlayerDao playerDao = new PlayerDao();

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
        Logger.debug("Click on Reset Leaderboard");
        playerDao.resetPlayersList();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/leaderboard.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void addToLeaderboard() {
        List<Player> topPlayers = playerDao.getTopPlayers();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

        for (int i=1; i < 16; i++) {

            if (i-1 < playerDao.getTopPlayers().size()) {
                leaderBoard.add(new Label(topPlayers.get(i-1).getPlayerName()), 0, i);
                leaderBoard.add(new Label(Integer.toString(topPlayers.get(i-1).getTotalSteps())),1, i);
                leaderBoard.add(new Label(ZonedDateTime.now().format(formatter)), 2, i);
            }
        }
    }
}
