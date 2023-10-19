package edu.uiuc.cs427app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(User user);

    @Query("SELECT * FROM Users WHERE firstName = :firstName AND lastName = :lastName")
    List<User> findUsersByName(String firstName, String lastName);
}
