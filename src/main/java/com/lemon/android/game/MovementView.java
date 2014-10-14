package com.lemon.android.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by limeng0402 on 14-10-14.
 */
public class MovementView extends SurfaceView implements SurfaceHolder.Callback {
    private int xPos;
    private int yPos;
    private int xVel;
    private int yVel;
    private int width;
    private int height;
    private int circleRadius;
    private Paint circlePaint;
    UpdateThread updateThread;

    public MovementView(Context context) {
        super(context);
        getHolder().addCallback(this);
        circleRadius = 20;
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        xVel = 5;
        yVel = 5;
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(xPos, yPos, circleRadius, circlePaint);
    }

    public void updatePhysics() {
        xPos += xVel;
        yPos += yVel;
        if (yPos - circleRadius < 0 || yPos + circleRadius > height) {
            if (yPos - circleRadius < 0) {
                yPos = circleRadius;
            } else {
                yPos = height - circleRadius;
            }
            yVel *= -1;
        }
        if (xPos - circleRadius < 0 || xPos + circleRadius > width) {
            if (xPos - circleRadius < 0) {
                xPos = circleRadius;
            } else {
                xPos = width - circleRadius;
            }
            xVel *= -1;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Rect surfaceFrame = surfaceHolder.getSurfaceFrame();
        width = surfaceFrame.width();
        height = surfaceFrame.height();
        xPos = width / 2;
        yPos = circleRadius;
        updateThread = new UpdateThread(this);
        updateThread.setRunning(true);
        updateThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        updateThread.setRunning(false);
        while (retry) {
            try {
                updateThread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }
}
