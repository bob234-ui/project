package com.example.worktracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private TextView textViewAccountInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        textViewAccountInfo = findViewById(R.id.textViewAccountInfo);

        Button buttonBack = findViewById(R.id.buttonBack);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        loadAccountInfo();

        buttonBack.setOnClickListener(v -> finish());

        buttonLogout.setOnClickListener(v -> {
            SharedPreferences pref = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();

            Intent intent = MainActivity.intentFactory(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void loadAccountInfo() {
        SharedPreferences pref = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);

        String username = pref.getString(MainActivity.KEY_USERNAME, "User");
        boolean isAdmin = pref.getBoolean(MainActivity.KEY_IS_ADMIN, false);

        String role;

        if (isAdmin) {
            role = "Admin";
        } else {
            role = "Normal User";
        }

        textViewAccountInfo.setText(
                "Username: " + username + "\n" +
                        "Account Type: " + role
        );
    }

    public static Intent intentFactory(Context context) {
        return new Intent(context, SettingsActivity.class);
    }
}