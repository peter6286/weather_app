package edu.uiuc.cs427app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LinkUserCityDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(LinkUserCity linkUserCity);

    @Query("SELECT c.* FROM Cities AS c JOIN LinkUsersCities AS uc ON c.cityID = uc.cityID " +
            "WHERE uc.userID = :userID")
    List<City> findCitiesByUserId(int userID);

    @Query("DELETE FROM LinkUsersCities WHERE userID = :userID AND cityID = :cityID")
    void deleteLinkUserCity(int userID, int cityID);
}
