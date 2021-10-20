package com.example.qrreader;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReadHistoryDataDao {
    @Insert
    void insertAll(ReadHistoryDataTable... readHistoryDataTable);

    @Insert
    void insert(ReadHistoryDataTable readHistoryDataTable);

    @Delete
    void delete(ReadHistoryDataTable readHistoryDataTable);

    @Query("SELECT * FROM read_history_data")
    List<ReadHistoryDataTable> getAll();

    @Query("SELECT count(*) FROM read_history_data")
    int getCount();

    @Query("DELETE FROM read_history_data WHERE auto_id = (SELECT auto_id FROM read_history_data LIMIT 1)")
    void deleteTop();

}
