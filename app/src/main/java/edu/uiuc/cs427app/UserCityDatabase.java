package edu.uiuc.cs427app;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents the Room database for managing User, City, and LinkUserCity entities.
 *
 * This database class provides access to Data Access Objects (DAOs) for performing
 * database operations related to user and city data, as well as the link between users and cities.
 *
 * @author Sherry Li
 * @version 10/19/2023
 */
@Database(entities = {User.class, City.class, LinkUserCity.class}, version = 1, exportSchema = false)
public abstract class UserCityDatabase extends RoomDatabase{

    /**
     * Returns the DAO for User entity operations.
     *
     * @return The UserDao instance.
     */
    public abstract UserDao userDao();
    /**
     * Returns the DAO for City entity operations.
     *
     * @return The CityDao instance.
     */
    public abstract CityDao cityDao();
    /**
     * Returns the DAO for LinkUserCity entity operations.
     *
     * @return The LinkUserCityDao instance.
     */
    public abstract LinkUserCityDao linkUserCityDao();

    private static volatile UserCityDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Gets an instance of the UserCityDatabase.
     *
     * @param context The application context.
     * @return The UserCityDatabase instance.
     */
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
