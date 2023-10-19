package edu.uiuc.cs427app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface CityDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(City city);
}
