package com.excape;

import java.io.IOException;

import com.model.EscapeGameFacade;
import com.model.MultipleChoice;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class MultipleChoiceController {

    @FXML
    private Button backButton;

    @FXML
    private Button optionA;

    @FXML
    private Button optionB;

    @FXML
    private Button optionC;

    @FXML
    private Label timer;

    @FXML
    private Label incorrectLabel;

    @FXML
    private Label question;
    
    EscapeGameFacade facade = EscapeGameFacade.getInstance();

    private Timeline timeline;
    private int secondsLeft = 0;

    @FXML
    public void initialize() {
        MultipleChoice puzzle = (MultipleChoice) facade.getCurrentPuzzle();
        question.setText(puzzle.getQuestion());
        optionA.setText(puzzle.getOptions().get(0));
        optionB.setText(puzzle.getOptions().get(1));
        optionC.setText(puzzle.getOptions().get(2));
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
    void tryAnswerA(ActionEvent event) throws IOException {
        String answer = optionA.getText();
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

    @FXML
    void tryAnswerB(ActionEvent event) throws IOException {
        String answer = optionB.getText();
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

    @FXML
    void tryAnswerC(ActionEvent event) throws IOException {
        String answer = optionC.getText();
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
