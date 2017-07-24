package com.example.leonardo.pokemonapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.leonardo.pokemonapp.PermissionApp;
import com.example.leonardo.pokemonapp.network.resources.User;

import java.util.regex.Pattern;

/**
 * Created by leonardo on 23/07/17.
 */

public class UserUtil {

    private static final String EMAIL_REGEX = ".+\\@.+\\..+";

    public static boolean loggedIn() {
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

    private static String getToken() {
        SharedPreferences preferences = PermissionApp.getPermissionApp().getSharedPreferences("user", Context.MODE_PRIVATE);
        return preferences.getString("token", null);
    }

    private static String getEmail() {
        SharedPreferences preferences = PermissionApp.getPermissionApp().getSharedPreferences("user", Context.MODE_PRIVATE);
        return preferences.getString("email", null);
    }

    private static String getUsername() {
        SharedPreferences preferences = PermissionApp.getPermissionApp().getSharedPreferences("user", Context.MODE_PRIVATE);
        return preferences.getString("username", null);
    }

    public static void logOutUser() {
        if(!loggedIn()) {
            return;
        }
        SharedPreferences preferences = PermissionApp.getPermissionApp().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("token").remove("email").remove("username").apply();
    }

    public static boolean validEmail(String email) {
        Pattern p = Pattern.compile(EMAIL_REGEX);
        return p.matcher(email).matches();
    }

    public static boolean internetConnectionActive() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) PermissionApp.getPermissionApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void logInUser(User user) {
        if(user == null) {
            throw new IllegalArgumentException("Tried to log in a null reference user.");
        }

        final String userAuthToken = user.getAuthanticationToken();
        final String userEmail = user.getEmail();
        final String userUsername = user.getUserName();

        if(userAuthToken == null || userEmail == null || userUsername == null) {
            throw new IllegalArgumentException("User that should be logged in has to have fields token, email, username different from null value");
        }

        SharedPreferences preferences = PermissionApp.getPermissionApp().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", userAuthToken);
        editor.putString("email", userEmail);
        editor.putString("username", userUsername);
        editor.apply();
    }

    public static User getLoggedInUser() {
        SharedPreferences preferences = PermissionApp.getPermissionApp().getSharedPreferences("user", Context.MODE_PRIVATE);

        final String username = preferences.getString("username", null);
        final String email = preferences.getString("email", null);
        final String token = preferences.getString("token", null);

        User loggedInUser = new User();
        loggedInUser.setEmail(email);
        loggedInUser.setUserName(username);
        loggedInUser.setAuthanticationToken(token);

        return loggedInUser;
    }
}
