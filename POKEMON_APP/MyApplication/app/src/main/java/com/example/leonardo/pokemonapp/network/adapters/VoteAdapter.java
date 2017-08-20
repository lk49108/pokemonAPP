package com.example.leonardo.pokemonapp.network.adapters;

import com.example.leonardo.pokemonapp.network.resources.enumerations.Gender;
import com.example.leonardo.pokemonapp.network.resources.enumerations.Vote;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

/**
 * Created by leonardo on 10/08/17.
 */

public class VoteAdapter {

    @FromJson
    public Vote fromJson(int json) {
        return Vote.getFromId(json);
    }

    @ToJson
    public int toJson(Vote gender) {
        return gender.getVoteId();
    }

}
