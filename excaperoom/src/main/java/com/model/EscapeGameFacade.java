package com.model;

import java.util.ArrayList;

public class EscapeGameFacade {
    private Game game;
    private PuzzlesManager puzzleman;
    private Players players;
    private int strikes;
    private static EscapeGameFacade instance;
    
    private EscapeGameFacade() {
        this.game = Game.getInstance();
        this.puzzleman = PuzzlesManager.getInstance();
        this.players = Players.getInstance();
        this.strikes = 0;
    }
    
    public static EscapeGameFacade getInstance() {
        if (instance == null) {
            instance = new EscapeGameFacade();
        }
        return instance;
    }
    
    public boolean startGame() {
        if (game == null) {
            return false;
        }
        
        if (puzzleman.getPuzzles().isEmpty()) {
            System.err.println("No puzzles loaded");
            return false;
        }
        
        puzzleman.setCurrentPuzzle(puzzleman.getPuzzles().get(0));
        this.strikes = 0;
        return true;
    }
    
    public boolean saveProgress() {
        if (players == null || game == null) {
            return false;
        }
        
        ArrayList<Player> currentPlayers = players.getPlayers();
        return DataWriter.savePlayers(currentPlayers) && DataWriter.savePuzzlesState(puzzleman);
    }
    
    public boolean loadProgress() {
        ArrayList<Player> loadedPlayers = DataLoader.getPlayers();
        
        if (loadedPlayers == null) {
            return false;
        }
        
        for (Player player : loadedPlayers) {
            players.addPlayer(player);
        }
        
        return true;
    }

    public void pauseGame() {
        if (game != null) {
            game.pause();
        }
    }

    public void resumeGame() {
        if (game != null) {
            game.resume();
        }
    }       

    public void endGame() {
        if (game != null) {
            game.exitMain();
        }
    }
    
    public boolean startPuzzle() {
        Puzzle current = puzzleman.getCurrentPuzzle();
        if (current != null) {
            current.startPuzzle();
            return true;
        }
        return false;
    }   
    
    public boolean completePuzzle() {
        Puzzle current = puzzleman.getCurrentPuzzle();
        if (current != null) {
            return current.completePuzzle();
        }
        return false;
    }

    public void skipPuzzle() {
        Puzzle current = puzzleman.getCurrentPuzzle();
        if (current != null) {
            current.skip();
        }
    }

    public boolean nextPuzzle() {
        if (game == null) {
            return false;
        }
        return game.nextPuzzle();
    }

    public boolean submitAnswer(String answer) {
        Puzzle current = puzzleman.getCurrentPuzzle();
        
        if (current == null || answer == null) {
            return false;
        }
        
        if (current instanceof Trivia) {
            return ((Trivia) current).checkAnswer(answer);
        } else if (current instanceof Riddle) {
            return ((Riddle) current).checkAnswer(answer);
        } else if (current instanceof Cipher) {
            return ((Cipher) current).checkAnswer(answer);
        }
        
        return false;
    }
/* 
    public boolean submitItemClick(int itemIndex) {
        Puzzle current = puzzleman.getCurrentPuzzle();
        
        if (current == null || !(current instanceof PointAndClick)) {
            return false;
        }
        
        return ((PointAndClick) current).clickItem(itemIndex);
    }
*/
    public double getProgressPercent() {
        if (game == null) {
            return 0.0;
        }
        return game.progressPercent();
    }

    public boolean createPlayer(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        ArrayList<Progress> initialProgress = new ArrayList<>();
        initialProgress.add(new Progress(0, new ArrayList<Item>(), new ArrayList<Hint>(), 0, 0));
        
        Player newPlayer = new Player(username, initialProgress);
        players.addPlayer(newPlayer);
        this.strikes = 0;
        
        return true;
    }

    public void displayStory() {
        if (game != null) {
            game.displayStory();
        }
    }

    public ArrayList<Hint> getAvailableHints() {
        Puzzle current = puzzleman.getCurrentPuzzle();
        if (current != null) {
            return current.getHints();
        }
        return new ArrayList<>();
    }

    public Hint revealHint(int hintIndex) {
        ArrayList<Hint> hints = getAvailableHints();
        
        if (hintIndex >= 0 && hintIndex < hints.size()) {
            Hint hint = hints.get(hintIndex);
            if (!hint.isUsed()) {
                hint.markUsed();
                return hint;
            }
        }
        
        return null;
    }

    public int getHintsUsed() {
        ArrayList<Hint> hints = getAvailableHints();
        int count = 0;
        
        for (Hint hint : hints) {
            if (hint.isUsed()) {
                count++;
            }
        }
        
        return count;
    }

    public Item revealHiddenItem(HiddenItem hiddenItem) {
        if (hiddenItem != null) {
            return hiddenItem.revealItem();
        }
        return null;
    }

    public int getStrikes() {
        return strikes;
    }

    public void addStrike() {
        strikes++;
        saveProgress();
    }

    public void resetStrikes() {
        strikes = 0;
    }

    public void updateScore(int points) {
        if (game != null) {
            game.setScore(game.getScore() + points);
            saveProgress();
        }
    }

    public int getScore() {
        if (game != null) {
            return game.getScore();
        }
        return 0;
    }

    public Puzzle getCurrentPuzzle() {
        return puzzleman.getCurrentPuzzle();
    }

    public ArrayList<Puzzle> getAllPuzzles() {
        return puzzleman.getPuzzles();
    }

    public int getTotalPuzzles() {
        return puzzleman.getPuzzles().size();
    }

    public int getCurrentPuzzleIndex() {
        ArrayList<Puzzle> puzzles = puzzleman.getPuzzles();
        Puzzle current = puzzleman.getCurrentPuzzle();
        
        if (current != null) {
            return puzzles.indexOf(current);
        }
        
        return -1;
    }
}