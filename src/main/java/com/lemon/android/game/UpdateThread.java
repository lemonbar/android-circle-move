package com.lemon.android.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by limeng0402 on 14-10-14.
 */
public class UpdateThread extends Thread {
    private long time;
    private final int fps = 20;
    private boolean toRun = false;
    private MovementView movementView;
    private SurfaceHolder surfaceHolder;

    public UpdateThread(MovementView rMovementView) {
        movementView = rMovementView;
        surfaceHolder = movementView.getHolder();
    }

    public void setRunning(boolean run) {
        toRun = run;
    }

    public void run() {
        Canvas c;
        time = System.currentTimeMillis();
        while (toRun) {
            long cTime = System.currentTimeMillis();
            if ((cTime - time) >= (1000 / fps)) {//control draw time period.
                //circle move each 50ms.
                c = null;
                try {
                    c = surfaceHolder.lockCanvas(null);
                    movementView.updatePhysics();
                    movementView.onDraw(c);
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                time = cTime;
            }
        }
    }
}
