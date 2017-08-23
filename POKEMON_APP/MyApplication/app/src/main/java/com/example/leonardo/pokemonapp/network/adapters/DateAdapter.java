package com.example.leonardo.pokemonapp.network.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by leonardo on 10/08/17.
 */

public class DateAdapter {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Nullable
    @FromJson
    public Date fromJson(String json) throws IOException {
        if (!TextUtils.isEmpty(json)) {
            try {
                return formatter.parse(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @ToJson
    public String toJson(Date date) throws IOException {

        if (date != null) {
            return date.toString();
        } else {
            return "";
        }

    }



}
