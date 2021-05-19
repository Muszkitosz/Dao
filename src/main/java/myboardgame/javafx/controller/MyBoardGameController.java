package myboardgame.javafx.controller;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


import javafx.stage.Stage;
import myboardgame.helper.Player;
import myboardgame.helper.PlayerDao;
import myboardgame.model.*;
import org.tinylog.Logger;

import static java.lang.Math.abs;

public class MyBoardGameController {

    private enum SelectionPhase {
        SELECT_FROM,
        SELECT_TO;

        public SelectionPhase alter() {
            return switch (this) {
                case SELECT_FROM -> SELECT_TO;
                case SELECT_TO -> SELECT_FROM;
            };
        }
    }

    private SelectionPhase selectionPhase = SelectionPhase.SELECT_FROM;

    private List<Position> selectablePositions = new ArrayList<>();

    private Position selected;

    private MyBoardGameModel model = new MyBoardGameModel();

    PlayerDao playerDao = new PlayerDao();

    @FXML
    private GridPane board;

    @FXML
    private TextField totalSteps;

    @FXML
    private TextField showCurrentPlayer;

    private String redPlayerName;

    private String bluePlayerName;

    public void setRedPlayerName(String redPlayerName) {
        this.redPlayerName = redPlayerName;
    }

    public void setBluePlayerName(String bluePlayerName) {
        this.bluePlayerName = bluePlayerName;
    }

    public String winnerName = "";

    @FXML
    private void initialize() {
        board.getStyleClass().add("board");
        createBoard();
        createPieces();
        setSelectablePositions();
        showSelectablePositions();
        totalSteps.textProperty().bind(model.totalStepsProperty().asString());
        showCurrentPlayer.styleProperty().bind(Bindings.when(model.nextPlayerProperty().isEqualTo(MyBoardGameModel.Player.PLAYER_BLUE)).then("-fx-text-fill:blue").otherwise("-fx-text-fill:red"));
        showCurrentPlayer.textProperty().bind(model.nextPlayerProperty().asString());
    }

    private void createBoard() {
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare();
                board.add(square, j, i);
            }
        }
    }

    private StackPane createSquare() {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    private void createPieces() {
        for (int i = 0; i < model.getPieceCount(); i++) {
            model.positionProperty(i).addListener(this::piecePositionChange);
            var piece = createPiece(Color.valueOf(model.getPieceType(i).name()));
            getSquare(model.getPiecePosition(i)).getChildren().add(piece);
        }
    }

    private Circle createPiece(Color color) {
        var piece = new Circle(50);
        piece.setFill(color);
        return piece;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        var position = new Position(row, col);
        Logger.debug("Clicked on square {}", position);
        handleClickOnSquare(position);
    }

    private void handleClickOnSquare(Position position) {
        switch (selectionPhase) {
            case SELECT_FROM -> {
                if (selectablePositions.contains(position)) {
                    selectPosition(position);
                    alterSelectionPhase();
                }
            }
            case SELECT_TO -> {
                if (position.equals(selected)) {
                    deselectSelectedPosition();
                    alterSelectionPhase();
                    Logger.debug("Deselecting piece");
                }
                else
                if (selectablePositions.contains(position)) {
                    var pieceNumber = model.getPieceNumber(selected).getAsInt();
                    var direction = DiskDirection.of(position.row() - selected.row(), position.col() - selected.col());
                    int rowDif = position.row() - selected.row();
                    int colDif = position.col() -selected.col();
                    int distance = 0;
                    if (rowDif == 0 || colDif == 0) {
                        distance = abs(rowDif+colDif);
                    } else {
                        distance = ((abs(rowDif)+abs(colDif))/2);
                    }
                    Logger.debug("Moving piece "+pieceNumber+" "+direction+" "+distance+" times");
                    model.move(selected, direction, position);
                    deselectSelectedPosition();
                    alterSelectionPhase();
                    handleGameOver();
                }
            }
        }
    }

    private void handleGameOver() {
        if (model.isGoal(PieceType.BLUE)) {
            winnerName = bluePlayerName;
            makeAnAlert(winnerName);
        }
        else if (model.isGoal(PieceType.RED)) {
            winnerName = redPlayerName;
            makeAnAlert(winnerName);
        }
    }

    private void makeAnAlert(String winnerName) {
        ButtonType menu = new ButtonType("Main menu");
        ButtonType exit = new ButtonType("Exit");
        Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION, "", menu, exit);
        gameOverAlert.initOwner(board.getScene().getWindow());
        gameOverAlert.setHeaderText("Game over");
        gameOverAlert.setContentText("The game has been won by "+winnerName+" in a total of "+model.getTotalSteps()+" steps!\nCongratulations!\nCheck out the Leaderboard in the Main menu!");
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        playerDao.savePlayer(new Player(winnerName, model.getTotalSteps(), ZonedDateTime.now().format(formatter)));
        Logger.info(winnerName+" won the game.");
        Optional<ButtonType> buttonType = gameOverAlert.showAndWait();
        if (buttonType.get().equals(menu)) {
            try {
                switchToMenu(gameOverAlert);
            }
            catch (IOException e) {
                Logger.debug("Could not exit to Main menu");
            }
        }
        else if (buttonType.get().equals(exit)) {
            Platform.exit();
            Logger.debug("Click on exit");
            Logger.info("Exiting...");
        }
    }

    @FXML
    private void switchToMenu(Alert alert) throws IOException {
        Stage stage = (Stage) alert.getOwner();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/first.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
        Logger.debug("Click on Main menu");
        Logger.info("Switching to Main menu...");
    }

    private void alterSelectionPhase() {
        selectionPhase = selectionPhase.alter();
        hideSelectablePositions();
        setSelectablePositions();
        showSelectablePositions();
    }

    private void selectPosition(Position position) {
        selected = position;
        showSelectedPosition();
    }

    private void showSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().add("selected");
    }

    private void deselectSelectedPosition() {
        hideSelectedPosition();
        selected = null;
    }

    private void hideSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().remove("selected");
    }

    private void setSelectablePositions() {
        selectablePositions.clear();
        switch (selectionPhase) {
            case SELECT_FROM -> {
                for (var position : model.getPlayerPositions()) {
                    selectablePositions.add(position);
                }
            }
            case SELECT_TO -> {
                var pieceNumber = model.getPieceNumber(selected).getAsInt();
                for (var position : model.getValidMoves(pieceNumber)) {
                    selectablePositions.add(position);
                }
            }
        }
    }

    private void showSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().add("selectable");
        }
    }

    private void hideSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().remove("selectable");
        }
    }

    private StackPane getSquare(Position position) {
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }

    private void piecePositionChange(ObservableValue<? extends Position> observable, Position oldPosition, Position newPosition) {
        Logger.debug("Move: {} -> {}", oldPosition, newPosition);
        StackPane oldSquare = getSquare(oldPosition);
        StackPane newSquare = getSquare(newPosition);
        newSquare.getChildren().addAll(oldSquare.getChildren());
        oldSquare.getChildren().clear();
    }

}
