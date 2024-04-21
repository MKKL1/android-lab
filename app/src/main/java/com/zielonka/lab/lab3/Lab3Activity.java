package com.zielonka.lab.lab3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    @SuppressLint("SetTextI18n")
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
                        binding.fileSizeText.setText(downloadFileInfo1.getSize()/1000 + " KB");
                        binding.fileTypeText.setText(downloadFileInfo1.getType());
                    })
            ).exceptionally((e) -> {
                Toast.makeText(Lab3Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return null;
            });
        });
    }
}