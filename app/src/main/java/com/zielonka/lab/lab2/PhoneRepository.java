package com.zielonka.lab.lab2;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PhoneRepository {
    private PhoneDao phoneDao;
    private LiveData<List<Phone>> allPhones;

    public PhoneRepository(Application app) {
        LabDatabase db = LabDatabase.getDatabase(app);
        phoneDao = db.phoneDao();
        allPhones = phoneDao.getAll();
    }

    public void addPhone(Phone phone) {
        LabDatabase.databaseWriteExecutor.execute(() -> {
            phoneDao.addPhone(phone);
        });
    }

    public void updatePhone(Phone phone) {
        LabDatabase.databaseWriteExecutor.execute(() -> {
            phoneDao.updatePhone(phone);
        });
    }

    public void deletePhone(Phone phone) {
        LabDatabase.databaseWriteExecutor.execute(() -> {
            phoneDao.deletePhone(phone);
        });
    }

    public void deleteAll() {
        LabDatabase.databaseWriteExecutor.execute(() -> {
            phoneDao.deleteAll();
        });
    }

    public LiveData<List<Phone>> getAllPhones() {
        return allPhones;
    }


}
