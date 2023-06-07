package org.soomo.domain.model;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

public class Ball extends Circle {

    // Set some constants for the ball's size and initial velocity
    public static final int BALL_RADIUS = 10;
    private Point2D velocity = new Point2D(1, 1);


    public Ball(double x, double y) {
        super(BALL_RADIUS);
        setTranslateX(x);
        setTranslateY(y);
    }

    // movement methods
    public void move() {
        setTranslateX(getTranslateX() + velocity.getX());
        setTranslateY(getTranslateY() + velocity.getY());
    }

    public void reverseDirection() {
        velocity = new Point2D(velocity.getX(), -velocity.getY());
    }

    public void reverseDirectionX() {
        velocity = new Point2D(-velocity.getX(), velocity.getY());
    }

    public void reverseDirectionY() {
        velocity = new Point2D(velocity.getX(), -velocity.getY());
    }

    public Bounds getBounds() {
        return this.getBoundsInParent();
    }





}
