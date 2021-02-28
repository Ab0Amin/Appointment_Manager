package com.example.appointmentmanager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class MyPrefernce {


    private static MyPrefernce myPrefernce;
    private SharedPreferences sharedPreferences;

    public static MyPrefernce getInstance(Context context) {
        if (myPrefernce == null) {
            myPrefernce = new MyPrefernce(context);
        }
        return myPrefernce;
    }

    private MyPrefernce(Context context) {
        sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
    }

    public void storeData(String key ,String str_Name) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, str_Name);
        prefsEditor.apply();


    }

    public Set<String> getSetStringData(String key) {
        return sharedPreferences.getStringSet(key,new HashSet<>());
    }
    public String getStringData(String key) {
        return sharedPreferences.getString(key,"");
    }

    public void storeSetData(String key , Set<String> str_Name) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putStringSet(key, str_Name);
        prefsEditor.apply();


    }



}