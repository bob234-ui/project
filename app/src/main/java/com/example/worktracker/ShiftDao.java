package com.example.worktracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ShiftDao {

    @Insert
    void insert(Shift shift);

    @Query("SELECT * FROM shifts WHERE user_id = :userId")
    List<Shift> getShiftsForUser(int userId);

    @Query("SELECT * FROM shifts")
    List<Shift> getAllShifts();

    @Query("SELECT * FROM shifts")
    LiveData<List<Shift>> getAllShiftsLiveData();

    @Query("DELETE FROM shifts")
    void deleteAllShifts();
}