package com.zielonka.lab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.zielonka.lab.lab1.Lab1GUIActivity;
import com.zielonka.lab.lab2.Lab2Activity;
import com.zielonka.lab.lab3.Lab3Activity;
import com.zielonka.lab.lab4.Lab4Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(view -> {
            startActivity(new Intent(this, Lab1GUIActivity.class));
        });
        findViewById(R.id.button2).setOnClickListener(view -> {
            startActivity(new Intent(this, Lab2Activity.class));
        });
        findViewById(R.id.button3).setOnClickListener(view -> {
            startActivity(new Intent(this, Lab3Activity.class));
        });
        findViewById(R.id.button4).setOnClickListener(view -> {
            startActivity(new Intent(this, Lab4Activity.class));
        });
    }
}