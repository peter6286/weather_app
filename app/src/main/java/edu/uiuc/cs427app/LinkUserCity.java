package edu.uiuc.cs427app;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "LinkUsersCities", foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "userID", childColumns = "userID", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = City.class, parentColumns = "cityID", childColumns = "cityID", onDelete = ForeignKey.CASCADE)
})
public class LinkUserCity {
    public int userID;
    public int cityID;
}
