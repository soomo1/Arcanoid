package org.soomo.controller;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.soomo.view.HomeScreenView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class GameStart {
    public static int SCENE_WIDTH;
    public static int SCENE_HEIGHT;

    static {
        // Load properties from config file
        Properties properties = new Properties();
        try (InputStream input =
                     GameStart.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);

            SCENE_WIDTH =
                    Integer.parseInt(
                            properties.getProperty("scene.width", "960")); // Fallback to 800 if not specified
            SCENE_HEIGHT =
                    Integer.parseInt(
                            properties.getProperty("scene.height", "540")); // Fallback to 600 if not specified
        } catch (IOException ex) {
            ex.printStackTrace();
            // You can decide how to handle this case
            SCENE_WIDTH = 960; // Fallback to a default value
            SCENE_HEIGHT = 540; // Fallback to a default value
        }
    }

    public static void startGame(Stage stage) {
        // Initialize HomeScreenView and HomeScreenLogic
        HomeScreenView homeScreenView = new HomeScreenView(stage);
        new HomeScreenLogic(homeScreenView, stage);

        // Create a new Scene for the home screen
        Scene homeScreenScene = new Scene(homeScreenView, SCENE_WIDTH, SCENE_HEIGHT);

        // Initialize the Stage
        Image icon =
                new Image(Objects.requireNonNull(GameStart.class.getResourceAsStream("/sprites/ico.jpg")));
        stage.getIcons().add(icon);
        stage.setScene(homeScreenScene);
        stage.setTitle("Arkanoid Game");
        stage.show();
        SoundManager.playTheme();
    }
}
