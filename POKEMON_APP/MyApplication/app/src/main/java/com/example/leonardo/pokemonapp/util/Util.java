package com.example.leonardo.pokemonapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import com.example.leonardo.pokemonapp.PermissionApp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_pokemon.jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File applicationDirectory = new File(storageDir, "/pokemonApp");
        if(!applicationDirectory.exists()) {
            applicationDirectory.mkdir();
        }

        File image = new File(applicationDirectory, imageFileName);

        return image;
    }

    public static boolean storageMounted() {
        String storageState = Environment.getExternalStorageState();
        if(!Environment.MEDIA_MOUNTED.equals(storageState)) {
            return false;
        }

        return true;
    }

}
