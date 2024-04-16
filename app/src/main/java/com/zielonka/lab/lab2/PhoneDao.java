package com.zielonka.lab.lab2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PhoneDao {
    @Insert
    void addPhone(Phone phone);

    @Update
    void updatePhone(Phone phone);

    @Query("SELECT * FROM phone")
    LiveData<List<Phone>> selectAll();

    @Delete
    void deletePhone(Phone phone);

    @Query("DELETE FROM phone")
    void deleteAll();
}
