package com.zielonka.lab.lab2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zielonka.lab.R;
import com.zielonka.lab.databinding.ActivityLab2Binding;

import java.util.List;


public class Lab2Activity extends AppCompatActivity {
    private ActivityLab2Binding binding;
    private Lab2ViewModel viewModel;
    private PhoneListAdapter phoneListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLab2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Phone list");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.phone_list);
        phoneListAdapter = new PhoneListAdapter(this);
        recyclerView.setAdapter(phoneListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(Lab2ViewModel.class);
        viewModel.getAllPhones().observe(this, phoneList -> phoneListAdapter.setPhoneList(phoneList));


    }

}