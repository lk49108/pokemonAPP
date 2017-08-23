package com.example.leonardo.pokemonapp.UI.pokemon.pokemonList;

import android.support.v4.app.Fragment;

import com.example.leonardo.pokemonapp.UI.pokemon.pokemonList.adapter.PokemonListAdapter;
import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.base.baseFragment.BaseFragmentMVP;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;

import java.util.List;

/**
 * Created by leonardo on 05/08/17.
 */

public interface PokemonListMVP {

    interface View extends BaseFragmentMVP.View {

        void setListener(PokemonListFragment.PokemonListFragmentListener listener);

        void showPokemonList();

        void showEmptyListPlaceHolder();

        void navigateToPokemonDetailsScreen(Pokemon pokemon);

        void hideSwipeToRefreshAnimation();

        void setPokemonList(PokemonListAdapter adapter);

    }

    interface Presenter extends BaseFragmentMVP.Presenter {

        void getPokemonsFromDatabase();

        void onPokemonListElementClicked(Pokemon pokemonClicked);

        void onSwipeToRefreshGestureStart();

        void pokemonAdded(Pokemon pokemon);

    }

    interface Interactor extends BaseMVP.Interactor {

        void downloadPokemonsFromServer(CallbackInt callbackInt);

        void savePokemonsToDatabase(List<Pokemon> pokemons);

        List<Pokemon> getPokemonsFromDatabase();

    }

}
