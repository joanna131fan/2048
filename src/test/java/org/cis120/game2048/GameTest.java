package org.cis120.game2048;

import org.junit.jupiter.api.*;

import java.io.*;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class GameTest {

    @Test
    public void checkInitialBoxesOnBoardIsTwo() {
        GameBoard board = new GameBoard(
                (Game2048.WIDTH - GameBoard.BOARD_WIDTH) / 2,
                (Game2048.HEIGHT - GameBoard.BOARD_HEIGHT) / 2
        );
        UnitBox[][] b = board.getBoard();
        int initial = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (b[i][j] == null) {
                    continue;
                }
                if (b[i][j].getValue() > 0) {
                    initial++;
                }
            }
        }
        assertEquals(2, initial);
    }

    @Test
    public void checkReset() {
        GameBoard board = new GameBoard(
                (Game2048.WIDTH - GameBoard.BOARD_WIDTH) / 2,
                (Game2048.HEIGHT - GameBoard.BOARD_HEIGHT) / 2
        );
        board.setBoardValue(1, 1, 4);
        board.setBoardValue(1, 2, 8);
        board.setBoardValue(2, 1, 16);
        board.reset();
        UnitBox[][] b = board.getBoard();
        int initial = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (b[i][j] == null) {
                    continue;
                }
                initial++;
            }
        }
        assertEquals(2, initial);
        assertFalse(board.won());
        assertFalse(board.isGameOver());
    }

    @Test
    public void checkResetScore() {
        GameBoard board = new GameBoard(
                (Game2048.WIDTH - GameBoard.BOARD_WIDTH) / 2,
                (Game2048.HEIGHT - GameBoard.BOARD_HEIGHT) / 2
        );
        board.setBoardValue(1, 1, 4);
        board.setBoardValue(1, 2, 4);
        board.setBoardValue(2, 1, 16);
        board.moveBox(0);
        assertNotEquals(0, board.getScore());
        board.reset();
        UnitBox[][] b = board.getBoard();
        assertEquals(0, board.getScore());
        int initial = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (b[i][j] == null) {
                    continue;
                }
                initial++;
            }
        }
        assertEquals(2, initial);
    }

    @Test
    public void testUpdateWinningBoard() {
        GameBoard board = new GameBoard(
                (Game2048.WIDTH - GameBoard.BOARD_WIDTH) / 2,
                (Game2048.HEIGHT - GameBoard.BOARD_HEIGHT) / 2
        );
        board.setBoardValue(1, 1, 2048);
        board.update();
        assertTrue(board.won());
    }

    @Test
    public void testMoveBoxUp() {
        GameBoard board = new GameBoard(
                (Game2048.WIDTH - GameBoard.BOARD_WIDTH) / 2,
                (Game2048.HEIGHT - GameBoard.BOARD_HEIGHT) / 2
        );
        board.setBoardValue(0, 0, 16);
        board.setBoardValue(1, 0, 16);
        board.setBoardValue(2, 0, 4);
        board.setBoardValue(0, 3, 2);

        // move up
        board.moveBox(3);
        UnitBox[][] b = board.getBoard();
        assertEquals(4, b[1][0].getValue());
        assertEquals(32, b[0][0].getValue());
    }

    @Test
    public void testMoveBoxDown() {
        GameBoard board = new GameBoard(
                (Game2048.WIDTH - GameBoard.BOARD_WIDTH) / 2,
                (Game2048.HEIGHT - GameBoard.BOARD_HEIGHT) / 2
        );
        board.setBoardValue(0, 0, 16);
        board.setBoardValue(1, 0, 16);
        board.setBoardValue(2, 2, 4);
        board.setBoardValue(3, 2, 4);
        board.setBoardValue(2, 1, 8);

        // move down
        board.moveBox(2);
        UnitBox[][] b = board.getBoard();
        assertEquals(8, b[3][2].getValue());
    }

    @Test
    public void testMoveBoxRight() {
        GameBoard board = new GameBoard(
                (Game2048.WIDTH - GameBoard.BOARD_WIDTH) / 2,
                (Game2048.HEIGHT - GameBoard.BOARD_HEIGHT) / 2
        );
        board.setBoardValue(0, 0, 16);
        board.setBoardValue(1, 0, 16);
        board.setBoardValue(2, 2, 4);
        board.setBoardValue(2, 3, 4);
        board.setBoardValue(2, 1, 8);

        // move right
        board.moveBox(1);
        UnitBox[][] b = board.getBoard();
        assertEquals(8, b[2][2].getValue());
        assertEquals(8, b[2][3].getValue());
    }

    @Test
    public void testMoveBoxLeft() {
        GameBoard board = new GameBoard(
                (Game2048.WIDTH - GameBoard.BOARD_WIDTH) / 2,
                (Game2048.HEIGHT - GameBoard.BOARD_HEIGHT) / 2
        );
        board.setBoardValue(0, 0, 16);
        board.setBoardValue(1, 0, 16);
        board.setBoardValue(2, 1, 4);
        board.setBoardValue(2, 2, 4);
        board.setBoardValue(2, 0, 8);

        // move left
        board.moveBox(0);
        UnitBox[][] b = board.getBoard();
        assertEquals(8, b[2][1].getValue());
        assertEquals(8, b[2][0].getValue());
    }

    @Test
    public void testMoveBoxLeftCombinesLeftMost() {
        GameBoard board = new GameBoard(
                (Game2048.WIDTH - GameBoard.BOARD_WIDTH) / 2,
                (Game2048.HEIGHT - GameBoard.BOARD_HEIGHT) / 2
        );
        board.setBoardValue(0, 0, 16);
        board.setBoardValue(1, 0, 16);
        board.setBoardValue(2, 1, 4);
        board.setBoardValue(2, 2, 4);
        board.setBoardValue(2, 0, 4);

        // move left
        board.moveBox(0);
        UnitBox[][] b = board.getBoard();
        assertEquals(4, b[2][1].getValue());
        assertEquals(8, b[2][0].getValue());
    }

    @Test
    public void testCombineBoxes() {
        GameBoard board = new GameBoard(
                (Game2048.WIDTH - GameBoard.BOARD_WIDTH) / 2,
                (Game2048.HEIGHT - GameBoard.BOARD_HEIGHT) / 2
        );
        board.setBoardValue(0, 0, 2);
        board.setBoardValue(0, 1, 4);
        board.setBoardValue(0, 2, 4);
        board.setBoardValue(0, 3, 2);
        // _ 4 4 2 --> // 8 _ _ _

        // move left
        board.moveBox(0);
        UnitBox[][] b = board.getBoard();
        assertEquals(8, b[0][1].getValue());
    }

    @Test
    public void testIO() throws URISyntaxException, IOException {
        String dataPath = GameBoard.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI().getPath();
        String fileName = "fileSavedData";
        GameBoard board = new GameBoard(
                (Game2048.WIDTH - GameBoard.BOARD_WIDTH) / 2,
                (Game2048.HEIGHT - GameBoard.BOARD_HEIGHT) / 2
        );
        int highScore = board.getHighScore();
        File file = new File(dataPath, fileName);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file))
        );
        assertEquals(highScore, Integer.parseInt(reader.readLine()));
    }
}
