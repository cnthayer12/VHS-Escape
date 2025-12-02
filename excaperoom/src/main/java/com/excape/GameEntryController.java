package com.excape;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GameEntryController {

    @FXML
    private Label lblEntry;

    @FXML
    private Button btnEnter;

    @FXML
    void switchToDifficulty(ActionEvent event) throws IOException {
        App.setRoot("difficulty");
    }

}
