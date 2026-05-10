package com.example.worktracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OngoingShiftDao {

    @Insert
    long insert(OngoingShift ongoingShift);

    @Update
    void update(OngoingShift ongoingShift);

    @Delete
    void delete(OngoingShift ongoingShift);

    @Query("SELECT * FROM ongoing_shifts")
    LiveData<List<OngoingShift>> getAllOngoingShiftsLiveData();

    @Query("SELECT * FROM ongoing_shifts WHERE ongoing_shift_id = :id LIMIT 1")
    OngoingShift getOngoingShiftById(int id);
}