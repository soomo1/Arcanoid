package org.soomo;

import javafx.application.Application;
import javafx.stage.Stage;
import org.soomo.controller.GameStart;

// Main class to initialize the Arkanoid game
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Starting point for the application
        GameStart.startGame(stage);
    }
}
