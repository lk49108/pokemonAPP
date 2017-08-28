package com.example.leonardo.pokemonapp.UI.pokemon.fragmentHandlers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.leonardo.pokemonapp.UI.pokemon.pokemonComments.PokemonCommentsFragment;
import com.example.leonardo.pokemonapp.network.resources.Comment;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.UI.pokemon.pokemonAdd.PokemonAddFragment;
import com.example.leonardo.pokemonapp.UI.pokemon.pokemonDetails.PokemonDetailsFragment;
import com.example.leonardo.pokemonapp.UI.pokemon.pokemonList.PokemonListFragment;
import com.example.leonardo.pokemonapp.UI.pokemon.PokemonMainActivity;

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
        transaction.replace(activity.pokemonMainFragmentContainer.getId(), PokemonListFragment.newInstance(), "listFragment");
        transaction.commit();
    }

    @Override
    public void fromOtherConfigurationInitialization() {
        PokemonAddFragment addPokemonFragment = (PokemonAddFragment) activity.getSupportFragmentManager().findFragmentByTag("addPokemonFragment");
        Fragment pokemonDetailsFragment = activity.getSupportFragmentManager().findFragmentByTag("pokemonDetailsFragment");
        Fragment pokemonCommentsFragment = activity.getSupportFragmentManager().findFragmentByTag("pokemonCommentsFragment");

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if(addPokemonFragment != null) {
            if(addPokemonFragment.shouldShowSavePokemonDialog()) {
                transaction.remove(addPokemonFragment).commit();
                activity.getSupportFragmentManager().executePendingTransactions();
                transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(activity.pokemonMainFragmentContainer.getId(), addPokemonFragment, "addPokemonFragment");
                transaction.addToBackStack("AddFragment on ListFragment");
                activity.switchHomeUpButton(false);
            } else {
                transaction.remove(addPokemonFragment);
                activity.switchHomeUpButton(true);
            }
        } else if(pokemonCommentsFragment != null) {
            activity.getSupportFragmentManager().popBackStackImmediate();
            transaction.remove(pokemonDetailsFragment).commit();
            activity.getSupportFragmentManager().executePendingTransactions();
            transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(activity.pokemonMainFragmentContainer.getId(), pokemonDetailsFragment, "pokemonDetailsFragment");
            transaction.addToBackStack("DetailsFragment on ListFragment");
            transaction.commit();
            activity.getSupportFragmentManager().executePendingTransactions();
            transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(activity.pokemonMainFragmentContainer.getId(), pokemonCommentsFragment, "pokemonCommentsFragment");
            transaction.addToBackStack("CommentsFragment on DetailsFragment");
            activity.switchHomeUpButton(false);
        } else if(pokemonDetailsFragment != null){
            transaction.remove(pokemonDetailsFragment).commit();
            activity.getSupportFragmentManager().executePendingTransactions();
            transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(activity.pokemonMainFragmentContainer.getId(), pokemonDetailsFragment, "pokemonDetailsFragment");
            transaction.addToBackStack("DetailsFragment on ListFragment");
            activity.switchHomeUpButton(false);
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

        activity.switchHomeUpButton(false);
        activity.invalidateOptionsMenu();
    }

    @Override
    public void pokemonSelected(Pokemon pokemon) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(activity.pokemonMainFragmentContainer.getId(), PokemonDetailsFragment.newInstance(pokemon), "pokemonDetailsFragment");
        transaction.addToBackStack("DetailsFragment on ListFragment");
        transaction.commit();

        activity.switchHomeUpButton(false);
        activity.invalidateOptionsMenu();
    }

    @Override
    public void pokemonAdded(Pokemon pokemon) {
        FragmentManager manager = activity.getSupportFragmentManager();
        manager.popBackStackImmediate();

        PokemonListFragment listFragment = (PokemonListFragment) manager.findFragmentByTag("listFragment");
        listFragment.addPokemon(pokemon);

        activity.switchHomeUpButton(true);
        activity.invalidateOptionsMenu();
    }

    @Override
    public void pokemonCreationCanceled() {
        FragmentManager manager = activity.getSupportFragmentManager();
        manager.popBackStack();

        activity.switchHomeUpButton(true);
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
                //pokemonDetailsFragment or commentsScreen on screen
                activity.getSupportFragmentManager().popBackStackImmediate();
                activity.switchHomeUpButton(!(activity.getSupportFragmentManager().getBackStackEntryCount() > 0));
                activity.invalidateOptionsMenu();
            }

            return true;
        }

        return false;
    }

    @Override
    public void onShowAllCommentsClicked(Comment[] comments, String pokemonName) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(activity.pokemonMainFragmentContainer.getId(), PokemonCommentsFragment.newInstance(comments, pokemonName), "pokemonCommentsFragment");
        transaction.addToBackStack("CommentsFragment on DetailsFragment");
        transaction.commit();

        activity.switchHomeUpButton(false);
        activity.invalidateOptionsMenu();
    }
}
