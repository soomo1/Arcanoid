package org.soomo.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HomeScreenView extends VBox {
    // Buttons for the menu
    private final Button newGameButton;
    private final Button selectLevelButton;
    private final Button highScoresButton;
    private final Button setDifficultyButton;
    private final Button soundToggleButton;
    private final Button exitButton;

    public HomeScreenView(Stage stage) {
        // Initialize buttons and set text
        newGameButton = new Button("New Game");
        selectLevelButton = new Button("Select Level");
        highScoresButton = new Button("High Scores");
        soundToggleButton = new Button("Sound: ON");
        setDifficultyButton = new Button("Difficulty");
        exitButton = new Button("Exit Game");

        // Set the alignment to center
        setAlignment(Pos.CENTER); // Center the children nodes vertically

        // Add some spacing between buttons
        setSpacing(10);
        // Add buttons to layout
        getChildren()
                .addAll(newGameButton, selectLevelButton, highScoresButton,  soundToggleButton, setDifficultyButton, exitButton);
        // Initialize background
        fitBackgroundToStage(stage);
    }

    public Button getNewGameButton() {
        return newGameButton;
    }

    public Button getSelectLevelButton() {
        return selectLevelButton;
    }

    public Button getHighScoresButton() {
        return highScoresButton;
    }

    public Button getSoundToggleButton() {
        return soundToggleButton;
    }
    public Button getSetDifficultyButton() {
        return setDifficultyButton;
    }

    public Button getExitButton() {
        return exitButton;
    }

    // Getter and Setter for the background
    public Background getVBoxBackground() {
        return this.getBackground();
    }

    private void fitBackgroundToStage(Stage stage) {
        // Attach listeners to stage width and height properties
        stage.widthProperty().addListener((obs, oldVal, newVal) -> updateBackground());
        stage.heightProperty().addListener((obs, oldVal, newVal) -> updateBackground());

        // Initial setting of the background
        updateBackground();
    }

    private void updateBackground() {
        // Replace with the actual path to your image
        Image backgroundImage = new Image("/backgrounds/homescreen.jpg");

        // Create a BackgroundSize object with stretching enabled but aspect ratio not maintained
        BackgroundSize backgroundSize =
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);

        // Create a BackgroundImage object with the new BackgroundSize
        BackgroundImage backgroundImageObj =
                new BackgroundImage(
                        backgroundImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        backgroundSize);

        // Set the background for this pane
        // Explicit background field for more control if needed
        Background vboxBackground = new Background(backgroundImageObj);
        this.setBackground(vboxBackground);
    }

}
