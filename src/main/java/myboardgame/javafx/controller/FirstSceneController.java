package myboardgame.javafx.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class FirstSceneController {

    @FXML
    private TextField redPlayerName;

    @FXML
    private TextField bluePlayerName;

    @FXML
    private void initialize() {
    }

    @FXML
    private void switchScene(ActionEvent event) throws IOException {

        if (redPlayerName.getText().isEmpty() || bluePlayerName.getText().isEmpty()) {
            if (redPlayerName.getText().isEmpty()) {
                redPlayerName.setPromptText("Please enter a name!");
            }
            if (bluePlayerName.getText().isEmpty()) {
                bluePlayerName.setPromptText("Please enter a name!");
            }
        } else {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ui.fxml"));
            Parent root = fxmlLoader.load();
            MyBoardGameController mainController = fxmlLoader.getController();
            mainController.setRedPlayerName(redPlayerName.getText());
            mainController.setBluePlayerName(bluePlayerName.getText());
            stage.setScene(new Scene(root));
            stage.show();
            Logger.debug("The red player's name is set to: "+redPlayerName.getText());
            Logger.debug("The blue player's name is set to: "+bluePlayerName.getText());
            Logger.debug("Click on Start game");
            Logger.info("Starting the game...");
        }
    }

    @FXML
    private void switchToAbout(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/about.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
        Logger.debug("Click on About");
    }

    @FXML
    private void quitScene() {
        Platform.exit();
        Logger.debug("Click on quit");
        Logger.info("Exiting...");
    }

    @FXML
    private void switchToLeaderboard(ActionEvent event) throws  IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/leaderboard.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
        Logger.debug("Click on Leaderboard");
        Logger.info("Loading leaderboard...");
    }

    @FXML
    private void clearRedName() {
        redPlayerName.clear();
    }

    @FXML
    private void clearBlueName() {
        bluePlayerName.clear();
    }
}
