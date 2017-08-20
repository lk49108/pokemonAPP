package com.example.leonardo.pokemonapp.UI.pokemon.fragmentHandlers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.leonardo.pokemonapp.UI.pokemon.fragmentHandlers.FragmentMainActivityHandlerInt;
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

public class FragmentMainActivityHandlerHorizontalTablet implements FragmentMainActivityHandlerInt {

    private final PokemonMainActivity activity;

    public FragmentMainActivityHandlerHorizontalTablet(PokemonMainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void initializeFragments() {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(activity.pokemonMainFragmentContainer.getId(), PokemonListFragment.newInstance(), "listFragment");
        transaction.replace(activity.pokemonAddDetailFragmentContainer.getId(), PokemonAddFragment.newInstance(), "addPokemonFragment");
        transaction.commit();
    }

    @Override
    public void fromOtherConfigurationInitialization() {
        Fragment addPokemonFragment = activity.getSupportFragmentManager().findFragmentByTag("addPokemonFragment");
        Fragment pokemonDetailsFragment = activity.getSupportFragmentManager().findFragmentByTag("pokemonDetailsFragment");
        Fragment pokemonCommentsFragment = activity.getSupportFragmentManager().findFragmentByTag("pokemonCommentsFragment");

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if(activity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            activity.getSupportFragmentManager().popBackStackImmediate();
            if (addPokemonFragment != null) {
                transaction.replace(activity.pokemonAddDetailFragmentContainer.getId(), addPokemonFragment, "addPokemonFragment");
            } else if(pokemonCommentsFragment != null) {
                activity.getSupportFragmentManager().popBackStackImmediate();
                transaction.replace(activity.pokemonAddDetailFragmentContainer.getId(), pokemonDetailsFragment, "pokemonDetailsFragment");
                transaction.commit();
                activity.getSupportFragmentManager().executePendingTransactions();
                transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(activity.pokemonAddDetailFragmentContainer.getId(), pokemonCommentsFragment, "pokemonDetailsFragment");
                transaction.addToBackStack("CommentsFragment on DetailsFragment");
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
        manager.beginTransaction().replace(activity.pokemonAddDetailFragmentContainer.getId(), PokemonAddFragment.newInstance(), "addPokemonFragment");
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
        } else if(activity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            activity.getSupportFragmentManager().popBackStackImmediate();
            return true;
        }

        return false;
    }

    @Override
    public void onShowAllCommentsClicked(Comment[] comments, String pokemonName) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(activity.pokemonAddDetailFragmentContainer.getId(), PokemonCommentsFragment.newInstance(comments, pokemonName), "pokemonCommentsFragment");
        transaction.addToBackStack("CommentsFragment on DetailsFragment");
        transaction.commit();
    }

}