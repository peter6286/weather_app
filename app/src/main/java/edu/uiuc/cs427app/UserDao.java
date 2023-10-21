package edu.uiuc.cs427app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) interface for performing database operations related to the User entity.
 *
 * @author Sherry Li
 * @version 10/19/2023
 */
@Dao
public interface UserDao {
    /**
     * Inserts a User object into the database.
     *
     * @param user The User object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(User user);

    /**
     * Retrieves a list of users based on their user name.
     *
     * @param userName The name of the users to search for.
     * @return A list of User objects matching the specified user name criteria.
     */
    @Query("SELECT * FROM Users WHERE userName = :userName")
    List<User> findUsersByName(String userName);



    /**
     * Checks if a specific username exists in the database.
     *
     * Usage:
     * ```java
     * UserDao userDao = db.userDao();
     * int count = userDao.countUsersWithUsername("desiredUsername");
     * if(count > 0) {
     *     // Username exists
     * } else {
     *     // Username does not exist
     * }
     * ```
     *
     * @param userName The name of the user to search for.
     * @return The number of users with the specified username.
     */
    @Query("SELECT COUNT(*) FROM Users WHERE userName = :userName")
    int countUsersWithUsername(String userName);
}