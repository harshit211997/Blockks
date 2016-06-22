package com.sdsmdg.bouncyball;

public class Score implements Observer{

    private int count = 0;

    @Override
    public void onNotify(int flag) {

        switch (flag) {
            case Constants.INCREASE_SCORE:
                count ++;
                break;
            case Constants.DECREASE_SCORE:
                count ++;
                break;
        }

    }

    public void reset() {
        count = 0;
    }

    public int getCount() {
        return count;
    }
}
