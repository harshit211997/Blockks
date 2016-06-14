package com.sdsmdg.bouncyball;

public class GameThread extends Thread{

    boolean running = false;
    GameSurfaceView parent;
    int MS_GAME_TIME = 16;
    boolean isPause = false;

    GameThread(GameSurfaceView gameSurfaceView) {
        parent = gameSurfaceView;
    }

    void setRunning(boolean r) {
        running = r;
    }

    @Override
    public void run() {
        super.run();

        long current, elapsed;
        double lag = 0.0;
        long previous = System.currentTimeMillis();

        while(running) {
            if(!isPause) {
                current = System.currentTimeMillis();
                elapsed = current - previous;
                lag += elapsed;

                while (lag >= MS_GAME_TIME) {
                    lag -= MS_GAME_TIME;

                    parent.update(MS_GAME_TIME);

                }

                previous = System.currentTimeMillis();

                parent.render(lag);
            }
        }
    }
}
