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

public class FragmentMainActivityHandlerRegular implements FragmentMainActivityHandlerInt {

    private final PokemonMainActivity activity;

    public FragmentMainActivityHandlerRegular(PokemonMainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void initializeFragments() {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(activity.pokemonMainFragmentContainer.getId(), PokemonListFragment.newInstance(), "listFragment");
        transaction.commit();
    }

    @Override
    public void fromOtherConfigurationInitialization() {
        Fragment addPokemonFragment = activity.getSupportFragmentManager().findFragmentByTag("addPokemonFragment");
        Fragment pokemonDetailsFragment = activity.getSupportFragmentManager().findFragmentByTag("pokemonDetailsFragment");

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if(addPokemonFragment != null) {
            transaction.remove(addPokemonFragment).commit();
            activity.getSupportFragmentManager().executePendingTransactions();
            transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(activity.pokemonMainFragmentContainer.getId(), addPokemonFragment, "addPokemonFragment");
            transaction.addToBackStack("AddFragment on ListFragment");
        } else if(pokemonDetailsFragment != null){
            transaction.remove(pokemonDetailsFragment).commit();
            activity.getSupportFragmentManager().executePendingTransactions();
            transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(activity.pokemonMainFragmentContainer.getId(), pokemonDetailsFragment, "pokemonDetailsFragment");
            transaction.addToBackStack("DetailsFragment on ListFragment");
        }

        transaction.commit();
        activity.invalidateOptionsMenu();
    }

    @Override
    public void insertPokemonAddFragmentToActivity() {
        Fragment pokemonDetailsFragment = activity.getSupportFragmentManager().findFragmentByTag("pokemonDetailsFragment");
        if(pokemonDetailsFragment != null && pokemonDetailsFragment.isVisible()) {
            activity.getSupportFragmentManager().popBackStackImmediate();
        }
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(activity.pokemonMainFragmentContainer.getId(), PokemonAddFragment.newInstance(), "addPokemonFragment");
        transaction.addToBackStack("AddFragment on ListFragment");
        transaction.commit();
        activity.invalidateOptionsMenu();
    }

    @Override
    public void pokemonSelected(Pokemon pokemon) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(activity.pokemonMainFragmentContainer.getId(), PokemonDetailsFragment.newInstance(pokemon), "pokemonDetailsFragment");
        transaction.addToBackStack("DetailsFragment on ListFragment");
        transaction.commit();

        activity.invalidateOptionsMenu();
    }

    @Override
    public void pokemonAdded(Pokemon pokemon) {
        FragmentManager manager = activity.getSupportFragmentManager();
        manager.popBackStackImmediate();

        PokemonListFragment listFragment = (PokemonListFragment) manager.findFragmentByTag("listFragment");
        listFragment.addPokemon(pokemon);

        activity.invalidateOptionsMenu();
    }

    @Override
    public void pokemonCreationCanceled() {
        FragmentManager manager = activity.getSupportFragmentManager();
        manager.popBackStack();
        activity.invalidateOptionsMenu();
    }

    @Override
    public boolean onBackPressed() {
        if(activity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if(activity.onScreenAddPokemonFragment()) {
                PokemonAddFragment addPokemonFragment = (PokemonAddFragment) activity.getSupportFragmentManager().findFragmentByTag("addPokemonFragment");
                if(addPokemonFragment.shouldShowSavePokemonDialog()) {
                    addPokemonFragment.makeDialog();
                } else {
                    pokemonCreationCanceled();
                }
            } else {
                //pokemonDetailsFragment on screen
                activity.getSupportFragmentManager().popBackStackImmediate();
                activity.invalidateOptionsMenu();
            }

            return true;
        }

        return false;
    }
}
