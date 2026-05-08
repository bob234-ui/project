package com.example.worktracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WorkLogDao {

    @Insert
    void insert(WorkLog workLog);

    @Query("SELECT * FROM worklogs WHERE user_id = :userId")
    List<WorkLog> getLogsForUser(int userId);

    @Query("SELECT * FROM worklogs")
    List<WorkLog> getAllLogs();
}