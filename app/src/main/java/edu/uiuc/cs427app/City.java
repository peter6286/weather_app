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
}
