package com.sdsmdg.bouncyball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    SurfaceHolder surfaceHolder;
    GameThread gameThread = null;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    int h;
    List<Block> blocks;

    public GameSurfaceView(Context context) {
        super(context);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        blocks = new ArrayList<Block>();
        gameThread = new GameThread(this);
        gameThread.setRunning(true);
        gameThread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    void onResume() {

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

    }

    void onPause() {

        boolean retry = true;
        gameThread.setRunning(false);

        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        paint.setStyle(Paint.Style.FILL);

        h = canvas.getHeight();

        paint.setColor(Color.WHITE);

        for(int i = 0; i<blocks.size(); i++) {

            Block block = blocks.get(i);

            canvas.drawRect(block.getX() - (block.side / 2),
                    block.getY() - (block.side / 2),
                    block.getX() + (block.side / 2),
                    block.getY() + (block.side / 2),
                    paint);

        }

    }

    public void update(int time) {

        for(int i=0; i<blocks.size(); i++) {

            blocks.get(i).update(time);

        }

    }

    public void render() {
        //The function run in background thread, not ui thread.

        Canvas canvas = null;

        try {
            canvas = surfaceHolder.lockCanvas();

            synchronized (surfaceHolder) {
                draw(canvas);
            }
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getActionMasked();

        switch (action) {

            case MotionEvent.ACTION_UP:

                blocks.add(new Block(x, y, h));
                break;

        }

        return true;
    }
}
