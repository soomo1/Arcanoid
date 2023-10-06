package org.soomo.model;

import java.util.Arrays;

/**
 * @param brickLayout Stores the layout of bricks for this level
 */
public record Level(int levelNum, int[][] brickLayout) {

    /**
     * Gets the layout of bricks for this level.
     *
     * @return A 2D array representing the layout of bricks.
     */
    @Override
    public int[][] brickLayout() {
        return this.brickLayout;
    }

    /**
     * Overridden toString method to provide a human-readable representation of the object.
     *
     * @return A string representation of the Level object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Level: ").append(levelNum).append("\n");
        sb.append("Bricks:\n");

        if (brickLayout != null) { // Check for null array
            for (int[] row : brickLayout) {
                sb.append(Arrays.toString(row)).append("\n");
            }
        } else {
            sb.append("No bricks available");
        }

        return sb.toString();
    }
}
