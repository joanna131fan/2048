package org.cis120.game2048;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.awt.Graphics2D;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 *
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 *
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Game2048 model; // model for the game
    private JLabel status; // current status text
    private InputKey key;
    private int x, y;
    private UnitBox[][] board;
    private LinkedList<UnitBox[][]> gameHistory = new LinkedList<>();
    private boolean win;
    // Board Itself
    private BufferedImage gameBoard;
    // Board + boxes
    private BufferedImage boxBoard;
    private boolean started;
    private boolean canAdd;
    private boolean gameOver = false;
    private Integer score = 0;
    private Integer highScore = 0;
    private Font scoreFont;

    // Game constants
    public static final int SPACE = 10;
    public static final int BOARD_WIDTH = 5 * SPACE + 4 * UnitBox.WIDTH;
    public static final int BOARD_HEIGHT = 5 * SPACE + 4 * UnitBox.HEIGHT;

    // Save Data
    private String dataPath;
    private String fileName = "fileSavedData";

    /**
     * Initializes the game board.
     */
    public GameBoard(int x, int y) {
        try {
            dataPath = GameBoard.class.getProtectionDomain().getCodeSource().getLocation().toURI()
                    .getPath();
            // TO FIND FILE:
            // System.out.print(dataPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        scoreFont = Game2048.getMainFont().deriveFont(20f);
        this.x = x;
        this.y = y;
        setFocusable(true);

        board = new UnitBox[4][4];
        gameHistory.add(board);
        gameBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        boxBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        loadHighScore();
        createImage();

        reset();
    }

    private void createFile(int init) {
        try {
            File file = new File(dataPath, fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("" + init);
            // System.out.print("WRITTEN FILE");
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createImage() {
        Graphics2D g = (Graphics2D) gameBoard.getGraphics();
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(new Color(0xDEDEDE));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int x = 10 * (j + 1) + UnitBox.WIDTH * j;
                int y = 10 * (i + 1) + UnitBox.HEIGHT * i;
                g.fillRoundRect(
                        x, y, UnitBox.WIDTH, UnitBox.HEIGHT, UnitBox.ARC_WIDTH, UnitBox.ARC_HEIGHT
                );
            }
        }
    }

    private void loadHighScore() {
        try {
            File file = new File(dataPath, fileName);
            if (!file.isFile()) {
                createFile(0);
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file))
            );
            highScore = Integer.parseInt(reader.readLine());

            reader.close();
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setHighScore() {
        FileWriter output = null;
        try {
            File file = new File(dataPath, fileName);

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.write("" + highScore);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    /**
     * (Re-)sets/Starts the game to its initial state.
     */
    public void reset() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = null;
            }
        }
        for (int i = 0; i < 2; i++) {
            randomNewBox();
        }
        gameHistory.clear();
        gameHistory.add(board);
        score = 0;
        win = false;
        gameOver = false;
    }

    private void randomNewBox() {
        Random ran = new Random();

        while (true) {
            int row = ran.nextInt(4);
            int col = ran.nextInt(4);
            if (board[row][col] == null) {
                int val = ran.nextInt(10) < 7 ? 2 : 4;
                int xCoord = 10 * (col + 1) + col * UnitBox.WIDTH;
                int yCoord = 10 * (row + 1) + row * UnitBox.HEIGHT;
                board[row][col] = new UnitBox(val, xCoord, yCoord);
                break;
            }
        }
    }

    public void update() {
        keyStatus();
        if (score > highScore) {
            highScore = score;
            setHighScore();
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                UnitBox box = board[i][j];
                if (box == null) {
                    continue;
                }
                box.update();
                updatePosition(box, i, j);
                if (box.getValue() == 2048) {
                    win = true;
                }
            }
        }
    }

    private void updatePosition(UnitBox curr, int row, int col) {
        if (curr == null) {
            return;
        }
        int x = 10 * (col + 1) + col * UnitBox.WIDTH;
        int y = 10 * (row + 1) + row * UnitBox.HEIGHT;

        if (Math.abs(curr.getX() - x) < UnitBox.SLIDE_SPEED) {
            curr.setX(x);
        }

        if (Math.abs(curr.getY() - y) < UnitBox.SLIDE_SPEED) {
            curr.setY(y);
        }

        if (curr.getX() < x) {
            curr.setX(curr.getX() + UnitBox.SLIDE_SPEED);
        }
        if (curr.getY() < y) {
            curr.setY(curr.getY() + UnitBox.SLIDE_SPEED);
        }
        if (curr.getX() > x) {
            curr.setX(curr.getX() - UnitBox.SLIDE_SPEED);
        }
        if (curr.getY() > y) {
            curr.setY(curr.getY() - UnitBox.SLIDE_SPEED);
        }

    }

    // for testing
    private void printBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == null) {
                    System.out.print(0);
                } else {
                    System.out.print(board[i][j].getValue());
                }
            }
            System.out.println();
        }
    }

    private void printPosition() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == null) {
                    System.out.print(0);
                } else {
                    System.out.print("(" + board[i][j].getX() + "," + board[i][j].getY() + ")");
                }
            }
            System.out.println();
        }
    }

    // Intentional: Will not return original if added tiles together
    private void addGameHistory() {
        UnitBox[][] temp = new UnitBox[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(board[i], 0, temp[i], 0, 4);
        }
        gameHistory.add(temp);
    }

    private void keyStatus() {

        if (InputKey.typed(KeyEvent.VK_LEFT)) {
            // left = 0
            addGameHistory();
            moveBox(0);
            if (!started) {
                started = !gameOver;
            }
            // System.out.print("highScore:" + highScore);
            // System.out.print("Score:" + score);
            // printBoard();
            // printPosition();
        } else if (InputKey.typed(KeyEvent.VK_RIGHT)) {
            // right = 1
            addGameHistory();
            moveBox(1);
            if (!started) {
                started = !gameOver;
            }
            // System.out.print("highScore:" + highScore);
            // System.out.print("Score:" + score);
            // printBoard();
            // printPosition();
        } else if (InputKey.typed(KeyEvent.VK_DOWN)) {
            // down = 2
            addGameHistory();
            moveBox(2);
            if (!started) {
                started = !gameOver;
            }
            // System.out.print("highScore:" + highScore);
            // System.out.print("Score:" + score);
            // printBoard();
            // printPosition();
        } else if (InputKey.typed(KeyEvent.VK_UP)) {
            // up = 3
            addGameHistory();
            moveBox(3);
            if (!started) {
                started = !gameOver;
            }
            // System.out.print("highScore:" + highScore);
            // System.out.print("Score:" + score);
            // printBoard();
            // printPosition();
        } else if (InputKey.typed(KeyEvent.VK_Z)) {
            if (!gameHistory.isEmpty()) {
                board = gameHistory.removeLast();
                printBoard();
            }
        }
    }

    public void moveBox(int dir) {
        boolean canMove = false;
        if (dir == 0) {
            // left
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (!canMove) {
                        canMove = movable(i, j, -1, 0);
                    } else {
                        movable(i, j, -1, 0);
                    }
                }
            }
        } else if (dir == 1) {
            // right
            for (int i = 0; i < 4; i++) {
                for (int j = 3; j >= 0; j--) {
                    if (!canMove) {
                        canMove = movable(i, j, 1, 0);
                    } else {
                        movable(i, j, 1, 0);
                    }
                }
            }
        } else if (dir == 2) {
            // down
            for (int i = 3; i >= 0; i--) {
                for (int j = 0; j < 4; j++) {
                    if (!canMove) {
                        canMove = movable(i, j, 0, 1);
                    } else {
                        movable(i, j, 0, 1);
                    }
                }
            }
        } else if (dir == 3) {
            // up
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (!canMove) {
                        canMove = movable(i, j, 0, -1);
                    } else {
                        movable(i, j, 0, -1);
                    }
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                UnitBox curr = board[i][j];
                if (curr == null) {
                    continue;
                }
                curr.setCanAdd(true);
            }
        }
        if (canMove) {
            randomNewBox();
            checkKO();
        }
    }

    private boolean checkNeighbors(int r, int c, UnitBox curr) {
        if (r > 0) {
            if (board[r - 1][c] == null) {
                return true;
            }
            return board[r - 1][c].getValue() == curr.getValue();
        }
        if (c > 0) {
            if (board[r][c - 1] == null) {
                return true;
            }
            return board[r][c - 1].getValue() == curr.getValue();
        }
        if (r < 3) {
            if (board[r + 1][c] == null) {
                return true;
            }
            return board[r + 1][c].getValue() == curr.getValue();
        }
        if (c < 3) {
            if (board[r][c + 1] == null) {
                return true;
            }
            return board[r][c + 1].getValue() == curr.getValue();
        }
        return false;
    }

    // game over
    private void checkKO() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == null) {
                    return;
                }
                if (checkNeighbors(i, j, board[i][j])) {
                    return;
                }
            }
        }
        gameOver = true;
        setHighScore();
    }

    private boolean movable(int r, int c, int xDir, int yDir) {
        boolean move = false;

        UnitBox curr = board[r][c];
        if (curr == null) {
            return false;
        }
        int newR = r;
        int newC = c;
        while (true) {
            newR += yDir;
            newC += xDir;
            if (outOfBounds(newR, newC)) {
                break;
            }
            if (board[newR][newC] == null) {
                board[newR][newC] = curr;
                board[newR - yDir][newC - xDir] = null;
                board[newR][newC].setNextPoint(newR, newC);
                move = true;
            } else if (board[newR][newC].getValue() == curr.getValue()
                    && board[newR][newC].canAdd()) {
                board[newR][newC].setCanAdd(false);
                move = true;
                board[newR][newC]
                        .setValue(board[newR][newC].getValue() + board[newR][newC].getValue());
                board[newR - yDir][newC - xDir] = null;
                board[newR][newC].setNextPoint(newR, newC);
                score += board[newR][newC].getValue();
            } else {
                break;
            }
        }
        return move;
    }

    private boolean outOfBounds(int r, int c) {
        return (r < 0 || r > 3 || c < 0 || c > 3);
    }

    public void render(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) boxBoard.getGraphics();
        g2.setColor(Color.white);
        g2.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        g2.drawImage(gameBoard, 0, 0, null);

        // draw tiles
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                UnitBox current = board[i][j];
                if (current == null) {
                    continue;
                }
                current = new UnitBox(
                        board[i][j].getValue(), board[i][j].getX(), board[i][j].getY()
                );
                current.render(g2);
            }
        }
        g.drawImage(boxBoard, x, y, null);
        g2.dispose();
        g.setColor(Color.blue);
        g.setFont(scoreFont);
        g.drawString("Score: " + score, 30, 40);
        g.setColor(Color.red);
        g.drawString("High Score: " + highScore, 30, 70);
        if (win) {
            g.setColor(Color.MAGENTA);
            g.drawString("You Win!", Game2048.WIDTH / 2, 90);
        }
        if (gameOver) {
            g.setColor(Color.MAGENTA);
            g.drawString("You Lose! Try Again!", Game2048.WIDTH / 2, 90);
        }
    }

    // For Testing
    public void setBoardValue(int r, int c, int val) {
        if (board[r][c] == null) {
            int xCoord = 10 * (c + 1) + c * UnitBox.WIDTH;
            int yCoord = 10 * (r + 1) + r * UnitBox.HEIGHT;
            board[r][c] = new UnitBox(val, xCoord, yCoord);
        } else {
            board[r][c].setValue(val);
        }
    }

    public UnitBox[][] getBoard() {
        return board;
    }

    public boolean won() {
        return win;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

}
