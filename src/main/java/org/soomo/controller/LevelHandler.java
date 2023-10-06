package org.soomo.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.soomo.model.Brick;
import org.soomo.model.Level;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class LevelHandler {
    private static List<Level> levels;

    /**
     * Getter for the 'levels' field.
     *
     * @return The list of Level objects.
     */
    public static List<Level> getLevels() {
        return levels;
    }

    /**
     * Setter for the 'levels' field.
     *
     * @param levels The new list of Level objects.
     */
    public static void setLevels(List<Level> levels) {
        LevelHandler.levels = levels;
    }

    // Section 1: Methods for reading levels from JSON files

    // Load levels from JSON file

    /**
     * Reads levels from a JSON file, the path to which is specified in a configuration file.
     *
     * @return A List of Level objects.
     */
    public static List<Level> readLevels() {
        System.out.println("Debug: Entered readLevels() method.");

        // Default JSON file path
        String defaultJsonPath = "/levels.json";
        System.out.println("Debug: Default JSON path set to " + defaultJsonPath);

        // Load properties from config file
        Properties properties = new Properties();
        String levelsJsonPath;
        try {
            System.out.println("Debug: Attempting to load properties from config file.");
            properties.load(LevelHandler.class.getResourceAsStream("/config.properties"));
            // Get JSON file path from properties
            System.out.println("Debug: Retrieving JSON path from properties.");
            levelsJsonPath = properties.getProperty("levels.json.path", defaultJsonPath);
            System.out.println("Debug: Using JSON path: " + levelsJsonPath);

        } catch (IOException e) {
            System.out.println("Debug: Error occurred while loading properties. Using default path.");
            e.printStackTrace();
            levelsJsonPath = defaultJsonPath; // Use default if config file is not found or corrupt
        }

        // Initialize Gson object
        System.out.println("Debug: Initializing Gson object.");
        Gson gson = new Gson();

        List<Level> levelList = null;

        // Create a Reader object to read the JSON file from the resource stream
        try (Reader reader =
                     new InputStreamReader(Objects.requireNonNull(LevelHandler.class.getResourceAsStream(levelsJsonPath)))) {
            // Deserialize the JSON file into a List of Level objects
            System.out.println("Debug: Attempting to deserialize JSON into List of Level objects.");
            levelList = gson.fromJson(reader, new TypeToken<List<Level>>() {
            }.getType());
            System.out.println("Debug: Successfully deserialized JSON.");

        } catch (Exception e) {
            System.out.println("Debug: Error during deserialization.");
            e.printStackTrace();
            // Handle error (You can also throw a custom exception or return a default value here)
        }

        System.out.println("Debug: Exiting readLevels() method.");
        setLevels(levelList);
        return levelList;
    }

    /**
     * Generates and resizes the bricks of a given level to fit within a specified pane.
     *
     * @param level The Level object containing the basic brick layout.
     * @return A list of resized and repositioned Brick objects.
     */
    public static List<Brick> generateBricksLayout(Level level) {

        // Retrieve the brick layout from the Level object
        int[][] brickLayout = level.brickLayout();

        // Determine the number of rows and columns based on the layout
        int numRows = brickLayout.length;
        int numCols = brickLayout[0].length; // Assuming all rows have the same number of columns

        // Calculate new width and height for each brick
        double newBrickWidth = (double) GameStart.SCENE_WIDTH / numCols;
        double newBrickHeight = (double) GameStart.SCENE_HEIGHT / 4 / numRows;

        List<Brick> bricks = new ArrayList<>();

        // Generate the new set of bricks with updated dimensions and positions
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (brickLayout[row][col] != 0) { // Assuming 0 means no brick
                    double x = col * newBrickWidth;
                    double y = row * newBrickHeight;
                    Brick brick = new Brick(x, y, newBrickWidth, newBrickHeight); // Using a default color
                    bricks.add(brick);
                }
            }
        }
        return bricks;
    }

    // Additional functionalities as needed

}
