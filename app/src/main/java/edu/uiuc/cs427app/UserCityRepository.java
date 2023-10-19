package edu.uiuc.cs427app;

import android.app.Application;

public class UserCityRepository {
    private UserDao mUserDao;
    UserCityRepository(Application application) {
        UserCityDatabase db = UserCityDatabase.getDatabase(application);
        mUserDao = db.userDao();
    }

    void insert(User user) {
        UserCityDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.insert(user);
        });
    }
}
