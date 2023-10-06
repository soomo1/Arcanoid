package org.soomo;

import org.soomo.controller.LevelHandler;
import org.soomo.model.Level;

import java.util.List;

// Assuming the LevelHandler and Level classes are in the same package or imported correctly
public class TestMain {

    /**
     * Main method to test readLevels() and print the output.
     */
    public static void main(String[] args) {

        // Call the readLevels method from LevelHandler class
        List<Level> levels = LevelHandler.readLevels();

        // Check if the list is empty or null
        if (levels == null || levels.isEmpty()) {
            System.out.println("No levels found.");
        } else {
            // Loop through the list and print each Level object
            for (Level level : levels) {
                System.out.println(level); // This will call the Level object's toString method
            }
        }
    }
}
