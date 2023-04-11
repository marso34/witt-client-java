package com.example.healthappttt.Data;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

public class PreferenceHelper
{
    private final String INTRO = "intro";
    private final String NAME = "name";
    private final String HOBBY = "hobby";
    private SharedPreferences app_prefs;
    private Context context;

    //getsh

    public PreferenceHelper(Context context)
    {
        File dir = new File(getSharedPreferencesPath(context), "mydir1");
        if (!dir.exists()) {
            dir.mkdir();
        }

        File app_prefs = new File(getSharedPreferencesPath(context),"mydir2");
        this.context = context;
    }

    private String getSharedPreferencesPath(Context context) {
        return context.getFilesDir().getParentFile().getPath() + "/shared_prefs";
    }


    public void putIsLogin(boolean loginOrOut)
    {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(INTRO, loginOrOut);
        edit.apply();
    }

    public void putName(String loginOrOut)
    {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(NAME, loginOrOut);
        edit.apply();
    }

    public String getName()
    {
        return app_prefs.getString(NAME, "");
    }

    public void putHobby(String loginOrOut)
    {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(HOBBY, loginOrOut);
        edit.apply();
    }

    public String getHobby()
    {
        return app_prefs.getString(HOBBY, "");
    }
}
