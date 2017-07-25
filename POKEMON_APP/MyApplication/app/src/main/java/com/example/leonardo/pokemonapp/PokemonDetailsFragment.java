package com.example.leonardo.pokemonapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.util.PokemonResourcesUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leonardo on 20/07/17.
 */

public class PokemonDetailsFragment extends Fragment {

    @BindView(R.id.fragment_pokemon_details_text_height_content)
    TextView pokemonHeightContent;
    @BindView(R.id.fragment_pokemon_details_text_weight_content)
    TextView pokemonWeightContent;
    @BindView(R.id.fragment_pokemon_details_text_category_content)
    TextView pokemonCategoryContent;
    @BindView(R.id.fragment_pokemon_details_text_abilities_content)
    TextView pokemonAbilitiesContent;
    @BindView(R.id.fragment_pokemon_details_text_name_pokemon)
    TextView pokemonNameContent;
    @BindView(R.id.fragment_pokemon_details_text_description_pokemon)
    TextView pokemonDescriptionContent;
    @BindView(R.id.fragment_pokemon_details_image_pokemon)
    ImageView pokemonImageContent;

    private Pokemon pokemon;

    public PokemonDetailsFragment() {
    }

    public static PokemonDetailsFragment newInstance(Pokemon pokemon) {
        PokemonDetailsFragment instance = new PokemonDetailsFragment();
        instance.pokemon = pokemon;

        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_details, container, false);

        ButterKnife.bind(this, view);

        if(savedInstanceState != null) {
            pokemon = savedInstanceState.getParcelable("pokemon");
        }
        reinitializeViews();

        return view;
    }

    private void reinitializeViews() {
        if(pokemon != null) {
            pokemonHeightContent.setText(pokemon.getHeight());
            pokemonWeightContent.setText(pokemon.getWeight());
            pokemonCategoryContent.setText(pokemon.getCategory());
            pokemonAbilitiesContent.setText(pokemon.getAbilities());
            pokemonNameContent.setText(pokemon.getName());
            pokemonDescriptionContent.setText(pokemon.getDescription());

            Uri pokemonImageSource = pokemon.getImageSource();
            if(pokemonImageSource != null && PokemonResourcesUtil.imageFileExists(pokemonImageSource, getActivity())) {
                Picasso.with(getActivity()).load(pokemonImageSource).into(pokemonImageContent);
            }

        }
    }

    public void showNewPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
        reinitializeViews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("pokemon", pokemon);
    }
}