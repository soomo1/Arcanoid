package org.soomo.model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Objects;

import static org.soomo.controller.GameStart.SCENE_HEIGHT;
import static org.soomo.controller.GameStart.SCENE_WIDTH;

public class Ball extends Circle {
    private final int BALL_RADIUS = SCENE_HEIGHT / 30;
    private double xSpeed; // Horizontal speed
    private double ySpeed; // Vertical speed
    public static double speed = 3;

    public Ball() {
        Image ballImage =
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/ball.gif")));
        ImagePattern ballPattern = new ImagePattern(ballImage);

        setRadius(BALL_RADIUS);
        setFill(ballPattern);
        setInitialPosition();
        xSpeed = speed;
        ySpeed = speed;

        System.out.println("Ball Constructor used Ball id = " + System.identityHashCode(this) + " xSpeed: " + xSpeed + ", ySpeed: " + ySpeed);
    }

    public double getXSpeed() {
        return xSpeed;
    }

    public double getYSpeed() {
        return ySpeed;
    }

    public void setInitialPosition() {
        setTranslateX((double) SCENE_WIDTH / 2 - (double) BALL_RADIUS / 2);
        setTranslateY((double) SCENE_HEIGHT / 2 - (double) BALL_RADIUS / 2);
    }

    public void move() {
        setTranslateX(getTranslateX() + xSpeed);
        setTranslateY(getTranslateY() + ySpeed);
    }

    public int getSize() {
        return BALL_RADIUS;
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
        if (xSpeed < speed) {
            xSpeed = speed;
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
        if (xSpeed < speed) {
            xSpeed = speed;
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
        ySpeed = -speed; // Make sure ySpeed is negative to move the ball upwards
    }
}
