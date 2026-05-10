package com.example.worktracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface ScheduledShiftDao {

    @Insert
    void insert(ScheduledShift scheduledShift);

    @Delete
    void delete(ScheduledShift scheduledShift);

    @Query("SELECT * FROM scheduled_shifts WHERE shift_date = :date")
    LiveData<List<ScheduledShift>> getScheduledShiftsForDate(String date);

    @Query("SELECT * FROM scheduled_shifts")
    LiveData<List<ScheduledShift>> getAllScheduledShifts();

    @Query("SELECT * FROM scheduled_shifts WHERE username = :username")
    List<ScheduledShift> getScheduledShiftsForUser(String username);
}