package com.example.worktracker;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AllDatabaseTablesTest {

    private AppDatabase db;
    private UserDao userDao;
    private ShiftDao shiftDao;
    private WorkLogDao workLogDao;
    private OngoingShiftDao ongoingShiftDao;
    private ScheduledShiftDao scheduledShiftDao;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();

        userDao = db.userDao();
        shiftDao = db.shiftDao();
        workLogDao = db.workLogDao();
        ongoingShiftDao = db.ongoingShiftDao();
        scheduledShiftDao = db.scheduledShiftDao();
    }

    @After
    public void tearDown() {
        db.close();
    }

    // ---------------- USER TESTS ----------------

    @Test
    public void insertUserTest() {
        User user = new User("testuser", "password", false);
        userDao.insert(user);

        User result = userDao.getUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    public void updateUserTest() {
        User user = new User("testuser", "password", false);
        userDao.insert(user);

        User result = userDao.getUserByUsername("testuser");
        result.setPassword("newpassword");
        result.setAdmin(true);
        userDao.update(result);

        User updated = userDao.getUserByUsername("testuser");

        assertEquals("newpassword", updated.getPassword());
        assertTrue(updated.isAdmin());
    }

    @Test
    public void deleteUserTest() {
        User user = new User("testuser", "password", false);
        userDao.insert(user);

        User result = userDao.getUserByUsername("testuser");
        userDao.delete(result);

        User deleted = userDao.getUserByUsername("testuser");

        assertNull(deleted);
    }

    // ---------------- SHIFT TESTS ----------------

    @Test
    public void insertShiftTest() {
        Shift shift = new Shift(
                1,
                "testuser",
                "05/10/2026",
                "9:00 AM",
                "5:00 PM",
                1,
                1000,
                8000
        );

        shiftDao.insert(shift);

        assertEquals(1, shiftDao.getAllShifts().size());
    }

    @Test
    public void updateShiftTest() {
        Shift shift = new Shift(
                1,
                "testuser",
                "05/10/2026",
                "9:00 AM",
                "5:00 PM",
                1,
                1000,
                8000
        );

        shiftDao.insert(shift);

        Shift result = shiftDao.getAllShifts().get(0);

        Shift updatedShift = new Shift(
                result.getUser_id(),
                result.getUsername(),
                result.getShift_date(),
                result.getShift_start(),
                result.getShift_end(),
                3,
                3000,
                result.getTotal_shift_millis()
        );

        updatedShift.setShift_id(result.getShift_id());
        shiftDao.update(updatedShift);

        Shift updated = shiftDao.getAllShifts().get(0);

        assertEquals(3, updated.getBreak_count());
        assertEquals(3000, updated.getTotal_break_millis());
    }

    @Test
    public void deleteShiftTest() {
        Shift shift = new Shift(
                1,
                "testuser",
                "05/10/2026",
                "9:00 AM",
                "5:00 PM",
                1,
                1000,
                8000
        );

        shiftDao.insert(shift);

        Shift result = shiftDao.getAllShifts().get(0);
        shiftDao.delete(result);

        assertEquals(0, shiftDao.getAllShifts().size());
    }

    // ---------------- WORKLOG TESTS ----------------

    @Test
    public void insertWorkLogTest() {
        WorkLog workLog = new WorkLog(1, "9:00 AM", "5:00 PM");

        workLogDao.insert(workLog);

        assertEquals(1, workLogDao.getAllLogs().size());
    }

    @Test
    public void updateWorkLogTest() {
        WorkLog workLog = new WorkLog(1, "9:00 AM", "5:00 PM");

        workLogDao.insert(workLog);

        WorkLog result = workLogDao.getAllLogs().get(0);

        WorkLog updatedLog = new WorkLog(
                result.getUser_id(),
                result.getClock_in_time(),
                "6:00 PM"
        );

        updatedLog.setLog_id(result.getLog_id());
        workLogDao.update(updatedLog);

        WorkLog updated = workLogDao.getAllLogs().get(0);

        assertEquals("6:00 PM", updated.getClock_out_time());
    }

    @Test
    public void deleteWorkLogTest() {
        WorkLog workLog = new WorkLog(1, "9:00 AM", "5:00 PM");

        workLogDao.insert(workLog);

        WorkLog result = workLogDao.getAllLogs().get(0);
        workLogDao.delete(result);

        assertEquals(0, workLogDao.getAllLogs().size());
    }

    // ---------------- ONGOING SHIFT TESTS ----------------

    @Test
    public void insertOngoingShiftTest() {
        OngoingShift ongoingShift = new OngoingShift(
                "testuser",
                "05/10/2026",
                "9:00 AM",
                1000,
                0,
                0,
                false,
                0
        );

        long id = ongoingShiftDao.insert(ongoingShift);

        assertNotNull(ongoingShiftDao.getOngoingShiftById((int) id));
    }

    @Test
    public void updateOngoingShiftTest() {
        OngoingShift ongoingShift = new OngoingShift(
                "testuser",
                "05/10/2026",
                "9:00 AM",
                1000,
                0,
                0,
                false,
                0
        );

        long id = ongoingShiftDao.insert(ongoingShift);

        OngoingShift result = ongoingShiftDao.getOngoingShiftById((int) id);
        result.setBreak_active(true);
        result.setBreak_count(1);
        ongoingShiftDao.update(result);

        OngoingShift updated = ongoingShiftDao.getOngoingShiftById((int) id);

        assertTrue(updated.isBreak_active());
        assertEquals(1, updated.getBreak_count());
    }

    @Test
    public void deleteOngoingShiftTest() {
        OngoingShift ongoingShift = new OngoingShift(
                "testuser",
                "05/10/2026",
                "9:00 AM",
                1000,
                0,
                0,
                false,
                0
        );

        long id = ongoingShiftDao.insert(ongoingShift);

        OngoingShift result = ongoingShiftDao.getOngoingShiftById((int) id);
        ongoingShiftDao.delete(result);

        assertNull(ongoingShiftDao.getOngoingShiftById((int) id));
    }

    // ---------------- SCHEDULED SHIFT TESTS ----------------

    @Test
    public void insertScheduledShiftTest() {
        ScheduledShift scheduledShift = new ScheduledShift(
                "testuser",
                "05/17/2026",
                "9:00 AM",
                "5:00 PM"
        );

        scheduledShiftDao.insert(scheduledShift);

        assertEquals(1, scheduledShiftDao.getScheduledShiftsForUser("testuser").size());
    }

    @Test
    public void updateScheduledShiftTest() {
        ScheduledShift scheduledShift = new ScheduledShift(
                "testuser",
                "05/17/2026",
                "9:00 AM",
                "5:00 PM"
        );

        scheduledShiftDao.insert(scheduledShift);

        ScheduledShift result = scheduledShiftDao.getScheduledShiftsForUser("testuser").get(0);

        ScheduledShift updatedShift = new ScheduledShift(
                result.getUsername(),
                result.getShift_date(),
                "10:00 AM",
                result.getShift_end()
        );

        updatedShift.setScheduled_shift_id(result.getScheduled_shift_id());
        scheduledShiftDao.update(updatedShift);

        ScheduledShift updated = scheduledShiftDao.getScheduledShiftsForUser("testuser").get(0);

        assertEquals("10:00 AM", updated.getShift_start());
    }

    @Test
    public void deleteScheduledShiftTest() {
        ScheduledShift scheduledShift = new ScheduledShift(
                "testuser",
                "05/17/2026",
                "9:00 AM",
                "5:00 PM"
        );

        scheduledShiftDao.insert(scheduledShift);

        ScheduledShift result = scheduledShiftDao.getScheduledShiftsForUser("testuser").get(0);
        scheduledShiftDao.delete(result);

        assertEquals(0, scheduledShiftDao.getScheduledShiftsForUser("testuser").size());
    }
}