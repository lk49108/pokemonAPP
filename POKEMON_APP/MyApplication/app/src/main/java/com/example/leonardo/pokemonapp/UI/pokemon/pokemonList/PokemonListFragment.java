package com.example.leonardo.pokemonapp.UI.pokemon.pokemonList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.UI.pokemon.pokemonList.adapter.PokemonListAdapter;
import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.UI.register.logIn.LogInMVP;
import com.example.leonardo.pokemonapp.base.BaseFragment;
import com.example.leonardo.pokemonapp.database.DatabaseCreator;
import com.example.leonardo.pokemonapp.database.SQLitePokedex;
import com.example.leonardo.pokemonapp.database.model.PokemonDb;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.executor.NetworkExecutor;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.network.resources.Type;
import com.example.leonardo.pokemonapp.util.StateUtil;
import com.example.leonardo.pokemonapp.util.Util;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leonardo on 20/07/17.
 */

public class PokemonListFragment extends BaseFragment implements PokemonListMVP.View {

    @BindView(R.id.fragment_pokemon_list_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_pokemon_list_empty_pokemon_list_layout)
    View emptyPokemonListView;
    @BindView(R.id.fragment_pokemon_list_recycler_view)
    RecyclerView recyclerView;

    public interface PokemonListFragmentListener {

        void pokemonSelected(Pokemon pokemon);

    }

    private PokemonListFragmentListener listener;

    public PokemonListFragment() {
    }

    public static PokemonListFragment newInstance() {
        PokemonListFragment fragment = new PokemonListFragment();
        return fragment;
    }

    private PokemonListMVP.Presenter presenter;

    @Override
    public void setListener(PokemonListFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void showPokemonList() {
        emptyPokemonListView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyListPlaceHolder() {
        emptyPokemonListView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void navigateToPokemonDetailsScreen(Pokemon pokemon) {
        listener.pokemonSelected(pokemon);
    }

    @Override
    public void hideSwipeToRefreshAnimation() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setPokemonList(PokemonListAdapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        presenter = new PokemonListPresenterImpl(this);
        presenter.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_pokemon_list, container, false);

        ButterKnife.bind(this, view);

        swipeRefreshLayout.setOnRefreshListener(new SwipeOnRefreshListener());

        presenter.subscribe(savedInstanceState == null ? null : StateUtil.readFromPokemonListBundle(savedInstanceState));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(emptyPokemonListView.getVisibility() == View.VISIBLE && progressDialog == null) {
            presenter.getPokemonsFromDatabase();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        StateUtil.writeToBundle(outState, presenter.getState());
    }

    public PokemonListFragment.PokemonListFragmentListener getListener() {
        return listener;
    }

    @Override
    public void onDestroy() {
        presenter.unsubscribe();

        super.onDestroy();
    }

    @Override
    public void onStop() {
        presenter.cancelCall();

        super.onStop();
    }

    private class SwipeOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            presenter.onSwipeToRefreshGestureStart();
        }

    }

    public void addPokemon(Pokemon pokemon) {
        presenter.pokemonAdded(pokemon);
    }

}