package org.soomo.controller;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.soomo.model.Level;
import org.soomo.view.GamePane;
import org.soomo.view.HomeScreenView;

import java.util.List;

public class HomeScreenLogic {

    private final HomeScreenView homeScreenView;
    private final Stage stage; // Assuming you have a reference to the JavaFX stage
    private GameEngine gameEngine; // Add a reference to GameEngine

    public HomeScreenLogic(HomeScreenView view, Stage stage) {
        this.homeScreenView = view;
        this.stage = stage;

        // Register button click handlers
        homeScreenView.getNewGameButton().setOnAction(e -> startNewGame());
        homeScreenView.getSelectLevelButton().setOnAction(e -> selectLevel());
        homeScreenView.getSoundToggleButton().setOnAction(e -> toggleSound());
        homeScreenView.getExitButton().setOnAction(e -> exitGame());

        // Register keyboard event handler
        homeScreenView.addEventHandler(KeyEvent.KEY_PRESSED, e -> handleKeyPress(e.getCode()));
    }

    // Method to show the home screen
    public static void showHomeScreen(Stage stage) {
        // Initialize HomeScreenView and HomeScreenLogic
        HomeScreenView homeScreenView = new HomeScreenView(stage);
        new HomeScreenLogic(homeScreenView, stage);

        // Create a new Scene for the home screen
        Scene homeScreenScene =
                new Scene(homeScreenView, GameStart.SCENE_WIDTH, GameStart.SCENE_HEIGHT);

        stage.setScene(homeScreenScene);
    }

    /**
     * Starts a new game by loading the levels and initializing the game engine with the first level.
     */
    private void startNewGame() {
        // Debug: Entry point of the method
        System.out.println("Debug: Entered startNewGame() method.");

        // Debug: Loading levels from JSON file
        System.out.println("Debug: Attempting to load levels from JSON file.");
        List<Level> levels = LevelHandler.readLevels();

        // Debug: Printing the levels
        System.out.println("Debug: Loaded levels are:");
        levels.forEach(level -> System.out.println(level.toString()));

        // Check if levels are present
        if (!levels.isEmpty()) {
            // Debug: Getting the first level
            System.out.println("Debug: Getting the first level from the loaded levels.");
            Level firstLevel = levels.get(0);

            // Debug: Initializing the game with the first level
            System.out.println(
                    "Debug: Initializing the game with the first level." + "\n" + firstLevel.toString());
            //GamePane gamePane = new GamePane();

            if (gameEngine == null) {
                gameEngine = new GameEngine(new GamePane(), stage);
            }
            gameEngine.startLevel(firstLevel);
        } else {
            // Debug: No levels found in the JSON file
            System.out.println("Debug: No levels found in the JSON file.");
            System.err.println("No levels found in the JSON file.");
        }

        // Debug: Method exit
        System.out.println("Debug: Exiting startNewGame() method.");
    }

    /**
     * Update the current scene to allow the user to select a level from available levels.
     */
    private void selectLevel() {
        this.gameEngine = new GameEngine(new GamePane(), stage);

        // Debug: Entry point of the method
        System.out.println("Debug: Entered selectLevel() method.");

        // Fetch the list of available levels
        List<Level> levels = LevelHandler.readLevels();

        // Create a new JavaFX VBox for level selection
        VBox levelSelectPane = new VBox(10); // 10 is the spacing between children in the VBox

        // Set the alignment to center
        levelSelectPane.setAlignment(Pos.CENTER);

        // Get the background from HomeScreenView and set it for this VBox
        levelSelectPane.setBackground(homeScreenView.getVBoxBackground());

        // Create buttons for each available level
        for (Level level : levels) {
            Button levelButton = new Button(String.valueOf(level.levelNum()));
            levelButton.setOnAction(e -> gameEngine.startLevel(level));
            levelSelectPane.getChildren().add(levelButton);
        }
        // Create an "Exit" button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(
                e -> {
                    showHomeScreen(gameEngine.getStage()); // Using the new method
                });
        levelSelectPane.getChildren().add(exitButton);
        // Create a new scene with the same dimensions as the current scene
        Scene levelSelectScene =
                new Scene(levelSelectPane, GameStart.SCENE_WIDTH, GameStart.SCENE_HEIGHT);

        // Set the new scene
        gameEngine.getStage().setScene(levelSelectScene);
    }

    // TODO: Implement later
    private void toggleHighScores() {
    }

    private void toggleSound() {
        if (SoundManager.getIsMuted()) {
            SoundManager.setIsMuted(false);
            SoundManager.setSound();
            homeScreenView.getSoundToggleButton().setText("Sound: ON");
        } else {
            SoundManager.setIsMuted(true);
            SoundManager.setSound();
            homeScreenView.getSoundToggleButton().setText("Sound: OFF");
        }
    }

    private void exitGame() {
        // Logic to exit the application
        Platform.exit();
    }

    private void handleKeyPress(KeyCode code) {
        switch (code) {
            case UP:
                // Move selection up
                break;
            case DOWN:
                // Move selection down
                break;
            case ENTER:
                // Confirm action
                break;
            default:
                break;
        }
    }
}
