package org.cis120.game2048;

public class Instructions {
    private static String message = "This is a game of 2048. " +
            "The objective is to use to arrow keys to move the " +
            "tiles on the screen. \n"
            +
            "Press the left button to move all the tiles to the leftmost " +
            "box available and similarly for all 4 " +
            "directions. \n \n"
            + "How To Play: You need to move the tiles with the arrow keys. " +
            "Every time you move, two new tiles pop up " +
            "in a random manner in any open box. \n"
            +
            "'Score' is the sum of tiles that you've successfully " +
            "combined on screen. 'High Score' keeps " +
            "track of the " +
            "highest score. \n"
            +
            "When two tiles with the same number on them collide with " +
            "one another as you move them, "
            +
            "they will merge into one tile with the sum of the " +
            "numbers written on them initially. \n"
            +
            "You win once you get to the 2048 tile. The message " +
            "'You Win!' will show up. \n\n" +
            "To restart the game, click the 'reset' button. \n\n\n" +
            "Game Made by Joanna Fan";

    public static String getMessage() {
        return message;
    }
}
