package com.sdsmdg.bouncyball;

public class Ball {

    double x = 50, y = 50;
    double vx = 0, vy = 0;
    double ax, ay = 9.8;
    boolean touched = false;
    int radius = 50;

    public Ball(int x, int y) {
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public boolean isTouched() {
        return touched;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void checkTouch(int X, int Y) {
        if(X<=x+radius && X>=x-radius && Y<=y+radius && Y>=y-radius) setTouched(true);

        else setTouched(false);
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getAy() {
        return ay;
    }
}
