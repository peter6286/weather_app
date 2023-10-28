package edu.uiuc.cs427app;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Define this class as an entity representing the "Cities" table in the database.
 *
 * @author Sherry Li, Sinja Sanandan
 * @version 10/19/2023
 */
@Entity(tableName = "Cities")
public class City {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int cityID;
    private String cityName;
    private String stateOrRegionName;
    private String countryName;
    private double latitude;
    private double longitude;

    public City() {}

    /** alternative constructor for creating a city 
        @param    cityName           city name
        @param    stateOrRegionName  state or region name
        @param    countryName        country name
        @param    latitude           city latitude
        @param    longitude          city longitude
    **/
    public City(String cityName, String stateOrRegionName, String countryName, double latitude, double longitude) {
        this.cityName = cityName;
        this.stateOrRegionName = stateOrRegionName;
        this.countryName = countryName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /** get city ID**/
    public int getCityID() {
        return cityID;
    }

    /** set city ID **/
    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    /** get City Name**/
    public String getCityName() {
        return cityName;
    }

    /** set City Name **/
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /** get state or region name **/
    public String getStateOrRegionName() {
        return stateOrRegionName;
    }

    /** set state or region **/
    public void setStateOrRegionName(String stateOrRegionName) {
        this.stateOrRegionName = stateOrRegionName;
    }

    /** get country name **/
    public String getCountryName() {
        return countryName;
    }

    /** set country name **/
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /** get latitude **/
    public double getLatitude() {
        return latitude;
    }

    /** set latitude **/
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /** get longitude **/
    public double getLongitude() {
        return longitude;
    }

    /** set longitude **/
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // overriding equals() method
    private static final double EPSILON = 1e-9;
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        City otherCity = (City) obj;
        return (cityID == otherCity.cityID &&
                cityName.equals(otherCity.cityName) &&
                stateOrRegionName.equals(otherCity.stateOrRegionName) &&
                countryName.equals(otherCity.countryName) &&
                Math.abs(latitude - otherCity.latitude) < EPSILON &&
                Math.abs(longitude - otherCity.longitude) < EPSILON);
            }
}
