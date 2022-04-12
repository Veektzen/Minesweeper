package minesweeper;

/**
 * This class implements the 'Game' Option in menu bar
 */
public class Function_Game {
    GUI gui;


    public Function_Game(GUI _gui) {
        this.gui = _gui;
    }

    public void newGame() {

        new GUI("Minesweeper", gui.getWidth(), gui.getHeight(), gui.getBombs());
        gui.window.dispose();
    }

    public void beginner() {
        gui.setWidth(9);
        gui.setHeight(9);
        gui.setBombs(10);
        newGame();
    }

    public void intermediate() {

        gui.setWidth(16);
        gui.setHeight(16);
        gui.setBombs(30);
        newGame();
    }

    public void expert() {
        gui.setWidth(30);
        gui.setHeight(16);
        gui.setBombs(50);
        newGame();
    }

    public void scores() {;
        gui.createHighScoresDialog();
    }

    public void exit() {
        gui.window.dispose();
    }


}
