package edu.uiuc.cs427app;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "LinkUsersCities", foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "userID", childColumns = "userID", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = City.class, parentColumns = "cityID", childColumns = "cityID", onDelete = ForeignKey.CASCADE)
}, indices = {@Index(value = {"userID"})})
public class LinkUserCity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    public int userID;
    public int cityID;
}
