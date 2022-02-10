package org.cis120.game2048;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class UnitBox {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;
    // rounded edges
    public static final int ARC_WIDTH = 8;
    public static final int ARC_HEIGHT = 8;
    public static final int SLIDE_SPEED = 30;

    // encapsulated
    private int val;
    private Point currPoint;
    private Point nextPoint;
    private Color background;
    private Color text;
    private Font font;
    private BufferedImage box;

    private boolean canAdd = true;

    public UnitBox(int val, int x, int y) {
        this.val = val;
        this.currPoint = new Point(x, y);
        this.nextPoint = new Point(x, y);
        box = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        drawImage();
    }

    public int getValue() {
        return val;
    }

    public void setValue(int val) {
        this.val = val;
    }

    // centering helper functions
    public static int textWidth(String text, Graphics2D g, Font font) {
        g.setFont(font);
        Rectangle2D bounds = g.getFontMetrics().getStringBounds(text, g);
        return (int) bounds.getWidth();
    }

    public static int textHeight(String text, Graphics2D g, Font font) {
        g.setFont(font);
        TextLayout t = new TextLayout(text, font, g.getFontRenderContext());
        Rectangle2D bounds = t.getBounds();
        return (int) bounds.getHeight();
    }

    private void drawImage() {
        Graphics2D g = (Graphics2D) box.getGraphics();
        // for each color
        if (val == 2) {
            background = new Color(0xD6D7E7);
            text = new Color(0x000000);
        } else if (val == 4) {
            background = new Color(0xC9E9FC);
            text = new Color(0x000000);
        } else if (val == 8) {
            background = new Color(0x669DE5);
            text = new Color(0x000000);
        } else if (val == 16) {
            background = new Color(0x6E66E5);
            text = new Color(0xBFBCBE);
        } else if (val == 32) {
            background = new Color(0xAA89F9);
            text = new Color(0x000000);
        } else if (val == 64) {
            background = new Color(0xD18AFA);
            text = new Color(0x000000);
        } else if (val == 128) {
            background = new Color(0xE479E8);
            text = new Color(0x000000);
        } else if (val == 256) {
            background = new Color(0xF974D1);
            text = new Color(0x000000);
        } else if (val == 512) {
            background = new Color(0xFA76A7);
            text = new Color(0x000000);
        } else if (val == 1024) {
            background = new Color(0xFA7676);
            text = new Color(0x000000);
        } else if (val == 2048) {
            background = new Color(0xFA9E76);
            text = new Color(0x000000);
        } else if (val == 4096) {
            background = new Color(0xFACA76);
            text = new Color(0x000000);
        }
        g.setColor(new Color(0xFFFFFF));
        g.fillRoundRect(0, 0, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);

        g.setColor(background);
        g.fillRoundRect(0, 0, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);

        g.setColor(text);
        if (val < 128) {
            font = Game2048.getMainFont().deriveFont(32f);
        } else {
            font = Game2048.getMainFont();
        }
        g.setFont(font);
        int drawX = (WIDTH - textWidth("" + val, g, font)) / 2;
        int drawY = (HEIGHT + textHeight("" + val, g, font)) / 2;
        g.drawString("" + val, drawX, drawY);
        g.dispose();
    }

    public void update() {

    }

    public Point getNextPoint() {
        return this.nextPoint;
    }

    public void setNextPoint(int x, int y) {
        this.nextPoint = new Point(x, y);
    }

    public void render(Graphics2D g) {
        g.drawImage(box, currPoint.row(), currPoint.col(), null);
    }

    public boolean canAdd() {
        return canAdd;
    }

    public void setCanAdd(boolean canAdd) {
        this.canAdd = canAdd;
    }

    public int getX() {
        return this.currPoint.row();
    }

    public void setX(int x) {
        this.currPoint.setR(x);
    }

    public void setY(int y) {
        this.currPoint.setC(y);
    }

    public int getY() {
        return this.currPoint.col();
    }
}
