package com.excape;

import java.io.IOException;
import java.util.ArrayList;

import com.model.EscapeGameFacade;
import com.model.Item;

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
    private Label timerLabel;
    private Timeline timeline;
    private int secondsLeft = 0;
    EscapeGameFacade facade = EscapeGameFacade.getInstance();

    @FXML
    public void initialize() {
        startTimer((int) facade.getRemainingTime());
    }

    public void startTimer(int seconds) {
        stopTimerIfRunning();

        this.secondsLeft = Math.max(0, seconds);
        updateLabel(secondsLeft);

        timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), ev -> {
                secondsLeft = (int) facade.getRemainingTime();
                if (secondsLeft <= 0) {
                    updateLabel(0);
                    stopTimerIfRunning();
                    try {
                        onTimerEnded();
                    } catch (IOException ex) {
                    }
                } else {
                    updateLabel(secondsLeft);
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void pauseTimer() {
        if (timeline != null) timeline.pause();
    }

    public void resumeTimer() {
        if (timeline != null) timeline.play();
    }

    public void stopTimerIfRunning() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }

    private void updateLabel(int totalSeconds) {
        int mins = totalSeconds / 60;
        int secs = totalSeconds % 60;
        String text = String.format("%02d:%02d", mins, secs);
        timerLabel.setText(text);
    }

    private void onTimerEnded() throws IOException {
        App.setRoot("YouLoseScreen");
    }


    @FXML
    void openInventory(MouseEvent event) {

    }

    @FXML
    void switchToLightPuzzle(MouseEvent event) throws IOException {
        ArrayList<Item> inventory = facade.getCurrentPlayer().getProgress().get(facade.getCurrentPlayer().getProgress().size()-1).getInventory();
        Item light = new Item("Flashlight", "Gives light", "Ground", null);
        for(Item i : inventory) {
            if(i.equals(light)) {
                return;
            }
        }
        facade.setCurrentItem("Flashlight");
        App.setRoot("lightswitch");
    }

    @FXML
    void giveRandomPuzzleVHS(MouseEvent event) throws IOException {
        ArrayList<Item> inventory = facade.getCurrentPlayer().getProgress().get(facade.getCurrentPlayer().getProgress().size()-1).getInventory();
        Item vhs = new Item("VHS Tape", "Contains a video", "Ground", null);
        for(Item i : inventory) {
            if(i.equals(vhs))
                return;
        }
        facade.nextPuzzle();
        facade.setCurrentItem("VHS");
        switch (facade.getCurrentPuzzle().getType()) {
            case "MultipleChoice" -> App.setRoot("MultipleChoice");
            case "Trivia" -> App.setRoot("trivia");
            case "Riddle" -> App.setRoot("riddle");
            case "Cipher" -> App.setRoot("cipher");
            default -> {
            }
        }
    }

    @FXML
    void giveRandomPuzzleCoins(MouseEvent event) throws IOException {
        ArrayList<Item> inventory = facade.getCurrentPlayer().getProgress().get(facade.getCurrentPlayer().getProgress().size()-1).getInventory();
        Item coins = new Item("Coins", "For an arcade machine", "Ground", null);
        for(Item i : inventory) {
            if(i.equals(coins))
                return;
        }
        facade.nextPuzzle();
        facade.setCurrentItem("Coins");
        switch (facade.getCurrentPuzzle().getType()) {
            case "MultipleChoice" -> App.setRoot("MultipleChoice");
            case "Trivia" -> App.setRoot("trivia");
            case "Riddle" -> App.setRoot("riddle");
            case "Cipher" -> App.setRoot("cipher");
            default -> {
            }
        }
    }

    @FXML
    void exitGame(MouseEvent event) throws IOException {
        facade.saveProgress();
        App.setRoot("landing");
    }
}
