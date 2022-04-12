package minesweeper;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * This is the square class that implements all the functionality of a square on the board
 */
public class Square extends ASquare implements MouseListener {

    // GUI instance
    private final GUI gui;

    // Bomb
    private boolean isBomb;

    // Red Flag
    private boolean isFlag;

    // Opened square
    private boolean isOpened;

    // The start time of the game
    private long startTime;

    private final int boardHeight;
    private final int boardWidth;
    private final int xLocation;
    private final int yLocation;

    public Square(int x, int y, GUI _gui) {
        super(x, y, "resources/block.png");
        this.setFocusable(false);

        // Assign board size
        this.gui = _gui;
        boardHeight = gui.getHeight();
        boardWidth = gui.getWidth();

        // Assign coordinates to this square.
        xLocation = x;
        yLocation = y;

        // Initialize attributes
        isBomb = false;
        isOpened = false;
        isFlag = false;
        startTime = 0;

        // add right mouse listener
        addMouseListener(this);

        // remove border
        setBorderPainted(false);
    }

    /**
     * this method counts the surroundings bomb of the given square
     * @param currentX location of the square
     * @param currentY location of the square
     */
    void countBombs(int currentX, int currentY) {
        Square currentObject;
        int count = 0;

        if (isOnBorder(currentX, currentY))
            return; // skip
        else if (((Square) gui.getSquareAt(currentX, currentY)).getIsOpened())
            return; // skip
        else {
            Square squareObject;
            currentObject = (Square) gui.getSquareAt(currentX, currentY);
            currentObject.setIsOpened(true);

            // Check the surroundings squares, and if we get to the border skip this square
            for (int x=-1;x<=1;x++) {
                for (int y=-1;y<=1;y++) {
                    if (isOnBorder(currentX + x, currentY + y)) {
                    } else if (x == 0 && y == 0) {
                    } else {
                        squareObject = (Square) gui.getSquareAt(currentX + x, currentY + y);
                        if (squareObject.getBombExist()){
                            count += 1;
                        }
                        else { // do nothing
                        }
                    }
                }
            }
        }

        if (!currentObject.getFlag()) {
            currentObject.setEnabled(false);
            if (count != 0) {
                currentObject.setDisabledIcon(new ImageIcon("resources/" + count + ".png"));
            } else {
                currentObject.setDisabledIcon(new ImageIcon("resources/0.png"));
                // up right
                countBombs(currentX + 1, currentY - 1);
                // right
                countBombs(currentX + 1, currentY);
                // down right
                countBombs(currentX + 1, currentY + 1);
                // down
                countBombs(currentX, currentY + 1);
                // down left
                countBombs(currentX - 1, currentY + 1);
                // left
                countBombs(currentX - 1, currentY);
                // up left
                countBombs(currentX - 1, currentY - 1);
                // up
                countBombs(currentX, currentY - 1);

            }
        }
    }

