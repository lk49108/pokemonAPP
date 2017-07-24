package com.example.leonardo.pokemonapp.network.resources;

import com.squareup.moshi.Json;

import java.util.List;

import moe.banana.jsonapi2.JsonApi;

/**
 * Created by leonardo on 24/07/17.
 */

@JsonApi(type = "pokemons")
public class Pokedex {

    @Json(name = "pokemons")
    List<Pokemon> pokemonList;

    public List<Pokemon> getPokemonList() {
        return pokemonList;
    }

    public void setPokemonList(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }
}
