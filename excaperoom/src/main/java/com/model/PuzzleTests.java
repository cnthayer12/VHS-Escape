package com.model;

public class PuzzleTests {
    
    public static void main(String[] args) {
        System.out.println("===== PUZZLE TESTS =====\n");
        
        testTrivia();
        testRiddle();
        testCipher();
        testPixelHunt();
        
        System.out.println("\n===== DONE =====");
    }
    
    public static void testTrivia() {
        System.out.println("Testing Trivia:");
        
        Trivia trivia = new Trivia();
        trivia.setTriviaText("What 1985 film features a DeLorean?");
        trivia.setCorrectAnswer("Back to the Future");
        
        System.out.println("Wrong answer: " + trivia.checkAnswer("Terminator"));
        System.out.println("Right answer: " + trivia.checkAnswer("Back to the Future"));
        System.out.println();
    }
    
    public static void testRiddle() {
        System.out.println("Testing Riddle:");
        
        Riddle riddle = new Riddle();
        riddle.setRiddleText("I spin but never move. What am I?");
        riddle.setCorrectAnswer("tape");
        
        System.out.println("Wrong answer: " + riddle.checkAnswer("clock"));
        System.out.println("Right answer: " + riddle.checkAnswer("tape"));
        System.out.println();
    }
    
    public static void testCipher() {
        System.out.println("Testing Cipher:");
        
        Cipher cipher = new Cipher();
        cipher.setCipherText("FKDQQHO WKLUWHHQ");
        cipher.setCorrectAnswer("CHANNEL THIRTEEN");
        
        System.out.println("Wrong answer: " + cipher.checkAnswer("CHANNEL TEN"));
        System.out.println("Right answer: " + cipher.checkAnswer("CHANNEL THIRTEEN"));
        System.out.println();
    }
    
    public static void testPixelHunt() {
        System.out.println("Testing PixelHunt:");
        
        PixelHunt hunt = new PixelHunt();
        hunt.setCorrectX(100);
        hunt.setCorrectY(150);
        hunt.setCloseness(5);
        
        System.out.println("Wrong click (50, 50): " + hunt.checkClick(50, 50));
        System.out.println("Right click (100, 150): " + hunt.checkClick(100, 150));
        System.out.println();
    }
}
