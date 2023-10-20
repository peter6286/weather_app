package edu.uiuc.cs427app;

import android.app.Application;

/**
 * Repository class responsible for interacting with the User entity and providing
 * access to database operations through the UserDao.
 *
 * @author Sherry Li
 * @version 10/19/2023
 */
public class UserCityRepository {
    private UserDao mUserDao;
    /**
     * Constructor to initialize the UserCityRepository.
     *
     * @param application The application context used to access the database.
     */
    UserCityRepository(Application application) {
        UserCityDatabase db = UserCityDatabase.getDatabase(application);
        mUserDao = db.userDao();
    }

    /**
     * Inserts a User object into the database on a background thread.
     *
     * @param user The User object to be inserted.
     */
    void insert(User user) {
        UserCityDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.insert(user);
        });
    }
}
