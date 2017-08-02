package com.example.leonardo.pokemonapp;

import android.app.Application;
import android.support.v4.app.FragmentManager;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by leonardo on 14/07/17.
 */

public class PermissionApp extends Application {

    private static PermissionApp permissionApp;

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(new FlowConfig.Builder(this).build());
        permissionApp = this;
    }

    public static PermissionApp getPermissionApp() {
        return permissionApp;
    }

}