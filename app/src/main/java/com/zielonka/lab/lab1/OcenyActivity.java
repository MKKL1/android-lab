package com.zielonka.lab.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zielonka.lab.R;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OcenyActivity extends AppCompatActivity {
    private List<ModelOceny> oceny;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oceny);
        fillGrades();
        findViewById(R.id.average_button).setOnClickListener(view -> {
            double average = oceny.stream()
                    .map(ModelOceny::getOcena)
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0);
            Bundle bundleOut = new Bundle();
            bundleOut.putDouble("average", average);
            Intent intentOut = new Intent();
            intentOut.putExtras(bundleOut);
            setResult(RESULT_OK, intentOut);
            finish();
        });
    }

    private void fillGrades() {
        Intent intent = getIntent();
        String[] nazwyPrzedmiotow = getResources().getStringArray(R.array.nazwy_przedmiotow);
        oceny = Arrays.stream(nazwyPrzedmiotow)
                .map(nazwa -> new ModelOceny(nazwa, 2))
                .limit(intent.getIntExtra("oceny", 5))
                .collect(Collectors.toList());
        OcenyModelAdapter ocenyModelAdapter = new OcenyModelAdapter(this, oceny);
        RecyclerView listaOcen = findViewById(R.id.lista_ocen);
        listaOcen.setAdapter(ocenyModelAdapter);
        listaOcen.setLayoutManager(new LinearLayoutManager(this));
    }
}