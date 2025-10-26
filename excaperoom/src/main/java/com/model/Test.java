package com.model;
import java.util.ArrayList;

public class Test {
    static EscapeGameFacade facade = EscapeGameFacade.getInstance();
    public static void main(String[] args){
        facade.loadProgress();
        facade.login("Lukin", "1234");
        Player currentPlayer = Players.getCurrentPlayer();
        Progress progress = new Progress();
        progress.setStrikes(2);
        progress.setScore(500);
        progress.addItem(new Item("Test Item", "item for testing", "Kitchen", null));
        ArrayList<Progress> progresses = new ArrayList<>();
        progresses.add(progress);
        currentPlayer.setProgress(progresses);
        facade.logout();
    }
}