    /**
     * this method checks and reveals all the bombs or the wrong flags
     * @param currentX location of the given square
     * @param currentY location of the given square
     */
    void revealBombs(int currentX, int currentY) {
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                if (currentX == x && currentY == y) {
                } else if (((Square) gui.getSquareAt(x, y)).getBombExist()) {
                    gui.getSquareAt(x, y).setEnabled(false);
                    gui.getSquareAt(x, y).setDisabledIcon(new ImageIcon("resources/bomb.png"));
                } else if (((Square) gui.getSquareAt(x, y)).getFlag()) {
                    gui.getSquareAt(x, y).setEnabled(false);
                    gui.getSquareAt(x, y).setDisabledIcon(new ImageIcon("resources/flagWrong.png"));
                } else if (!((Square) gui.getSquareAt(x, y)).getIsOpened()){
                    gui.getSquareAt(x, y).setEnabled(false);
                    gui.getSquareAt(x, y).setDisabledIcon(new ImageIcon("resources/block.png"));
                }
            }
        }
    }

    /**
     * This method checks if there's a high score.
     */
    void checkHighScore() {
        Singleton instance = Singleton.getInstance();
        List<Score> scores = instance.getScoresR().readJson();
        if (gui.getBombs() == 10 && gui.costTime < scores.get(0).getTime()) {
            gui.createScoreDialog();
        }
        if (gui.getBombs() == 30 && gui.costTime < scores.get(1).getTime()) {
            gui.createScoreDialog();
        }
        if (gui.getBombs() == 50 && gui.costTime < scores.get(2).getTime()) {
            gui.createScoreDialog();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        // if the button is disabled don't do anything
        if (!this.isEnabled()) {
            return;
        }
        // If user right-click on this square.
        if (e.getButton() == MouseEvent.BUTTON3) {
            int minesLeft = Integer.parseInt(gui.minesLeftLabel.getText());

            if (getIcon().toString().equals("resources/block.png")) {
                setIcon(new ImageIcon("resources/redFlag.png"));
                isFlag = true;
                gui.minesLeftLabel.setText(String.format("%03d", minesLeft - 1));
            } else if (getIcon().toString().equals("resources/redFlag.png")) {
                setIcon(new ImageIcon("resources/questionMark.png"));
                isFlag = false;
                setIsOpened(false);
                gui.minesLeftLabel.setText(String.format("%03d", minesLeft + 1));

            } else if (getIcon().toString().equals("resources/questionMark.png")) {
                setIcon(new ImageIcon("resources/block.png"));
            }
        }
        if (e.getButton() == MouseEvent.BUTTON1 && !getFlag()) {
            // When the first click is pressed, start the timer
            if (((Square) gui.getSquareAt(0, 0)).getStartTime() == 0) {
                ((Square) gui.getSquareAt(0, 0)).setStartTime(System.currentTimeMillis());
            }

            isFlag = false;

            // if you click on a bomb
            if (isBomb) {
                // stop timer
                gui.timer.cancel();
                setEnabled(false);
                setDisabledIcon(new ImageIcon("resources/redBomb.png"));
                revealBombs(xLocation, yLocation);
                // Turn the face sad
                gui.faceButton.setIcon(new ImageIcon("resources/sadFace.png"));
            } else {
                countBombs(xLocation, yLocation);
                if (isSuccessful()) {
                    gui.faceButton.setIcon(new ImageIcon("resources/winnerFace.png"));
                    revealBombs(xLocation, yLocation);
                    // set mine counter to 0
                    gui.minesLeftLabel.setText("000");
                    // stop timer
                    gui.timer.cancel();
                    // check high score
                    checkHighScore();
                }
            }
        }
    }

    protected boolean isSuccessful() {
        // Ensure count start at 0 once this method is invoked.
        int count = 0;

        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                if (((Square) gui.getSquareAt(x, y)).getIsOpened())
                    count++;
            }
        }

        return count == boardHeight * boardWidth;
    }

    private boolean isOnBorder(int x, int y) {
        return x < 0 || x >= boardWidth || y < 0 || y >= boardHeight;
    }

    protected void setBombExist(boolean result) {
        isBomb = result;
    }

    protected boolean getBombExist() {
        return isBomb;
    }

    protected boolean getIsOpened() {
        return isOpened;
    }

    protected void setIsOpened(boolean result) {
        isOpened = result;
    }

    protected boolean getFlag() {
        return isFlag;
    }

    protected void setStartTime(long time) {
        startTime = time;
    }

    protected long getStartTime() {
        return startTime;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // if the button is disabled don't do anything
        if (!this.isEnabled()) {
            return;
        }
        if (e.getButton() == MouseEvent.BUTTON1 && !getFlag()) {
            gui.faceButton.setIcon(new ImageIcon("resources/enthusiastFace.png"));
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // if the button is disabled don't do anything
        if (!this.isEnabled()) {
            return;
        }
        if (e.getButton() == MouseEvent.BUTTON1 && !getFlag()) {
            gui.faceButton.setIcon(new ImageIcon("resources/smileFace.png"));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
