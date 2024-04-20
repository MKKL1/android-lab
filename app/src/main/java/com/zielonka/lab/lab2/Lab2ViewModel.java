package com.zielonka.lab.lab2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Objects;

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

    public void addPhone(Phone phone) {
        phoneRepository.addPhone(phone);
    }

    public void updatePhone(Phone phone) {
        phoneRepository.updatePhone(phone);
    }

    public void deletePhone(Phone phone) {
        phoneRepository.deletePhone(phone);
    }

    public void deletePhone(int position) {
        phoneRepository.deletePhone(Objects.requireNonNull(allPhones.getValue()).get(position));
    }

    public void deleteAll() {
        phoneRepository.deleteAll();
    }
}
