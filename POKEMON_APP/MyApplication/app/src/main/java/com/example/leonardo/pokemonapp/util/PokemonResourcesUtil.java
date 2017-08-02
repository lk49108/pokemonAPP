package com.example.leonardo.pokemonapp.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by leonardo on 17/07/17.
 */

public class PokemonResourcesUtil {

    public static byte[] readImageFile(Uri imageSource, Context context) {
        InputStream is = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[128];
        try {
            is = context.getContentResolver().openInputStream(imageSource);
            while (true) {
                int i = is.read(buffer);
                if(i < 1) {
                    break;
                }

                outputStream.write(buffer, 0, i);
            }
        } catch (Exception ex) {}
        finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return outputStream.toByteArray();


    }

    public static boolean imageFileExists(Uri imageSource, Context context) {

        /*ContentResolver cr = context.getContentResolver();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cur = cr.query(imageSource, projection, null, null, null);
        if(cur != null) {

            if(cur.moveToFirst()) {

                String filePath = cur.getString(0);
                Toast.makeText(context, filePath == null ? "null" : filePath, Toast.LENGTH_LONG).show();

                if((new File(filePath)).exists()) {
                    return true;
                }

            }

        }

        return false;
        */

        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(imageSource);
        } catch (NullPointerException | FileNotFoundException e) {
            return false;
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;

    }
}
