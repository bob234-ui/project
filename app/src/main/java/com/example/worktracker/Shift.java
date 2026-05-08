package com.example.worktracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shifts")
public class Shift {

    @PrimaryKey(autoGenerate = true)
    private int shift_id;

    private int user_id;
    private String shift_date;
    private String shift_start;
    private String shift_end;

    public Shift(int user_id, String shift_date, String shift_start, String shift_end) {
        this.user_id = user_id;
        this.shift_date = shift_date;
        this.shift_start = shift_start;
        this.shift_end = shift_end;
    }

    public int getShift_id() { return shift_id; }
    public void setShift_id(int shift_id) { this.shift_id = shift_id; }

    public int getUser_id() { return user_id; }
    public String getShift_date() { return shift_date; }
    public String getShift_start() { return shift_start; }
    public String getShift_end() { return shift_end; }
}