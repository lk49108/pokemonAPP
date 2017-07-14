package com.example.leonardo.pokemonapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PokemonListActivity extends AppCompatActivity {

    private PokemonListAdapter pokemonListAdapter;
    private final List<Pokemon> pokemons = new ArrayList<>();

    private static final int ADD_POKEMON_REQUEST_CODE = 20;

    private boolean listEmpty = true;

    @BindView(R.id.empty_pokemon_list_layout) FrameLayout emptyPokemonListLayout;
    @BindString(R.string.pokemon_list_activity_title) String title;
    @BindView(R.id.toolBarPokemonList) Toolbar toolBarPokemonList;
    @BindView(R.id.pokemonListActivityRecyclerView) RecyclerView recyclerView;

    private MenuItem addPokemonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list);

        ButterKnife.bind(this);

        setSupportActionBar(toolBarPokemonList);
        getSupportActionBar().setTitle(title);

        configureRecyclerViewBehaviour();
    }

    private void configureRecyclerViewBehaviour() {
        pokemonListAdapter = new PokemonListAdapter(this, pokemons);
        recyclerView.setAdapter(pokemonListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pokemon_list_activity_menu, menu);

        addPokemonButton = menu.findItem(R.id.action_add_pokemon);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_pokemon:
                Intent intent = new Intent(this, AddPokemonActivity.class);
                startActivityForResult(intent, ADD_POKEMON_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {
            case ADD_POKEMON_REQUEST_CODE:

                if(resultCode == RESULT_OK) {


                    final String name = data.getStringExtra(AddPokemonActivity.POKEMON_NAME_KEY);
                    final String description = data.getStringExtra(AddPokemonActivity.POKEMON_DESCRIPTION_KEY);
                    Pokemon newPokemon = new Pokemon(name, description);

                    pokemonListAdapter.addPokemon(newPokemon);
                    emptyPokemonListLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    return;
                }

        }

    }
}