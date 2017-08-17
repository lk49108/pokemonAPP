package com.example.leonardo.pokemonapp.UI.pokemon.pokemonComments;

import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.network.resources.Comment;

/**
 * Created by leonardo on 11/08/17.
 */

public class PokemonCommentsState implements BaseMVP.State {

    private final String pokemonName;

    private final Comment[] comments;

    public PokemonCommentsState(Comment[] comments, String pokemonName) {
        this.comments = comments;
        this.pokemonName = pokemonName;
    }

    public Comment[] getComments() {
        return comments;
    }

    public String getPokemonName() {
        return pokemonName;
    }
}
