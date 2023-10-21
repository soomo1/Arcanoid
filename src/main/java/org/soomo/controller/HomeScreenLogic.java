package org.soomo.controller;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.soomo.model.Ball;
import org.soomo.model.Level;
import org.soomo.view.GamePane;
import org.soomo.view.HomeScreenView;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.soomo.controller.ScoreManager.setAccumulatedScore;

public class HomeScreenLogic {
    private final HomeScreenView homeScreenView;
    private final Stage stage; // Assuming you have a reference to the JavaFX stage
   // private GameEngine gameEngine; // Add a reference to GameEngine

    public HomeScreenLogic(HomeScreenView view, Stage stage) {
        this.homeScreenView = view;
        this.stage = stage;

        // Register button click handlers
        homeScreenView.getNewGameButton().setOnAction(e -> startNewGame());
        homeScreenView.getSelectLevelButton().setOnAction(e -> selectLevel());
        homeScreenView.getSoundToggleButton().setOnAction(e -> toggleSound());
        homeScreenView.getHighScoresButton().setOnAction(e -> showHighScoresScreen());
        homeScreenView.getSetDifficultyButton().setOnAction(e -> setDifficulty());
        homeScreenView.getExitButton().setOnAction(e -> exitGame());
    }

    private void setDifficulty() {
        VBox difficultyPane = new VBox(10);
        difficultyPane.setAlignment(Pos.CENTER);

        // Create buttons for each difficulty setting
        Button easyButton = new Button("Easy");
        easyButton.setOnAction(e -> {
            updateDifficulty(1, 1);
        });

        Button normalButton = new Button("Normal");
        normalButton.setOnAction(e -> {
            updateDifficulty(3, 3);
        });

        Button hardButton = new Button("Hard");
        hardButton.setOnAction(e -> {
            updateDifficulty(7, 7);
        });

        // Create an "Exit" button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> {
            showHomeScreen(stage);
        });

        // Add buttons to the pane
        difficultyPane.getChildren().addAll(easyButton, normalButton, hardButton, exitButton);

        // Get the background from HomeScreenView and set it for this VBox
        difficultyPane.setBackground(homeScreenView.getVBoxBackground());

        // Create a new scene with the same dimensions as the current scene
        Scene difficultyScene = new Scene(difficultyPane, GameStart.SCENE_WIDTH, GameStart.SCENE_HEIGHT);

        // Set the new scene
        stage.setScene(difficultyScene);
    }

//    // Utility method to debug Ball speed
//    private void debugBallSpeed( double speed) {
//        // Show ball speeds before update
//        System.out.println("Before update: ");
//        System.out.println("ySpeed: " + gameEngine.getGamePane().getBall().getYSpeed());
//        System.out.println("xSpeed: " + gameEngine.getGamePane().getBall().getXSpeed());
//
//        // Update ball speed using updateSpeed method
//        gameEngine.getGamePane().getBall().updateSpeed();
//        System.out.println("After update using .updateSpeed(): ");
//        System.out.println("ySpeed: " + gameEngine.getGamePane().getBall().getYSpeed());
//        System.out.println("xSpeed: " + gameEngine.getGamePane().getBall().getXSpeed());
//
//        // Update ball speed directly
//        gameEngine.getGamePane().getBall().setXSpeed(speed);
//        gameEngine.getGamePane().getBall().setYSpeed(speed);
//        System.out.println("After update using speed YX mets" + speed);
//        System.out.println("ySpeed: " + gameEngine.getGamePane().getBall().getYSpeed());
//        System.out.println("xSpeed: " + gameEngine.getGamePane().getBall().getXSpeed());
//    }


    private void updateDifficulty(int xSpeed, int ySpeed) {
        String filePath = "src/main/resources/config.properties";
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ball.xSpeed=")) {
                    line = "ball.xSpeed=" + xSpeed;
                }
                if (line.startsWith("ball.ySpeed=")) {
                    line = "ball.ySpeed=" + ySpeed;
                }
                fileContent.append(line).append(System.lineSeparator());
            }
        } catch (IOException io) {
            io.printStackTrace();
        }

        // Write the updated content back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(fileContent.toString());
        } catch (IOException io) {
            io.printStackTrace();
        }
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
        List<Level> levels = LevelHandler.readLevels();
        ScoreManager.resetLevelsToComplete();
        setAccumulatedScore(0L);
        // Check if levels are present
        if (!levels.isEmpty()) {
            Level firstLevel = levels.get(0);
            GameEngine gameEngine = new GameEngine(new GamePane(), stage);
            gameEngine.startLevel(firstLevel);
        } else {
            // Debug: No levels found in the JSON file
            System.out.println("Debug: No levels found in the JSON file.");
        }
    }

    /**
     * Update the current scene to allow the user to select a level from available levels.
     */
    private void selectLevel() {
        GameEngine gameEngine = new GameEngine(new GamePane(), stage);
        gameEngine.getGamePane().getBall().updateSpeed();
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

    public void showHighScoresScreen() {
        GameEngine gameEngine = new GameEngine(new GamePane(), stage);

        VBox highScoresPane = new VBox(10);
        highScoresPane.setAlignment(Pos.CENTER);
        highScoresPane.setBackground(homeScreenView.getVBoxBackground());

        // Fetch the high scores
        List<Long> scores = fetchHighScores();

        // Display each rank and score
        int rank = 1;
        for (Long score : scores) {
            // Create a button-like frame for each score
            Button scoreFrame = new Button("Rank: " + rank + " Score: " + score);
            highScoresPane.getChildren().add(scoreFrame);
            rank++;
            if (rank > 10) break; // Show only top 10
        }

        // Create an "Exit" button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> showHomeScreen(gameEngine.getStage()));
        highScoresPane.getChildren().add(exitButton);

        // Create a new scene with the same dimensions as the current scene
        Scene highScoresScene = new Scene(highScoresPane, GameStart.SCENE_WIDTH, GameStart.SCENE_HEIGHT);

        // Set the new scene
        gameEngine.getStage().setScene(highScoresScene);
    }

    private List<Long> fetchHighScores() {
        String scoresPath = "src/main/resources/score.json"; // adjust if your path is different
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader(scoresPath);
            return Arrays.asList(gson.fromJson(reader, Long[].class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
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
}
