package com.excape;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class LandingController {

    @FXML
    private VBox bgLanding;

    @FXML
    private Button btnCreateAcc;

    @FXML
    private Button btnLeaderboard;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblWelcome;

    @FXML
    void switchToCreateAcc(ActionEvent event) throws IOException {
        App.setRoot("createAcc");
    }

    @FXML
    void switchToLeaderboard(ActionEvent event) throws IOException {
        App.setRoot("leaderboard");
    }

    @FXML
    void switchToLogin(ActionEvent event) throws IOException {
        App.setRoot("login");
    }

}
