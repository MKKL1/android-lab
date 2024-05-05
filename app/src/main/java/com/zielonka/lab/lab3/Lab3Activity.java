package com.zielonka.lab.lab3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.zielonka.lab.R;
import com.zielonka.lab.databinding.ActivityLab3Binding;
import com.zielonka.lab.lab1.Lab1GUIActivity;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class Lab3Activity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ActivityLab3Binding binding;
    private final BroadcastReceiver fileProgressReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ProgressData progressInfo = intent.getParcelableExtra("progress");
            Log.i("PROGRESS INFO", progressInfo.toString());

            binding.downloadedBytes.setText(String.valueOf(progressInfo.getFileSize()));

            binding.progressBar.setMax(progressInfo.getFileSize());
            binding.progressBar.setProgress(progressInfo.getProgressBytes(), true);

            if(progressInfo.getStatus().equals("Running")){
                binding.downloadFile.setText("Downloading...");
            } else {
                binding.downloadFile.setText("Download file");
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        ContextCompat.registerReceiver(this,
                fileProgressReceiver,
                new IntentFilter(DownloadService.BROADCAST_ID),
                ContextCompat.RECEIVER_NOT_EXPORTED);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(fileProgressReceiver);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        binding = ActivityLab3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.getInfo.setOnClickListener(v -> {
            String urlText = binding.url.getText().toString();
            CompletableFuture.supplyAsync(() -> {
                HttpsURLConnection httpsURLConnection = null;
                try {
                    URL url = new URL(urlText);
                    httpsURLConnection = (HttpsURLConnection) url.openConnection();
                    httpsURLConnection.setRequestMethod("GET");

                    return new DownloadFileInfo(httpsURLConnection.getContentType(), httpsURLConnection.getContentLength());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (httpsURLConnection != null) httpsURLConnection.disconnect();
                }
            }, executorService).thenAccept(downloadFileInfo1 ->
                    runOnUiThread(() -> {
                        binding.fileSizeText.setText(downloadFileInfo1.getSize() + " B");
                        binding.fileTypeText.setText(downloadFileInfo1.getType());
                    })
            ).exceptionally((e) -> {
                Toast.makeText(Lab3Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return null;
            });
        });

        binding.downloadFile.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                }
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }


            Intent intent = new Intent(Lab3Activity.this, DownloadService.class);
            intent.putExtra("url", binding.url.getText().toString());
            startService(intent);
        });
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {});


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("fileSizeText", binding.fileSizeText.getText().toString());
        outState.putString("fileTypeText", binding.fileTypeText.getText().toString());
        outState.putString("url", binding.url.getText().toString());
        outState.putString("downloadedBytes", binding.downloadedBytes.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        binding.fileSizeText.setText(state.getString("fileSizeText"));
        binding.fileTypeText.setText(state.getString("fileTypeText"));
        binding.url.setText(state.getString("url"));
        binding.downloadedBytes.setText(state.getString("downloadedBytes"));
    }
}