package com.thirteendollars.hopener.util;

import android.content.Context;

/**
 * Created by Damian Nowakowski on 03/05/2017.
 * mail: thirteendollars.com@gmail.com
 */

public class Preferences {

    private static final String PREFERENCES_NAME = "HopenerPreferencesName";
    private static final String UUID_PREFERENCES_KEY = "UuidPreferencesKey";
    private ObscuredSharedPreferences mPreferences;

    public Preferences(Context context) {
        mPreferences = ObscuredSharedPreferences.getPrefs(context,PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public String getUuid() {
        return mPreferences.getString(UUID_PREFERENCES_KEY,null);
    }

    public boolean isUuidSet() {
        return getUuid()!=null;
    }

    public void setUuid(String uuid) {
        mPreferences.edit().putString(UUID_PREFERENCES_KEY,uuid).apply();
    }
}
