package minesweeper;

import javax.swing.*;

/**
 * This is the abstract square class
 */
public abstract class ASquare extends JButton
{
    int xLocation;
    int yLocation;

    public ASquare(int x, int y, String filename)
    {
        super(new ImageIcon(filename));
        xLocation = x;
        yLocation = y;
    }

}
