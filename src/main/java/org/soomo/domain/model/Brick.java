package org.soomo.domain.model;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Brick extends Rectangle {
    public static final int WIDTH = 30;
    public static final int HEIGHT = 20;
    private boolean destroyed = false;

    public Brick(double x, double y) {
        super(WIDTH, HEIGHT);
        setTranslateX(x);
        setTranslateY(y);
        setFill(Color.BLUE);
    }

    public Bounds getBounds() {
        return this.getBoundsInParent();
    }



    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
    }
}
