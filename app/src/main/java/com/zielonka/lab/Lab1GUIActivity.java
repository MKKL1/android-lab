package com.zielonka.lab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Lab1GUIActivity extends AppCompatActivity {
    private boolean isImieCorrect = false;
    private boolean isNazwiskoCorrect = false;
    private boolean isOcenyCorrect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1_guiactivity);


        registerValidator(findViewById(R.id.imieEditText), view -> {
            isImieCorrect = false;
            if(view.getText().toString().isEmpty())
                throw new RuntimeException("Nie moze byc pusty");
            isImieCorrect = true;
        });

        registerValidator(findViewById(R.id.nazwiskoEditText), view -> {
            isNazwiskoCorrect = false;
            if(view.getText().toString().isEmpty())
                throw new RuntimeException("Nie moze byc pusty");
            isNazwiskoCorrect = true;
        });

        registerValidator(findViewById(R.id.ocenyEditText), view -> {
            isOcenyCorrect = false;
            if(view.getText().toString().isEmpty())
                throw new RuntimeException("Nie moze byc pusty");

            int c;
            try {
                c = Integer.parseInt(view.getText().toString());
            } catch (NumberFormatException e) {
                throw new RuntimeException("To nie liczba");
            }

            if(c < 5 || c > 15)
                throw new RuntimeException("Zakres [5-15]");
            isOcenyCorrect = true;
        });
    }

    private void registerValidator(EditText editText, FormElementValidator<EditText> validator) {
        editText.setOnFocusChangeListener((view, b) -> {
            if(b) return;
            try {
                validator.validate(editText);
            } catch (RuntimeException e) {
                editText.setError(e.getMessage());
                Toast.makeText(Lab1GUIActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            checkIfAllCorrect();
        });
    }

    private void checkIfAllCorrect() {
        if(isOcenyCorrect&&isImieCorrect&&isNazwiskoCorrect) {
            findViewById(R.id.ocenybutton).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.ocenybutton).setVisibility(View.GONE);
        }

    }

}