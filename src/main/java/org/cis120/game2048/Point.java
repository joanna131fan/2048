package org.cis120.game2048;

public class Point {
    private int r, c;

    public Point(int row, int col) {
        this.r = row;
        this.c = col;
    }

    public int row() {
        return this.r;
    }

    public int col() {
        return this.c;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setC(int c) {
        this.c = c;
    }
}
