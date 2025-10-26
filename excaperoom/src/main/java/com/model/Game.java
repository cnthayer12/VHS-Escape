package com.model;
 
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class Game {
    private static final int EASY_TIME_LIMIT = 30 * 60;    //30 minutes
    private static final int MEDIUM_TIME_LIMIT = 20 * 60;  //20 minutes
    private static final int HARD_TIME_LIMIT = 15 * 60;    //15 minutes
    
    private static final int BASE_SCORE = 1000;
    private static final int HINT_PENALTY = 50;
    private static final int PUZZLE_COMPLETION_BONUS = 100;
    private static final int TIME_BONUS_MULTIPLIER = 10;
    
    private String gameID;
    private Player currentPlayer;
    private Instant startTime;
    private Instant endTime;
    private Difficulty difficulty;
    private int score;
    private boolean isPaused;
    private boolean isOver;
    private ArrayList<Puzzle> puzzles;
    private int completedCount;
    private String story;
    private Progress progress;
    private long pausedDuration; 
    private Instant pauseStartTime;
    
    private static Game instance;
    
    public enum Difficulty {
        EASY(EASY_TIME_LIMIT),
        MEDIUM(MEDIUM_TIME_LIMIT),
        HARD(HARD_TIME_LIMIT);
        
        private final int timeLimit;
        
        Difficulty(int timeLimit) {
            this.timeLimit = timeLimit;
        }
        
        public int getTimeLimit() {
            return timeLimit;
        }
    }
 
    private Game() {
        this.gameID = UUID.randomUUID().toString();
        this.score = BASE_SCORE;
        this.isPaused = false;
        this.isOver = false;
        this.puzzles = new ArrayList<>();
        this.completedCount = 0;
        this.story = "Welcome to the VHS Escape Room! You've discovered a mysterious VHS tape that transports you into a retro world. Solve the puzzles to escape before time runs out!";
        this.difficulty = Difficulty.MEDIUM; 
        this.pausedDuration = 0;
    }
  
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }
 
    public void initializeGame(Player player, Difficulty difficulty) {
        this.currentPlayer = player;
        this.difficulty = difficulty;
        this.startTime = Instant.now();
        this.score = BASE_SCORE;
        this.isPaused = false;
        this.isOver = false;
        this.completedCount = 0;
        this.pausedDuration = 0;
        
        this.puzzles = PuzzlesManager.getInstance().getPuzzles();
        
        if (player.getProgress() != null && !player.getProgress().isEmpty()) {
            this.progress = player.getProgress().get(0);
        } else {
            ArrayList<Progress> progressList = new ArrayList<>();
            progressList.add(new Progress(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, 0, BASE_SCORE));
            player.setProgress(progressList);
            this.progress = progressList.get(0);
        }
    }
   
    public int calculateScore() {
        int finalScore = BASE_SCORE;
        
        finalScore += (completedCount * PUZZLE_COMPLETION_BONUS);
        EscapeGameFacade facade = EscapeGameFacade.getInstance();
        if (facade != null) {
            finalScore -= (facade.getHintsUsed() * HINT_PENALTY);
            finalScore -= (facade.getStrikes() * 25); // 25 points per strike
        }

        if (isOver && completedCount == puzzles.size()) {
            long elapsedSeconds = getElapsedTime();
            long remainingSeconds = difficulty.getTimeLimit() - elapsedSeconds;
            if (remainingSeconds > 0) {
                finalScore += (int)(remainingSeconds * TIME_BONUS_MULTIPLIER);
            }
        }
        //not negative score
        finalScore = Math.max(0, finalScore);
        
        this.score = finalScore;
        return finalScore;
    }
    
    public long getElapsedTime() {
        if (startTime == null) {
            return 0;
        }
        
        Instant endPoint = (endTime != null) ? endTime : Instant.now();
        long totalSeconds = Duration.between(startTime, endPoint).getSeconds();
        
        long activePausedTime = pausedDuration;
        if (isPaused && pauseStartTime != null) {
            activePausedTime += Duration.between(pauseStartTime, Instant.now()).getSeconds();
        }
        
        return totalSeconds - activePausedTime;
    }

    public long getRemainingTime() {
        long elapsed = getElapsedTime();
        long remaining = difficulty.getTimeLimit() - elapsed;
        return Math.max(0, remaining);
    }

    public boolean isTimeUp() {
        return getRemainingTime() <= 0;
    }

    public void deleteGame() {
        // Reset instance
        instance = null;
    }

    public void pause() {
        if (!isPaused && !isOver) {
            isPaused = true;
            pauseStartTime = Instant.now();
        }
    }

    public void resume() {
        if (isPaused) {
            if (pauseStartTime != null) {
                pausedDuration += Duration.between(pauseStartTime, Instant.now()).getSeconds();
                pauseStartTime = null;
            }
            isPaused = false;
        }
    }

    public double progressPercent() {
        if (puzzles == null || puzzles.isEmpty()) {
            return 0.0;
        }
        return (completedCount * 100.0) / puzzles.size();
    }

    public boolean nextPuzzle() {
        if (puzzles == null || puzzles.isEmpty()) {
            return false;
        }

        boolean hasNext = PuzzlesManager.getInstance().nextPuzzle();
        
        if (hasNext) {
            return true;
        } else {
            endGame(true);
            return false;
        }
    }

    public Puzzle getCurrentPuzzle() {
        return PuzzlesManager.getInstance().getCurrentPuzzle();
    }
  
    public void completePuzzle() {
        completedCount++;
        calculateScore();
        
        if (isTimeUp()) {
            endGame(false);
        }
    }

    private void endGame(boolean successful) {
        isOver = true;
        endTime = Instant.now();
        calculateScore();
    }

    public void exitMain() {
        pause();
        System.out.println("Exiting to main menu...");
    }
    public void displayStory() {
        System.out.println("=".repeat(60));
        System.out.println(story);
        System.out.println("=".repeat(60));
    }
    
    public String getGameID() {
        return gameID;
    }
    
    public int getScore() {
        return score;
    }
    
   public void setScore(int score) {
    this.score = score;
}
    
    public boolean isOver() {
        return isOver;
    }
    
    public boolean isPaused() {
        return isPaused;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }
    
    public Difficulty getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    
    public ArrayList<Puzzle> getPuzzles() {
        return puzzles;
    }
    
    public void setPuzzles(ArrayList<Puzzle> puzzles) {
        this.puzzles = puzzles;
    }
    
    public int getCompletedCount() {
        return completedCount;
    }
    
    public Progress getProgress() {
        return progress;
    }
    
    public void setProgress(Progress progress) {
        this.progress = progress;
    }
    
    public String getStory() {
        return story;
    }
    
    public void setStory(String story) {
        this.story = story;
    }
    
    public Instant getStartTime() {
        return startTime;
    }
    
    public Instant getEndTime() {
        return endTime;
    }
    
    public int getCurrentPuzzleIndex() {
        return PuzzlesManager.getInstance().getCurrentPuzzleIndex();
    }
}