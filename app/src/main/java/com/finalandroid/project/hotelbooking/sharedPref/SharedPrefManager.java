package com.finalandroid.project.hotelbooking.sharedPref;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.finalandroid.project.hotelbooking.activities.SignIn;
import com.finalandroid.project.hotelbooking.modelClass.User;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "EStoreShearedPref";

    private static final String KEY_ID = "keyId";
    private static final String KEY_USERNAME = "keyUsername";
    private static final String KEY_EMAIL = "keyEmail";
    private static final String KEY_PHONE = "keyPhone";
    private static final String KEY_PASSWORD = "keyPassword";
    private static final String KEY_USER_TYPES = "keyUserTypes";

    @SuppressLint("StaticFieldLeak")
    private static SharedPrefManager mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private SharedPrefManager(Context context){

        SharedPrefManager.context = context;
    }

    public static SharedPrefManager getInstance(Context context){

        if(mInstance == null){

            synchronized(SharedPrefManager.class) {
                mInstance = new SharedPrefManager(context);
            }
        }
        return mInstance;
    }

    public void userLogin(@NonNull User user){

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_USER_TYPES, user.getUserTypes());
        editor.apply();
    }

    public User getUser(){

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return new User(
                sharedPreferences.getInt(KEY_ID, 0),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PASSWORD, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_USER_TYPES, null) );
    }

    public boolean isLoggedIN(){

        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(KEY_USERNAME, null) != null;
    }

    public void logout(){

        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.clear();
        editor.apply();

        Intent i = new Intent(context, SignIn.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void delAcc(){

        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.clear();
        editor.apply();

        Intent i = new Intent(context, SignIn.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void userUpdate(@NonNull User user) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_PHONE,user.getPhone());
        editor.putString(KEY_USER_TYPES,user.getUserTypes());
        editor.apply();
    }
}