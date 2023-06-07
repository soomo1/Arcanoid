package org.soomo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.soomo.domain.Game;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Menu menu = new Menu("Menu");

        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(e -> {
            Game game = new Game(primaryStage); // Pass the primary stage to the Game class
            game.start();
        });

        MenuItem levelSelect = new MenuItem("Level Select");
        levelSelect.setOnAction(e -> System.out.println("Level select"));

        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction(e -> Platform.exit());

        menu.getItems().add(newGame);
        menu.getItems().add(levelSelect);
        menu.getItems().add(quit);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

        VBox vBox = new VBox(menuBar);
        Scene scene = new Scene(vBox, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
