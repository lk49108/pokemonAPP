package com.example.leonardo.pokemonapp.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by leonardo on 26/07/17.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "PokemonDatabase";

    public static final int VERSION = 1;
}
