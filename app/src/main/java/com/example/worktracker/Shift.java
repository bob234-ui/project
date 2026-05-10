package com.example.worktracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shifts")
public class Shift {

    @PrimaryKey(autoGenerate = true)
    private int shift_id;

    private int user_id;
    private String username;
    private String shift_date;
    private String shift_start;
    private String shift_end;
    private int break_count;
    private long total_break_millis;
    private long total_shift_millis;

    public Shift(int user_id, String username, String shift_date, String shift_start, String shift_end,
                 int break_count, long total_break_millis, long total_shift_millis) {
        this.user_id = user_id;

        this.username = username;

        this.shift_date = shift_date;

        this.shift_start = shift_start;

        this.shift_end = shift_end;

        this.break_count = break_count;

        this.total_break_millis = total_break_millis;

        this.total_shift_millis = total_shift_millis;
    }

    public int getShift_id() { return shift_id; }
    public void setShift_id(int shift_id) { this.shift_id = shift_id; }

    public int getUser_id() { return user_id; }

    public String getUsername() { return username; }

    public String getShift_date() { return shift_date; }

    public String getShift_start() { return shift_start; }

    public String getShift_end() { return shift_end; }

    public int getBreak_count() { return break_count; }

    public long getTotal_break_millis() { return total_break_millis; }

    public long getTotal_shift_millis() { return total_shift_millis; }
}