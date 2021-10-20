package com.example.qrreader;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "read_history_data")
public class ReadHistoryDataTable{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "auto_id")
    private int id;

    @ColumnInfo(name = "read_time")
    private String readTime;

    @ColumnInfo(name = "read_result")
    private String readResult;

    @ColumnInfo(name = "result_title")
    private String resultTitle;

    public ReadHistoryDataTable(String readTime,String readResult,String resultTitle) {
        this.readTime = readTime;
        this.readResult = readResult;
        this.resultTitle = resultTitle;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadResult(String readResult) {
        this.readResult = readResult;
    }

    public String getReadResult() {
        return readResult;
    }

    public void setResultTitle(String resultTitle) {
        this.resultTitle = resultTitle;
    }

    public String getResultTitle() {
        return resultTitle;
    }
}
