package com.example.worktracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ongoing_shifts")
public class OngoingShift {

    @PrimaryKey(autoGenerate = true)
    private int ongoing_shift_id;

    private String username;
    private String shift_date;
    private String shift_start;
    private long shift_start_millis;

    private int break_count;
    private long total_break_millis;

    private boolean break_active;
    private long break_start_millis;

    public OngoingShift(String username, String shift_date, String shift_start, long shift_start_millis,
                        int break_count, long total_break_millis, boolean break_active, long break_start_millis) {
        this.username = username;
        this.shift_date = shift_date;
        this.shift_start = shift_start;
        this.shift_start_millis = shift_start_millis;
        this.break_count = break_count;
        this.total_break_millis = total_break_millis;
        this.break_active = break_active;
        this.break_start_millis = break_start_millis;
    }

    public int getOngoing_shift_id() {
        return ongoing_shift_id;
    }

    public void setOngoing_shift_id(int ongoing_shift_id) {
        this.ongoing_shift_id = ongoing_shift_id;
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

    public long getShift_start_millis() {
        return shift_start_millis;
    }

    public int getBreak_count() {
        return break_count;
    }

    public void setBreak_count(int break_count) {
        this.break_count = break_count;
    }

    public long getTotal_break_millis() {
        return total_break_millis;
    }

    public void setTotal_break_millis(long total_break_millis) {
        this.total_break_millis = total_break_millis;
    }

    public boolean isBreak_active() {
        return break_active;
    }

    public void setBreak_active(boolean break_active) {
        this.break_active = break_active;
    }

    public long getBreak_start_millis() {
        return break_start_millis;
    }

    public void setBreak_start_millis(long break_start_millis) {
        this.break_start_millis = break_start_millis;
    }
}