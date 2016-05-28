package com.sdsmdg.bouncyball;

public class Block {

    private float x, y ;
    private float vx = 0, vy = 0;
    private float ax, ay = 9.8f;
    int side = 100;
    int h;

    public Block(float x, float y, int h) {
        this.x = x;
        this.y = y;
        this.h = h;
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

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void update(int time) {

        if (y <= h - side / 2) {
            vy = vy + (ay * 0.01f * time);
            y = y + (vy * 0.01f * time);
        } else {
            vy = -3 * vy / 10;
            y = h - side / 2 + vy * time * 0.01f;
        }

    }

    public void checkCollission(Block block) {
        float diffX;
        float diffY;
        if(block.x - x >= 0)
            diffX = block.x - x;
        else
            diffX = x - block.x;

        if(block.y - y >= 0)
            diffY = block.y - y;
        else
            diffY = y - block.y;

        if(diffY <= side && diffX <= side) {
            h -= side + 5;
        }
    }

}
