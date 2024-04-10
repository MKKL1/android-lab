package com.zielonka.lab.lab1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zielonka.lab.R;

public class Lab1GUIActivity extends AppCompatActivity {
    private boolean isImieCorrect = false;
    private boolean isNazwiskoCorrect = false;
    private boolean isOcenyCorrect = false;
    private boolean zdane = false;
    private boolean returned = false;
    private double average = 2f;
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

        findViewById(R.id.ocenybutton).setOnClickListener(view -> {
            Intent intent = new Intent(Lab1GUIActivity.this, OcenyActivity.class);
            intent.putExtra("oceny", Integer.parseInt(((EditText) findViewById(R.id.ocenyEditText)).getText().toString()));
            ocenyActivityResult.launch(intent);
        });
        findViewById(R.id.resultButton).setOnClickListener(view -> {
            AlertDialog alert = new AlertDialog.Builder(this)
                    .setMessage(zdane ? getString(R.string.finish_success)
                            : getString(R.string.finish_failure))
                    .setTitle(R.string.leaving_app)
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .create();
            alert.show();
        });

        findViewById(R.id.resultView).setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private final ActivityResultLauncher<Intent> ocenyActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                handleActivityResult(data);
            }
    );

    private void handleActivityResult(Intent data) {
        if(data == null) return;
        returned = true;
        average = data.getDoubleExtra("average", 0);
        drawResult();
    }

    private void drawResult() {
        findViewById(R.id.resultView).setVisibility(View.VISIBLE);
//        findViewById(R.id.resultTextView).setVisibility(View.VISIBLE);
//        findViewById(R.id.resultButton).setVisibility(View.VISIBLE);

        ((TextView) findViewById(R.id.resultTextView))
                .setText(getResources().getString(R.string.result_average_text, average));

        Button resultButton = findViewById(R.id.resultButton);
        zdane = average >= 3.0;
        if(zdane) resultButton.setText(R.string.result_button_success_text);
        else resultButton.setText(R.string.result_button_failure_text);
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("imie",((TextView) findViewById(R.id.imieTextView)).getText().toString());
        outState.putString("nazwisko",((TextView) findViewById(R.id.nazwiskoTextView)).getText().toString());
        outState.putString("oceny",((TextView) findViewById(R.id.ocenyTextView)).getText().toString());
        outState.putBoolean("isImieCorrect", isImieCorrect);
        outState.putBoolean("isNazwiskoCorrect", isNazwiskoCorrect);
        outState.putBoolean("isOcenyCorrect", isOcenyCorrect);
        outState.putBoolean("zdane", zdane);
        outState.putBoolean("returned", returned);
        outState.putDouble("average", average);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        ((TextView) findViewById(R.id.imieTextView)).setText(savedInstanceState.getString("imie"));
        ((TextView) findViewById(R.id.nazwiskoTextView)).setText(savedInstanceState.getString("nazwisko"));
        ((TextView) findViewById(R.id.ocenyTextView)).setText(savedInstanceState.getString("oceny"));
        isImieCorrect = savedInstanceState.getBoolean("isImieCorrect");
        isNazwiskoCorrect = savedInstanceState.getBoolean("isNazwiskoCorrect");
        isOcenyCorrect = savedInstanceState.getBoolean("isOcenyCorrect");
        checkIfAllCorrect();
        zdane = savedInstanceState.getBoolean("zdane");
        returned = savedInstanceState.getBoolean("returned");
        average = savedInstanceState.getDouble("average");
        findViewById(R.id.resultView).setVisibility(View.GONE);
//        findViewById(R.id.resultTextView).setVisibility(View.GONE);
//        findViewById(R.id.resultButton).setVisibility(View.GONE);
        drawResult();
        super.onRestoreInstanceState(savedInstanceState);
    }

}