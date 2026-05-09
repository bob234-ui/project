package com.example.worktracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PastShiftsActivity extends AppCompatActivity {

    private TextView textViewNoShifts;
    private ShiftAdapter shiftAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_shifts);

        textViewNoShifts = findViewById(R.id.textViewNoShifts);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPastShifts);
        Button buttonBack = findViewById(R.id.buttonBack);

        ShiftDao shiftDao = AppDatabase.getDatabase(this).shiftDao();

        shiftAdapter = new ShiftAdapter(new ArrayList<>());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(shiftAdapter);

        shiftDao.getAllShiftsLiveData().observe(this, shifts -> {
            shiftAdapter.updateShifts(shifts);

            if (shifts.isEmpty()) {
                textViewNoShifts.setText("No past shifts yet.");
            } else {
                textViewNoShifts.setText("");
            }
        });

        buttonBack.setOnClickListener(v -> finish());
    }

    public static Intent intentFactory(Context context) {
        return new Intent(context, PastShiftsActivity.class);
    }
}