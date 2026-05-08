package com.example.worktracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "worklogs")
public class WorkLog {

    @PrimaryKey(autoGenerate = true)
    private int log_id;

    private int user_id;
    private String clock_in_time;
    private String clock_out_time;

    public WorkLog(int user_id, String clock_in_time, String clock_out_time) {
        this.user_id = user_id;
        this.clock_in_time = clock_in_time;
        this.clock_out_time = clock_out_time;
    }

    public int getLog_id() { return log_id; }
    public void setLog_id(int log_id) { this.log_id = log_id; }

    public int getUser_id() { return user_id; }
    public String getClock_in_time() { return clock_in_time; }
    public String getClock_out_time() { return clock_out_time; }
}