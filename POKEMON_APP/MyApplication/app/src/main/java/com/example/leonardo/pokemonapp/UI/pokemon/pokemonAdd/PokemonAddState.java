package com.example.leonardo.pokemonapp.UI.pokemon.pokemonAdd;

import android.net.Uri;

import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.network.resources.Move;
import com.example.leonardo.pokemonapp.network.resources.Type;
import com.example.leonardo.pokemonapp.network.resources.enumerations.Gender;

/**
 * Created by leonardo on 06/08/17.
 */

public class PokemonAddState implements BaseMVP.State {

    private final String name;

    private final String description;

    private final Type[] types;

    private final String height;

    private final String weight;

    private final Move[] moves;

    private final Gender gender;

    private final String imageUri;

    public PokemonAddState(String name, String description, Type[] types, String height, String weight, Move[] moves, Gender gender, String imageUri) {
        this.name = name;
        this.description = description;
        this.types = types;
        this.height = height;
        this.weight = weight;
        this.moves = moves;
        this.gender = gender;
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public Type[] getTypes() {
        return types;
    }

    public Move[] getMoves() {
        return moves;
    }

    public Gender getGender() {
        return gender;
    }

    public String getImageUri() {
        return imageUri;
    }
}
