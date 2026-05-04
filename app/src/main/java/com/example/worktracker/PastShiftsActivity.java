package com.example.worktracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PastShiftsActivity extends AppCompatActivity {

    private TextView textViewPastShifts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_shifts);

        textViewPastShifts = findViewById(R.id.textViewPastShifts);
        Button buttonBack = findViewById(R.id.buttonBack);

        loadPastShifts();

        buttonBack.setOnClickListener(v -> finish());
    }

    private void loadPastShifts() {
        SharedPreferences pref = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        String pastShifts = pref.getString("pastShifts", "");

        if (pastShifts.isEmpty()) {
            textViewPastShifts.setText("No past shifts yet.");
        } else {
            textViewPastShifts.setText(pastShifts);
        }
    }

    public static Intent intentFactory(Context context) {
        return new Intent(context, PastShiftsActivity.class);
    }
}