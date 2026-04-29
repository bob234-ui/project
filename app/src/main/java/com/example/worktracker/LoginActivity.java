package com.example.worktracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UserRepository repo = new UserRepository(this);
        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            User user = repo.login(username, password);
            if (user == null) {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save login to SharedPreferences
            SharedPreferences pref = getSharedPreferences("WorkTrackerPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("username", user.getUsername());
            editor.putBoolean("isAdmin", user.isAdmin());
            editor.apply();

            // Landing page
            startActivity(LandingActivity.intentFactory(this));
            finish();
        });
    }

    public static Intent intentFactory(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}