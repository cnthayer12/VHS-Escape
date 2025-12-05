package com.excape;

import java.io.IOException;

import com.model.EscapeGameFacade;
import com.model.Item;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class CongratsController {

    @FXML
    private Button nextButton;

    @FXML
    private ImageView iconCoins;

    @FXML
    private ImageView iconFlashlight;

    @FXML
    private ImageView iconVHS;

    @FXML
    private Label collectLabel;

    EscapeGameFacade facade = EscapeGameFacade.getInstance();

    @FXML
    public void initialize() {
        String itemRoom = facade.getCurrentItem();
        switch (itemRoom) {
            case "Flashlight":
                collectLabel.setText("You've collected the flashlight.");
                iconVHS.setVisible(false);
                iconCoins.setVisible(false);
                break;
            case "VHS":
                collectLabel.setText("You've collected the VHS.");
                iconFlashlight.setVisible(false);
                iconCoins.setVisible(false);
                break;
            case "Coins":
                collectLabel.setText("You've collected the coins.");
                iconVHS.setVisible(false);
                iconFlashlight.setVisible(false);
                break;
            default:
                break;
        }
    }

    @FXML
    void next(ActionEvent event) throws IOException {
        String itemRoom = facade.getCurrentItem();
        Item item = new Item();
        switch (itemRoom) {
            case "Flashlight":
                item = new Item("Flashlight", "Gives light", "Ground", null);
                break;
            case "VHS":
                item = new Item("VHS Tape", "Contains a video", "Ground", null);
                break;
            case "Coins":
                item = new Item("Coins", "For an arcade machine", "Ground", null);
                break;
            default:
                break;
        }
        facade.addItem(item);
        facade.setCurrentItem("");
        App.setRoot("roomscreen");
    }
}
