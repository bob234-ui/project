package com.example.worktracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LandingActivity extends AppCompatActivity {

    private boolean shiftStarted = false;
    private boolean breakStarted = false;

    private long shiftStartMillis = 0;
    private long breakStartMillis = 0;

    private String shiftStartTime = "";
    private String shiftDate = "";

    private int breakCount = 0;
    private long totalBreakMillis = 0;

    private Handler handler = new Handler();

    private TextView textViewTimer;
    private TextView textViewBreakTimer;
    private Button buttonShift;
    private Button buttonBreak;

    private Runnable shiftTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (shiftStarted) {
                long elapsed = System.currentTimeMillis() - shiftStartMillis;
                textViewTimer.setText("Shift Timer: " + formatTime(elapsed));
                handler.postDelayed(this, 1000);
            }
        }
    };

    private Runnable breakTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (breakStarted) {
                long elapsed = System.currentTimeMillis() - breakStartMillis;
                textViewBreakTimer.setText("Break Timer: " + formatTime(elapsed));
                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        SharedPreferences pref = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        String username = pref.getString(MainActivity.KEY_USERNAME, null);
        boolean isAdmin = pref.getBoolean(MainActivity.KEY_IS_ADMIN, false);

        if (username == null) {
            startActivity(MainActivity.intentFactory(this));
            finish();
            return;
        }

        TextView textViewUsername = findViewById(R.id.textViewUsername);

        buttonShift = findViewById(R.id.buttonShift);
        buttonBreak = findViewById(R.id.buttonBreak);

        Button buttonPastShifts = findViewById(R.id.buttonPastShifts);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        Button buttonAdminOnly = findViewById(R.id.buttonAdminOnly);
        Button buttonOngoingShifts = findViewById(R.id.buttonOngoingShifts);
        Button buttonCalendar = findViewById(R.id.buttonCalendar);

        textViewTimer = findViewById(R.id.textViewTimer);
        textViewBreakTimer = findViewById(R.id.textViewBreakTimer);

        textViewUsername.setText("Welcome, " + username);

        if (isAdmin) {
            buttonAdminOnly.setVisibility(View.VISIBLE);
            buttonOngoingShifts.setVisibility(View.VISIBLE);
            buttonCalendar.setVisibility(View.VISIBLE);
        } else {
            buttonAdminOnly.setVisibility(View.GONE);
            buttonOngoingShifts.setVisibility(View.GONE);
            buttonCalendar.setVisibility(View.GONE);
        }

        buttonShift.setOnClickListener(v -> {
            ShiftDao shiftDao = AppDatabase.getDatabase(this).shiftDao();

            if (!shiftStarted) {
                shiftStarted = true;

                shiftStartMillis = System.currentTimeMillis();
                shiftDate = getCurrentDate();
                shiftStartTime = getCurrentTime();

                breakCount = 0;
                totalBreakMillis = 0;

                buttonShift.setText("Stop Shift");
                textViewTimer.setText("Shift Timer: 00:00:00");

                handler.post(shiftTimerRunnable);

                Toast.makeText(this, "Shift started at " + shiftStartTime, Toast.LENGTH_SHORT).show();

            } else {
                shiftStarted = false;

                String shiftEndTime = getCurrentTime();

                if (breakStarted) {
                    long breakDuration = System.currentTimeMillis() - breakStartMillis;
                    totalBreakMillis += breakDuration;
                    breakCount++;

                    breakStarted = false;
                    buttonBreak.setText("Start Break");
                    textViewBreakTimer.setVisibility(View.GONE);
                    handler.removeCallbacks(breakTimerRunnable);
                }

                Shift shift = new Shift(
                        1,
                        shiftDate,
                        shiftStartTime,
                        shiftEndTime,
                        breakCount,
                        totalBreakMillis
                );

                shiftDao.insert(shift);

                buttonShift.setText("Start Shift");
                handler.removeCallbacks(shiftTimerRunnable);

                Toast.makeText(this, "Shift saved!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBreak.setOnClickListener(v -> {
            if (!shiftStarted) {
                Toast.makeText(this, "Start a shift first.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!breakStarted) {
                breakStarted = true;

                breakStartMillis = System.currentTimeMillis();
                buttonBreak.setText("Stop Break");
                textViewBreakTimer.setVisibility(View.VISIBLE);
                textViewBreakTimer.setText("Break Timer: 00:00:00");

                handler.post(breakTimerRunnable);

            } else {
                breakStarted = false;

                long breakDuration = System.currentTimeMillis() - breakStartMillis;
                totalBreakMillis += breakDuration;
                breakCount++;

                buttonBreak.setText("Start Break");
                handler.removeCallbacks(breakTimerRunnable);

                Toast.makeText(this, "Break ended.", Toast.LENGTH_SHORT).show();
            }
        });

        buttonPastShifts.setOnClickListener(v -> {
            startActivity(PastShiftsActivity.intentFactory(this));
        });

        buttonLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();

            Intent intent = MainActivity.intentFactory(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new Date());
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("hh:mm a", Locale.US).format(new Date());
    }

    private String formatTime(long millis) {
        int seconds = (int) (millis / 1000);
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs);
    }

    public static Intent intentFactory(Context context) {
        return new Intent(context, LandingActivity.class);
    }
}