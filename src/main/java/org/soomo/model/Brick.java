package org.soomo.model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class Brick extends Rectangle {
    public boolean destroyed;

    public Brick(double x, double y, double width, double height) {
        setWidth(width);
        setHeight(height);
        // Load the image from resources
        Image ballImage =
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/brick.gif")));

        // Create an ImagePattern based on the loaded image
        ImagePattern ballPattern = new ImagePattern(ballImage);
        setFill(ballPattern); // Color of the brick
        setTranslateX(x);
        setTranslateY(y);
        destroyed = false;
    }
}
