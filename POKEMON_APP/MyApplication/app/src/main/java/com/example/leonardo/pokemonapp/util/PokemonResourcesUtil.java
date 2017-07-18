package com.example.leonardo.pokemonapp.util;

import android.content.Context;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by leonardo on 17/07/17.
 */

public class PokemonResourcesUtil {

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
