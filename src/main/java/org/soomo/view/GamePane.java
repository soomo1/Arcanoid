package org.soomo.view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.soomo.controller.GameStart;
import org.soomo.model.Ball;
import org.soomo.model.Brick;
import org.soomo.model.Paddle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * GamePane class is a JavaFX Pane representing the game area.
 */
public class GamePane extends Pane {
    private List<Brick> bricks; // A list to hold the current set of Brick objects
    private Paddle paddle; // Paddle object to control by the user
    private Ball ball; // Ball object that will bounce off walls and paddle
    private Label lifeLabel; // Label to show heart symbols

    public GamePane() {
        bricks = new ArrayList<>();
        // brickRectangles = new ArrayList<>();
        ball = new Ball();
        paddle = new Paddle();
        initLifeIndicator();
        initBallAndPaddle();
        // Load background image from resources
        Image backgroundImage =
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/backgrounds/background.gif")));

        // Create a BackgroundImage object with various settings
        BackgroundImage background =
                new BackgroundImage(
                        backgroundImage,
                        BackgroundRepeat.NO_REPEAT, // Or BackgroundRepeat.NO_REPEAT
                        BackgroundRepeat.NO_REPEAT, // Or BackgroundRepeat.NO_REPEAT
                        BackgroundPosition.DEFAULT,
                        new BackgroundSize(
                                BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));

        // Set the background to the GamePane
        setBackground(new Background(background));
    }


    public Paddle getPaddle() {
        return paddle;
    }

    public Ball getBall() {
        return ball;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    /**
     * Updates the pane with a new set of bricks.
     *
     * @param brickList The new list of Brick objects.
     */
    public void setBricks(List<Brick> brickList) {
        // Clear the existing bricks and their corresponding Rectangles
        this.bricks.clear();
        // this.getChildren().removeAll(this.brickRectangles);
        // this.brickRectangles.clear();

        // Set the new bricks
        this.bricks = brickList;

        getChildren().addAll(brickList);
    }

    /**
     * Clears all game elements from the pane.
     */
    public void clear() {
        // Remove all children elements from this pane
        getChildren().clear();

        // If you have other resources or timers to clear, do it here
    }

    // Initialize life indicator
    public void initLifeIndicator() {
        // Create a new Label that will be updated later with the number of hearts.
        lifeLabel = new Label("❤❤❤"); // Three hearts to start the game

        // Make the text red
        lifeLabel.setTextFill(Color.RED);

        // Make the text bold
        lifeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        // Place the lifeLabel in a HBox (Horizontal Box) to provide flexibility for future UI
        // enhancements.
        // Container to hold heart symbols
        HBox lifeContainer = new HBox();
        lifeContainer.getChildren().add(lifeLabel);

        // Position the lifeContainer in the upper-left corner of the GamePane.
        // You can adjust these values as needed to fit your specific layout.
        lifeContainer.setLayoutX(GameStart.SCENE_WIDTH * 0.90);
        lifeContainer.setLayoutY(GameStart.SCENE_HEIGHT * 0.95);

        // Add the lifeContainer to the GamePane, so it becomes visible.
        getChildren().add(lifeContainer);
    }

    /**
     * Initializes the ball and paddle and adds them to the game pane.
     */
    private void initBallAndPaddle() {
        // Initialize ball and paddle
        this.ball = new Ball(/* parameters for position, radius, etc. */); // Updating class field
        this.paddle =
                new Paddle(/* parameters for position, dimensions, etc. */); // Updating class field

        // Add them to the game pane
        getChildren().addAll(ball, paddle);
    }

    // Method to update heart symbols based on remaining lives.
    public void updateLifeIndicator(int remainingLives) {
        // Create a new StringBuilder to hold the heart symbols.

        // Append a heart symbol for each remaining life.
        // Update the text in lifeLabel to reflect the current number of lives.
        lifeLabel.setText("❤".repeat(Math.max(0, remainingLives))
                // Update the text in lifeLabel to reflect the current number of lives.
        );
    }
}
