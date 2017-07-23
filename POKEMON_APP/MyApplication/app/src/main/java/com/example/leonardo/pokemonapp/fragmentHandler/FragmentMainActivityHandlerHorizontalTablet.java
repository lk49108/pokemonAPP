package com.example.leonardo.pokemonapp.fragmentHandler;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.leonardo.pokemonapp.Pokemon;
import com.example.leonardo.pokemonapp.PokemonAddFragment;
import com.example.leonardo.pokemonapp.PokemonDetailsFragment;
import com.example.leonardo.pokemonapp.PokemonListFragment;
import com.example.leonardo.pokemonapp.PokemonMainActivity;

/**
 * Created by leonardo on 21/07/17.
 */

public class FragmentMainActivityHandlerHorizontalTablet implements FragmentMainActivityHandlerInt {

    private final PokemonMainActivity activity;

    public FragmentMainActivityHandlerHorizontalTablet(PokemonMainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void initializeFragments() {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(activity.pokemonMainFragmentContainer.getId(), PokemonListFragment.newInstance(), "listFragment");
        transaction.add(activity.pokemonAddDetailFragmentContainer.getId(), PokemonAddFragment.newInstance(), "addPokemonFragment");
        transaction.commit();
    }

    @Override
    public void fromOtherConfigurationInitialization() {
        Fragment addPokemonFragment = activity.getSupportFragmentManager().findFragmentByTag("addPokemonFragment");
        Fragment pokemonDetailsFragment = activity.getSupportFragmentManager().findFragmentByTag("pokemonDetailsFragment");

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if(activity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            activity.getSupportFragmentManager().popBackStackImmediate();
            if(addPokemonFragment != null) {
                transaction.replace(activity.pokemonAddDetailFragmentContainer.getId(), addPokemonFragment, "addPokemonFragment");
            } else {
                transaction.replace(activity.pokemonAddDetailFragmentContainer.getId(), pokemonDetailsFragment, "pokemonDetailsFragment");
            }
        } else {
            transaction.replace(activity.pokemonAddDetailFragmentContainer.getId(), PokemonAddFragment.newInstance(), "addPokemonFragment");
        }
        transaction.commit();

        activity.invalidateOptionsMenu();
    }

    @Override
    public void insertPokemonAddFragmentToActivity() {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(activity.pokemonAddDetailFragmentContainer.getId(), PokemonAddFragment.newInstance(), "addPokemonFragment");
        transaction.commit();
        activity.invalidateOptionsMenu();
    }

    @Override
    public void pokemonSelected(Pokemon pokemon) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(activity.pokemonAddDetailFragmentContainer.getId(), PokemonDetailsFragment.newInstance(pokemon), "pokemonDetailsFragment");
        transaction.commit();
        activity.invalidateOptionsMenu();
    }

    @Override
    public void pokemonAdded(Pokemon pokemon) {
        FragmentManager manager = activity.getSupportFragmentManager();
        PokemonListFragment listFragment = (PokemonListFragment) manager.findFragmentByTag("listFragment");
        listFragment.addPokemon(pokemon);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(activity.pokemonAddDetailFragmentContainer.getId(), PokemonDetailsFragment.newInstance(pokemon), "pokemonDetailsFragment");
        transaction.commit();

        activity.invalidateOptionsMenu();
    }

    @Override
    public void pokemonCreationCanceled() {
        FragmentManager manager = activity.getSupportFragmentManager();
        PokemonAddFragment pokemonAddFragment = (PokemonAddFragment) manager.findFragmentByTag("addPokemonFragment");
        pokemonAddFragment.resetLayoutState();
        activity.invalidateOptionsMenu();
    }

    @Override
    public boolean onBackPressed() {
        if(activity.onScreenAddPokemonFragment()) {
            PokemonAddFragment addPokemonFragment = (PokemonAddFragment) activity.getSupportFragmentManager().findFragmentByTag("addPokemonFragment");
            if(addPokemonFragment.shouldShowSavePokemonDialog()) {
                addPokemonFragment.makeDialog();
                return true;
            }
        }

        return false;
    }

}