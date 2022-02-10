package org.cis120;

import org.cis120.game2048.Game2048;
import org.cis120.game2048.Instructions;

import javax.swing.*;

public class Game {
    /**
     * Main method run to start and run the game. Initializes the runnable game
     * class of your choosing and runs it. IMPORTANT: Do NOT delete! You MUST
     * include a main method in your final submission.
     */
    public static void main(String[] args) {
        JPanel container = new JPanel();
        JPanel buttonContainer = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));
        Game2048 game = new Game2048();

        JFrame window = new JFrame("2048");
        JButton instructionsButton = new JButton();
        JButton resetButton = new JButton();
        instructionsButton.setText("Instructions");
        resetButton.setText("Reset");
        instructionsButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JOptionPane.showMessageDialog(window, Instructions.getMessage());
                game.requestFocus();
            }
        });

        resetButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                game.restart();
                game.requestFocus();
            }
        });

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("2048");
        window.setResizable(false);
        buttonContainer.add(instructionsButton);
        buttonContainer.add(resetButton);
        container.add(buttonContainer);
        container.add(game);
        window.add(container);
        window.pack();
        game.requestFocus();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        game.start(); // Set the game you want to run
                      // here
        // SwingUtilities.invokeLater(game);
    }
}
