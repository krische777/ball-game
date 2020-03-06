package com.ch.ballgame.model;

import java.io.Serializable;

public class TwoDObject implements Serializable {
    private int size;
    private int x;
    private int y;

    public TwoDObject(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }
}
