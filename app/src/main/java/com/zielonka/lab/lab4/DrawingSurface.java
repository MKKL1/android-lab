package com.zielonka.lab.lab4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

public class DrawingSurface extends SurfaceView implements SurfaceHolder.Callback, Runnable{
    private final SurfaceHolder holder;
    private Bitmap bitmap;
    private Thread drawingThread;
    private boolean threadWorking = false;
    private Matrix identityMatrix;
    Path path = new Path();;

    private Paint pathPaint;
    private Paint dotPaint;
    private Canvas canvas;
    public DrawingSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);

        pathPaint = new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setDither(true);
        pathPaint.setColor(Color.BLUE);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStrokeWidth(18);

        dotPaint = new Paint(pathPaint);
        dotPaint.setStyle(Paint.Style.FILL);
    }

    public void setPaintColor(int color) {
        pathPaint.setColor(color);
        dotPaint.setColor(color);
    }

    public void resumeDrawing() {
        drawingThread = new Thread(this);
        threadWorking = true;
        drawingThread.start();
    }
    public void pauseDrawing() {
        threadWorking = false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        Log.i(DrawingSurface.class.getSimpleName(), "click");
        synchronized (DrawingSurface.this) {
            float X = event.getX();
            float Y = event.getY();
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    path.reset();
                    path.moveTo(X, Y);
                    canvas.drawCircle(X, Y, 25, dotPaint);
                    break;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(X, Y);
                    break;
                case MotionEvent.ACTION_UP:
                    canvas.drawCircle(X, Y, 25, dotPaint);
                    canvas.drawPath(path, pathPaint);
                    path.reset();
                    break;
            }
        }
        return true;
    }
    public boolean performClick()
    {
        return super.performClick();
    }

    @Override
    public void run() {
        Log.i(DrawingSurface.class.getSimpleName(), "run");

        Canvas canvas1 = null;
        while (threadWorking) {

            try {
                synchronized (holder) {
                    if (!holder.getSurface().isValid())
                        continue;
                    canvas1 = holder.lockCanvas(null);
                    synchronized (DrawingSurface.this) {
                        if (threadWorking) {
                            canvas1.drawBitmap(bitmap, identityMatrix, null);
                            if(!path.isEmpty())
                                canvas1.drawPath(path, pathPaint);
                        }
                    }
                }
            } finally {
                if (canvas1 != null) {
                    //TODO crashes when rotate
                    holder.unlockCanvasAndPost(canvas1);
                }
            }
            try {
                Thread.sleep(1000 / 50); // 25
            } catch (InterruptedException ignored) { }
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Log.i(DrawingSurface.class.getSimpleName(), "surface created");
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawARGB(255, 255, 255, 255);
        identityMatrix = new Matrix();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (DrawingSurface.this) {
            threadWorking = false;
        }
    }

    public void clearCanvas() {
        canvas.drawARGB(255, 255, 255, 255);
    }

    public boolean saveCanvas(String filename) {
        String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
//        robimy folder LAB5 na rysunki
        File dir = new File(imagesDir + File.separator + "LAB5");
        if (!dir.exists()){
            if (!dir.mkdirs()) {
                Log.e("ERROR", "Problem przy tworzeniu katalogu " + dir);
                return false;
            }
        }
//        dodanie folderu do sciezki zapisu pliku
        imagesDir+=File.separator+"LAB5";

//        co odpalenie apki bez zmiany parametru filename w Lab5Activity będą się nadpisywac, chyba spoko mniej kasowania śmeici z telefonu
        filename +=  "_" + PaintingContent.getPaintingItems().size() + ".jpg";
//        filename +=  "rysunek_" + UUID.randomUUID().toString() + ".jpg";
        PaintingContent.PaintingItem paintingItem = new PaintingContent.PaintingItem(filename, imagesDir + "/" + filename);

        File file = new File(imagesDir, filename);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            PaintingContent.addItem(paintingItem);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
