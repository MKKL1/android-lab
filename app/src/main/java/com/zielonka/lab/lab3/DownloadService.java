package com.zielonka.lab.lab3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.zielonka.lab.R;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadService extends Service {

    private final static int NOTIFICATION_ID = 1;
    public final static String BROADCAST_ID = "service broadcast";
    private final static String NOTIFICATION_CHANNEL_ID = "notification channel id";
    private final static String NOTIFICATION_CHANNEL_NAME = "notification channel name";

    private final String logName = DownloadService.class.getName();
    private NotificationManager notificationManager;

    private Handler serviceThreadHandler;
    private Handler mainThreadHandler;


    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread(logName);
        handlerThread.start();
        serviceThreadHandler = new Handler(handlerThread.getLooper());
        mainThreadHandler = new Handler(Looper.getMainLooper());
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW);
        notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, getNotification(0));
        Log.d(logName, "onStartCommand startForeground");
        serviceThreadHandler.post(() -> {
            updateNotification(0);
            String textUrl = intent.getStringExtra("url");
            Log.d(logName, "Url: " + textUrl);

            HttpsURLConnection httpsURLConnection = null;
            DataInputStream dataInputStream = null;
            FileOutputStream fileOutputStream = null;
            int progress = 0;
            int fileSize = 0;
            try {
                Log.i(logName, "Start download");
                URL url = new URL(textUrl);
                assert textUrl != null;
                String fileName = textUrl.substring(textUrl.lastIndexOf("/"));
                File outputFile = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "Download" + fileName);
                Log.i(logName, "Save " + outputFile.getAbsolutePath());
                if(outputFile.exists()) {
                    outputFile.delete();
                }
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.connect();

                dataInputStream = new DataInputStream(httpsURLConnection.getInputStream());
                fileOutputStream = new FileOutputStream(outputFile.getPath());
                byte[] buffer = new byte[1024];
                int bytesRead;
                fileSize = httpsURLConnection.getContentLength();
                int step = 1;
                while((bytesRead = dataInputStream.read(buffer, 0, 1024)) != -1) {
                    progress += bytesRead;
                    if(progress >= (fileSize/10)*step) {
                        updateNotification(step*10);
                        Log.i(logName, "Download progress: " + step*10 + "/" + 100);
                        step++;
                        sendProgressBroadcast(progress, fileSize, "Running");

                        //Test only
                        Thread.sleep(1000);
                    }
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
                Log.i("HttpConnectionService", "Download complete");
                updateNotification(100);
                sendProgressBroadcast(progress, fileSize, "Completed");
            } catch (IOException e) {
                sendProgressBroadcast(progress, fileSize, "Error");
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (dataInputStream != null) {
                        dataInputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (httpsURLConnection != null) {
                        httpsURLConnection.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Log.d(logName, "DownloadService stop");
            mainThreadHandler.post(this::stopSelf);
        });

        return super.onStartCommand(intent, flags, startId);
    }

    private void sendProgressBroadcast(int progress, int filesize, String status){
        Intent broadcastIntent = new Intent(BROADCAST_ID);
        ProgressData info = new ProgressData(progress, filesize, status);
        broadcastIntent.putExtra("progress", info);
        broadcastIntent.setPackage(this.getPackageName());
        sendBroadcast(broadcastIntent);
        Log.d(logName, "Broadcast");
    }

    private Notification getNotification(int progress){
        Log.d(logName, "start getNotification");
        Intent intent = new Intent(getApplicationContext(), Lab3Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Pobieranie pliku")
            .setContentText("Trwa pobieranie pliku")
            .setOngoing(true)
            .setProgress(100, progress, false)
            .setPriority(Notification.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setTicker("Notification ticker")
            .setWhen(System.currentTimeMillis())
            .setChannelId(NOTIFICATION_CHANNEL_ID);

        return builder.build();
    }

    private void updateNotification(int progress){
        Log.d(logName, "updateNotification p:" + progress);
        notificationManager.notify(NOTIFICATION_ID, getNotification(progress));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
