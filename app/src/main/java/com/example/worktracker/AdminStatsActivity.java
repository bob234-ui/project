package com.example.worktracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdminStatsActivity extends AppCompatActivity {

    private TextView textViewAdminStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stats);

        textViewAdminStats = findViewById(R.id.textViewAdminStats);
        Button buttonBack = findViewById(R.id.buttonBack);

        loadStats();

        buttonBack.setOnClickListener(v -> finish());
    }

    private void loadStats() {
        ShiftDao shiftDao = AppDatabase.getDatabase(this).shiftDao();
        List<Shift> shifts = shiftDao.getAllShifts();

        if (shifts.isEmpty()) {
            textViewAdminStats.setText("No shift data yet.");
            return;
        }

        HashMap<String, Integer> shiftCounts = new HashMap<>();
        HashMap<String, Integer> breakCounts = new HashMap<>();
        HashMap<String, Long> totalShiftTimes = new HashMap<>();
        HashMap<String, Long> totalBreakTimes = new HashMap<>();

        for (Shift shift : shifts) {
            String username = shift.getUsername();

            if (username == null || username.isEmpty()) {
                username = "Unknown User";
            }

            shiftCounts.put(username, shiftCounts.getOrDefault(username, 0) + 1);
            breakCounts.put(username, breakCounts.getOrDefault(username, 0) + shift.getBreak_count());
            totalShiftTimes.put(username, totalShiftTimes.getOrDefault(username, 0L) + shift.getTotal_shift_millis());
            totalBreakTimes.put(username, totalBreakTimes.getOrDefault(username, 0L) + shift.getTotal_break_millis());
        }

        StringBuilder builder = new StringBuilder();

        for (String username : shiftCounts.keySet()) {
            builder.append("User: ").append(username).append("\n");
            builder.append("Shifts worked: ").append(shiftCounts.get(username)).append("\n");
            builder.append("Total time worked: ").append(formatTime(totalShiftTimes.get(username))).append("\n");
            builder.append("Total breaks taken: ").append(breakCounts.get(username)).append("\n");
            builder.append("Total break time: ").append(formatTime(totalBreakTimes.get(username))).append("\n\n");
        }

        textViewAdminStats.setText(builder.toString());
    }

    private String formatTime(long millis) {
        int seconds = (int) (millis / 1000);
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs);
    }

    public static Intent intentFactory(Context context) {
        return new Intent(context, AdminStatsActivity.class);
    }
}