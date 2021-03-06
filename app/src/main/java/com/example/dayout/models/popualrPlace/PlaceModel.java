package com.example.dayout.models.popualrPlace;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


import com.example.dayout.models.room.popularPlaceRoom.converters.PopularDataConverter;

import java.io.Serializable;
import java.util.List;

import static com.example.dayout.config.AppConstants.POPULAR_PLACE_TABLE;

@Entity(tableName = POPULAR_PLACE_TABLE)
public class PlaceModel implements Serializable {

    @Ignore
    public boolean success;
    @Ignore
    public String message;

    @TypeConverters(PopularDataConverter.class)
    public List<PlaceData> data;


    @PrimaryKey(autoGenerate = true)
    int modelId;

}
