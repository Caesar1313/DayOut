package com.example.dayout.config;

import android.util.Patterns;

import com.example.dayout.R;
import com.example.dayout.models.Error.ErrorModel;
import com.google.gson.Gson;

import java.util.regex.Pattern;

public class AppConstants {

    public final static int AUTH_FRC = R.id.auth_fr_c;
    public final static int MAIN_FRC = R.id.main_fr_c;

    public final static Pattern NAME_REGEX = Pattern.compile("[a-zA-Z]([a-zA-Z]+| )*");
    public final static Pattern PHONE_NUMBER_REGEX = Pattern.compile("09\\d{8}");
    public final static Pattern EMAIL_REGEX = Patterns.EMAIL_ADDRESS;


    // shared preferences keys

    public static final String USER_ID = "user_id";
    public static final String ACC_TOKEN = "acc_token";
    public static final String REMEMBER_ME = "remember_me";


    // const function

    public static String getErrorMessage(String errorAsString) {
        try {
            Gson gson = new Gson();
            ErrorModel errorModel = gson.fromJson(errorAsString, ErrorModel.class);
            return errorModel.getMessage();
        } catch (Exception e) {
            return "Failed while reading the error message";
        }

    }

}
