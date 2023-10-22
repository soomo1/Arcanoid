package org.soomo.controller;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.soomo.model.Brick;
import org.soomo.model.Level;
import org.soomo.view.GamePane;

import java.util.Iterator;
import java.util.Objects;

import static org.soomo.controller.ScoreManager.*;

public class GameEngine {
    private final Stage stage; // JavaFX stage
    private GamePane gamePane; // Reference to the game pane (view)
    private Level currentLevel;
    private int remainingLives;
    private AnimationTimer gameLoop; // AnimationTimer for the game loop
    private boolean goLeft, goRight; // Booleans to control the paddle's movement
    private boolean isPaused = false; // Flag to track pause state
    private int score = 0; // Store the player's score
    private Label scoreLabel = new Label("Score: " + score); // Label to display the score

    /**
     * @param gamePane The game pane
     * @param stage    The JavaFX stage
     */
    public GameEngine(GamePane gamePane, Stage stage) {
        this.stage = stage;
        this.gamePane = gamePane; // Now we have a reference to the GamePane

        scoreLabel.setLayoutX(10);
        scoreLabel.setLayoutY(GameStart.SCENE_HEIGHT - 30);
        gamePane.getChildren().add(scoreLabel);
    }
    public Stage getStage() {
        return this.stage;
    }

    public void setMovementFlags(boolean goLeft, boolean goRight) {
        this.goLeft = goLeft;
        this.goRight = goRight;
    }

    // Method to update the game logic
    public void update() {
        // If the game is paused, do nothing
        if (isPaused) {
            return;
        }
        // Update paddle position
        if (goLeft) gamePane.getPaddle().moveLeft();
        if (goRight) gamePane.getPaddle().moveRight();
        // Update ball position
        gamePane.getBall().move();
        // collision detection
        checkCollisionWithPaddle();
        checkCollisionWithBallAndBricks();
        checkCollisionWithWalls();

        // Check if the game is won or lost
        checkGameStatus();
    }

    private void checkGameStatus() {
        // Check win condition
        // A field to keep track of the number of bricks destroyed
        int bricksDestroyed = 0;
        if (bricksDestroyed == gamePane.getBricks().size()) {
            // Stop the game and display "You Win!" message
            stopScoring();
            stopGame();
            setLevelsToComplete(currentLevel.levelNum());
            showWinScreen();
        }
        // Check lose condition
        // Check if ball hits the bottom
        if (gamePane.getBall().getTranslateY() >= gamePane.getHeight()) {
            // Decrement remaining lives
            remainingLives--;
            gamePane.updateLifeIndicator(remainingLives);

            // Check if there are no more lives left
            if (remainingLives <= 0) {
                // Game over logic
                stopScoring();
                stopGame();
                showGameOverScreen();
            } else {
                // Reset the ball position for the new life
                gamePane.getBall().setInitialPosition();
            }
        }
    }

