package com.example.worktracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BreakDao {

    @Insert
    void insert(Break breakItem);

    @Query("SELECT * FROM breaks WHERE shift_id = :shiftId")
    List<Break> getBreaksForShift(int shiftId);

    @Query("SELECT * FROM breaks")
    List<Break> getAllBreaks();

    @Query("DELETE FROM breaks")
    void deleteAllBreaks();
}