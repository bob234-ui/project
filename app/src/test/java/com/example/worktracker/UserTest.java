package com.example.worktracker;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    //Checks that a User is created with correct username
    @Test
    public void testUserUsername() {
        User user = new User("testuser1", "testuser1", false);
        assertEquals("testuser1", user.getUsername());
    }

    //Checks that User is correctly identified as admin
    @Test
    public void testUserIsAdmin() {
        User adminUser = new User("admin2", "admin2", true);
        assertTrue(adminUser.isAdmin());
    }

    //Checks that a normal user is not an admin
    @Test
    public void testUserIsNotAdmin() {
        User regularUser = new User("testuser1", "testuser1", false);
        assertFalse(regularUser.isAdmin());
    }

    //Checks tha User is created with correct password
    @Test
    public void testUserPassword() {
        User user = new User("testuser1", "testuser1", false);
        assertEquals("testuser1", user.getPassword());
    }
}