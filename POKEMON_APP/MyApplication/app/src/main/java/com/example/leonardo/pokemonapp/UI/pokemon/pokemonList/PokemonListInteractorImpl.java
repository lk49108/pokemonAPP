package com.example.leonardo.pokemonapp.UI.pokemon.pokemonList;

import android.util.Log;

import com.example.leonardo.pokemonapp.database.DatabaseCreator;
import com.example.leonardo.pokemonapp.database.model.PokemonDb;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.executor.NetworkExecutor;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;

import java.util.Collections;
import java.util.List;

/**
 * Created by leonardo on 05/08/17.
 */

public class PokemonListInteractorImpl implements PokemonListMVP.Interactor {
    @Override
    public void cancel() {
        NetworkExecutor.getInstance().destroyAnyPendingTransactions();
    }

    @Override
    public void downloadPokemonsFromServer(CallbackInt callbackInt) {
        NetworkExecutor.getInstance().getAllPokemons(callbackInt);
    }

    @Override
    public void savePokemonsToDatabase(List<Pokemon> pokemons) {
        DatabaseCreator.getPokedexTableConnection().clearPokemonTable();

        PokemonDb[] pokemonDbs = PokemonDb.fromPokemonList(pokemons);
        DatabaseCreator.getPokedexTableConnection().savePokemons(pokemonDbs);
    }

    @Override
    public List<Pokemon> getPokemonsFromDatabase() {
        List<Pokemon> pokemonList = Pokemon.fromPokemonDbListlToList(DatabaseCreator.getPokedexTableConnection().getPokemons());
        Collections.sort(pokemonList);

        return pokemonList;
    }
}
