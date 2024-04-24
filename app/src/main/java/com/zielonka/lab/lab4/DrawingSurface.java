package com.zielonka.lab.lab4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawingSurface extends SurfaceView implements SurfaceHolder.Callback {
    private final SurfaceHolder holder;
//    private Thread drawingThread;
//    private boolean threadWorking = false;

    private Paint paint;
    private Canvas canvas;
    public DrawingSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);


    }

//    public void resumeDrawing() {
//        drawingThread = new Thread(this);
//        threadWorking = true;
//        drawingThread.start();
//    }
//    public void pauseDrawing() {
//        threadWorking = false;
//    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        synchronized (DrawingSurface.this) {
            canvas.drawCircle(event.getX(), event.getY(), 5, paint);
        }
        return true;
    }
    public boolean performClick()
    {
        return super.performClick();
    }

//    @Override
//    public void run() {
//        Log.i(DrawingSurface.class.getSimpleName(), "run");
//
//
//        while (threadWorking) {
//
//            try {
//                synchronized (holder) {
//                    if (!holder.getSurface().isValid())
//                        continue;
//                    canvas = holder.lockCanvas(null);
//                    synchronized (DrawingSurface.this) {
//                        if (threadWorking) {
//
//                        }
//                    }
//                }
//            } finally {
//                if (canvas != null) {
//                    holder.unlockCanvasAndPost(canvas);
//                }
//            }
//            try {
//                Thread.sleep(1000 / 25); // 25
//            } catch (InterruptedException ignored) { }
//        }
//    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(DrawingSurface.class.getSimpleName(), "surface created");
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawARGB(255,255,255,255);

        setOnTouchListener((view, event) -> {

            return true;
        });
//        resumeDrawing();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//        threadWorking = false;
    }
}
