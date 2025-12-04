package com.excape;

import java.io.IOException;

import com.model.EscapeGameFacade;
import com.model.Game.Difficulty;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DifficultyController {

    @FXML
    private Button btnEasy;

    @FXML
    private Button btnHard;

    @FXML
    private Button btnMedium;

    @FXML
    void setEasy(ActionEvent event) throws IOException {
        EscapeGameFacade facade = EscapeGameFacade.getInstance();
        facade.loadProgress();
        facade.startGame(facade.getCurrentPlayer(), Difficulty.EASY);
        App.setRoot("roomscreen");
    }

    @FXML
    void setMedium(ActionEvent event) throws IOException {
        EscapeGameFacade facade = EscapeGameFacade.getInstance();
        facade.loadProgress();
        facade.startGame(facade.getCurrentPlayer(), Difficulty.MEDIUM);
        App.setRoot("roomscreen");
    }

    @FXML
    void setHard(ActionEvent event) throws IOException {
        EscapeGameFacade facade = EscapeGameFacade.getInstance();
        facade.loadProgress();
        facade.startGame(facade.getCurrentPlayer(), Difficulty.HARD);
        App.setRoot("roomscreen");
    }
}
