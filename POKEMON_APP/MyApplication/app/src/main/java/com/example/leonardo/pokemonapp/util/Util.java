package com.example.leonardo.pokemonapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.leonardo.pokemonapp.PermissionApp;

/**
 * Created by leonardo on 05/08/17.
 */

public class Util {

    public static boolean internetConnectionActive() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) PermissionApp.getPermissionApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
