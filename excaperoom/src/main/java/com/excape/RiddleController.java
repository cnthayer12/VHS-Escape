package com.excape;

import java.io.IOException;

import com.model.EscapeGameFacade;
import com.model.Hint;
import com.model.Riddle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class RiddleController {

    @FXML
    private TextField answerBox;

    @FXML
    private Button backButton;

    @FXML
    private Button enterButton;

    @FXML
    private Button hintButton;

    @FXML
    private Label timer;

    @FXML
    private Label incorrectLabel;

    @FXML
    private Rectangle hintBox;

    @FXML
    private Label hintText;

    @FXML
    private TextArea hintText2;

    @FXML
    private Label question;

    Boolean hintUsed = false;
    
    EscapeGameFacade facade = EscapeGameFacade.getInstance();

    private Timeline timeline;
    private int secondsLeft = 0;

    @FXML
    public void initialize() {
        Riddle puzzle = (Riddle) facade.getCurrentPuzzle();
        question.setText(puzzle.getRiddleText());
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
        timer.setText(text);
    }

    private void onTimerEnded() throws IOException {
        App.setRoot("YouLoseScreen");
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        facade.setCurrentItem("");
        facade.saveProgress();
        App.setRoot("roomscreen");
    }

    @FXML
    void giveHint(ActionEvent event) throws IOException {
        if(!hintUsed) {
            Hint hint = facade.revealHint();
            if(hint != null) {
                hintBox.setOpacity(0.76);
                hintText.setOpacity(1);
                hintText2.setOpacity(1);
                hintText2.setText(hint.getText());
            }
            hintUsed = true;
        }
    }

    @FXML
    void tryAnswer(ActionEvent event) throws IOException {
        String answer = answerBox.getText();
        if(facade.submitAnswer(answer)) {
            facade.completePuzzle();
            if(facade.getCurrentItem().equals("Coins"))
                App.setRoot("Congrats");
            if(facade.getCurrentItem().equals("VHS"))
                App.setRoot("CongratsVHS");
        } else {
            incorrectLabel.setOpacity(1);
        }
    }

}
