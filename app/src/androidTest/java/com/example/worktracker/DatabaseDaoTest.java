/*package com.example.worktracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DatabaseDaoTest {

    private AppDatabase db;
    private UserDao userDao;
    private ShiftDao shiftDao;
    private WorkLogDao workLogDao;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();

        userDao = db.userDao();
        shiftDao = db.shiftDao();
        workLogDao = db.workLogDao();
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void insertUser_returnsUserFromDatabase() {
        User user = new User("testuser", "password123", false);

        userDao.insert(user);

        User result = userDao.getUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertEquals(false, result.isAdmin());
    }

    @Test
    public void insertShift_returnsShiftForUser() {
        Shift shift = new Shift(1, "2026-05-08", "9:00 AM", "5:00 PM");

        shiftDao.insert(shift);

        List<Shift> result = shiftDao.getShiftsForUser(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getUser_id());
        assertEquals("2026-05-08", result.get(0).getShift_date());
        assertEquals("9:00 AM", result.get(0).getShift_start());
        assertEquals("5:00 PM", result.get(0).getShift_end());
    }

    @Test
    public void insertWorkLog_returnsLogForUser() {
        WorkLog workLog = new WorkLog(1, "9:00 AM", "5:00 PM");

        workLogDao.insert(workLog);

        List<WorkLog> result = workLogDao.getLogsForUser(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getUser_id());
        assertEquals("9:00 AM", result.get(0).getClock_in_time());
        assertEquals("5:00 PM", result.get(0).getClock_out_time());
    }
}
 */