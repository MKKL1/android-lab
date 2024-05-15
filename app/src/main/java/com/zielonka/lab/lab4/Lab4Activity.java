package com.zielonka.lab.lab4;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.zielonka.lab.R;
import com.zielonka.lab.databinding.ActivityLab3Binding;
import com.zielonka.lab.databinding.ActivityLab4Binding;

public class Lab4Activity extends AppCompatActivity {

    private ActivityLab4Binding binding;
    private boolean galleryVisible;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLab4Binding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Laboratorium 5 - Grafika");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawingSurface drawingSurface = binding.paintSurfaceView;

        binding.buttonRed.setOnClickListener( v -> drawingSurface.setPaintColor(Color.RED));
        binding.buttonYellow.setOnClickListener( v -> drawingSurface.setPaintColor(Color.YELLOW));
        binding.buttonBlue.setOnClickListener( v -> drawingSurface.setPaintColor(Color.BLUE));
        binding.buttonGreen.setOnClickListener( v -> drawingSurface.setPaintColor(Color.GREEN));
        binding.clearPaintButton.setOnClickListener( v-> drawingSurface.clearCanvas());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.graphic_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_image) {
            saveImage();
            return true;
        } else if (item.getItemId() == R.id.show_images){
            showImages();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().getBackStackEntryCount()==1 ){
            binding.lab5submain.setVisibility(View.VISIBLE);
            binding.buttons.setVisibility(View.VISIBLE);
            binding.paintSurfaceView.clearCanvas();
            galleryVisible = false;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onSupportNavigateUp();
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {});


    private void saveImage(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }

        if (binding.paintSurfaceView.saveCanvas("rysunek")) {
            Toast.makeText(this, "Zapisano rysunek", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Nie udało sie zapisac", Toast.LENGTH_SHORT).show();
        }
    }

    private void showImages(){
        binding.paintSurfaceView.clearCanvas();
        binding.lab5submain.setVisibility(View.INVISIBLE);
        binding.buttons.setVisibility(View.INVISIBLE);

        galleryVisible = true;
        
        PaintingFragmentList fragmentList = new PaintingFragmentList();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, fragmentList)
                .addToBackStack("gallery")
                .commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("galleryVisible", galleryVisible);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        galleryVisible = savedInstanceState.getBoolean("visible");
        if(galleryVisible){
            binding.lab5submain.setVisibility(View.INVISIBLE);
            binding.buttons.setVisibility(View.INVISIBLE);
        } else{
            binding.lab5submain.setVisibility(View.VISIBLE);
            binding.buttons.setVisibility(View.VISIBLE);
        }
    }
}
