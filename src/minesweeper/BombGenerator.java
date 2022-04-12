package minesweeper;

import java.util.Random;

/**
 * This class implements the abstract bomb class and generates a bomb to a random square on the board
 */
public class BombGenerator
{
    GUI gui;

    int boardHeight;

    int boardWidth;

    public BombGenerator(GUI _gui, int number)
    {
        this.gui = _gui;
        boardHeight = gui.getHeight();
        boardWidth = gui.getWidth();

        int count =0;
        do {
            makeRandomBomb();
            count++;
        }while (count < number);
    }

    // Recursion
    public void makeRandomBomb()
    {
        Random r = new Random();
        int yLocation = r.nextInt(boardHeight);
        int xLocation = r.nextInt(boardWidth);
        Square square = (Square) gui.getSquareAt(xLocation, yLocation);
        if (!square.getBombExist())
        {
            square.setBombExist(true);

            // not really opened, but if we win, the board will stil go on if we won't do that
            square.setIsOpened(true);

        } else {
            makeRandomBomb();
        }
    }
}
