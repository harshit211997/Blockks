package com.sdsmdg.bouncyball;

public class Block {

    private float x, y ;
    private float vx = 0, vy = 0;
    private float ax, ay = 9.8f;
    int side = 100;

    public Block(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getAy() {
        return ay;
    }

    public void update(int time, int h) {

        if (y <= h - side / 2) {
            vy = vy + (ay * 0.01f * time);
            y = y + (vy * 0.01f * time);
        } else {
            vy = -3 * vy / 10;
            y = h - side / 2 + vy * time * 0.01f;
        }

    }

}
