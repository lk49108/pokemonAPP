package com.example.leonardo.pokemonapp.network.adapters;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.leonardo.pokemonapp.network.executor.ServiceCreator;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

/**
 * Created by leonardo on 25/07/17.
 */

public class UriAdapter {

    @Nullable
    @FromJson
    public Uri fromJson(String json) {
        if(json != null) {
            if(json.equals("null")) {
                return null;
            }

            return Uri.parse(ServiceCreator.API_ENDPOINT + json);
        }

        return null;
    }

    @ToJson
    public String toJson(Uri uri) {
        if(uri == null) {
            return "null";
        }

        return uri.toString();
    }

}
