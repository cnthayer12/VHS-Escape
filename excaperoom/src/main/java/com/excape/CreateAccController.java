package com.excape;
import java.io.IOException;

import com.model.EscapeGameFacade;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

public class CreateAccController {

    @FXML
    private Label backArrow;

    @FXML
    private Button createAccButton;

    @FXML
    private Label createLabel;

    @FXML
    private Label donthaveLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Label insteadLabel;

    @FXML
    private Line line;

    @FXML
    private Label loginLink;

    @FXML
    private TextField pwInput;

    @FXML
    private Label pwLabel;

    @FXML
    private TextField unInput;

    @FXML
    private Label unLabel;

    @FXML
    void attemptCreate(MouseEvent event) throws IOException {
        String username = unInput.getText();
        String password = pwInput.getText();

        EscapeGameFacade facade = EscapeGameFacade.getInstance();

        facade.loadProgress();
        int createSuccess = facade.createPlayer(username, password);
        switch(createSuccess) {
            case 0:
                errorLabel.setText("User is already logged in");
                return;
            case 1:
                errorLabel.setText("Password cannot be empty");
                return;
            case 2:
                errorLabel.setText("Account already exists");
                return;
        }
        App.setRoot("gameEntry");
    }

    @FXML
    void switchToLanding(MouseEvent event) throws IOException {
        App.setRoot("landing");
    }

    @FXML
    void switchToLogin(MouseEvent event) throws IOException {
        App.setRoot("login");
    }

}
