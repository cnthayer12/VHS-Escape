package com.excape;

import java.io.IOException;

import javafx.fxml.FXML;

public class LoginController {

    @FXML
    private void switchToLanding() throws IOException {
        App.setRoot("landing");
    }
}