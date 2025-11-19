package com.excape;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

public class LoginController {

    @FXML
    private Label backArrow;

    @FXML
    private Label createAccLink;

    @FXML
    private Label donthaveLabel;

    @FXML
    private Label insteadLabel;

    @FXML
    private Line line;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginLabel;

    @FXML
    private TextField pwInput;

    @FXML
    private Label pwLabel;

    @FXML
    private TextField unInput;

    @FXML
    private Label unLabel;

    @FXML
    void attemptLogin(MouseEvent event) throws IOException {

    }

    @FXML
    void switchToCreateAcc(MouseEvent event) throws IOException {
        App.setRoot("createAccount");
    }

    @FXML
    void switchToLanding(MouseEvent event) throws IOException {
        App.setRoot("landing");
    }

}
