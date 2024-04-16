package com.zielonka.lab.lab2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@androidx.room.Database(entities = {Phone.class}, version=1, exportSchema = false)
public abstract class LabDatabase extends RoomDatabase {
    public abstract PhoneDao phoneDao();
    private static volatile LabDatabase INSTANCE;

    public static LabDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (LabDatabase.class) {
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    LabDatabase.class,
                                    "phone_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return  INSTANCE;
    }

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                PhoneDao phoneDao = INSTANCE.phoneDao();
                phoneDao.addPhone(new Phone("Samsung", "Galaxy", "20", "cs.pollub.pl"));
                phoneDao.addPhone(new Phone("Apple", "iPhone", "12", "cs.pollub.pl"));
                phoneDao.addPhone(new Phone("Xiaomi", "Mi", "10", "cs.pollub.pl"));
                phoneDao.addPhone(new Phone("Huawei", "P", "40", "cs.pollub.pl"));
                phoneDao.addPhone(new Phone("OnePlus", "8", "Pro", "cs.pollub.pl"));
            });
        }
    };
}
