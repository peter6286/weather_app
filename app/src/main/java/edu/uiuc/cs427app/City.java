package edu.uiuc.cs427app;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
