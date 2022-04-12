package minesweeper;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class contains all the graphical user interface of the game
 */
public class GUI implements ActionListener {

    // Main frame
    JFrame window;
    // Game board panel
    JPanel boardPanel;
    // Upper panel
    JPanel upperPanel;
    // Timer panel and label
    JPanel timerPanel;
    JLabel timerLabel;
    Timer timer;
    int costTime;
    // Squares
    Square[][] square;
    // Face
    JPanel facePanel;
    JButton faceButton;
    // Mines left counter
    JPanel minesLeftPanel;
    JLabel minesLeftLabel;
    // Top menu bar
    JMenuBar menuBar;
    JMenu menuGame, menuHelp;
    // Game menu
    JMenuItem iNew, iBeginner, iIntermediate, iExpert, iScores, iExit;
    // Help menu
    JMenuItem iAbout;
    // Title, width and height and bombs
    String title;
    int width, height, bombs;

    Function_Game game = new Function_Game(this);
    Function_Help help = new Function_Help(this);

    public GUI(String _title, int _width, int _height, int _bombs) {
        this.title = _title;
        this.width = _width;
        this.height = _height;
        this.bombs = _bombs;

        createWindow(title, width, height);
        createFace();
        createBoard(height, width);
        createTimer();
        createMineCounter();
        createMenuBar();
        createGameMenu();
        createHelpMenu();
        createUpperPanel();

        new BombGenerator(this, bombs);

        window.setVisible(true);
    }

    // Getters and setters for height and width
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getBombs() {
        return bombs;
    }

    public void setWidth(int _width) {
        this.width = _width;
    }

    public void setHeight(int _height) {
        this.height = _height;
    }

    public void setBombs(int _bombs) {
        this.bombs = _bombs;
    }

    // Getter for position of the square
    public ASquare getSquareAt(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight())
            return null;

