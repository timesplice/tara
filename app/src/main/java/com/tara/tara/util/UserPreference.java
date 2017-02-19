package com.tara.tara.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.tara.tara.model.UserModel;

/**
 * Created by M1032467 on 9/1/2016.
 */

public class UserPreference {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;
    // Shared preferences file name
    private static final String ID = "id";
    private static final String PREF_NAME = "user_preference";
    public static final String USERNAME = "userName";
    public static final String EMAIL = "emailAddress";
    public static final String USERID = "userid";


    public UserPreference(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setUserInPreference(String id, String username, String email) {
        editor.putString(EMAIL, email);
        editor.putString(USERNAME, username);
        editor.putString(USERID, id);
        editor.commit();
    }

    public boolean isUserPresent() {
        String email = pref.getString(EMAIL, null);
        if (email == null)
            return false;
        return true;
    }

    public UserModel getUserDetails() {
        UserModel user = new UserModel();
        user.setUserId(pref.getString(USERID, "unknownId"));
        user.setName(pref.getString(USERNAME, "username"));
        user.setEmail(pref.getString(EMAIL, "Email"));

        return user;
    }

    public void clearPreference() {
        editor.clear();
        editor.commit();
    }
}
