package com.example.leonardo.pokemonapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PokemonDetailsActivity extends AppCompatActivity {

    @BindView(R.id.pokemon_details_activity_pokemon_name)
    TextView pokemonName;

    @BindView(R.id.pokemon_details_activity_pokemon_description)
    TextView pokemonDescription;

    @BindView(R.id.toolBarPokemonDetails)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent requestIntent = getIntent();
        pokemonName.setText(requestIntent.getStringExtra(PokemonListAdapter.POKEMON_DETAILS_NAME_KEY));
        pokemonDescription.setText(requestIntent.getStringExtra(PokemonListAdapter.POKEMON_DETAILS_DESCRIPTION_KEY));


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
