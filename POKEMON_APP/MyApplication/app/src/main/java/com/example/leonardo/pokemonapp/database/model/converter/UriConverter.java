package com.example.leonardo.pokemonapp.database.model.converter;

import android.net.Uri;

import com.raizlabs.android.dbflow.converter.TypeConverter;

/**
 * Created by leonardo on 26/07/17.
 */

public class UriConverter extends TypeConverter<String, Uri> {


    @Override
    public String getDBValue(Uri model) {
        return model == null ? null : model.toString();
    }

    @Override
    public Uri getModelValue(String data) {
        return data == null ? null : Uri.parse(data);
    }
}
