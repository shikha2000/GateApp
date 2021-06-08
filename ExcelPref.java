package com.example.excelschool.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class ExcelPref {
    private SharedPreferences sharedPreferencesef;
    private SharedPreferences.Editor editor;
    private Context context;
    // shared pref mode
    private int PRIVATE_MODE = 0;
    // Shared preferences file name
    private static final String PREF_NAME = "excel";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String MOBILE = "mobile";
    private static final String OTP = "otp";
    private static final String USER_NAME = "user";

    public ExcelPref(Context context) {
        this.context = context;
        sharedPreferencesef = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferencesef.edit();
    }
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public boolean isFirstTimeLaunch() {
        return sharedPreferencesef.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
    public static void saveMobile(String mob){

    }
    public String getMobile(){
        return sharedPreferencesef.getString(MOBILE,null);
    }
    public void saveUserName(String name){
        editor.putString(USER_NAME,name);
        editor.commit();
    }
    public String getUserName(){
        return sharedPreferencesef.getString(USER_NAME,null);
    }

}
