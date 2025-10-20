package com.model;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args){
        Players players = Players.getInstance();
        ArrayList<Player> playerList = DataLoader.getPlayers();
        players.setPlayers(playerList);
        System.out.println("Login scenario, success:");
        players.login("Lukin");

        System.out.println("\nLogin scenario, unsuccessful (already logged in):");
        players.login("Bob");

        System.out.println("\nLogout scenario, successful:");
        players.logout();

        System.out.println("\nLogin scenario, unsuccessful (account doesn't exist):");
        players.login("Bob");

        System.out.println("\nLogout scenario, unsuccessful (already logged out):");
        players.logout();
        
        System.out.println("\nCreate account scenario, unsuccessful (account exists already):");
        players.createAccount("Lukin");

        System.out.println("\nCreate account scenario, successful:");
        players.createAccount("Bob");

        System.out.println("\nCreate account scenario, unsuccessful (already logged in):");
        players.createAccount("Phil");

        System.out.println("\nLogout scenario, successful:");
        players.logout();

        // Puzzle Tests
        System.out.println("\n========== PUZZLE TESTS ==========");
        
        PuzzlesManager manager = PuzzlesManager.getInstance();
        manager.addPuzzle(PuzzleFactory.createVHSMultipleChoice());
        manager.addPuzzle(PuzzleFactory.createTVStaticPixelHunt());
        manager.addPuzzle(PuzzleFactory.createRemoteCipher());
        manager.addPuzzle(PuzzleFactory.create80sTrivia());
        manager.addPuzzle(PuzzleFactory.createVHSRiddle());
        
        System.out.println("\nMultiple choice puzzle, wrong answer:");
        MultipleChoicePuzzle mc = (MultipleChoicePuzzle) manager.getPuzzles().get(0);
        System.out.println(mc.displayQuestion());
        System.out.println("Answer 'Rewind the tape': " + mc.checkAnswer("Rewind the tape"));
        
        System.out.println("\nMultiple choice puzzle, correct answer:");
        System.out.println("Answer 'Pause at 13:37': " + mc.checkAnswer("Pause at 13:37"));
        
        System.out.println("\nPixel hunt puzzle, wrong answer:");
        PixelHuntPuzzle ph = (PixelHuntPuzzle) manager.getPuzzles().get(1);
        System.out.println(ph.displayGrid());
        System.out.println("Answer 'ABC': " + ph.checkAnswer("ABC"));
        
        System.out.println("\nPixel hunt puzzle, correct answer:");
        System.out.println("Answer 'VHS': " + ph.checkAnswer("VHS"));
        
        System.out.println("\nCipher puzzle, wrong answer:");
        CipherPuzzle cp = (CipherPuzzle) manager.getPuzzles().get(2);
        System.out.println(cp.displayPuzzle());
        System.out.println("Answer 'CHANNEL TEN': " + cp.checkAnswer("CHANNEL TEN"));
        
        System.out.println("\nCipher puzzle, correct answer:");
        System.out.println("Answer 'CHANNEL THIRTEEN': " + cp.checkAnswer("CHANNEL THIRTEEN"));
        
        System.out.println("\nTrivia puzzle, wrong answer:");
        TriviaPuzzle tp = (TriviaPuzzle) manager.getPuzzles().get(3);
        System.out.println(tp.displayQuestion());
        System.out.println("Answer 'The Terminator': " + tp.checkAnswer("The Terminator"));
        
        System.out.println("\nTrivia puzzle, correct answer:");
        System.out.println("Answer 'Back to the Future': " + tp.checkAnswer("Back to the Future"));
        
        System.out.println("\nRiddle puzzle, wrong answer:");
        RiddlePuzzle rp = (RiddlePuzzle) manager.getPuzzles().get(4);
        System.out.println(rp.displayRiddle());
        System.out.println("Answer 'clock': " + rp.checkAnswer("clock"));
        
        System.out.println("\nRiddle puzzle, correct answer:");
        System.out.println("Answer 'tape': " + rp.checkAnswer("tape"));
    }

    
}
