package com.excape;

import java.io.IOException;
import java.util.ArrayList;

import com.model.EscapeGameFacade;
import com.model.Leaderboard;
import com.model.Leaderboard.LeaderboardEntry;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LeaderboardController {

    @FXML
    private Button menuButton;

    @FXML
    private Label player1;

    @FXML
    private Label player2;

    @FXML
    private Label player3;

    @FXML
    private Label player4;

    @FXML
    private Label score1;

    @FXML
    private Label score2;

    @FXML
    private Label score3;

    @FXML
    private Label score4;

    EscapeGameFacade facade = EscapeGameFacade.getInstance();

    @FXML
    public void initialize() {
        facade.loadProgress();
        Leaderboard leaderboard = Leaderboard.getInstance();
        ArrayList<LeaderboardEntry> top4 = leaderboard.getTopEntries(4);
        player1.setText(top4.get(0).getPlayerName());
        score1.setText("" + top4.get(0).getScore());
        player2.setText(top4.get(1).getPlayerName());
        score2.setText("" + top4.get(1).getScore());
        player3.setText(top4.get(2).getPlayerName());
        score3.setText("" + top4.get(2).getScore());
        player4.setText(top4.get(3).getPlayerName());
        score4.setText("" + top4.get(3).getScore());
    }

    @FXML
    void toMenu(ActionEvent event) throws IOException {
        App.setRoot("landing");
    }

}
