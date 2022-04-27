package com.example.dayout.config;

import com.example.dayout.R;

import java.util.regex.Pattern;

public class AppConstants {

    public final static  int AUTH_FRC = R.id.auth_fr_c;
    public final static  int MAIN_FRC = R.id.main_fr_c;
    public final static Pattern NAME_REGEX = Pattern.compile("[a-zA-Z]");
    public final static Pattern PHONE_NUMBER_REGEX = Pattern.compile("09" + "[0-9]");
}
