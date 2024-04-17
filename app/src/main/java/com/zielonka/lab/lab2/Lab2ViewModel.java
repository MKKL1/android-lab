package com.zielonka.lab.lab2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class Lab2ViewModel extends AndroidViewModel {
    private final PhoneRepository phoneRepository;
    private final LiveData<List<Phone>> allPhones;
    public Lab2ViewModel(@NonNull Application application) {
        super(application);
        phoneRepository = new PhoneRepository(application);
        this.allPhones = phoneRepository.getAllPhones();
    }

    public LiveData<List<Phone>> getAllPhones() {
        return allPhones;
    }

    public void deleteAll() {
        phoneRepository.deleteAll();
    }
}
