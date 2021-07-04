package com.example.goal;

import android.content.SharedPreferences;

public class HandleSharedPreferences {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static String LOGIN_KEY = "LOGIN";

    public HandleSharedPreferences(SharedPreferences preferences){
        this.preferences = preferences;
        this.editor = preferences.edit();
    }

    public boolean existLogin(){
        return preferences.getBoolean(LOGIN_KEY, false);
    }

    public void setLogin(){
        editor.putBoolean(LOGIN_KEY, true);
        editor.apply();
    }

}
