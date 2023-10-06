package org.soomo.controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.soomo.model.Level;
import org.soomo.view.GamePane;

import java.util.Objects;

import static org.soomo.controller.HomeScreenLogic.showHomeScreen;

public class ButtonFactory {

    public static Button createHomeScreenButton(Stage stage, Alert alert) {
        Button homeScreenButton = new Button("Home Screen");
        homeScreenButton.setOnAction(e -> {
            // Close the alert dialog
            alert.getDialogPane().getScene().getWindow().hide();
            showHomeScreen(stage);
            HomeScreenLogic.showHomeScreen(stage);
        });
        return homeScreenButton;
    }

    public static Button createTryButton(Level level, Alert alert, Stage stage) {
        Button tryButton = new Button("Try Again");
        tryButton.setOnAction(e -> {
            // Close the alert dialog
            alert.getDialogPane().getScene().getWindow().hide();
            // Code to restart current level;
            GameEngine gameEngine = new GameEngine(new GamePane(), stage);
            gameEngine.startLevel(level);
        });
        return tryButton;
    }


    public static Button createExitButton() {
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> System.exit(0));
        return exitButton;
    }

    public static Button createResumeButton(Alert alert, GameEngine gameEngine) {
        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(e -> {
            // Toggle pause state
            gameEngine.togglePause();

            // Close the alert dialog
            alert.getDialogPane().getScene().getWindow().hide();
        });
        return resumeButton;
    }

    public static Button createNextLevelButton(Alert alert, Level currentLevel, Stage stage, GameEngine gameEngine) {
        Button nextLevelButton = new Button("Next Level");

        // Add event handlers for the buttons
        nextLevelButton.setOnAction(e -> {
            // Close the alert dialog
            alert.getDialogPane().getScene().getWindow().hide();

            // Code to navigate to the next level
            int nextLevelIndex = currentLevel.levelNum();
            // Existing condition to check if there are more levels
            if (nextLevelIndex < LevelHandler.getLevels().size()) {

                // Code to restart current level;
                GameEngine engine = new GameEngine(new GamePane(), stage);

                engine.startLevel(LevelHandler.getLevels().get(nextLevelIndex));
            } else {
                // No more levels left, show the final win screen
                Platform.runLater(() -> {
                    // Create an ImageView to hold the final win GIF
                    ImageView finalWinGif = new ImageView(new Image(Objects.requireNonNull(ButtonFactory.class.getResourceAsStream("/backgrounds/final_win.gif"))));

                    // Set dimensions for the ImageView, optional
                    finalWinGif.setPreserveRatio(true);
                    finalWinGif.setFitHeight(300);

//                    //TODO `final score`
//                    // Display the final score
//                    Label finalScoreLabel = new Label("Final Score: " + "69");
//
//                    // Create HBox to hold the buttons
//                    Alert alert2 = new Alert(Alert.AlertType.NONE);
//                    HBox hbox = new HBox(10, ButtonFactory.createHomeScreenButton(stage, alert2), ButtonFactory.createTryButton(currentLevel, alert2, stage), ButtonFactory.createExitButton());
//
//                    // Create VBox to hold the ImageView, final score label and the HBox
//                    VBox vbox = new VBox(10, finalWinGif, finalScoreLabel, hbox);
//
//                    // Remove default spacings and paddings
//                    hbox.setSpacing(10);
//                    hbox.setAlignment(Pos.CENTER); // This centers the buttons in the HBox
//                    hbox.setPadding(new Insets(0));
//                    vbox.setSpacing(10);
//                    vbox.setPadding(new Insets(0));
//                    vbox.setAlignment(Pos.CENTER); // This centers the content in the VBox
//
//                    // Create the Alert and set its properties
//                    alert2.initStyle(StageStyle.TRANSPARENT);
//                    alert2.getDialogPane().setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0;"); // Make the dialog pane transparent and remove padding
//                    vbox.setStyle("-fx-background-color: transparent;"); // Make the VBox transparent
//                    alert2.getDialogPane().setContent(vbox);
//                    alert2.show();
                    gameEngine.createAlertWithContent( finalWinGif,"finalWin").show();
                });
            }
        });
        return nextLevelButton;
    }


}
