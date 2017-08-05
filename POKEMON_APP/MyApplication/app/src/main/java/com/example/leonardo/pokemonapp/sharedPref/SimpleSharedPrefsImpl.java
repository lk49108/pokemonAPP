package com.example.leonardo.pokemonapp.sharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.leonardo.pokemonapp.PermissionApp;
import com.example.leonardo.pokemonapp.network.resources.User;

/**
 * Created by leonardo on 05/08/17.
 */

public class SimpleSharedPrefsImpl implements SharedPreferencesInt {

    private static SimpleSharedPrefsImpl instance;

    private final Context context;

    private SimpleSharedPrefsImpl(Context context) {
        this.context = context;
    }

    public static SharedPreferencesInt getInstance() {
        if(instance == null) {
            instance = new SimpleSharedPrefsImpl(PermissionApp.getPermissionApp());
        }

        return instance;
    }

    @Override
    public String getEmail() {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getString(SHARED_PREFS_EMAIL_KEY, null);
    }

    @Override
    public String getUsername() {
        SharedPreferences preferences = PermissionApp.getPermissionApp().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getString(SHARED_PREFS_USERNAME_KEY, null);    }

    @Override
    public String getToken() {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getString(SHARED_PREFS_TOKEN_KEY, null);
    }

    @Override
    public void saveUser(User user) {

    }

    @Override
    public boolean isLoggedIn() {
        if(getToken() == null) {
            return false;
        }
        if(getEmail() == null) {
            return false;
        }
        if(getUsername() == null) {
            return false;
        }

        return true;
    }

    @Override
    public void logOutUser(){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(SHARED_PREFS_TOKEN_KEY).remove(SHARED_PREFS_EMAIL_KEY).remove(SHARED_PREFS_USERNAME_KEY).apply();
    }

    @Override
    public void logInUser(String email, String authToken, String username) {

        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SHARED_PREFS_TOKEN_KEY, authToken);
        editor.putString(SHARED_PREFS_EMAIL_KEY, email);
        editor.putString(SHARED_PREFS_USERNAME_KEY, username);
        editor.apply();

    }

    @Override
    public User getLoggedInUser() {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        final String username = preferences.getString(SHARED_PREFS_USERNAME_KEY, null);
        final String email = preferences.getString(SHARED_PREFS_EMAIL_KEY, null);
        final String token = preferences.getString(SHARED_PREFS_TOKEN_KEY, null);

        User loggedInUser = new User();
        loggedInUser.setEmail(email);
        loggedInUser.setUserName(username);
        loggedInUser.setAuthanticationToken(token);

        return loggedInUser;
    }

}
