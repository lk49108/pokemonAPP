package com.example.leonardo.pokemonapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.executor.NetworkExecutor;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.util.UserUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leonardo on 20/07/17.
 */

public class PokemonListFragment extends Fragment {

    private int i;
    private boolean successedToDownloadPokemons;

    private static final String SAVE_INSTANCE_STATE_EXISTING_POKEMONS_KEY = "pokemons.list.fragment.save.instance.state.key";

    public interface PokemonListFragmentListener {

        void pokemonSelected(Pokemon pokemon);

    }

    public PokemonListAdapter pokemonListAdapter;

    @BindView(R.id.fragment_pokemon_list_empty_pokemon_list_layout)
    View emptyPokemonListView;
    @BindView(R.id.fragment_pokemon_list_recycler_view)
    RecyclerView recyclerView;

    private PokemonListFragmentListener listener;

    public PokemonListFragment() {
    }

    public static PokemonListFragment newInstance() {
        return new PokemonListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(pokemonListAdapter == null) {
            pokemonListAdapter = new PokemonListAdapter(this);
        }
        if(savedInstanceState == null) {
            getPokemonsFromService();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_pokemon_list, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        configureRecyclerViewBehaviour();

        if(savedInstanceState != null) {
            Pokemon[] pokemons = (Pokemon[])savedInstanceState.getParcelableArray(SAVE_INSTANCE_STATE_EXISTING_POKEMONS_KEY);
            pokemonListAdapter.addAll(pokemons);
        }

        switchFragmentLayout();
    }

    private void getPokemonsFromService() {

        if(!UserUtil.internetConnectionActive()) {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
            return;
        }

        for(i = 0; i < 3; i++) {
            NetworkExecutor.getInstance().getAllPokemons(new CallbackInt() {
                @Override
                public void onSuccess(Object object) {
                    if(successedToDownloadPokemons) {
                        return;
                    }
                    successedToDownloadPokemons = true;
                    Pokemon[] pokemons = (Pokemon[]) object;
                    pokemonListAdapter.addAll(pokemons);
                    switchFragmentLayout();
                }

                @Override
                public void onFailure(String message) {
                    if(i == 2) {
                        Toast.makeText(getActivity(), "Failed to download pokemons", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof PokemonListFragmentListener) {
            listener = (PokemonListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement PokemonListFragmentListener");
        }
    }

    private void configureRecyclerViewBehaviour() {
        recyclerView.setAdapter(pokemonListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    public void addPokemon(Pokemon pokemon) {

        if(!UserUtil.internetConnectionActive()) {
            Toast.makeText(getActivity(), "pokemon will not be uploaded to server because there is no active internet connection", Toast.LENGTH_LONG).show();
        }

        pokemonListAdapter.addPokemon(pokemon);
        switchFragmentLayout();

        NetworkExecutor.getInstance().createPokemon(pokemon, new CallbackInt() {
            @Override
            public void onSuccess(Object object) {
                Toast.makeText(getActivity(), "Pokemon succesfully uploaded to server.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getActivity(), "Unable to upload pokemon to server.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void switchFragmentLayout() {

        if(!pokemonListAdapter.isEmpty()) {
            emptyPokemonListView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            emptyPokemonListView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        List<Pokemon> pokemonList = pokemonListAdapter.getPokemons();
        Pokemon[] pokemons = new Pokemon[pokemonList.size()];
        for(int i = 0; i < pokemons.length; i++) {
            pokemons[i] = pokemonList.get(i);
        }
        outState.putParcelableArray(SAVE_INSTANCE_STATE_EXISTING_POKEMONS_KEY, pokemons);

    }

    public PokemonListFragment.PokemonListFragmentListener getListener() {
        return listener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        NetworkExecutor.getInstance().destroyAnyPendingTransactions();
    }
}