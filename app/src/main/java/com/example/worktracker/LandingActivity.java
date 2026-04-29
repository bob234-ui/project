//Landing page activity
package com.example.worktracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class LandingActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        SharedPreferences pref = getSharedPreferences("WorkTrackerPref", MODE_PRIVATE);
        String username = pref.getString("username", "User");
        boolean isAdmin = pref.getBoolean("isAdmin", false);
        TextView textViewUsername = findViewById(R.id.textViewUsername);
        Button buttonAdminOnly = findViewById(R.id.buttonAdminOnly);
        Button buttonLogout = findViewById(R.id.buttonLogout);        textViewUsername.setText("Welcome, " + username);

        //admin button only for admins
        if (isAdmin){
            buttonAdminOnly.setVisibility(View.VISIBLE);
        } else {
            buttonAdminOnly.setVisibility(View.INVISIBLE);
        }
        //logout button
        buttonLogout.setOnClickListener( v -> {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(LandingActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

    }

    public static Intent intentFactory(Context context){
        return new Intent(context, LandingActivity.class);

    }
}
