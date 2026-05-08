package com.example.worktracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ShiftDao {

    @Insert
    void insert(Shift shift);

    @Query("SELECT * FROM shifts WHERE username = :username")
    List<Shift> getShiftsForUser(String username);

    @Query("SELECT * FROM shifts")
    List<Shift> getAllShifts();

    @Query("DELETE FROM shifts")
    void deleteAllShifts();
}