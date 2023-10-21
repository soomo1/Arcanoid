package org.soomo.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ScoreManager {
    private static long accumulatedScore = 0L;  // Renamed startScore to accumulatedScore for clarity
    private static final Label scoreLabel = new Label("Score: " + accumulatedScore);
    private static Timeline timeline;
    private static Set<Integer> levelsToComplete = LevelHandler.getLevelNums();
    public ScoreManager() {
        scoreLabel.setLayoutX(10);
        scoreLabel.setLayoutY(GameStart.SCENE_HEIGHT - 30);
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        scoreLabel.setTextFill(Color.WHITE);
    }
    public static void startScoring() {
        System.out.println("Debug:  private static Set<Integer> levelsToComplete = " +  levelsToComplete);

        // Initialize the timeline here
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateScore()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    public static void updateScore() {
        // Increment the accumulatedScore
        accumulatedScore++;

        // Update the scoreLabel text
        scoreLabel.setText("Score: " + accumulatedScore);
    }

    // Optionally, you might want to add a method to stop the scoring when the level is over.
    public static void stopScoring() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }


    public static void setLevelsToComplete(int levelNum){
        levelsToComplete.remove(levelNum);
    }
    public static void resetLevelsToComplete(){
        levelsToComplete = LevelHandler.getLevelNums();
    }

    public static long getAccumulatedScore() {
        return accumulatedScore;
    }

    public static void setAccumulatedScore(Long score) {
        accumulatedScore = score;
    }

    public static String getFinalScore() {
        if (levelsToComplete.isEmpty()) {
            return "Final Score: " + accumulatedScore;
        } else {
            return "You have to complete all levels to get final score!";
        }
    }
    private static List<Long> readScoresFromFile() throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader("src/main/resources/score.json")) {
            Type listType = new TypeToken<List<Long>>() {}.getType();
            return gson.fromJson(reader, listType);
        }
    }

    private static void writeScoresToFile(List<Long> scores) throws IOException {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter("src/main/resources/score.json")) {
            gson.toJson(scores, writer);
        }
    }

    public static void updateTopScores(long newScore) {
        List<Long> scores;
        try {
            scores = readScoresFromFile();
        } catch (IOException e) {
            scores = new ArrayList<>();
        }

        scores.add(newScore);
        scores.sort(Comparator.naturalOrder());

        while (scores.size() > 10) {
            scores.remove(scores.size() - 1);
        }
        try {
            writeScoresToFile(scores);
        } catch (IOException e) {
            System.out.println("Failed to write updated scores to file.");
        }
    }

}
