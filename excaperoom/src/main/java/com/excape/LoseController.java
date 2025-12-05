package com.excape;

import java.io.IOException;

import com.model.EscapeGameFacade;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LoseController {

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button playAgain;

    EscapeGameFacade facade = EscapeGameFacade.getInstance();

    @FXML
    void backToMain(ActionEvent event) throws IOException {
        facade.getCurrentPlayer().getProgress().get(facade.getCurrentPlayer().getProgress().size()-1).setScore(0);
        facade.saveProgress();
        facade.logout();
        App.setRoot("landing");
    }

    @FXML
    void toLeaderboard(ActionEvent event) throws IOException {
        facade.getCurrentPlayer().getProgress().get(facade.getCurrentPlayer().getProgress().size()-1).setScore(0);
        facade.saveProgress();
        facade.logout();
        App.setRoot("leaderboard");
    }

}
