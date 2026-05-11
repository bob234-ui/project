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
import java.util.List;
import androidx.appcompat.app.AlertDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingActivity extends AppCompatActivity {

    private int ongoingShiftId = -1;

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
        Button buttonSettings = findViewById(R.id.buttonSettings);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        Button buttonAdminOnly = findViewById(R.id.buttonAdminOnly);
        Button buttonOngoingShifts = findViewById(R.id.buttonOngoingShifts);
        Button buttonCalendar = findViewById(R.id.buttonCalendar);

        textViewTimer = findViewById(R.id.textViewTimer);
        textViewBreakTimer = findViewById(R.id.textViewBreakTimer);

        textViewUsername.setText("Welcome, " + username);
        if (!isAdmin) {
            checkUpcomingShifts(username);
        }

        if (isAdmin) {
            buttonShift.setVisibility(View.GONE);
            buttonBreak.setVisibility(View.GONE);
            buttonPastShifts.setVisibility(View.GONE);
            textViewTimer.setVisibility(View.GONE);
            textViewBreakTimer.setVisibility(View.GONE);

            buttonAdminOnly.setVisibility(View.VISIBLE);
            buttonOngoingShifts.setVisibility(View.VISIBLE);
            buttonCalendar.setVisibility(View.VISIBLE);
        } else {
            buttonShift.setVisibility(View.VISIBLE);
            buttonBreak.setVisibility(View.VISIBLE);
            buttonPastShifts.setVisibility(View.VISIBLE);
            textViewTimer.setVisibility(View.VISIBLE);

            buttonAdminOnly.setVisibility(View.GONE);
            buttonOngoingShifts.setVisibility(View.GONE);
            buttonCalendar.setVisibility(View.GONE);
        }

        buttonCalendar.setOnClickListener(v -> {
            startActivity(AdminCalendarActivity.intentFactory(this));
        });

        buttonShift.setOnClickListener(v -> {
            ShiftDao shiftDao = AppDatabase.getDatabase(this).shiftDao();
            OngoingShiftDao ongoingShiftDao = AppDatabase.getDatabase(this).ongoingShiftDao();

            if (!shiftStarted) {
                shiftStarted = true;

                shiftStartMillis = System.currentTimeMillis();
                shiftDate = getCurrentDate();
                shiftStartTime = getCurrentTime();

                breakCount = 0;
                totalBreakMillis = 0;

                OngoingShift ongoingShift = new OngoingShift(
                        username,
                        shiftDate,
                        shiftStartTime,
                        shiftStartMillis,
                        0,
                        0,
                        false,
                        0
                );

                ongoingShiftId = (int) ongoingShiftDao.insert(ongoingShift);

                buttonShift.setText("Stop Shift");
                textViewTimer.setText("Shift Timer: 00:00:00");

                handler.post(shiftTimerRunnable);

                Toast.makeText(this, "Shift started at " + shiftStartTime, Toast.LENGTH_SHORT).show();

            } else {
                shiftStarted = false;

                String shiftEndTime = getCurrentTime();

                OngoingShift ongoingShift = null;

                if (ongoingShiftId != -1) {
                    ongoingShift = ongoingShiftDao.getOngoingShiftById(ongoingShiftId);
                }

                if (breakStarted) {
                    long breakDuration = System.currentTimeMillis() - breakStartMillis;
                    totalBreakMillis += breakDuration;
                    breakCount++;

                    breakStarted = false;
                    buttonBreak.setText("Start Break");
                    textViewBreakTimer.setVisibility(View.GONE);
                    handler.removeCallbacks(breakTimerRunnable);
                }

                long totalShiftMillis = System.currentTimeMillis() - shiftStartMillis;

                Shift shift = new Shift(
                        1,
                        username,
                        shiftDate,
                        shiftStartTime,
                        shiftEndTime,
                        breakCount,
                        totalBreakMillis,
                        totalShiftMillis
                );


                shiftDao.insert(shift);

                if (ongoingShift != null) {
                    ongoingShiftDao.delete(ongoingShift);
                }

                ongoingShiftId = -1;

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

            OngoingShiftDao ongoingShiftDao = AppDatabase.getDatabase(this).ongoingShiftDao();

            if (!breakStarted) {
                breakStarted = true;

                breakStartMillis = System.currentTimeMillis();
                buttonBreak.setText("Stop Break");
                textViewBreakTimer.setVisibility(View.VISIBLE);
                textViewBreakTimer.setText("Break Timer: 00:00:00");

                if (ongoingShiftId != -1) {
                    OngoingShift ongoingShift = ongoingShiftDao.getOngoingShiftById(ongoingShiftId);

                    if (ongoingShift != null) {
                        ongoingShift.setBreak_active(true);
                        ongoingShift.setBreak_start_millis(breakStartMillis);
                        ongoingShiftDao.update(ongoingShift);
                    }
                }

                handler.post(breakTimerRunnable);

            } else {
                breakStarted = false;

                long breakDuration = System.currentTimeMillis() - breakStartMillis;
                totalBreakMillis += breakDuration;
                breakCount++;

                buttonBreak.setText("Start Break");
                handler.removeCallbacks(breakTimerRunnable);

                if (ongoingShiftId != -1) {
                    OngoingShift ongoingShift = ongoingShiftDao.getOngoingShiftById(ongoingShiftId);

                    if (ongoingShift != null) {
                        ongoingShift.setBreak_active(false);
                        ongoingShift.setBreak_start_millis(0);
                        ongoingShift.setBreak_count(breakCount);
                        ongoingShift.setTotal_break_millis(totalBreakMillis);
                        ongoingShiftDao.update(ongoingShift);
                    }
                }

                Toast.makeText(this, "Break ended.", Toast.LENGTH_SHORT).show();
            }
        });

        buttonPastShifts.setOnClickListener(v -> {
            startActivity(PastShiftsActivity.intentFactory(this));
        });

        buttonSettings.setOnClickListener(v -> {
            startActivity(SettingsActivity.intentFactory(this));
        });

        buttonOngoingShifts.setOnClickListener(v -> {
            startActivity(OngoingShiftsActivity.intentFactory(this));
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
        buttonAdminOnly.setOnClickListener(v -> {
            startActivity(AdminStatsActivity.intentFactory(this));
        });
    }

    private void checkUpcomingShifts(String username) {
        ScheduledShiftDao scheduledShiftDao = AppDatabase.getDatabase(this).scheduledShiftDao();

        List<ScheduledShift> scheduledShifts =
                scheduledShiftDao.getScheduledShiftsForUser(username);

        if (scheduledShifts == null || scheduledShifts.isEmpty()) {
            return;
        }

        StringBuilder message = new StringBuilder();

        for (ScheduledShift shift : scheduledShifts) {
            message.append("Date: ")
                    .append(shift.getShift_date())
                    .append("\nTime: ")
                    .append(shift.getShift_start())
                    .append(" - ")
                    .append(shift.getShift_end())
                    .append("\n\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("Upcoming Shift Reminder")
                .setMessage("You have upcoming scheduled shift(s):\n\n" + message)
                .setPositiveButton("OK", null)
                .show();
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