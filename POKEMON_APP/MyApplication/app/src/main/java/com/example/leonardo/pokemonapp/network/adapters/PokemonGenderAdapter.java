package com.example.leonardo.pokemonapp.network.adapters;


import com.example.leonardo.pokemonapp.network.resources.enumerations.Gender;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

/**
 * Created by leonardo on 09/08/17.
 */

public class PokemonGenderAdapter {

    @FromJson
    public Gender fromJson(String json) {
        if(json == null) {
            return Gender.UNKNOWN;
        }

        switch(json) {
            case "Male":
                return Gender.MALE;
            case "Female":
                return Gender.FEMALE;
            default:
                return Gender.UNKNOWN;
        }
    }

    @ToJson
    public String toJson(Gender gender) {
        if(gender == null) {
            return "Unknown";
        }

        return gender.getName();
    }

}
