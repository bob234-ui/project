package com.example.worktracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scheduled_shifts")
public class ScheduledShift {

    @PrimaryKey(autoGenerate = true)
    private int scheduled_shift_id;

    private String username;
    private String shift_date;
    private String shift_start;
    private String shift_end;

    public ScheduledShift(String username, String shift_date, String shift_start, String shift_end) {
        this.username = username;
        this.shift_date = shift_date;
        this.shift_start = shift_start;
        this.shift_end = shift_end;
    }

    public int getScheduled_shift_id() {
        return scheduled_shift_id;
    }

    public void setScheduled_shift_id(int scheduled_shift_id) {
        this.scheduled_shift_id = scheduled_shift_id;
    }

    public String getUsername() {
        return username;
    }

    public String getShift_date() {
        return shift_date;
    }

    public String getShift_start() {
        return shift_start;
    }

    public String getShift_end() {
        return shift_end;
    }
}