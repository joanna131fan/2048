package org.cis120.game2048;

import java.awt.event.KeyEvent;

public class InputKey {

    private static boolean[] key = new boolean[200];
    private static boolean[] prevKey = new boolean[200];

    public static boolean[] getKey() {
        return key;
    }

    public static boolean[] getPrevKey() {
        return prevKey;
    }

    private InputKey() {
    }

    public void keyTyped() {
    }

    public static void update() {
        for (int i = 0; i < 5; i++) {
            switch (i) {
                // left key
                case 0:
                    prevKey[KeyEvent.VK_LEFT] = key[KeyEvent.VK_LEFT];
                    break;
                // right key
                case 1:
                    prevKey[KeyEvent.VK_RIGHT] = key[KeyEvent.VK_RIGHT];
                    break;
                // down key
                case 2:
                    prevKey[KeyEvent.VK_DOWN] = key[KeyEvent.VK_DOWN];
                    break;
                // up key
                case 3:
                    prevKey[KeyEvent.VK_UP] = key[KeyEvent.VK_UP];
                    break;
                // undo
                case 4:
                    prevKey[KeyEvent.VK_Z] = key[KeyEvent.VK_Z];
                    break;
                default:
                    break;
            }
        }
    }

    public static boolean typed(int k) {
        return (prevKey[k] && !key[k]);
    }

    public static void keyPressed(KeyEvent e) {
        key[e.getKeyCode()] = true;
    }

    public static void keyReleased(KeyEvent e) {
        key[e.getKeyCode()] = false;
    }
}
