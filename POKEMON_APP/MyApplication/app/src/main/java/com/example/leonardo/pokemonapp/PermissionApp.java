package com.example.leonardo.pokemonapp;

import android.app.Application;

/**
 * Created by leonardo on 14/07/17.
 */

public class PermissionApp extends Application {

    private static PermissionApp permissionApp;

    @Override
    public void onCreate() {
        super.onCreate();

        permissionApp = this;
    }

    public static PermissionApp getPermissionApp() {
        return permissionApp;
    }
}