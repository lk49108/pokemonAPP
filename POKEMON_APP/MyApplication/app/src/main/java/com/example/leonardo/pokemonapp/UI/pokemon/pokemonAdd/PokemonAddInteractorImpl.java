package com.example.leonardo.pokemonapp.UI.pokemon.pokemonAdd;

import com.example.leonardo.pokemonapp.PermissionApp;
import com.example.leonardo.pokemonapp.database.DatabaseCreator;
import com.example.leonardo.pokemonapp.database.model.PokemonDb;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.executor.NetworkExecutor;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;

/**
 * Created by leonardo on 06/08/17.
 */

public class PokemonAddInteractorImpl implements PokemonAddMVP.Interactor {
    @Override
    public void cancel() {
        NetworkExecutor.getInstance().destroyAnyPendingTransactions();
    }

    @Override
    public void savePokemonToServer(Pokemon pokemon, CallbackInt callback) {
        NetworkExecutor.getInstance().createPokemon(pokemon, callback, PermissionApp.getPermissionApp());
    }

    @Override
    public void savePokemonToDatabase(Pokemon pokemon) {
        DatabaseCreator.getPokedexTableConnection().addPokemon(PokemonDb.fromPokemon(pokemon));
    }
}
