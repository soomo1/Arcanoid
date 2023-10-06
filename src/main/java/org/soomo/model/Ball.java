package org.soomo.model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Objects;

import static org.soomo.controller.GameStart.SCENE_HEIGHT;
import static org.soomo.controller.GameStart.SCENE_WIDTH;

public class Ball extends Circle {
    // Class to represent the bouncing ball
    private final int BALL_SIZE = SCENE_HEIGHT / 30; // Size of the ball
    private double xSpeed = 3; // Horizontal speed of the ball
    private double ySpeed = 4; // Vertical speed of the ball


    public Ball() { // Constructor for the ball

        // Load the image from resources
        Image ballImage =
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/ball.gif")));

        // Create an ImagePattern based on the loaded image
        ImagePattern ballPattern = new ImagePattern(ballImage);

        // Set the attributes for the ball
        setRadius(BALL_SIZE);
        setFill(ballPattern); // Use ImagePattern here instead of a simple color

        // Set initial position
        setInitialPosition();
    }

    // Set Ball initial position
    public void setInitialPosition() {
        setTranslateX((double) SCENE_WIDTH / 2 - (double) BALL_SIZE / 2);
        setTranslateY((double) SCENE_HEIGHT / 2 - (double) BALL_SIZE / 2);
    }

    public void move() {
        // Method to move the ball
        setTranslateX(getTranslateX() + xSpeed);
        setTranslateY(getTranslateY() + ySpeed);
    }

    public int getSize() {
        return BALL_SIZE;
    }

    public void reverseYSpeed() {
        this.ySpeed = -this.ySpeed;
    }

    public void reverseXSpeed() {
        this.xSpeed = -this.xSpeed;
    }

    /**
     * Set the ball's direction to go left at a specific angle.
     */
    public void setDirectionLeftAtAngle(double angleInDegrees) {
        double angleInRadians = Math.toRadians(angleInDegrees);
        if (xSpeed < 3) {
            xSpeed = 3;
        }
        double speed = Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed); // Calculate the current speed
        xSpeed = (-speed * Math.cos(angleInRadians)); // Set xSpeed for left direction
        ySpeed = (-speed * Math.sin(angleInRadians)); // Set ySpeed (negative to move upwards)
    }

    /**
     * Set the ball's direction to go right at a specific angle.
     */
    public void setDirectionRightAtAngle(double angleInDegrees) {
        double angleInRadians = Math.toRadians(angleInDegrees);
        if (xSpeed < 3) {
            xSpeed = 3;
        }
        double speed = Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed); // Calculate the current speed
        xSpeed = (speed * Math.cos(angleInRadians)); // Set xSpeed for right direction
        ySpeed = (-speed * Math.sin(angleInRadians)); // Set ySpeed (negative to move upwards)
    }

    /**
     * Set the ball's direction to move straight up.
     */
    public void setDirectionUp() {
        xSpeed = 0; // No horizontal movement
        ySpeed = -Math.abs(4); // Make sure ySpeed is negative to move the ball upwards
    }
}
