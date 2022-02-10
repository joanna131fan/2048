package org.cis120.game2048;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Game2048 extends JPanel implements KeyListener, Runnable {
    private static Font mainFont = new Font("Arial", Font.PLAIN, 28);
    private GameBoard board;

    private JFrame frame;
    private InputKey key;
    // CHANGE VALUES
    public static final int WIDTH = 500;
    public static final int HEIGHT = 700;
    private Thread game;

    private BufferedImage window = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    private boolean running;

    private long startTime;
    private long timeElapsed;

    public Game2048() {
        setFocusable(true);
        // start window
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        board = new GameBoard(
                (WIDTH - GameBoard.BOARD_WIDTH) / 2, (HEIGHT - GameBoard.BOARD_HEIGHT - 10)
        );
    }

    public void restart() {
        board.reset();
    }

    private void update() {
        // if (InputKey.typed(KeyEvent.VK_RIGHT)) {
        // System.out.println("hit right");
        // }
        board.update();
        InputKey.update();
    }

    public static Font getMainFont() {
        return mainFont;
    }

    private void renderWindow() {
        Graphics2D g = (Graphics2D) window.getGraphics();
        g.setColor(Color.white);
        // whole window background
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // board
        board.render(g);
        g.dispose();

        // graphics of JPanel
        Graphics2D gBoard = (Graphics2D) getGraphics();
        gBoard.drawImage(window, 0, 0, null);
        gBoard.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        InputKey.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        InputKey.keyReleased(e);
    }

    @Override
    public void run() {
        int fps = 0, updates = 0;
        long timer = System.currentTimeMillis();
        double nsPerUpdate = 1000000000.0 / 60.0;

        // last update time in nanoseconds
        double then = System.nanoTime();
        double queue = 0;

        while (running) {

            boolean render = false;

            double now = System.nanoTime();
            queue += (now - then) / nsPerUpdate;
            then = now;

            // Update queue
            while (queue > 0) {
                queue--;
                updates++;
                update();

                render = true;
            }

            // Render
            if (render) {
                fps++;
                renderWindow();
                continue;
            }
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // FPS timer
            if (System.currentTimeMillis() - timer > 1000) {
                // System.out.printf("%d fps %d updates", fps, updates);
                // System.out.println();
                fps = 0;
                updates = 0;
                timer += 1000;
            }
        }
    }

    public synchronized void start() {
        running = true;
        game = new Thread(this, "loop2048");
        game.start();
    }

    public synchronized void stop() {
        running = false;
        System.exit(0);
    }
}
