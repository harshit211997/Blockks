package com.sdsmdg.bouncyball;

import java.util.ArrayList;
import java.util.List;

public class Block extends Subject{

    private float x, y;
    private float vx = 0, vy = 0;
    private float ax, ay = 9.8f;
    int side = 20;
    static int increasedSize = 100;
    static int h;
    String TAG = "harshit";
    boolean firstBlock = false;
    boolean lastBlock = false;
    static List<Block> stack = new ArrayList<>();
    boolean grow = false, shrink = false;

    public Block(float x, float y, Life life, Score score) {
        this.x = x;
        this.y = y;
        addObserver(life);
        addObserver(score);
    }

    public void moveTo(float x, float y) {
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

    public int getH() {
        return h;
    }

    public void setH(int h) {
        Block.h = h;
    }

    public void update(int time) {


        if (y <= h - side / 2) {
            vy = vy + (ay * 0.01f * time);
            y = y + (vy * 0.01f * time);
            x = x + (vx * 0.01f * time);
        } else {
            vy = -3 * vy / 10;
            vx = 0;
            y = h - side / 2;
            if (firstBlock && !stack.contains(this)) {
                stack.add(this);
            } else if (!stack.contains(this) && !shrink) {
                shrink = true;
                notifyObservers(Constants.DECREASE_LIFE);
            }
        }

        grow();
        shrink();

    }

    public void checkCollission(Block block) {

        if (x + (vx - block.vx) * 0.01 * 16 - block.x >= 0 && x + (vx - block.vx) * 0.01 * 16 - block.x < (side + block.side) / 2 && y + (vy - block.vy) * 0.01 * 16 - block.y >= 0 && y + (vy - block.vy) * 0.01 * 16 - block.y < (side + block.side) / 2) {//top left
            if (x + (vx - block.vx) * 0.01 * 16 - block.x >= y + (vx - block.vx) * 0.01 * 16 - block.y) {
                block.x = x - (side + block.side) / 2;
                block.vx = -0.3f * block.vx;
                if (!stack.contains(block) && !block.shrink) {
                    block.shrink = true;
                    notifyObservers(Constants.DECREASE_LIFE);
                }
            } else {
                block.y = y - (side + block.side) / 2;
                block.vy = vy - 0.3f * block.vy;
                block.vx = 0;
                if (lastBlock && !block.shrink) {//so that the block which was shrinking can't be added to stack
                    block.lastBlock = true;
                    lastBlock = false;
                    block.grow = true;
                    notifyObservers(Constants.INCREASE_SCORE);
                    if (!stack.contains(block))
                        stack.add(block);
                } else if (!stack.contains(block) && !block.shrink) {
                    block.shrink = true;
                    notifyObservers(Constants.DECREASE_LIFE);
                }
            }
        } else if (x + (vx - block.vx) * 0.01 * 16 - block.x >= 0 && x + (vx - block.vx) * 0.01 * 16 - block.x < (side + block.side) / 2 && block.y + (block.vy - vy) * 0.01 * 16 - y >= 0 && block.y + (block.vy - vy) * 0.01 * 16 - y < (side + block.side) / 2) {//bottom left
            if (x + (vx - block.vx) * 0.01 * 16 - block.x >= block.y + (block.vy - vy) * 0.01 * 16 - y) {
                block.x = x - (side + block.side) / 2;
                block.vx = -0.3f * block.vx;
                if (!stack.contains(block) && !block.shrink) {
                    block.shrink = true;
                    notifyObservers(Constants.DECREASE_LIFE);
                }
            } else {
                block.y = y + (side + block.side) / 2;
                block.vy = vy - 0.3f * block.vy;
                if (!stack.contains(block) && !block.shrink) {
                    block.shrink = true;
                    notifyObservers(Constants.DECREASE_LIFE);
                }
            }
        } else if (block.x + (vx - block.vx) * 0.01 * 16 - x >= 0 && block.x + (vx - block.vx) * 0.01 * 16 - x < (side + block.side) / 2 && block.y + (block.vy - vy) * 0.01 * 16 - y >= 0 && block.y + (block.vy - vy) * 0.01 * 16 - y < (side + block.side) / 2) {//bottom right
            if (block.x + (vx - block.vx) * 0.01 * 16 - x >= block.y + (block.vy - vy) * 0.01 * 16 - y) {
                block.x = x + (side + block.side) / 2;
                block.vx = -0.3f * block.vx;
                if (!stack.contains(block) && !block.shrink) {
                    block.shrink = true;
                    notifyObservers(Constants.DECREASE_LIFE);
                }
            } else {
                block.y = y + (side + block.side) / 2;
                block.vy = vy - 0.3f * block.vy;
                if (!stack.contains(block) && !block.shrink) {
                    block.shrink = true;
                    notifyObservers(Constants.DECREASE_LIFE);
                }
            }
        } else if (block.x + (vx - block.vx) * 0.01 * 16 - x >= 0 && block.x + (vx - block.vx) * 0.01 * 16 - x < (side + block.side) / 2 && y + (vy - block.vy) * 0.01 * 16 - block.y >= 0 && y + (vy - block.vy) * 0.01 * 16 - block.y < (side + block.side) / 2) {//top right
            if (block.x + (vx - block.vx) * 0.01 * 16 - x >= y + (vx - block.vx) * 0.01 * 16 - block.y) {
                block.x = x + (side + block.side) / 2;
                block.vx = -0.3f * block.vx;
                if (!stack.contains(block) && !block.shrink) {
                    block.shrink = true;
                    notifyObservers(Constants.DECREASE_LIFE);
                }
            } else {
                block.y = y - (side + block.side) / 2;
                block.vy = vy - 0.3f * block.vy;
                block.vx = 0;
                if (lastBlock && !block.shrink) {//so that the block which was shrinking can't be added to stack
                    block.lastBlock = true;
                    lastBlock = false;
                    block.grow = true;
                    notifyObservers(Constants.INCREASE_SCORE);
                    if (!stack.contains(block))
                        stack.add(block);
                } else if (!stack.contains(block) && !block.shrink) {
                    block.shrink = true;
                    notifyObservers(Constants.DECREASE_LIFE);
                }
            }
        }

    }

    public void grow() {
        if (grow) {
            side += 2;
        }
        if (side >= increasedSize) {
            grow = false;
        }
    }

    public void shrink() {
        if (shrink) {
            side--;
        }
        if (side == 0) {
            shrink = false;
        }
    }

}

