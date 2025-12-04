package com.excape;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class RoomScreenController {

    @FXML
    private ImageView inventory;

    @FXML
    private Label timer;

    private int secondsRemaining;
    private Timeline timeline;

    public void initializeTimer() {
        secondsRemaining = 59;
        updateTimer();

        timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                secondsRemaining--;
                updateTimer();
                if (secondsRemaining <= 0) {
                    timeline.stop();
                    // Optional: Add actions when countdown finishes
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateTimer() {
        int minutes = secondsRemaining / 60;
        int seconds = secondsRemaining % 60;
        timer.setText(String.format("%02d:%02d", minutes, seconds));
    }

    @FXML
    void exitGame(MouseEvent event) {

    }

    @FXML
    void giveRandomPuzzle(MouseEvent event) {

    }

    @FXML
    void openInventory(MouseEvent event) {

    }

    @FXML
    void switchToLightPuzzle(MouseEvent event) {

    }

}
