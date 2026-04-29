package com.example.worktracker;

import android.content.Context;

public class UserRepository {

    private final UserDao userDao;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        userDao = db.userDao();
        seedUsers();
    }

    public void seedUsers() {
        if (userDao.getUserCount() == 0) {
            userDao.insert(new User("testuser1", "testuser1", false));
            userDao.insert(new User("admin2", "admin2", true));
        }
    }

    public User login(String username, String password) {
        return userDao.login(username, password);
    }

    public void insertUser(User user) {
        userDao.insert(user);
    }

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }
}