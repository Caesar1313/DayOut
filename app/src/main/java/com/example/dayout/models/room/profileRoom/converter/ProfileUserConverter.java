package com.example.dayout.models.room.profileRoom.converter;

import androidx.room.TypeConverter;

import com.example.dayout.models.profile.organizer.ProfileUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class ProfileUserConverter implements Serializable {
    @TypeConverter
    public String fromProfile(ProfileUser profileUser) {

        if (profileUser == null)
            return null;

        Type type = new TypeToken<ProfileUser>() {
        }.getType();
        Gson gson = new Gson();

        return gson.toJson(profileUser, type);
    }


    @TypeConverter
    public ProfileUser toProfile(String data) {


        if (data == null)
            return null;

        Type type = new TypeToken<List<ProfileUser>>() {
        }.getType();
        Gson gson = new Gson();

        return gson.fromJson(data, type);

    }
}
