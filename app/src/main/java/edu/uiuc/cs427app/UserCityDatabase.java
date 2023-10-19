package edu.uiuc.cs427app;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, City.class}, version = 1, exportSchema = false)
public abstract class UserCityDatabase extends RoomDatabase{

    public abstract UserDao userDao();
    public abstract CityDao cityDao();

    private static volatile UserCityDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static UserCityDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserCityDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    UserCityDatabase.class, "user_city_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
