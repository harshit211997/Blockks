package com.sdsmdg.bouncyball;

public class Life implements Observer{

    private int count = 10;

    @Override
    public void onNotify(int flag) {

        switch (flag) {
            case 0:
                count ++;
                break;
            case 1:
                count --;
                break;
        }

    }

    void reset() {
        count = 10;
    }

    int getCount() {
        return count;
    }

    boolean isGameOver() {
        if(count < 0) {
            return true;
        }
        else
            return false;
    }

}
