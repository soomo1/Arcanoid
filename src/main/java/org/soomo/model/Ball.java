package org.soomo.model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.soomo.controller.GameStart;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import static org.soomo.controller.GameStart.SCENE_HEIGHT;
import static org.soomo.controller.GameStart.SCENE_WIDTH;

public class Ball extends Circle {
    private final int BALL_RADIUS = SCENE_HEIGHT / 30;
    private double xSpeed; // Horizontal speed
    private double ySpeed; // Vertical speed

    public Ball() {
        updateSpeed();
        Image ballImage =
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/ball.gif")));
        ImagePattern ballPattern = new ImagePattern(ballImage);

        setRadius(BALL_RADIUS);
        setFill(ballPattern);
        setInitialPosition();
        System.out.println("Ball Constructor used Ball id = " + System.identityHashCode(this) + " xSpeed: " + xSpeed + ", ySpeed: " + ySpeed);
    }


    public double getXSpeed() {
        return xSpeed;
    }

    public void setXSpeed(double speed) {
        xSpeed = speed;
    }

    public double getYSpeed() {
        return ySpeed;
    }
    public void setYSpeed(double speed) {
        ySpeed = speed;
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
        if (xSpeed < 3) {
            xSpeed = 3;
        }
        double speed = Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed); // Calculate the current speed
        xSpeed = (-speed * Math.cos(angleInRadians)); // Set xSpeed for left direction
        ySpeed = (-speed * Math.sin(angleInRadians)); // Set ySpeed (negative to move upwards)
    }

    // Method to update the ball speed from the config properties file
    public void updateSpeed() {
        System.out.println("updateSpeed() called");
        // Load properties from config file
        Properties properties = new Properties();
        try (InputStream input = GameStart.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);

            xSpeed = Double.parseDouble(properties.getProperty("ball.xSpeed", "3"));
            ySpeed = Double.parseDouble(properties.getProperty("ball.ySpeed", "4"));

            System.out.println("Updated speeds - xSpeed: " + xSpeed + ", ySpeed: " + ySpeed);

        } catch (IOException ex) {
            ex.printStackTrace();
            // Fallback to a default value
            xSpeed = 3;
            ySpeed = 4;
        }
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
