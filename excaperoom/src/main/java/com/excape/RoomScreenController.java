package com.excape;

import java.io.IOException;
import java.util.ArrayList;

import com.model.EscapeGameFacade;
import com.model.Item;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class RoomScreenController {

    @FXML
    private Button inventory;

    @FXML
    private Label lightCheck;

    @FXML
    private Label vhsCheck;

    @FXML
    private Label coinsCheck;

    @FXML
    private Label lightLabel;

    @FXML
    private Label vhsLabel;

    @FXML
    private Label coinsLabel;

    @FXML
    private Rectangle invBox;

    @FXML
    private Label timerLabel;
    private Timeline timeline;
    private int secondsLeft = 0;
    EscapeGameFacade facade = EscapeGameFacade.getInstance();
    ArrayList<Item> inventoryL = facade.getCurrentPlayer().getProgress().get(facade.getCurrentPlayer().getProgress().size()-1).getInventory();
    Boolean invOpen = false;

    @FXML
    public void initialize() {
        Sfx.play("/audio/teleport.wav");
        startTimer((int) facade.getRemainingTime());
    }

    public void startTimer(int seconds) {
        stopTimerIfRunning();

        this.secondsLeft = Math.max(0, seconds);
        updateLabel(secondsLeft);

        timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), ev -> {
                secondsLeft = (int) facade.getRemainingTime();
                if (secondsLeft <= 0) {
                    updateLabel(0);
                    stopTimerIfRunning();
                    try {
                        onTimerEnded();
                    } catch (IOException ex) {
                    }
                } else {
                    updateLabel(secondsLeft);
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void pauseTimer() {
        if (timeline != null) timeline.pause();
    }

    public void resumeTimer() {
        if (timeline != null) timeline.play();
    }

    public void stopTimerIfRunning() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }

    private void updateLabel(int totalSeconds) {
        int mins = totalSeconds / 60;
        int secs = totalSeconds % 60;
        String text = String.format("%02d:%02d", mins, secs);
        timerLabel.setText(text);
    }

    private void onTimerEnded() throws IOException {
        App.setRoot("YouLoseScreen");
    }


    @FXML
    void openInventory(MouseEvent event) throws IOException {
        System.out.println(invOpen);
        if(invOpen){
            lightLabel.setVisible(false);
            lightCheck.setVisible(false);
            vhsLabel.setVisible(false);
            vhsCheck.setVisible(false);
            coinsLabel.setVisible(false);
            coinsCheck.setVisible(false);
            invBox.setVisible(false);
        } else {
            Item light = new Item("Flashlight", "Gives light", "Ground", null);
            Item vhs = new Item("VHS Tape", "Contains a video", "Ground", null);
            Item coins = new Item("Coins", "For an arcade machine", "Ground", null);
            if(inventoryL != null && !inventoryL.isEmpty()){
                for(Item i : inventoryL) {
                    if(i.equals(light)) {
                        lightCheck.setText("✓");
                        lightCheck.setTextFill(Color.GREEN);
                    }
                    if(i.equals(vhs)) {
                        vhsCheck.setText("✓");
                        vhsCheck.setTextFill(Color.GREEN);
                    }
                    if(i.equals(coins)) {
                        coinsCheck.setText("✓");
                        coinsCheck.setTextFill(Color.GREEN);
                    }
                }
            }
                lightLabel.setVisible(true);
                lightCheck.setVisible(true);
                vhsLabel.setVisible(true);
                vhsCheck.setVisible(true);
                coinsLabel.setVisible(true);
                coinsCheck.setVisible(true);
                invBox.setVisible(true);
        }
        invOpen = !invOpen;
    }

    @FXML
    void switchToLightPuzzle(MouseEvent event) throws IOException {
        Item light = new Item("Flashlight", "Gives light", "Ground", null);
        for(Item i : inventoryL) {
            if(i.equals(light)) {
                return;
            }
        }
        Sfx.play("/audio/pickup.wav"); 
        facade.setCurrentItem("Flashlight");
        App.setRoot("lightswitch");
    }

    @FXML
    void giveRandomPuzzleVHS(MouseEvent event) throws IOException {
        Item vhs = new Item("VHS Tape", "Contains a video", "Ground", null);
        for(Item i : inventoryL) {
            if(i.equals(vhs))
                return;
        }
        Sfx.play("/audio/pickup.wav"); 
        facade.nextPuzzle();
        facade.setCurrentItem("VHS");
        facade.startPuzzle();
        switch (facade.getCurrentPuzzle().getType()) {
            case "MultipleChoice" -> App.setRoot("MultipleChoice");
            case "Trivia" -> App.setRoot("trivia");
            case "Riddle" -> App.setRoot("riddle");
            case "Cipher" -> App.setRoot("cipher");
            default -> {
            }
        }
    }

    @FXML
    void giveRandomPuzzleCoins(MouseEvent event) throws IOException {
        Item coins = new Item("Coins", "For an arcade machine", "Ground", null);
        for(Item i : inventoryL) {
            if(i.equals(coins))
                return;
        }

        Sfx.play("/audio/pickup.wav");
        facade.nextPuzzle();
        facade.setCurrentItem("Coins");
        facade.startPuzzle();
        switch (facade.getCurrentPuzzle().getType()) {
            case "MultipleChoice" -> App.setRoot("MultipleChoice");
            case "Trivia" -> App.setRoot("trivia");
            case "Riddle" -> App.setRoot("riddle");
            case "Cipher" -> App.setRoot("cipher");
            default -> {
            }
        }
    }

    @FXML
    void exitGame(MouseEvent event) throws IOException {
        facade.saveProgress();
        facade.logout();
        App.setRoot("landing");
    }

 

}
