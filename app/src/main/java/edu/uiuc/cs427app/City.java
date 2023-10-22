package edu.uiuc.cs427app;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Define this class as an entity representing the "Cities" table in the database.
 *
 * @author Sherry Li
 * @version 10/19/2023
 */
@Entity(tableName = "Cities")
public class City {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int cityID;
    public String cityName;
    public String stateOrRegionName;
    public String countryName;
    public double latitude;
    public double longitude;


    // Getter and Setter Methods

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateOrRegionName() {
        return stateOrRegionName;
    }

    public void setStateOrRegionName(String stateOrRegionName) {
        this.stateOrRegionName = stateOrRegionName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        City otherCity = (City) obj;
        return (cityID == otherCity.cityID && cityName.equals(otherCity.cityName) && stateOrRegionName.equals(otherCity.stateOrRegionName) && countryName.equals(otherCity.countryName));
    }

}
