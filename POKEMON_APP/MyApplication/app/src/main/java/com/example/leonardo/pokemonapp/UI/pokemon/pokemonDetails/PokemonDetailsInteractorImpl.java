package com.example.leonardo.pokemonapp.UI.pokemon.pokemonDetails;

import android.util.Log;

import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.executor.NetworkExecutor;
import com.example.leonardo.pokemonapp.network.resources.Comment;
import com.example.leonardo.pokemonapp.network.resources.User;

/**
 * Created by leonardo on 06/08/17.
 */

public class PokemonDetailsInteractorImpl implements PokemonDetailsMVP.Interactor {

    private int numOfReturnedAuthors;

    @Override
    public void cancel() {
        NetworkExecutor.getInstance().destroyAnyPendingTransactions();
    }

    @Override
    public void upVotePokemon(int pokemonId, CallbackInt callback) {
        NetworkExecutor.getInstance().upVotePokemon(pokemonId, callback);
    }

    @Override
    public void downVotePokemon(int pokemonId, CallbackInt callback) {
        NetworkExecutor.getInstance().downVotePokemon(pokemonId, callback);
    }

    @Override
    public void getComments(int pokemonId, CallbackInt callback) {
        NetworkExecutor.getInstance().getCommentsForPokemon(pokemonId, callback);
    }

    @Override
    public void commentPokemon(int id, Comment comment, CallbackInt callbackInt) {
        NetworkExecutor.getInstance().postPokemonComment(id, comment, callbackInt);
    }

}
