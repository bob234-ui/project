package com.example.worktracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminCalendarActivity extends AppCompatActivity {

    private String selectedDate;
    private ScheduledShiftAdapter adapter;
    private TextView textViewSelectedDate;
    private TextView textViewShiftCount;
    private LiveData<List<ScheduledShift>> currentLiveData;
    private ScheduledShiftDao scheduledShiftDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_calendar);

        scheduledShiftDao = AppDatabase.getDatabase(this).scheduledShiftDao();

        CalendarView calendarView = findViewById(R.id.calendarViewAdmin);
        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);
        textViewShiftCount = findViewById(R.id.textViewShiftCount);

        EditText editTextUsername = findViewById(R.id.editTextAssignUsername);
        EditText editTextStart = findViewById(R.id.editTextShiftStart);
        EditText editTextEnd = findViewById(R.id.editTextShiftEnd);

        Button buttonAssignShift = findViewById(R.id.buttonAssignShift);

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            startActivity(LandingActivity.intentFactory(this));
            finish();
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerViewScheduledShifts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ScheduledShiftAdapter(new ArrayList<>(), scheduledShift -> {
            scheduledShiftDao.delete(scheduledShift);
            Toast.makeText(this, "Shift cancelled", Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapter);

        selectedDate = formatDate(System.currentTimeMillis());
        updateSelectedDate();

        observeDate(selectedDate);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.US, "%02d/%02d/%04d", month + 1, dayOfMonth, year);
            updateSelectedDate();
            observeDate(selectedDate);
        });

        buttonAssignShift.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String start = editTextStart.getText().toString().trim();
            String end = editTextEnd.getText().toString().trim();

            if (username.isEmpty() || start.isEmpty() || end.isEmpty()) {
                Toast.makeText(this, "Fill in username, start time, and end time", Toast.LENGTH_SHORT).show();
                return;
            }

            ScheduledShift scheduledShift = new ScheduledShift(username, selectedDate, start, end);
            scheduledShiftDao.insert(scheduledShift);

            editTextUsername.setText("");
            editTextStart.setText("");
            editTextEnd.setText("");

            Toast.makeText(this, "Shift assigned", Toast.LENGTH_SHORT).show();
        });

        buttonBack.setOnClickListener(v -> finish());
    }

    private void observeDate(String date) {
        if (currentLiveData != null) {
            currentLiveData.removeObservers(this);
        }

        currentLiveData = scheduledShiftDao.getScheduledShiftsForDate(date);

        currentLiveData.observe(this, shifts -> {
            adapter.updateShifts(shifts);
            textViewShiftCount.setText("Upcoming shifts on this day: " + shifts.size());
        });
    }

    private void updateSelectedDate() {
        textViewSelectedDate.setText("Selected Date: " + selectedDate);
    }

    private String formatDate(long millis) {
        return new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new Date(millis));
    }

    public static Intent intentFactory(Context context) {
        return new Intent(context, AdminCalendarActivity.class);
    }
}