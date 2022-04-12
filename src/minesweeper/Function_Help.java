package minesweeper;

/**
 * This class implements the 'Help' Option in menu bar
 */
public class Function_Help {

    GUI gui;


    public Function_Help(GUI _gui) {

        this.gui = _gui;
    }

    public void about() {

        gui.createAboutDialog();
    }


}
