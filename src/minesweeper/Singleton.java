package minesweeper;

/**
 * This is the singleton class that returns an instance that holds the score repository
 */
public class Singleton {
    private static Singleton instance = new Singleton();
    private ScoreRepository scoresR;

    private Singleton(){
    scoresR = new ScoreRepository();
    }

    /**
     * @return an instance of the singleton class
     */
    public static Singleton getInstance(){
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public ScoreRepository getScoresR(){
        return (ScoreRepository) scoresR;
    }
}
