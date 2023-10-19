package edu.uiuc.cs427app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CityDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(City city);

    @Query("SELECT * FROM Cities WHERE cityName = :cityName " +
            "AND stateOrRegionName = :stateOrRegionName AND countryName = :countryName")
    List<City> findCitiesByName(String cityName, String stateOrRegionName, String countryName);
}
