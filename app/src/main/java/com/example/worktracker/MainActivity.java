//Main activity
package com.example.worktracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "WorkTrackerPrefs";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_IS_ADMIN = "isAdmin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //checks if already logged in
        SharedPreferences pref = getSharedPreferences("WorkTrackerPref", MODE_PRIVATE);
        String username = pref.getString("username", null);
        if (username != null) {
            startActivity(LandingActivity.intentFactory(this));
            finish();
            return;
        }

        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonLogin.setOnClickListener(v -> {
            startActivity(LoginActivity.intentFactory(this));

        });
        buttonSignUp.setOnClickListener(v -> {
            startActivity(SignUpActivity.intentFactory(this));
        });


    }

    public static Intent intentFactory(Context context){
        return new Intent(context, MainActivity.class);
    }
}
