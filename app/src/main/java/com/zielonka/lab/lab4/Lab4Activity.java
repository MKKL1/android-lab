package com.zielonka.lab.lab4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.zielonka.lab.databinding.ActivityLab3Binding;
import com.zielonka.lab.databinding.ActivityLab4Binding;

public class Lab4Activity extends AppCompatActivity {

    private ActivityLab4Binding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLab4Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //binding.drawingSurface.resumeDrawing();
    }
}
