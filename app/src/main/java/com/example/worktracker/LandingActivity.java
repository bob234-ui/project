package com.example.worktracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
    private boolean tookBreak = false;

    private long shiftStartTime = 0;
    private long breakStartTime = 0;
    private long breakEndTime = 0;
    private long totalBreakTime = 0;

    private SharedPreferences pref;

    private TextView textViewTimer;
    private TextView textViewBreakTimer;

    private Handler timerHandler = new Handler(Looper.getMainLooper());
    private Handler breakTimerHandler = new Handler(Looper.getMainLooper());

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (shiftStarted) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - shiftStartTime;

                textViewTimer.setText("Shift Timer: " + formatTimer(elapsedTime));
                timerHandler.postDelayed(this, 1000);
            }
        }
    };

    private Runnable breakTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (breakStarted) {
                long currentTime = System.currentTimeMillis();
                long elapsedBreakTime = totalBreakTime + (currentTime - breakStartTime);

                textViewBreakTimer.setText("Break Timer: " + formatTimer(elapsedBreakTime));
                breakTimerHandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        pref = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);

        String username = pref.getString(MainActivity.KEY_USERNAME, "User");
        boolean isAdmin = pref.getBoolean(MainActivity.KEY_IS_ADMIN, false);

        TextView textViewUsername = findViewById(R.id.textViewUsername);

        textViewTimer = findViewById(R.id.textViewTimer);
        textViewBreakTimer = findViewById(R.id.textViewBreakTimer);

        Button buttonShift = findViewById(R.id.buttonShift);
        Button buttonBreak = findViewById(R.id.buttonBreak);
        Button buttonPastShifts = findViewById(R.id.buttonPastShifts);
        Button buttonSettings = findViewById(R.id.buttonSettings);

        Button buttonOngoingShifts = findViewById(R.id.buttonOngoingShifts);
        Button buttonCalendar = findViewById(R.id.buttonCalendar);
        Button buttonAdminOnly = findViewById(R.id.buttonAdminOnly);

        Button buttonLogout = findViewById(R.id.buttonLogout);

        if (isAdmin) {
            textViewUsername.setText("Welcome, " + username + " (Admin)");

            buttonShift.setVisibility(View.GONE);
            buttonBreak.setVisibility(View.GONE);
            buttonPastShifts.setVisibility(View.GONE);
            buttonSettings.setVisibility(View.GONE);

            textViewTimer.setVisibility(View.GONE);
            textViewBreakTimer.setVisibility(View.GONE);

            buttonOngoingShifts.setVisibility(View.VISIBLE);
            buttonCalendar.setVisibility(View.VISIBLE);
            buttonAdminOnly.setVisibility(View.VISIBLE);

        } else {
            textViewUsername.setText("Welcome, " + username + " (Normal User)");

            buttonShift.setVisibility(View.VISIBLE);
            buttonBreak.setVisibility(View.VISIBLE);
            buttonPastShifts.setVisibility(View.VISIBLE);
            buttonSettings.setVisibility(View.VISIBLE);

            textViewTimer.setVisibility(View.VISIBLE);
            textViewBreakTimer.setVisibility(View.GONE);

            buttonOngoingShifts.setVisibility(View.GONE);
            buttonCalendar.setVisibility(View.GONE);
            buttonAdminOnly.setVisibility(View.GONE);
        }

        buttonShift.setOnClickListener(v -> {
            if (!shiftStarted) {
                shiftStarted = true;
                breakStarted = false;
                tookBreak = false;

                shiftStartTime = System.currentTimeMillis();
                breakStartTime = 0;
                breakEndTime = 0;
                totalBreakTime = 0;

                buttonShift.setText("Stop Shift");
                buttonBreak.setText("Start Break");

                textViewTimer.setText("Shift Timer: 00:00:00");
                textViewBreakTimer.setText("Break Timer: 00:00:00");
                textViewBreakTimer.setVisibility(View.GONE);

                timerHandler.post(timerRunnable);

                Toast.makeText(this, "Shift started", Toast.LENGTH_SHORT).show();

            } else {
                shiftStarted = false;

                long shiftEndTime = System.currentTimeMillis();

                timerHandler.removeCallbacks(timerRunnable);
                breakTimerHandler.removeCallbacks(breakTimerRunnable);

                textViewTimer.setText("Shift Timer: 00:00:00");
                textViewBreakTimer.setText("Break Timer: 00:00:00");
                textViewBreakTimer.setVisibility(View.GONE);

                if (breakStarted) {
                    breakEndTime = System.currentTimeMillis();
                    totalBreakTime += breakEndTime - breakStartTime;
                    breakStarted = false;
                    buttonBreak.setText("Start Break");
                }

                String breakInfo;

                if (tookBreak) {
                    breakInfo =
                            "Break Taken: Yes\n" +
                                    "Break Started: " + formatTime(breakStartTime) + "\n" +
                                    "Break Ended: " + formatTime(breakEndTime) + "\n" +
                                    "Break Total: " + getDuration(0, totalBreakTime) + "\n";
                } else {
                    breakInfo = "Break Taken: No\n";
                }

                String shiftRecord =
                        "Shift\n" +
                                "Started: " + formatTime(shiftStartTime) + "\n" +
                                "Ended: " + formatTime(shiftEndTime) + "\n" +
                                "Shift Total: " + getDuration(shiftStartTime, shiftEndTime) + "\n" +
                                breakInfo +
                                "\n";

                savePastShift(shiftRecord);

                buttonShift.setText("Start Shift");
                buttonBreak.setText("Start Break");

                Toast.makeText(this, "Shift saved", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBreak.setOnClickListener(v -> {
            if (!shiftStarted) {
                Toast.makeText(this, "Start a shift first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!breakStarted) {
                breakStarted = true;
                tookBreak = true;

                breakStartTime = System.currentTimeMillis();

                buttonBreak.setText("Stop Break");

                textViewBreakTimer.setVisibility(View.VISIBLE);
                textViewBreakTimer.setText("Break Timer: " + formatTimer(totalBreakTime));
                breakTimerHandler.post(breakTimerRunnable);

                Toast.makeText(this, "Break started", Toast.LENGTH_SHORT).show();

            } else {
                breakStarted = false;

                breakEndTime = System.currentTimeMillis();
                totalBreakTime += breakEndTime - breakStartTime;

                breakTimerHandler.removeCallbacks(breakTimerRunnable);

                buttonBreak.setText("Start Break");
                textViewBreakTimer.setText("Break Timer: " + formatTimer(totalBreakTime));

                Toast.makeText(
                        this,
                        "Break ended: " + getDuration(0, totalBreakTime),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        buttonPastShifts.setOnClickListener(v ->
                startActivity(PastShiftsActivity.intentFactory(this))
        );

        buttonSettings.setOnClickListener(v ->
                startActivity(SettingsActivity.intentFactory(this))
        );

        buttonLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();

            Intent intent = MainActivity.intentFactory(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void savePastShift(String newShift) {
        String oldShifts = pref.getString("pastShifts", "");
        String updatedShifts = newShift + oldShifts;

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("pastShifts", updatedShifts);
        editor.apply();
    }

    private String formatTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault());
        return formatter.format(new Date(time));
    }

    private String getDuration(long start, long end) {
        long diff = (start == 0) ? end : (end - start);

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        minutes %= 60;
        seconds %= 60;

        return hours + " hr " + minutes + " min " + seconds + " sec";
    }

    private String formatTimer(long ms) {
        long totalSeconds = ms / 1000;

        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static Intent intentFactory(Context context) {
        return new Intent(context, LandingActivity.class);
    }
}