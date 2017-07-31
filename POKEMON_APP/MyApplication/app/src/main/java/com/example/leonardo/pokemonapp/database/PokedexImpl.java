package com.example.leonardo.pokemonapp.database;

import com.example.leonardo.pokemonapp.database.model.PokemonDb;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * Created by leonardo on 26/07/17.
 */

public class PokedexImpl implements SQLitePokedex {
    @Override
    public List<PokemonDb> getPokemons() {
        return SQLite.select().from(PokemonDb.class).flowQueryList();
    }

    @Override
    public void savePokemons(PokemonDb[] pokemons) {
        for(int i = 0; i < pokemons.length; i++) {
            pokemons[i].save();
        }
    }

    @Override
    public void addPokemon(PokemonDb pokemonDb) {
        pokemonDb.save();
    }

    @Override
    public void updatePokemon(PokemonDb pokemonDb) {
        pokemonDb.update();
    }

    @Override
    public void removePokemon(PokemonDb pokemonDb) {
        pokemonDb.delete();
    }

    @Override
    public void clearPokemonTable() {
        List<PokemonDb> pokemonsInDatabase = SQLite.select().from(PokemonDb.class).flowQueryList();
        for(int i = 0; i < pokemonsInDatabase.size(); i++) {
            pokemonsInDatabase.get(i).delete();
        }
    }

}
