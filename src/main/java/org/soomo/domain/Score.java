package org.soomo.domain;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Score extends Label {
    private int score;

    public Score() {
        score = 0;
        updateScore();
    }

    public void increment() {
        score++;
        updateScore();
    }

    public void reset() {
        score = 0;
        updateScore();
    }

    public int getScore() {
        return score;
    }

    private void updateScore() {
        setText("Score: " + score);
        setFont(Font.font("Verdana", FontWeight.BOLD, 18)); // Adjust according to your preference
    }
}
