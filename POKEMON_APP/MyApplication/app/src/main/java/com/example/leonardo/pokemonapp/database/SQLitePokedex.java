package com.example.leonardo.pokemonapp.database;

import com.example.leonardo.pokemonapp.database.model.PokemonDb;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;

import java.util.List;

/**
 * Created by leonardo on 26/07/17.
 */

public interface SQLitePokedex {

    List<PokemonDb> getPokemons();

    void savePokemons(PokemonDb[] pokemons);

    void addPokemon(PokemonDb pokemonDb);

    void updatePokemon(PokemonDb pokemonDb);

    void removePokemon(PokemonDb pokemonDb);

    void clearPokemonTable();
}