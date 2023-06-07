package org.soomo.domain.model;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle extends Rectangle {

    // Set some constants for the paddle's size
    public static final int PADDLE_WIDTH = 60;
    public static final int PADDLE_HEIGHT = 20;

    public static final int PADDLE_SPEED = 10; // Change this value to adjust speed


    public Paddle(double x, double y) {
        super(PADDLE_WIDTH, PADDLE_HEIGHT);
        setTranslateX(x);
        setTranslateY(y);
        setFill(Color.BLACK);
    }

    public void moveLeft() {
        if (getTranslateX() - PADDLE_SPEED > 0) {
            setTranslateX(getTranslateX() - PADDLE_SPEED);
        }
    }

    public void moveRight() {
        if (getTranslateX() + PADDLE_SPEED < 800 - PADDLE_WIDTH) {
            setTranslateX(getTranslateX() + PADDLE_SPEED);
        }
    }

    public Bounds getBounds() {
        return this.getBoundsInParent();
    }


}
