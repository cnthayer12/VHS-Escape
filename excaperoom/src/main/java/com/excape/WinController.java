package com.excape;

import java.io.IOException;

import com.model.EscapeGameFacade;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class WinController {

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button playAgain;

    @FXML
    private Label score;

    @FXML
    private Label time;

    EscapeGameFacade facade = EscapeGameFacade.getInstance();

    @FXML
    public void initialize() {
        score.setText("Score: " + facade.getScore());
        int totalSeconds = (int) facade.getElapsedTime();
        int mins = totalSeconds / 60;
        int secs = totalSeconds % 60;
        String text = String.format("%02d:%02d", mins, secs);
        time.setText("Time: " + text);
    }

    @FXML
    void backToMain(ActionEvent event) throws IOException {
        facade.saveProgress();
        facade.logout();
        App.setRoot("landing");
    }

    @FXML
    void toLeaderboard(ActionEvent event) throws IOException {
        facade.getCurrentPlayer().getProgress().get(facade.getCurrentPlayer().getProgress().size()-1).setScore(facade.getScore());
        facade.saveProgress();
        facade.logout();
        App.setRoot("leaderboard");
    }

}
