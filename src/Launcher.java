import minesweeper.GUI;

/**
 * @authors Manos Tzenakis-TP4945 & Stamatis Mathioudakis Sarantopoulos-Tp4946
 * This is the launcher class and holds the
 * main method that runs the minesweeper game.
 */
public class Launcher {
    public static void main(String[] args) {
        // Start the game with a menu.
        new GUI("Minesweeper",9,9,10);
    }
}