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
    private Paint paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintScore = new Paint(Paint.ANTI_ALIAS_FLAG);
    int h, w;
    List<Block> blocks;
    int prevX = 0, prevY = 0;
    boolean surfaceCreated = false;
    int cameraHeight = 0;
    boolean increaseCameraHeight = false;

    public GameSurfaceView(Context context) {
        super(context);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        blocks = new ArrayList<>();
        gameThread = GameThread.getInstance(this);
        gameThread.setRunning(true);
        gameThread.start();
        surfaceCreated = true;

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceCreated = false;
    }

    void onResume() {

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        if (gameThread != null)
            gameThread.isPause = false;

    }

    void onPause() {
        if (gameThread != null)
            gameThread.isPause = true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.WHITE);

        paintScore.setStyle(Paint.Style.FILL);
        paintScore.setColor(Color.BLACK);
        paintScore.setTextSize(40);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);

        h = canvas.getHeight();
        w = canvas.getWidth();

        paint.setColor(Color.GRAY);

        if (blocks.size() == 0) {
            Block temp = new Block(w * 0.5f, h - 50);
            temp.side = 100;
            Block.h = h;
            temp.firstBlock = true;
            temp.lastBlock = true;
            blocks.add(temp);

        }

        canvas.drawText("" + (Block.stack.size() - 1), w * 0.5f, 40, paintScore);

        for (int i = 0; i < blocks.size(); i++) {

            Block block = blocks.get(i);
            canvas.drawRoundRect(block.getX() - (block.side / 2),
                    block.getY() + cameraHeight - (block.side / 2),
                    block.getX() + (block.side / 2),
                    block.getY() + cameraHeight + (block.side / 2),
                    3,
                    3,
                    paint);
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

        increaseCameraHeight();

    }

    public void render() {

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
                    block = new Block((float) 0.75 * h, (float) 0.75 * w);
                    blocks.add(block);
                    ACTION_DOWN_PRESSED = true;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (ACTION_DOWN_PRESSED) {
                        block.setX(x);
                        block.setY(y - cameraHeight);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if (ACTION_DOWN_PRESSED) {
                        block.setVx(prevX - x);
                        block.setVy(prevY - y);
                    }
                    ACTION_DOWN_PRESSED = false;
                    break;

            }

        }

        return true;
    }

    public void increaseCameraHeight() {
        if (increaseCameraHeight) {
            cameraHeight++;
            if (cameraHeight % 100 == 0) {
                increaseCameraHeight = false;
            }
        }
    }

}
