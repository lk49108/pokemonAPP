package com.example.leonardo.pokemonapp.UI.pokemon.pokemonList;

import android.content.Context;
import android.util.Log;

import com.example.leonardo.pokemonapp.UI.pokemon.pokemonList.adapter.PokemonListAdapter;
import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.database.model.PokemonDb;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.resources.Comment;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardo on 05/08/17.
 */

public class PokemonListPresenterImpl implements PokemonListMVP.Presenter {

    private PokemonListAdapter adapter;
    private List<Pokemon> pokemons;

    private PokemonListMVP.View view;
    private PokemonListMVP.Interactor interactor;

    public PokemonListPresenterImpl(PokemonListMVP.View view) {
        this.view = view;
        interactor = new PokemonListInteractorImpl();
    }

    @Override
    public void cancelCall() {
        interactor.cancel();
    }

    @Override
    public void onAttach(Context context) {
        if(context instanceof PokemonListFragment.PokemonListFragmentListener) {
            view.setListener((PokemonListFragment.PokemonListFragmentListener) context);
        } else {
            throw new RuntimeException(context.toString() + " must implement PokemonListFragmentListener");
        }
    }

    @Override
    public void subscribe(BaseMVP.State state) {

        if(state == null) {
            if(pokemons == null) {

                //Try to fetch them from server, if that fails, get them from database (if there is any pokemon in database)
                fetchPokemons();

            } else {

                showPokemons();

            }

            return;
        }

        //getFromDatabase and show them...
        getPokemonsFromDatabase();

    }

    private void fetchPokemons() {
        if(Util.internetConnectionActive()) {

            getPokemonsFromServer();

        } else {

            getPokemonsFromDatabase();

        }

    }

    private void showPokemons() {
        if(pokemons == null || pokemons.isEmpty()) {
            view.showEmptyListPlaceHolder();
        } else {
            adapter = new PokemonListAdapter((PokemonListFragment) view, pokemons);
            view.setPokemonList(adapter);
            view.showPokemonList();
        }
    }

    private void getPokemonsFromServer() {
        view.showProgress("Download in progress", "Downloading pokemons from server");
        interactor.downloadPokemonsFromServer(new CallbackInt() {
            @Override
            public void onSuccess(Object object) {
                Pokemon[] fetchedPokemons = (Pokemon[]) object;

                if(fetchedPokemons.length == 0) {
                    view.showEmptyListPlaceHolder();
                    return;
                }


                List<Pokemon> pokemonList = new ArrayList<>();
                for(int i = 0; i < fetchedPokemons.length; i++) {
                    pokemonList.add(fetchedPokemons[i]);
                }

                interactor.savePokemonsToDatabase(pokemonList);

                pokemons = pokemonList;
                showPokemons();

                view.hideProgress();
            }

            @Override
            public void onFailure(String message) {
                view.hideProgress();
                if(message.equals("Wrong credentials not authenticated")) {
                    view.showError("Failed to download pokemons from server, try to relogin.");
                } else {
                    view.showError("Failed to download pokemons from server");
                }
                getPokemonsFromDatabase();
            }

            @Override
            public void onCancel() {
                view.hideProgress();
            }
        });
    }

    @Override
    public void unsubscribe() {
        pokemons = null;
        adapter = null;
    }

    @Override
    public BaseMVP.State getState() {
        return new PokemonListState();
    }

    @Override
    public void getPokemonsFromDatabase() {
        List<Pokemon> pokemonList = interactor.getPokemonsFromDatabase();

        pokemons = pokemonList;

        showPokemons();
    }

    @Override
    public void onPokemonListElementClicked(Pokemon pokemonClicked) {
        view.navigateToPokemonDetailsScreen(pokemonClicked);
    }

    @Override
    public void onSwipeToRefreshGestureStart() {

        if(!Util.internetConnectionActive()) {
            view.hideSwipeToRefreshAnimation();
            view.showError("No internet connection, could not fetch pokemons from server.");
            return;
        }

        interactor.downloadPokemonsFromServer(new CallbackInt() {
            @Override
            public void onSuccess(Object object) {
                Pokemon[] fetchedPokemons = (Pokemon[]) object;

                List<Pokemon> pokemonList = new ArrayList<>();
                for(int i = 0; i < fetchedPokemons.length; i++) {
                    pokemonList.add(fetchedPokemons[i]);
                }

                interactor.savePokemonsToDatabase(pokemonList);

                pokemons = pokemonList;

                showPokemons();

                view.hideSwipeToRefreshAnimation();
            }

            @Override
            public void onFailure(String message) {
                view.hideSwipeToRefreshAnimation();

                if(message.equals("Wrong credentials not authenticated")) {
                    view.showError("Failed to refresh pokemon list, try to relogin.");
                } else {
                    view.showError("Failed to refresh pokemon list");
                }
            }

            @Override
            public void onCancel() {
                view.hideSwipeToRefreshAnimation();
            }
        });
    }

    @Override
    public void pokemonAdded(Pokemon pokemon) {
        adapter.addPokemon(pokemon);
        pokemons.add(0, pokemon);
    }

}
