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
                    gameEngine.createAlertWithContent( finalWinGif,"finalWin").show();
                });
            }
        });
        return nextLevelButton;
    }


}
