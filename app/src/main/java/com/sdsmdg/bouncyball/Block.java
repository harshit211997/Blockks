package com.sdsmdg.bouncyball;

import android.util.Log;

public class Block {

    private float x, y;
    private float vx = 0, vy = 0;
    private float ax, ay = 9.8f;
    int side = 100;
    int h;
    String TAG = "harshit";

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
            x = x + (vx * 0.01f * time);
        } else {
            vy = -3 * vy / 10;
            y = h - side / 2 + vy * time * 0.01f;
        }

    }

    public void checkCollission(Block block) {

        if (x - block.x >= 0 && x - block.x <= side && y - block.y >= 0 && y - block.y <= side) {
            if (x - block.x > y - block.y) {
                block.x = x - side;
                block.vx = -0.3f * block.vx;
            } else {
                block.y = y - side;
                block.vy = -0.3f * block.vy;
                block.vx = 0;
            }
            Log.i(TAG, "top left");
        }

        else if (x - block.x >= 0 && x - block.x <= side && block.y - y >= 0 && block.y - y <= side) {
            if (x - block.x > block.y - y) {
                block.x = x - side;
                block.vx = -0.3f * block.vx;
            } else {
                block.y = y + side;
                block.vy = -0.3f * block.vy;
            }
            Log.i(TAG, "bottom left");
        }

        else if (block.x - x >= 0 && block.x - x <= side && block.y - y >= 0 && block.y - y <= side) {
            if (block.x - x > y - block.y) {
                block.x = x + side;
                block.vx = -0.3f * block.vx;
            } else {
                block.y = y + side;
                block.vy = -0.3f * block.vy;
            }
            Log.i(TAG, "bottom right");
        }

        else if (block.x - x >= 0 && block.x - x <= side && y - block.y >= 0 && y - block.y <= side) {
            if (block.x - x > y - block.y) {
                Log.i("harshit", (block.x - x) + " " + (y - block.y));
                block.x = x + side;
                block.vx = -0.3f * block.vx;
            } else {
                block.y = y - side;
                block.vy = -0.3f * block.vy;
                block.vx = 0;
            }
            Log.i(TAG, "top right");
        }
    }
}