package com.example.dayout.models.room.pollsRoom.converters;

import androidx.room.TypeConverter;

import com.example.dayout.models.poll.PollsPaginationData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class PollsPaginationDataConverter implements Serializable {

    @TypeConverter
    public String fromPoll(PollsPaginationData pollData) {

        if (pollData == null)
            return null;

        Type type = new TypeToken<PollsPaginationData>() {
        }.getType();
        Gson gson = new Gson();

        return gson.toJson(pollData, type);
    }


    @TypeConverter
    public PollsPaginationData toPoll(String data) {


        if (data == null)
            return null;

        Type type = new TypeToken<List<PollsPaginationData>>() {
        }.getType();
        Gson gson = new Gson();

        return gson.fromJson(data, type);

    }

}
