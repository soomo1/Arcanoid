package org.soomo.domain;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Game {
    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Stage stage;
    private List<Brick> bricks = new ArrayList<>();
    private Paddle paddle;
    private Ball ball;
    private Score score;

    private AnimationTimer timer;

    public Game(Stage stage) {
        this.stage = stage;
        this.score = new Score();
        score.setLayoutX(10);
        score.setLayoutY(530); // Assuming the height of your scene is 600
        appRoot.getChildren().add(score);
    }

    public void start() {
        createLevel();
        createContent();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();

        Menu menu = new Menu("Menu");
        MenuItem levelSelect = new MenuItem("Level Select");
        levelSelect.setOnAction(e -> System.out.println("Level select"));

        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction(e -> Platform.exit());
        menu.getItems().addAll(levelSelect, quit);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

        VBox vBox = new VBox(menuBar, appRoot);

        Scene gameScene = new Scene(vBox, 800, 600);
        handleKeyInput(gameScene);

        stage.setScene(gameScene);

        stage.setMinWidth(800);
        stage.setMaxWidth(800);
        stage.setMinHeight(600 + menuBar.getHeight());
        stage.setMaxHeight(600 + menuBar.getHeight());

        stage.show();
    }

    private void update() {
        ball.move();

        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.reverseDirection();
        }

        if ((ball.getTranslateX() <= 0 || ball.getTranslateX() >= appRoot.getWidth() - Ball.BALL_RADIUS)) {
            ball.reverseDirectionX();
        }

        if (ball.getTranslateY() <= 0) {
            ball.reverseDirectionY();
        }

        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (ball.getBounds().intersects(brick.getBounds())) {
                ball.reverseDirection();
                brickIterator.remove();
                gameRoot.getChildren().remove(brick);
                score.increment();
            }
        }

        if (ball.getTranslateY() >= appRoot.getHeight()) {
            timer.stop();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setHeaderText(null);
                alert.setContentText("Try again");

                ButtonType buttonTypeOne = new ButtonType("Try Again");
                ButtonType buttonTypeCancel = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeOne) {
                    resetGame();
                    timer.start();
                } else {
                    Platform.exit();
                }
            });
            score.reset();
        }
    }

    private void resetGame() {
        ball.setTranslateX(appRoot.getWidth() / 2);
        ball.setTranslateY(appRoot.getHeight() / 2);
        bricks.clear();
        gameRoot.getChildren().clear();
        createLevel();
        gameRoot.getChildren().addAll(paddle, ball);
        gameRoot.getChildren().addAll(bricks);
    }

    private Scene createContent() {
        int sceneWidth = 800;
        int sceneHeight = 600;
        appRoot.setPrefSize(sceneWidth, sceneHeight);

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(sceneWidth, sceneHeight);
        borderPane.setCenter(appRoot);

        paddle = new Paddle(sceneWidth / 2 - Paddle.PADDLE_WIDTH / 2, sceneHeight * 0.85 - Paddle.PADDLE_HEIGHT);
        ball = new Ball(sceneWidth / 2, sceneHeight / 2);

        gameRoot.getChildren().add(paddle);
        gameRoot.getChildren().add(ball);
        gameRoot.getChildren().addAll(bricks);

        appRoot.getChildren().add(gameRoot);

        Scene scene = new Scene(borderPane, sceneWidth, sceneHeight);
        handleKeyInput(scene);

        return scene;
    }

    private static final int BRICK_GAP = 12;

    private void createLevel() {
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 25; j++) {
                Brick brick = new Brick((Brick.WIDTH + BRICK_GAP) * j, Brick.HEIGHT * i);
                bricks.add(brick);
            }
        }
    }

    private void handleKeyInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT:
                    paddle.moveLeft();
                    break;
                case RIGHT:
                    paddle.moveRight();
                    break;
            }
        });
    }
}
