package com.example.qrreader;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ReadHistoryDataTable.class}, version = 1, exportSchema = false)
abstract class AppDatabase extends RoomDatabase {
    public abstract ReadHistoryDataDao readHistoryDataDao();
}
