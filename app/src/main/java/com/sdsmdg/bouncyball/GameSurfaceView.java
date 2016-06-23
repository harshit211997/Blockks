package com.sdsmdg.bouncyball;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    String TAG = "harshit";
    Context context;

    SurfaceHolder surfaceHolder;
    GameThread gameThread = null;
    private Paint paintWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintBlue = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintScore = new Paint(Paint.ANTI_ALIAS_FLAG);
    int h, w;
    List<Block> blocks;
    int prevX = 0, prevY = 0;
    boolean surfaceCreated = false;
    int cameraHeight = 0;
    boolean increaseCameraHeight = false;
    double lag = 0.0;
    boolean drawLine = false;
    int x1, x2, y1, y2;
    Life life = new Life();
    Score score = new Score();

    public GameSurfaceView(Context context) {
        super(context);

        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged: called");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        blocks = new ArrayList<>();
        gameThread = new GameThread(this);
        gameThread.setRunning(true);
        gameThread.start();
        surfaceCreated = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceCreated = false;
        gameThread.setRunning(false);
        boolean retry = true;
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

        reset();

        Log.i(TAG, "surfaceDestroyed: called");
    }

    void onResume() {

        if (gameThread != null) {
            gameThread.isPause = false;
        }

        Log.i(TAG, "onResume: called");

    }

    void onPause() {
        if (gameThread != null)
            gameThread.isPause = true;

        Log.i(TAG, "onPause: called");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        paintBlue.setStyle(Paint.Style.FILL);
        paintBlue.setColor(Color.CYAN);

        paintWhite.setStyle(Paint.Style.FILL);
        paintWhite.setColor(Color.WHITE);

        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setColor(Color.BLACK);

        paintScore.setStyle(Paint.Style.FILL);
        paintScore.setColor(Color.BLACK);
        paintScore.setTextSize(40);
        paintScore.setTextAlign(Paint.Align.CENTER);

        h = canvas.getHeight();
        w = canvas.getWidth();

        //Adding the first block
        if (blocks.size() == 0) {
            Block temp = new Block(w * 0.5f, h - 50, life, score);
            temp.side = 100;
            Block.h = h;
            temp.firstBlock = true;
            temp.lastBlock = true;
            blocks.add(temp);
        }

        canvas.drawRect(0, 0, w, h * 0.5f, paintWhite);
        canvas.drawRect(0, h * 0.5f, w, h, paintBlue);

        canvas.drawRoundRect(w * 0.5f - 25, 50 - 25, w * 0.5f + 25, 50 + 25, 3, 3, paintBorder);
        canvas.drawText("" + score.getCount(), w * 0.5f, 65, paintScore);//Score

        paintScore.setTextAlign(Paint.Align.LEFT);
        canvas.drawRoundRect(10, 10, 30, 30, 3, 3, paintBorder);
        paintScore.setTextSize(30);
        canvas.drawText("x " + life.getCount(), 40, 30, paintScore);//life

        for (int i = 0; i < blocks.size(); i++) {

            Block block = blocks.get(i);
            canvas.drawRoundRect(block.getX() + block.getVx() * 0.01f * (float) lag - (block.side / 2),
                    block.getY() + block.getVy() * 0.01f * (float) lag + cameraHeight - (block.side / 2),
                    block.getX() + block.getVx() * 0.01f * (float) lag + (block.side / 2),
                    block.getY() + block.getVy() * 0.01f * (float) lag + cameraHeight + (block.side / 2),
                    3,
                    3,
                    paintWhite);

            canvas.drawRoundRect(block.getX() + block.getVx() * 0.01f * (float) lag - (block.side / 2),
                    block.getY() + block.getVy() * 0.01f * (float) lag + cameraHeight - (block.side / 2),
                    block.getX() + block.getVx() * 0.01f * (float) lag + (block.side / 2),
                    block.getY() + block.getVy() * 0.01f * (float) lag + cameraHeight + (block.side / 2),
                    3,
                    3,
                    paintBorder);

        }

        if (drawLine) {
            canvas.drawLine(x1, y1, x2, y2, paintWhite);
        }

    }

    public void update(int time) {

        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).update(time);
            for (int j = 0; j < i; j++) {
                blocks.get(j).checkCollission(blocks.get(i));
            }
        }

        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i).side == 0) {
                blocks.remove(i);
            }
        }

        if ((Block.stack.size() >= 1) && (Block.stack.get(Block.stack.size() - 1).getY() + cameraHeight < 150)) {
            increaseCameraHeight = true;
        }

        if (Block.stack.size() >= 9) {
            Block.stack.remove(0);
            blocks.remove(0);
            Block.h -= Block.increasedSize;
        }

        if (life.isGameOver()) {
            gameOver();
        }

        increaseCameraHeight();

    }

    public void render(double time) {

        lag = time;
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

    Block block;
    boolean ACTION_DOWN_PRESSED = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getActionMasked();

        if (y >= h * 0.5 && y <= h) {

            switch (action) {

                case MotionEvent.ACTION_DOWN:
                    prevX = x;
                    prevY = y;
                    //so that the block does not shrink on birth :)
                    block = new Block(x, y - cameraHeight, life, score);
                    blocks.add(block);
                    ACTION_DOWN_PRESSED = true;
                    drawLine = true;
                    x1 = x;
                    y1 = y;
                    x2 = x;
                    y2 = y;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (ACTION_DOWN_PRESSED) {
                        block.setX(x);
                        block.setY(y - cameraHeight);
                        x2 = x;
                        y2 = y;
                        block.setVx(0);
                        block.setVy(0);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if (ACTION_DOWN_PRESSED) {
                        block.setVx(prevX - x);
                        block.setVy(prevY - y);
                    }
                    ACTION_DOWN_PRESSED = false;
                    drawLine = false;
                    break;

            }

        }

        return true;
    }

    public void increaseCameraHeight() {
        if (increaseCameraHeight) {
            cameraHeight += 2;
            if (cameraHeight % 100 == 0 || (cameraHeight - 1) % 100 == 0) {
                increaseCameraHeight = false;
            }
        }
    }

    public void reset() {
        blocks = new ArrayList<>();
        life.reset();
        score.reset();
        Block.stack = new ArrayList<>();
        cameraHeight = 0;
        lag = 0.0;
        increaseCameraHeight = false;
    }

    public void gameOver() {
        Intent i = new Intent(context, GameOverActivity.class);
        i.putExtra("score", score.getCount());
        context.startActivity(i);
        ((Activity) context).finish();
    }

}
