package com.zielonka.lab.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
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
        phoneListAdapter = new PhoneListAdapter(this, phone -> {
            Intent intent = new Intent(Lab2Activity.this, PhoneFormActivity.class);
            intent.putExtra("phone", phone);
            activityResultLauncher.launch(intent);
        });

        recyclerView.setAdapter(phoneListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.SimpleCallback callback =
                new ItemTouchHelper.SimpleCallback(
                        0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        viewModel.deletePhone(position);
                    }
                };

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        viewModel = new ViewModelProvider(this).get(Lab2ViewModel.class);
        viewModel.getAllPhones().observe(this, phoneList -> phoneListAdapter.setPhoneList(phoneList));

        binding.fabAddPhone.setOnClickListener(v ->
                activityResultLauncher.launch(new Intent(this, PhoneFormActivity.class)));
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    Phone phone;
                    if(data != null && (phone = data.getParcelableExtra("phone")) != null) {
                        if(data.getBooleanExtra("edited", false)){
                            viewModel.updatePhone(phone);
                        } else {
                            viewModel.addPhone(phone);
                        }
                    }
                }
            }
    );


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phone_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_phones) {
            viewModel.deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}