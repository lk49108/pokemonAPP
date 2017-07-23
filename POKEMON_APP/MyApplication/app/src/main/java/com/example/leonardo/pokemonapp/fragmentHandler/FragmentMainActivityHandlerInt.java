package com.example.leonardo.pokemonapp.fragmentHandler;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.leonardo.pokemonapp.Pokemon;

/**
 * Created by leonardo on 20/07/17.
 */

public interface FragmentMainActivityHandlerInt {

    void initializeFragments();

    void fromOtherConfigurationInitialization();

    void insertPokemonAddFragmentToActivity();

    void pokemonSelected(Pokemon pokemon);

    void pokemonAdded(Pokemon pokemon);

    void pokemonCreationCanceled();

    boolean onBackPressed();
}
