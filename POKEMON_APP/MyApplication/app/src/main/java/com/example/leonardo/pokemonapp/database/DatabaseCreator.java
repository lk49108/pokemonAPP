package com.example.leonardo.pokemonapp.database;

/**
 * Created by leonardo on 26/07/17.
 */

public class DatabaseCreator {

    public static SQLitePokedex getPokedexTableConnection() {
        return new PokedexImpl();
    }

}
