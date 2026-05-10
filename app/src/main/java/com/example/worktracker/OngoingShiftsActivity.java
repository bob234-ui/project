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

public class OngoingShiftsActivity extends AppCompatActivity {

    private TextView textViewNoOngoingShifts;
    private OngoingShiftAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_shifts);

        textViewNoOngoingShifts = findViewById(R.id.textViewNoOngoingShifts);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewOngoingShifts);
        Button buttonBack = findViewById(R.id.buttonBack);

        OngoingShiftDao ongoingShiftDao = AppDatabase.getDatabase(this).ongoingShiftDao();

        adapter = new OngoingShiftAdapter(new ArrayList<>());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ongoingShiftDao.getAllOngoingShiftsLiveData().observe(this, ongoingShifts -> {
            adapter.updateOngoingShifts(ongoingShifts);

            if (ongoingShifts.isEmpty()) {
                textViewNoOngoingShifts.setText("No users are currently on shift.");
            } else {
                textViewNoOngoingShifts.setText("");
            }
        });

        buttonBack.setOnClickListener(v -> finish());
    }

    public static Intent intentFactory(Context context) {
        return new Intent(context, OngoingShiftsActivity.class);
    }
}