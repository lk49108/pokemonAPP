package com.example.leonardo.pokemonapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.leonardo.pokemonapp.PermissionApp;
import com.example.leonardo.pokemonapp.network.resources.User;
import com.example.leonardo.pokemonapp.sharedPref.SimpleSharedPrefsImpl;

import java.util.regex.Pattern;

/**
 * Created by leonardo on 23/07/17.
 */

public class UserUtil {

    private static final String EMAIL_REGEX = ".+\\@[^\\+]+\\.[^\\+]+";

    public static boolean isLoggedIn() {
        return SimpleSharedPrefsImpl.getInstance().isLoggedIn();
    }

    public static void logOutUser() {
        if(!isLoggedIn()) {
            return;
        }

        SimpleSharedPrefsImpl.getInstance().logOutUser();
    }

    public static boolean validEmail(String email) {
        Pattern p = Pattern.compile(EMAIL_REGEX);
        return p.matcher(email).matches();
    }

    public static void logInUser(User user) {
        if(user == null) {
            throw new IllegalArgumentException("Tried to log in a null reference user.");
        }
        final String userAuthToken = user.getAuthanticationToken();
        final String userEmail = user.getEmail();
        final String username = user.getUserName();

        if(userAuthToken == null || userEmail == null || username == null) {
            throw new IllegalArgumentException("User that should be logged in has to have fields token, email, username different from null value");
        }

        SimpleSharedPrefsImpl.getInstance().logInUser(userEmail, userAuthToken, username);
    }

    public static User getLoggedInUser() {
        if(!isLoggedIn()) {
            return null;
        }

        return SimpleSharedPrefsImpl.getInstance().getLoggedInUser();
    }
}