        return square[x][y];
    }

    public void createWindow(String title, int width, int height) {

        // Initialize window
        window = new JFrame();
        // Give title to the window
        window.setTitle(title);
        // Set layout to the window
        window.setLayout(new BorderLayout());
        // When you hit X, the program closes completely
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set the size of the window
        window.setSize(5 + width * 20, 65 + height * 25);
        // Frame is displayed in the middle of the screen
        window.setLocationRelativeTo(null);
        // Frame is not resizable
        window.setResizable(false);
        // set frame up left icon
        ImageIcon frameImg = new ImageIcon("resources/Minesweeper.png");
        window.setIconImage(frameImg.getImage());
    }

    private void createUpperPanel() {
        // Add upper panel to the window
        upperPanel = new JPanel();
        upperPanel.setOpaque(true);
        upperPanel.setBackground(Color.DARK_GRAY);
        upperPanel.setLayout(new GridLayout(0, 3));
        upperPanel.add(timerPanel);
        upperPanel.add(facePanel);
        upperPanel.add(minesLeftPanel);
        window.add(upperPanel, BorderLayout.NORTH);
    }

    private void createFace() {

        // Initialize panel
        facePanel = new JPanel();
        facePanel.setSize(40, 40);
        facePanel.setBackground(Color.DARK_GRAY);

        // Initialize button
        faceButton = new JButton(new ImageIcon("resources/smileFace.png"));
        faceButton.setBorderPainted(false);
        faceButton.setPreferredSize(new Dimension(40, 40));
        faceButton.addActionListener(this);
        faceButton.setActionCommand("Face");
        facePanel.add(faceButton);

    }

    private void createBoard(int height, int width) {

        // Initialize panel
        boardPanel = new JPanel();
        // Give color
        boardPanel.setOpaque(true);
        boardPanel.setBackground(Color.DARK_GRAY);
        // Set layout
        GridLayout gridLayout = new GridLayout(height, width);
        gridLayout.setHgap(0);
        gridLayout.setVgap(0);
        boardPanel.setLayout(gridLayout);
        // Initialize
        square = new Square[width][height];
        // Fill the panel with game squares
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                square[x][y] = new Square(x, y, this);
                square[x][y].addActionListener(this);

                boardPanel.add(square[x][y]);
            }
        }
        // Add this panel to the main window
        window.add(boardPanel);
    }

    private void createMineCounter() {
        // Initialize panel and label
        minesLeftLabel = new JLabel("0" + String.valueOf(getBombs()));
        minesLeftPanel = new JPanel();
        minesLeftPanel.setBackground(Color.DARK_GRAY);
        minesLeftLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        minesLeftLabel.setForeground(Color.white);

        minesLeftPanel.add(minesLeftLabel);
    }

    private void createTimer() {
        // Initialize panel and label
        timerLabel = new JLabel("000");
        timerPanel = new JPanel();
        // Set font family
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        // And color
        timerLabel.setForeground(Color.white);
        timerPanel.setBackground(Color.DARK_GRAY);
        timerPanel.add(timerLabel);
        // Initialize timer
        timer = new Timer();

        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                costTime = Integer.parseInt(TimeConverter.calculateTime(System.currentTimeMillis() - ((Square) getSquareAt(0, 0)).getStartTime())) + 1;
                if (((((Square) getSquareAt(0, 0)).getStartTime())) != 0) {

                    if (costTime < 10)
                        timerLabel.setText("00" + costTime);

                    if (costTime >= 10 && costTime < 100)
                        timerLabel.setText("0" + costTime);

                    if (costTime >= 100)
                        timerLabel.setText(String.valueOf(costTime));

                    if (costTime >= 999) {
                        timerLabel.setText(String.valueOf(999));
                    }
                }
            }
        };
        // Do every 1 second ( 1000 ms )
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void createScoreDialog() {

        // Create padding
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        // Initialize dialog and its settings
        JDialog dialog = new JDialog(this.window, "Scores", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 180);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setBackground(Color.white);
        // set frame up left icon
        ImageIcon frameImg = new ImageIcon("resources/Minesweeper.png");
        dialog.setIconImage(frameImg.getImage());
        // Initialize panels
        JPanel textPanel = new JPanel(new GridLayout(2, 0));
        textPanel.setBorder(padding);
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.lightGray);
        buttonPanel.setBorder(padding);
        // Initialize text field
        JTextField nameField = new JTextField("Player 1");
        // Initialize labels
        JLabel messageLabel = new JLabel("""
                <html>
                    <body>
                "Congratulations, you achieved the highest score.
                <br>
                Please enter your name below.
                    </body>
                </html>
                """);
        textPanel.add(messageLabel);
        textPanel.add(nameField);
        Score highScore = new Score();
        // Initialize button
        JButton button = new JButton("OK");
        button.setFocusable(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Singleton instance = Singleton.getInstance();
                    List<Score> scores = instance.getScoresR().readJson();
                    ScoreDao dao = new ScoreDao(scores);
                    if (getBombs() == 10) {
                        highScore.setName(nameField.getText());
                        highScore.setTime(costTime);
                        dao.setNewScore(highScore, 0);
                        dialog.dispose();
                    } else if (getBombs() == 30) {
                        highScore.setName(nameField.getText());
                        highScore.setTime(costTime);
                        dao.setNewScore(highScore, 1);
                        dialog.dispose();
                    } else if (getBombs() == 50) {
                        highScore.setName(nameField.getText());
                        highScore.setTime(costTime);
                        dao.setNewScore(highScore, 2);
                        dialog.dispose();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonPanel.add(button, BorderLayout.EAST);

        // Add panels to the dialog
        dialog.add(textPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Set default button when pressing 'Enter'
        JRootPane rootPane = SwingUtilities.getRootPane(button);
        rootPane.setDefaultButton(button);

        dialog.setVisible(true);

    }

    public void createHighScoresDialog() {

        // Create padding
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        // Initialize dialog and its settings
        JDialog dialog = new JDialog(this.window, "Fastest Mine Sweepers", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 150);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setBackground(Color.white);
        // set frame up left icon
        ImageIcon frameImg = new ImageIcon("resources/Minesweeper.png");
        dialog.setIconImage(frameImg.getImage());
        // Initialize panels
        JPanel scorePanel = new JPanel(new GridLayout(3, 3));
        scorePanel.setBorder(padding);
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.lightGray);
        buttonPanel.setBorder(padding);
        // Initialize labels
        Singleton instance = Singleton.getInstance();
        List<Score> scores = instance.getScoresR().readJson();
        // Beginner
        JLabel beginnerTitle = new JLabel("Beginner");
        JLabel beginnerTime = new JLabel(scores.get(0).getTime() + " seconds");
        JLabel beginnerName = new JLabel(scores.get(0).getName());
        if (scores.get(0).getTime() == 999 && scores.get(0).getName().equals("Anonymous")){
            beginnerTime.setText("--");
        }
        // Intermediate
        JLabel intermediateTitle = new JLabel("Intermediate");
        JLabel intermediateTime = new JLabel(scores.get(1).getTime() + " seconds");
        JLabel intermediateName = new JLabel(scores.get(1).getName());
        if (scores.get(1).getTime() == 999 && scores.get(1).getName().equals("Anonymous")){
           intermediateTime.setText("--");
        }
        // Expert
        JLabel expertTitle = new JLabel("Expert");
        JLabel expertTime = new JLabel(scores.get(2).getTime() + " seconds");
        JLabel expertName = new JLabel(scores.get(2).getName());
        if (scores.get(2).getTime() == 999 && scores.get(2).getName().equals("Anonymous")){
            expertTime.setText("--");
        }
        // Add them on the panel
        scorePanel.add(beginnerTitle);
        scorePanel.add(beginnerTime);
        scorePanel.add(beginnerName);
        scorePanel.add(intermediateTitle);
        scorePanel.add(intermediateTime);
        scorePanel.add(intermediateName);
        scorePanel.add(expertTitle);
        scorePanel.add(expertTime);
        scorePanel.add(expertName);

        // Initialize button
        JButton button = new JButton("OK");
        button.setFocusable(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        buttonPanel.add(button, BorderLayout.EAST);

        // Add panels to the dialog
        dialog.add(scorePanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Set default button when pressing 'Enter'
        JRootPane rootPane = SwingUtilities.getRootPane(button);
        rootPane.setDefaultButton(button);

        dialog.setVisible(true);

    }


    public void createAboutDialog() {

        // Create padding
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        // Initialize dialog and its settings
        JDialog dialog = new JDialog(this.window, "About Minesweeper", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 350);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setBackground(Color.white);
        // set frame up left icon
        ImageIcon frameImg = new ImageIcon("resources/Minesweeper.png");
        dialog.setIconImage(frameImg.getImage());
        // Initialize panels
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(padding);
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(padding);
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.lightGray);
        buttonPanel.setBorder(padding);
        // Insert image to the panel
        try {
            BufferedImage myPicture = ImageIO.read(new File("resources/WindowsImage.png"));
            JLabel imageLabel = new JLabel(new ImageIcon(myPicture));
            imageLabel.setToolTipText("Windows Version");
            imagePanel.add(imageLabel, BorderLayout.CENTER);
            // Add Separator
            JSeparator separator = new JSeparator();
            separator.setForeground(Color.lightGray);
            separator.setBackground(Color.lightGray);
            imagePanel.add(separator, BorderLayout.SOUTH);
            // Or just insert text if the file is not found
        } catch (Exception e) {
            imagePanel.add(new JLabel("Windows Version"), BorderLayout.CENTER);
            // Add Separator
            JSeparator separator = new JSeparator();
            separator.setForeground(Color.lightGray);
            separator.setBackground(Color.lightGray);
            imagePanel.add(separator, BorderLayout.SOUTH);
        }

        JLabel informationLabel = new JLabel("""
                <html>
                    <body>
                        Minesweeper dark theme
                        <br>
                        Manos T
                        <br>
                        Version: 1.0.1
                        <br>
                        <br>
                        This version of Minesweeper made with love and compassion
                        </body>
                </html>
                    """);
        textPanel.add(informationLabel, BorderLayout.CENTER);

        // Initialize button
        JButton button = new JButton("OK");
        button.setFocusable(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        buttonPanel.add(button, BorderLayout.EAST);

        // Add panels to the dialog
        dialog.add(imagePanel, BorderLayout.NORTH);
        dialog.add(textPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Set default button when pressing 'Enter'
        JRootPane rootPane = SwingUtilities.getRootPane(button);
        rootPane.setDefaultButton(button);

        dialog.setVisible(true);
    }

    private void createMenuBar() {
        // Initialize menu bar
        menuBar = new JMenuBar();
        // Give color
        menuBar.setBackground(Color.DARK_GRAY);
        menuBar.setBorderPainted(false);
        // Add menu bar to the window
        window.setJMenuBar(menuBar);
        // Initialize menu option Game and Help
        // Game
        menuGame = new JMenu("Game");
        menuGame.setForeground(Color.white);
        menuBar.add(menuGame);
        // Help
        menuHelp = new JMenu("Help");
        menuHelp.setForeground(Color.white);
        menuBar.add(menuHelp);

    }


    private void createGameMenu() {
        // Initialize menu items for 'Game' option
        // New
        iNew = new JMenuItem("New");
        iNew.addActionListener(this);
        iNew.setActionCommand("New");
        menuGame.add(iNew);

        // Separator
        menuGame.addSeparator();

        // Beginner
        iBeginner = new JMenuItem("Beginner");
        iBeginner.addActionListener(this);
        iBeginner.setActionCommand("Beginner");
        menuGame.add(iBeginner);
        // Intermediate
        iIntermediate = new JMenuItem("Intermediate");
        iIntermediate.addActionListener(this);
        iIntermediate.setActionCommand("Intermediate");
        menuGame.add(iIntermediate);
        // Expert
        iExpert = new JMenuItem("Expert");
        iExpert.addActionListener(this);
        iExpert.setActionCommand("Expert");
        menuGame.add(iExpert);

        // Separator
        menuGame.addSeparator();

        // Scores
        iScores = new JMenuItem("Best Times...");
        iScores.addActionListener(this);
        iScores.setActionCommand("Scores");
        menuGame.add(iScores);

        // Separator
        menuGame.addSeparator();

        // Exit
        iExit = new JMenuItem("Exit");
        iExit.addActionListener(this);
        iExit.setActionCommand("Exit");
        menuGame.add(iExit);
    }

    public void createHelpMenu() {
        // About
        iAbout = new JMenuItem("About");
        iAbout.addActionListener(this);
        iAbout.setActionCommand("About");
        menuHelp.add(iAbout);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        // Get String action command and store it to variable 'command'
        String command = e.getActionCommand();

        switch (command) {
            case "New" -> game.newGame();
            case "Beginner" -> game.beginner();
            case "Intermediate" -> game.intermediate();
            case "Expert" -> game.expert();
            case "Scores" -> game.scores();
            case "Exit" -> game.exit();
            case "About" -> help.about();
            case "Face" -> {
                game.newGame();
                timer.cancel();
            }
        }
    }
}
