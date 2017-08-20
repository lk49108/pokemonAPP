package com.example.leonardo.pokemonapp.database.model.converter;

import com.example.leonardo.pokemonapp.network.resources.enumerations.Gender;
import com.raizlabs.android.dbflow.converter.TypeConverter;

/**
 * Created by leonardo on 09/08/17.
 */

public class PokemonGenderConverter extends TypeConverter<Integer, Gender> {

    @Override
    public Integer getDBValue(Gender model) {
        if(model == null) {
            return 0;
        }

        return model.getId();
    }

    @Override
    public Gender getModelValue(Integer data) {
        if(data == null) {
            data = 0;
        }
        return Gender.getFromId(data);
    }

}
