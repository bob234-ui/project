package com.example.worktracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        UserRepository repo = new UserRepository(this);

        EditText editTextUsername = findViewById(R.id.editTextNewUsername);
        EditText editTextPassword = findViewById(R.id.editTextNewPassword);
        EditText editTextConfirm = findViewById(R.id.editTextConfirmPassword);

        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        Button buttonGoToLogin = findViewById(R.id.buttonGoToLogin);
        Button buttonBack = findViewById(R.id.buttonBack);

        buttonSignUp.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirm = editTextConfirm.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirm)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            User existing = repo.getUserByUsername(username);

            if (existing != null) {
                Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show();
                return;
            }

            repo.insertUser(new User(username, password, false));

            Toast.makeText(this, "Account created! Please log in.", Toast.LENGTH_SHORT).show();

            startActivity(LoginActivity.intentFactory(this));
            finish();
        });

        buttonGoToLogin.setOnClickListener(v -> {
            startActivity(LoginActivity.intentFactory(this));
            finish();
        });

        buttonBack.setOnClickListener(v -> finish());
    }

    public static Intent intentFactory(Context context) {
        return new Intent(context, SignUpActivity.class);
    }
}