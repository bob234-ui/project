package com.example.worktracker;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class IntentTests {

    // Test 1: Check MainActivity intent is created correctly
    @Test
    public void testMainActivityIntent() {
        Intent intent = MainActivity.intentFactory(
                ApplicationProvider.getApplicationContext());
        assertEquals("com.example.worktracker.MainActivity",
                intent.getComponent().getClassName());
    }

    // Test 2: Check LoginActivity intent is created correctly
    @Test
    public void testLoginActivityIntent() {
        Intent intent = LoginActivity.intentFactory(
                ApplicationProvider.getApplicationContext());
        assertEquals("com.example.worktracker.LoginActivity",
                intent.getComponent().getClassName());
    }

    // Test 3: Check SignUpActivity intent is created correctly
    @Test
    public void testSignUpActivityIntent() {
        Intent intent = SignUpActivity.intentFactory(
                ApplicationProvider.getApplicationContext());
        assertEquals("com.example.worktracker.SignUpActivity",
                intent.getComponent().getClassName());
    }

    // Test 4: Check LandingActivity intent is created correctly
    @Test
    public void testLandingActivityIntent() {
        Intent intent = LandingActivity.intentFactory(
                ApplicationProvider.getApplicationContext());
        assertEquals("com.example.worktracker.LandingActivity",
                intent.getComponent().getClassName());
    }
}