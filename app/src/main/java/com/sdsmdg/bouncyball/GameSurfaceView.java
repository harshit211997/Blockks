package com.sdsmdg.bouncyball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    SurfaceHolder surfaceHolder;
    GameThread gameThread = null;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Ball ball;
    int h;

    public GameSurfaceView(Context context) {
        super(context);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        ball = new Ball(50, 50);
        gameThread = new GameThread(this);
        gameThread.setRunning(true);
        gameThread.start();
        Log.i("harshit", "gamethread running");


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
        canvas.drawCircle((int)ball.getX(), (int)ball.getY(), ball.radius, paint);

    }

    public void update(double time) {

        if (!ball.isTouched()) {
            if (ball.getY() <= h) {
                ball.setVy(ball.getVy() + ball.getAy() * time * 0.01);
                ball.setY((int) (ball.getY() + ball.getVy() * time * 0.01));
            } else {
                ball.setVy(-0.3 * ball.getVy());
                ball.setY((int) (h + ball.getVy() * time * 0.01));
            }
            Log.i("harshit", "y="+ball.getY() + ", vy=" + ball.getVy());
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

            case MotionEvent.ACTION_DOWN:
                ball.checkTouch(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                if (ball.isTouched()) {
                    ball.setX(x);
                    ball.setY(y);
                    ball.setVy(0);
                }
                break;
            case MotionEvent.ACTION_UP:
                ball.setTouched(false);
        }

        return true;
    }
}
