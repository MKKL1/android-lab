package com.zielonka.lab.lab2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.zielonka.lab.databinding.ActivityLab2Binding;


public class Lab2Activity extends AppCompatActivity {
    private ActivityLab2Binding binding;
    private Lab2ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLab2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Phone list");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

}