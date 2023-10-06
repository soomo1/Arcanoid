package org.soomo.model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

import static org.soomo.controller.GameStart.SCENE_HEIGHT;
import static org.soomo.controller.GameStart.SCENE_WIDTH;

public class Paddle extends Rectangle {
    // Class to represent the paddle controlled by the player

    private static final int PADDLE_WIDTH = SCENE_WIDTH / 10; // Width of the paddle
    private static final int PADDLE_HEIGHT = SCENE_HEIGHT / 25; // Height of the paddle
    private static final int MAX_SPEED = 25; // Maximum speed of the paddle
    private static final double ACCELERATION = 0.4; // Acceleration rate for the paddle
    private double velocity = 0; // Velocity of the paddle (can be positive or negative)

    public Paddle() {
        // Constructor for the paddle
        // Load the image
        Image paddleImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/paddle.jpg")));

        // Create an image pattern from the image
        ImagePattern imagePattern = new ImagePattern(paddleImage);

        // Set the width, height, and position of the paddle
        setWidth((double) SCENE_WIDTH / 10);
        setHeight((double) SCENE_HEIGHT / 25);

        // Set the image pattern as the fill for the paddle
        setFill(imagePattern);

        setTranslateX((double) SCENE_WIDTH / 2 - (double) PADDLE_WIDTH / 2);
        setTranslateY(SCENE_HEIGHT - PADDLE_HEIGHT - ((double) SCENE_HEIGHT / 10));
    }

    public void moveLeft() {
        // Method to move the paddle left
        // If the velocity is positive (i.e., moving right), reset it
        if (velocity > 0) {
            resetVelocity();
        }
        // Modify the velocity, taking acceleration into account
        if (velocity > -MAX_SPEED) {
            velocity -= ACCELERATION;
        }
        // Move the paddle while keeping it inside the screen boundary
        if (getTranslateX() > 0) {
            setTranslateX(Math.max(0, getTranslateX() + velocity));
        }
    }

    public void moveRight() {
        // Method to move the paddle right
        // If the velocity is negative (i.e., moving left), reset it
        if (velocity < 0) {
            resetVelocity();
        }
        // Modify the velocity, taking acceleration into account
        if (velocity < MAX_SPEED) {
            velocity += ACCELERATION;
        }
        // Move the paddle while keeping it inside the screen boundary
        if (getTranslateX() + PADDLE_WIDTH < SCENE_WIDTH) {
            setTranslateX(Math.min(SCENE_WIDTH - PADDLE_WIDTH, getTranslateX() + velocity));
        }
    }

    public void resetVelocity() {
        // Method to reset velocity to zero (e.g., when paddle stops)
        velocity = 0;
    }
}
