package com.zielonka.lab.lab1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zielonka.lab.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OcenyActivity extends AppCompatActivity {
    private ArrayList<ModelOceny> oceny;
    private RecyclerView.LayoutManager layoutManager;
    private Parcelable mListState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oceny);

        Intent intent = getIntent();
        String[] nazwyPrzedmiotow = getResources().getStringArray(R.array.nazwy_przedmiotow);
        oceny = Arrays.stream(nazwyPrzedmiotow)
                .map(nazwa -> new ModelOceny(nazwa, 2))
                .limit(intent.getIntExtra("oceny", 5))
                        .collect(Collectors.toList())
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
        OcenyModelAdapter ocenyModelAdapter = new OcenyModelAdapter(this, oceny);
        RecyclerView listaOcen = findViewById(R.id.lista_ocen);
        listaOcen.setAdapter(ocenyModelAdapter);
        layoutManager = new LinearLayoutManager(this);
        listaOcen.setLayoutManager(layoutManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArray("state", oceny.toArray(new ModelOceny[0]));
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            oceny = Arrays.stream(Objects.requireNonNull(savedInstanceState.getParcelableArray("state", ModelOceny.class))).collect(Collectors.toList());
        }
        fillGrades();
    }
}
