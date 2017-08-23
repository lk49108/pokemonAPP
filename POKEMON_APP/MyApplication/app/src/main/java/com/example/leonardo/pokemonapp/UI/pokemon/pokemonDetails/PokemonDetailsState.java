package com.example.leonardo.pokemonapp.UI.pokemon.pokemonDetails;

import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;

/**
 * Created by leonardo on 06/08/17.
 */

public class PokemonDetailsState implements BaseMVP.State {

    private final Pokemon pokemon;

    private String pendingComment;

    public String getPendingComment() {
        return pendingComment;
    }

    public PokemonDetailsState(Pokemon pokemon, String pendingComment) {
        this.pokemon = pokemon;
        this.pendingComment = pendingComment;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

}
