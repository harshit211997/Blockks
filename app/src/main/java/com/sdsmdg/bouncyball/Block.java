package com.sdsmdg.bouncyball;

import android.util.Log;

public class Block {

    private float x, y;
    private float vx = 0, vy = 0;
    private float ax, ay = 9.8f;
    int side = 20;
    int h;
    String TAG = "harshit";
    boolean grow = false;
    boolean shrink = false;
    boolean firstBlock = false;
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
            vx = 0;
            y = h - side / 2 + vy * time * 0.01f;
            if(firstBlock)
                grow = true;
            else
                shrink = true;
        }
        grow();
        shrink();
    }

    public void checkCollission(Block block) {

        if (x - block.x >= 0 && x - block.x < (side + block.side)/2 && y - block.y >= 0 && y - block.y < (side + block.side)/2) {
            if (x - block.x > y - block.y) {
                block.x = x - (side + block.side)/2;
                block.vx = -0.3f * block.vx;
                block.shrink = true;
            } else {
                block.y = y - (side + block.side)/2;
                block.vy = vy - 0.3f * block.vy;
                block.vx = 0;
                block.grow = true;
            }
            Log.i(TAG, "top left");
        }

        else if (x - block.x >= 0 && x - block.x < (side + block.side)/2 && block.y - y >= 0 && block.y - y < (side + block.side)/2) {
            if (x - block.x > block.y - y) {
                block.x = x - (side + block.side)/2;
                block.vx = -0.3f * block.vx;
                block.shrink = true;
            } else {
                block.y = y + (side + block.side)/2;
                block.vy = vy - 0.3f * block.vy;
                block.shrink = true;
            }
            Log.i(TAG, "bottom left");
        }

        else if (block.x - x >= 0 && block.x - x < (side + block.side)/2 && block.y - y >= 0 && block.y - y < (side + block.side)/2) {
            if (block.x - x > block.y - y) {
                block.x = x + (side + block.side)/2;
                block.vx = -0.3f * block.vx;
                block.shrink = true;
            } else {
                block.y = y + (side + block.side)/2;
                block.vy = vy - 0.3f * block.vy;
                block.shrink = true;
            }
            Log.i(TAG, "bottom right");
        }

        else if (block.x - x >= 0 && block.x - x < (side + block.side)/2 && y - block.y >= 0 && y - block.y < (side + block.side)/2) {
            if (block.x - x > y - block.y) {
                block.x = x + (side + block.side)/2;
                block.vx = -0.3f * block.vx;
                block.shrink = true;
            } else {
                block.y = y - (side + block.side)/2;
                block.vy = vy - 0.3f * block.vy;
                block.vx = 0;
                block.grow = true;
            }
            Log.i(TAG, "top right");
        }
    }

    public void grow(){
        if(grow && shrink==false){
            if(side < 100) {
                side ++;
            }
            if(y == 100) {
                grow = false;
            }
        }
    }

    public void shrink(){
        if(shrink){
            if(side>0){
                side--;
            }
        }
    }

}