    private void stopGame() {
        // Stops the game by stopping the AnimationTimer
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    private void showWinScreen() {
        Platform.runLater(() -> {
            // Create an ImageView to hold the winning GIF or image
            ImageView winGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/backgrounds/win.gif"))));

            // Set dimensions for the ImageView, optional
            winGif.setPreserveRatio(true);
            winGif.setFitHeight(300);

            //alert.show();
            createAlertWithContent(winGif, "winScreen").show();
        });
    }

    public Alert createAlertWithContent(ImageView imageView, String screenType) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        VBox vbox;
        HBox hbox;

        switch (screenType.toLowerCase()) {
            case "winscreen":
                hbox = new HBox(10, ButtonFactory.createNextLevelButton(alert, currentLevel, stage, this), ButtonFactory.createHomeScreenButton(stage, alert), ButtonFactory.createExitButton());
                vbox = new VBox(10, imageView, hbox);
                break;
            case "gameoverscreen":
                hbox = new HBox(10, ButtonFactory.createHomeScreenButton(stage, alert), ButtonFactory.createExitButton(), ButtonFactory.createTryButton(currentLevel, alert, stage));
                vbox = new VBox(10, imageView, hbox);
                break;
            case "pausescreen":
                hbox = new HBox(10, ButtonFactory.createResumeButton(alert, this), ButtonFactory.createHomeScreenButton(stage, alert), ButtonFactory.createExitButton());
                vbox = new VBox(10, imageView, hbox);
                break;
            case "finalwin":
                Label finalScoreLabel = new Label(getFinalScore());
                updateTopScores(getAccumulatedScore());
                hbox = new HBox(10, ButtonFactory.createHomeScreenButton(stage, alert), ButtonFactory.createTryButton(currentLevel, alert, stage), ButtonFactory.createExitButton());
                vbox = new VBox(10, imageView, finalScoreLabel, hbox);
                break;
            default:
                throw new IllegalArgumentException("Unknown screenType: " + screenType);
        }
        // Remove default spacings and paddings
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER); // This centers the buttons in the HBox
        hbox.setPadding(new Insets(0));
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(0));
        vbox.setAlignment(Pos.CENTER); // This centers the content in the VBox

        // Create the Alert and set its properties
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.getDialogPane().setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0;"); // Make the dialog pane transparent and remove padding
        vbox.setStyle("-fx-background-color: transparent;"); // Make the VBox transparent
        alert.getDialogPane().setContent(vbox);
        return alert;
    }

    private void showGameOverScreen() {
        Platform.runLater(() -> {
            // Create an ImageView to hold the GIF
            ImageView gameOverGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/backgrounds/game_over.gif"))));

            // Ensure ImageView stretches to fill its container
            gameOverGif.setPreserveRatio(true);
            gameOverGif.setFitWidth(300);

            createAlertWithContent(gameOverGif, "gameOverScreen").show();
        });
    }

    // Collision detection between ball and bricks
    private void checkCollisionWithBallAndBricks() {
        Iterator<Brick> brickIterator = gamePane.getBricks().iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (Shape.intersect(gamePane.getBall(), brick).getBoundsInLocal().getWidth() != -1) {
                gamePane.getChildren().remove(brick);
                brickIterator.remove();
                gamePane.getBall().reverseYSpeed();
                // Play the hit brick sound when a collision occurs
                SoundManager.playHitBrickSound();
            }
        }
    }

    // Collision detection between ball and stage borders
    private void checkCollisionWithWalls() {
        if (gamePane.getBall().getTranslateY() <= 0 || gamePane.getBall().getTranslateY() >= stage.getHeight() - gamePane.getBall().getSize()) {
            gamePane.getBall().reverseYSpeed();
        }
        if (gamePane.getBall().getTranslateX() <= 0 || gamePane.getBall().getTranslateX() >= stage.getWidth() - gamePane.getBall().getSize()) {
            gamePane.getBall().reverseXSpeed();
        }
    }

    private void checkCollisionWithPaddle() {
        // Get the bounds of the ball and paddle in the parent coordinate system
        Bounds ballBounds = gamePane.getBall().getBoundsInParent();
        Bounds paddleBounds = gamePane.getPaddle().getBoundsInParent();

        // Check for intersection between ball and paddle
        if (ballBounds.intersects(paddleBounds)) {
            // Calculate the x-coordinate of the ball's center of intersection
            double intersectionCenterX = (ballBounds.getMinX() + ballBounds.getMaxX()) / 2;

            // Determine the x-coordinate ranges based on percentages
            double paddleLeftThird = paddleBounds.getMinX() + (paddleBounds.getWidth() * 0.35);
            double paddleRightThird = paddleBounds.getMinX() + (paddleBounds.getWidth() * 0.65);

            // Reverse the ball's Y-speed to make it bounce
            gamePane.getBall().reverseYSpeed();

            // Change x-speed and y-speed based on where the ball hit the paddle
            if (intersectionCenterX < paddleLeftThird) {
                gamePane.getBall().setDirectionLeftAtAngle(30);
            } else if (intersectionCenterX > paddleRightThird) {
                gamePane.getBall().setDirectionRightAtAngle(30);
            } else {
                gamePane.getBall().setDirectionUp();
            }
        }
    }

    /**
     * Starts the level of the game based on the provided Level object.
     *
     * @param level The Level object that defines the new level.
     */
    public void startLevel(Level level) {
        // Clear any existing game elements from the game pane
        gamePane.clear();
        gamePane = new GamePane();
       // gamePane.getBall().updateSpeed();
        // Score Manager
        gamePane.getChildren().add(new ScoreManager().getScoreLabel());
        ScoreManager.startScoring();

        Text readyText = createReady();
        Text goText = createGo();
        gamePane.getChildren().addAll(readyText, goText);

        // Schedule a "later" operation to position Text after Scene is initialized
        Platform.runLater(() -> {
            readyText.setX(gamePane.getWidth() / 2 - readyText.getLayoutBounds().getWidth() / 2);
            readyText.setY(gamePane.getHeight() / 2 - readyText.getLayoutBounds().getHeight() / 2);

            goText.setX(gamePane.getWidth() / 2 - goText.getLayoutBounds().getWidth() / 2);
            goText.setY(gamePane.getHeight() / 2 - goText.getLayoutBounds().getHeight() / 2);
        });

        // Create the Timeline for 'Ready'
        Timeline readyTimeline = getTimeline(readyText, goText);
        readyTimeline.play();

        // Set the new brick layout in the game pane
        gamePane.setBricks(LevelHandler.generateBricksLayout(level));
        currentLevel = level;
        remainingLives = 3;

        // Create the game scene and add event filters
        Scene gameScene = new Scene(gamePane, GameStart.SCENE_WIDTH, GameStart.SCENE_HEIGHT);
        gameScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        gameScene.addEventFilter(KeyEvent.KEY_RELEASED, this::handleKeyRelease);

        // Debug: Ball
        System.out.println(
                "Debug: Ball Sped:" + "\n" + "x = " + gamePane.getBall().getXSpeed() + "\n" + "y = " + gamePane.getBall().getYSpeed());

        stage.setScene(gameScene);
    }

    public Text createReady() {
        // Create the 'Ready' and 'Go' text
        Text readyText = new Text("Ready");
        readyText.setFont(Font.font("Arial", FontWeight.BOLD, 80));
        readyText.setFill(Color.RED);
        readyText.setStroke(Color.PURPLE);
        readyText.setStrokeWidth(2);
        readyText.setVisible(false);
        return readyText;
    }

    public Text createGo() {
        Text goText = new Text("Go");
        goText.setFont(Font.font("Arial", FontWeight.BOLD, 80));
        goText.setFill(Color.RED);
        goText.setStroke(Color.PURPLE);
        goText.setStrokeWidth(2);
        goText.setVisible(false);
        return goText;
    }

    private Timeline getTimeline(Text readyText, Text goText) {
        Timeline readyTimeline = new Timeline(new KeyFrame(Duration.seconds(1), ae -> readyText.setVisible(true)));

        // Create the Timeline for 'Go'
        Timeline goTimeline = new Timeline(new KeyFrame(Duration.seconds(1), ae -> {
            readyText.setVisible(false);
            goText.setVisible(true);
        }), new KeyFrame(Duration.seconds(2), ae -> {
            goText.setVisible(false);
            // Start the game loop here
            startGameLoop();
        }));

        readyTimeline.setOnFinished(e -> goTimeline.play());
        return readyTimeline;
    }

    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT, A:
                setMovementFlags(true, false);
                break;
            case RIGHT, D:
                setMovementFlags(false, true);
                break;
            case SPACE, ENTER:
                togglePause();
                break;
            default:
                break;
        }
    }

    private void handleKeyRelease(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT, RIGHT, A, D:
                setMovementFlags(false, false);
                break;
            default:
                break;
        }
    }

    private void showPauseScreen() {
        Platform.runLater(() -> {
            // Create an ImageView to hold the GIF or Text
            ImageView pauseGif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/backgrounds/pause.gif"))));
            // Set dimensions for the ImageView, optional
            pauseGif.setPreserveRatio(true);
            pauseGif.setFitHeight(300);
            createAlertWithContent(pauseGif, "pauseScreen").show();
        });
    }


    public void togglePause() {
        if (isPaused) {
            // Unpause game
            gameLoop.start();
        } else {
            // Pause game
            gameLoop.stop();
            showPauseScreen();
        }
        isPaused = !isPaused;
    }

//    public void startGameLoop() {
//        gameLoop = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                if (previousTime == 0) {
//                    previousTime = now;
//                }
//
//                float deltaTime = (now - previousTime) / 1_000_000_000.0f; // Convert to seconds
//                previousTime = now;
//
//                update(deltaTime); // Pass deltaTime to your update method
//            }
//        };
//        gameLoop.start();
//    }

    public void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(); // Update game logic
            }
        };
        gameLoop.start();
    }
}
