package com.example.leonardo.pokemonapp.UI.pokemon.fragmentHandlers;

import com.example.leonardo.pokemonapp.network.resources.Comment;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;

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

    void onShowAllCommentsClicked(Comment[] comments, String pokemonName);
}
