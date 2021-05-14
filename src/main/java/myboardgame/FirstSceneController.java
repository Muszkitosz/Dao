package myboardgame;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class FirstSceneController {

    @FXML
    private TextField redPlayerName;

    @FXML
    private TextField bluePlayerName;

    @FXML
    private void switchScene(ActionEvent event) throws IOException {

        if (redPlayerName.getText().isEmpty() || bluePlayerName.getText().isEmpty()) {
            if (redPlayerName.getText().isEmpty()) {
                redPlayerName.setText("Please enter a name!");
            }
            if (bluePlayerName.getText().isEmpty()) {
                bluePlayerName.setText("Please enter a name!");
            }
        } else {

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ui.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    @FXML
    private void quitScene() {
        Platform.exit();
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
