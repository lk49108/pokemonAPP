package com.example.leonardo.pokemonapp.database.model.converter;

import android.util.Log;

import com.example.leonardo.pokemonapp.network.resources.enumerations.Vote;
import com.raizlabs.android.dbflow.converter.TypeConverter;

/**
 * Created by leonardo on 12/08/17.
 */

public class PokemonVoteConverter extends TypeConverter<Integer, Vote> {
    @Override
    public Integer getDBValue(Vote model) {
        if(model == null) {
            return 0;
        }

        return model.getVoteId();
    }

    @Override
    public Vote getModelValue(Integer data) {
        if(data == null) {
            data = 0;
        }

        return Vote.getFromId(data);
    }
}
