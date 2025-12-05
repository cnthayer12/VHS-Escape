package com.model;
 
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Core game controller for the VHS Escape Room experience.
 * <p>
 * The {@code Game} class manages:
 * <ul>
 *     <li>Game lifecycle (start, pause, resume, end)</li>
 *     <li>Timing and difficulty-based time limits</li>
 *     <li>Score calculation (base score, bonuses, and penalties)</li>
 *     <li>Progress tracking (completed puzzles, percentage complete)</li>
 *     <li>Association with a {@link Player} and their {@link Progress}</li>
 * </ul>
 * <p>
 * This class is implemented as a singleton. Use {@link #getInstance()} to obtain
 * the active game instance.
 */
public class Game {

    /** Time limit in seconds for easy difficulty (30 minutes). */
    private static final int EASY_TIME_LIMIT = 30 * 60;    // 30 minutes

    /** Time limit in seconds for medium difficulty (20 minutes). */
    private static final int MEDIUM_TIME_LIMIT = 20 * 60;  // 20 minutes

    /** Time limit in seconds for hard difficulty (15 minutes). */
    private static final int HARD_TIME_LIMIT = 15 * 60;    // 15 minutes

    /** Base score awarded at the start of a game. */
    private static final int BASE_SCORE = 1000;

    /** Score penalty applied for each hint used. */
    private static final int HINT_PENALTY = 50;

    /** Bonus awarded for each puzzle completed. */
    private static final int PUZZLE_COMPLETION_BONUS = 100;

    /** Multiplier for remaining time when calculating time bonus. */
    private static final int TIME_BONUS_MULTIPLIER = 10;

    /** Unique identifier for this game instance. */
    private String gameID;

    /** The player currently associated with this game session. */
    private Player currentPlayer;

    /** Timestamp when the game started. */
    private Instant startTime;

    /** Timestamp when the game ended. */
    private Instant endTime;

    /** Current difficulty setting for the game. */
    private Difficulty difficulty;

    /** Current score for this game. */
    private int score;

    /** Flag indicating whether the game is currently paused. */
    private boolean isPaused;

    /** Flag indicating whether the game has ended. */
    private boolean isOver;

    /** List of puzzles included in this game. */
    private ArrayList<Puzzle> puzzles;

    /** Number of puzzles completed in this game. */
    private int completedCount;

    /** Narrative story text displayed at the start of the game. */
    private String story;

    /** Progress object associated with the current player for this game. */
    private Progress progress;

    /**
     * Total accumulated pause time in seconds. Used so pauses don't count
     * against the time limit.
     */
    private long pausedDuration;

    /** Timestamp when the game was last paused (if currently paused). */
    private Instant pauseStartTime;

    /** Singleton instance of the {@code Game}. */
    private static Game instance;

    /**
     * Difficulty levels for the game, each with an associated time limit
     * (in seconds).
     */
    public enum Difficulty {
        /** Easy difficulty with the longest time limit. */
        EASY(EASY_TIME_LIMIT),

        /** Medium difficulty with a moderate time limit. */
        MEDIUM(MEDIUM_TIME_LIMIT),

        /** Hard difficulty with the shortest time limit. */
        HARD(HARD_TIME_LIMIT);

        /** Time limit in seconds for this difficulty. */
        private final int timeLimit;

        /**
         * Constructs a difficulty level with the given time limit.
         *
         * @param timeLimit the time limit in seconds
         */
        Difficulty(int timeLimit) {
            this.timeLimit = timeLimit;
        }

        /**
         * Returns the time limit for this difficulty level.
         *
         * @return the time limit in seconds
         */
        public int getTimeLimit() {
            return timeLimit;
        }
    }

    /**
     * Private constructor to enforce the singleton pattern.
     * Initializes default values, including a random game ID, base score,
     * default difficulty (MEDIUM), and default story text.
     */
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

    /**
     * Returns the singleton instance of the {@code Game}.
     *
     * @return the shared {@code Game} instance
     */
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    /**
     * Initializes a new game session for the given player and difficulty.
     * <p>
     * This method:
     * <ul>
     *     <li>Associates the game with the specified player.</li>
     *     <li>Sets the difficulty and resets score and flags.</li>
     *     <li>Loads puzzles from {@link PuzzlesManager}.</li>
     *     <li>Initializes or reuses the player's {@link Progress} object.</li>
     * </ul>
     *
     * @param player     the player starting the game
     * @param difficulty the selected difficulty level
     */
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

    /**
     * Calculates the final score for the game based on:
     * <ul>
     *     <li>Base score</li>
     *     <li>Puzzles completed</li>
     *     <li>Hints used (penalty)</li>
     *     <li>Strikes (penalty)</li>
     *     <li>Remaining time (bonus, if all puzzles are completed and game is over)</li>
     * </ul>
     * The score is never allowed to go below zero.
     *
     * @return the computed final score
     */
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
        // not negative score
        finalScore = Math.max(0, finalScore);
        
        this.score = finalScore;
        return finalScore;
    }

    /**
     * Returns the elapsed active game time in seconds.
     * <p>
     * Paused time is subtracted so it does not count against the time limit.
     *
     * @return elapsed time in seconds, excluding paused durations
     */
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

    /**
     * Returns the remaining time in seconds before the time limit is reached.
     *
     * @return remaining time in seconds; will not be negative
     */
    public long getRemainingTime() {
        long elapsed = getElapsedTime();
        long remaining = difficulty.getTimeLimit() - elapsed;
        return Math.max(0, remaining);
    }

    /**
     * Indicates whether the time limit has been reached or exceeded.
     *
     * @return {@code true} if no time remains; {@code false} otherwise
     */
    public boolean isTimeUp() {
        return getRemainingTime() <= 0;
    }

    /**
     * Deletes the game singleton instance by setting it to {@code null}.
     * <p>
     * The next call to {@link #getInstance()} will create a fresh game object.
     */
    public void deleteGame() {
        // Reset instance
        instance = null;
    }

    /**
     * Pauses the game timer if the game is currently running and not over.
     * Records the time the game was paused so it can be excluded from elapsed time.
     */
    public void pause() {
        if (!isPaused && !isOver) {
            isPaused = true;
            pauseStartTime = Instant.now();
        }
    }

    /**
     * Resumes the game timer if the game is currently paused.
     * Accumulates the paused duration so it can be subtracted from elapsed time.
     */
    public void resume() {
        if (isPaused) {
            if (pauseStartTime != null) {
                pausedDuration += Duration.between(pauseStartTime, Instant.now()).getSeconds();
                pauseStartTime = null;
            }
            isPaused = false;
        }
    }

    /**
     * Returns the percentage of puzzles completed.
     *
     * @return completion percentage from {@code 0.0} to {@code 100.0}
     */
    public double progressPercent() {
        if (puzzles == null || puzzles.isEmpty()) {
            return 0.0;
        }
        return (completedCount * 100.0) / puzzles.size();
    }

    /**
     * Advances to the next puzzle using {@link PuzzlesManager}.
     * <p>
     * If there are no more puzzles, the game is ended successfully.
     *
     * @return {@code true} if a next puzzle exists; {@code false} if this was the last puzzle
     */
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

    /**
     * Returns the current puzzle from {@link PuzzlesManager}.
     *
     * @return the current {@link Puzzle}, or {@code null} if none is active
     */
    public Puzzle getCurrentPuzzle() {
        return PuzzlesManager.getInstance().getCurrentPuzzle();
    }

    /**
     * Marks the current puzzle as completed and updates score and progress.
     * <p>
     * This method:
     * <ul>
     *     <li>Adds the current puzzle to the player's completed puzzles.</li>
     *     <li>Increments the completed puzzle count.</li>
     *     <li>Recalculates the score.</li>
     *     <li>Ends the game (unsuccessfully) if time has expired.</li>
     * </ul>
     */
    public void completePuzzle() {
        Progress progress = Players.getCurrentPlayer().getProgress().get(Players.getCurrentPlayer().getProgress().size()-1);
        PuzzlesManager pman = PuzzlesManager.getInstance();
        progress.addCompletedPuzzle(pman.getCurrentPuzzle());
        completedCount++;
        calculateScore();
        
        if (isTimeUp()) {
            endGame(false);
        }
    }

    /**
     * Ends the current game session.
     * <p>
     * This method sets the {@code isOver} flag, records the end time,
     * and recalculates the final score.
     *
     * @param successful {@code true} if the game was completed successfully;
     *                   {@code false} otherwise
     */
    public void endGame(boolean successful) {
        isOver = true;
        endTime = Instant.now();
        calculateScore();
    }

    /**
     * Performs any cleanup or main-loop exit logic when the game is finished.
     * Currently implemented as a simple call to {@link #pause()}.
     */
    public void exitMain() {
        pause();
    }

    /**
     * Displays the game's story text to the console, framed by separator lines.
     */
    public void displayStory() {
        System.out.println("=".repeat(60));
        System.out.println(story);
        System.out.println("=".repeat(60));
    }

    /**
     * Returns the unique identifier of this game instance.
     *
     * @return the game ID
     */
    public String getGameID() {
        return gameID;
    }

    /**
     * Returns the current score.
     *
     * @return the current score value
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the current score to the provided value.
     *
     * @param score the new score value
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Indicates whether the game has ended.
     *
     * @return {@code true} if the game is over; {@code false} otherwise
     */
    public boolean isOver() {
        return isOver;
    }

    /**
     * Indicates whether the game is currently paused.
     *
     * @return {@code true} if the game is paused; {@code false} otherwise
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Returns the player associated with this game session.
     *
     * @return the current {@link Player}
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player associated with this game session.
     *
     * @param player the {@link Player} to associate with the game
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    /**
     * Returns the current difficulty setting.
     *
     * @return the game {@link Difficulty}
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty level for the game.
     *
     * @param difficulty the new {@link Difficulty} value
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Returns the list of puzzles in this game.
     *
     * @return an {@link ArrayList} of {@link Puzzle} objects
     */
    public ArrayList<Puzzle> getPuzzles() {
        return puzzles;
    }

    /**
     * Sets the list of puzzles for this game.
     *
     * @param puzzles the list of puzzles to use
     */
    public void setPuzzles(ArrayList<Puzzle> puzzles) {
        this.puzzles = puzzles;
    }

    /**
     * Returns the number of puzzles that have been completed.
     *
     * @return the completed puzzle count
     */
    public int getCompletedCount() {
        return completedCount;
    }

    /**
     * Returns the progress object associated with the current player.
     *
     * @return the {@link Progress} for this game
     */
    public Progress getProgress() {
        return progress;
    }

    /**
     * Sets the progress object for this game.
     *
     * @param progress the {@link Progress} to associate
     */
    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    /**
     * Returns the story text describing the game scenario.
     *
     * @return the story string
     */
    public String getStory() {
        return story;
    }

    /**
     * Sets the story text describing the game scenario.
     *
     * @param story the new story string
     */
    public void setStory(String story) {
        this.story = story;
    }

    /**
     * Returns the timestamp when the game started.
     *
     * @return the start {@link Instant}, or {@code null} if not started
     */
    public Instant getStartTime() {
        return startTime;
    }

    /**
     * Returns the timestamp when the game ended.
     *
     * @return the end {@link Instant}, or {@code null} if the game is still running
     */
    public Instant getEndTime() {
        return endTime;
    }

    /**
     * Returns the index of the current puzzle from {@link PuzzlesManager}.
     *
     * @return zero-based index of the current puzzle
     */
    public int getCurrentPuzzleIndex() {
        return PuzzlesManager.getInstance().getCurrentPuzzleIndex();
    }
}
