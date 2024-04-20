package com.zielonka.lab.lab2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zielonka.lab.R;
import com.zielonka.lab.databinding.ActivityPhoneFormBinding;

public class PhoneFormActivity extends AppCompatActivity {

    private ActivityPhoneFormBinding binding;
    private boolean editing = false;
    private boolean isValid = true;
    Phone editedPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPhoneFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Phone form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent.getExtras() != null && intent.getExtras().getParcelable("phone") != null){
            editing = true;
            editedPhone = intent.getExtras().getParcelable("phone");
            assert editedPhone != null;
            binding.producer.setText(editedPhone.getProducer());
            binding.model.setText(editedPhone.getModel());
            binding.version.setText(editedPhone.getVersion());
            binding.website.setText(editedPhone.getWebsite());
        }

        binding.websiteButton.setOnClickListener(v -> {
            String url = binding.website.getText().toString();
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });
        binding.cancelButton.setOnClickListener(v -> {
            finish();
        });
        binding.saveButton.setOnClickListener(v -> {
            isValid = true;

            //ty
            if(binding.producer.getText().toString().isEmpty()){
                isValid = false;
                binding.producer.setError("Producer cannot be empty!");
            }

            if(binding.model.getText().toString().isEmpty()){
                isValid = false;
                binding.model.setError("Model cannot be empty!");
            }

            if(binding.version.getText().toString().isEmpty()){
                isValid = false;
                binding.version.setError("Version cannot be empty!");
            }

            if(binding.website.getText().toString().isEmpty()){
                isValid = false;
                binding.website.setError("Website cannot be empty!");
            }


            if(isValid){
                Bundle bundle = new Bundle();
                if(editing){
                    editedPhone = getIntent().getParcelableExtra("phone");
                    assert editedPhone != null;
                    editedPhone.setProducer(binding.producer.getText().toString());
                    editedPhone.setModel(binding.model.getText().toString());
                    editedPhone.setVersion(binding.version.getText().toString());
                    editedPhone.setWebsite(binding.website.getText().toString());
                    bundle.putBoolean("edited", true);
                } else {
                    editedPhone = new Phone(
                            binding.producer.getText().toString(),
                            binding.model.getText().toString(),
                            binding.version.getText().toString(),
                            binding.website.getText().toString()
                    );
                }

                bundle.putParcelable("phone", editedPhone);

                setResult(RESULT_OK, new Intent().putExtras(bundle));
                finish();
            }
        });


    }
}