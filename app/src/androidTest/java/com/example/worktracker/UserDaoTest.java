package com.example.worktracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserDaoTest {

    private AppDatabase db;
    private UserDao userDao;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();

        userDao = db.userDao();
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void insertUser_getUserByUsername_returnsUser() {
        User user = new User("testuser", "password123", false);

        userDao.insert(user);

        User result = userDao.getUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertEquals(false, result.isAdmin());
    }

    @Test
    public void login_withWrongPassword_returnsNull() {
        User user = new User("testuser", "correctpass", false);

        userDao.insert(user);

        User result = userDao.login("testuser", "wrongpass");

        assertNull(result);
    }
}