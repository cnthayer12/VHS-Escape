
package com.model;

import java.util.ArrayList;

/**
 * SIMPLE Multiple Choice Puzzle
 */
class MultipleChoicePuzzle extends Puzzle {
    private String question;
    private ArrayList<String> choices;
    private String correctAnswer;
    
    public MultipleChoicePuzzle(String question, ArrayList<String> choices, String correctAnswer) {
        super();
        this.question = question;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }
    
    public String displayQuestion() {
        String display = "\n" + question + "\n";
        for (int i = 0; i < choices.size(); i++) {
            display += (char)('A' + i) + ") " + choices.get(i) + "\n";
        }
        return display;
    }
    
    public boolean checkAnswer(String answer) {
        return answer.equalsIgnoreCase(correctAnswer);
    }
}

/**
 * SIMPLE Pixel Hunt Puzzle
 */
class PixelHuntPuzzle extends Puzzle {
    private String[][] grid;
    private String hiddenCode;
    
    public PixelHuntPuzzle(String[][] grid, String hiddenCode) {
        super();
        this.grid = grid;
        this.hiddenCode = hiddenCode;
    }
    
    public String displayGrid() {
        String display = "\nFind the hidden code:\n";
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                display += grid[i][j] + " ";
            }
            display += "\n";
        }
        return display;
    }
    
    public boolean checkAnswer(String answer) {
        return answer.equalsIgnoreCase(hiddenCode);
    }
}

/**
 * SIMPLE Cipher Puzzle
 */
class CipherPuzzle extends Puzzle {
    private String encryptedMessage;
    private String decryptedAnswer;
    
    public CipherPuzzle(String encryptedMessage, String decryptedAnswer) {
        super();
        this.encryptedMessage = encryptedMessage;
        this.decryptedAnswer = decryptedAnswer;
    }
    
    public String displayPuzzle() {
        return "\nDecrypt this message: " + encryptedMessage + "\n";
    }
    
    public boolean checkAnswer(String answer) {
        return answer.equalsIgnoreCase(decryptedAnswer);
    }
}

/**
 * SIMPLE Trivia Puzzle
 */
class TriviaPuzzle extends Puzzle {
    private String question;
    private String correctAnswer;
    
    public TriviaPuzzle(String question, String correctAnswer) {
        super();
        this.question = question;
        this.correctAnswer = correctAnswer;
    }
    
    public String displayQuestion() {
        return "\n" + question + "\n";
    }
    
    public boolean checkAnswer(String answer) {
        return answer.equalsIgnoreCase(correctAnswer);
    }
}

/**
 * SIMPLE Riddle Puzzle
 */
class RiddlePuzzle extends Puzzle {
    private String riddle;
    private String answer;
    
    public RiddlePuzzle(String riddle, String answer) {
        super();
        this.riddle = riddle;
        this.answer = answer;
    }
    
    public String displayRiddle() {
        return "\n" + riddle + "\n";
    }
    
    public boolean checkAnswer(String answer) {
        return answer.equalsIgnoreCase(this.answer);
    }
}

/**
 * SIMPLE Factory to create puzzles
 */
class PuzzleFactory {
    
    public static MultipleChoicePuzzle createVHSMultipleChoice() {
        ArrayList<String> choices = new ArrayList<>();
        choices.add("Rewind the tape");
        choices.add("Fast forward");
        choices.add("Play normally");
        choices.add("Pause at 13:37");
        
        return new MultipleChoicePuzzle(
            "What should you do with the VHS tape?",
            choices,
            "Pause at 13:37"
        );
    }
    
    public static PixelHuntPuzzle createTVStaticPixelHunt() {
        String[][] grid = {
            {"X", "O", "X", "O", "X"},
            {"O", "X", "O", "X", "O"},
            {"X", "V", "H", "S", "X"},
            {"O", "X", "O", "X", "O"},
            {"X", "O", "X", "O", "X"}
        };
        
        return new PixelHuntPuzzle(grid, "VHS");
    }
    
    public static CipherPuzzle createRemoteCipher() {
        return new CipherPuzzle("FKDQQHO WKLUWHHQ", "CHANNEL THIRTEEN");
    }
    
    public static TriviaPuzzle create80sTrivia() {
        return new TriviaPuzzle(
            "What 1985 film features a DeLorean time machine?",
            "Back to the Future"
        );
    }
    
    public static RiddlePuzzle createVHSRiddle() {
        return new RiddlePuzzle(
            "I spin but never move, I show you the past. What am I?",
            "tape"
        );
    }
}