package com.example.leonardo.pokemonapp;

import android.content.Intent;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.util.PokemonResourcesUtil;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PokemonDetailsActivity extends AppCompatActivity {

    @BindView(R.id.pokemon_details_activity_pokemon_height)
    View pokemonHeightView;
    private final DetailElement pokemonHeightElement = new DetailElement();

    @BindView(R.id.pokemon_details_activity_pokemon_weight)
    View pokemonWeightView;
    private final DetailElement pokemonWeightElement = new DetailElement();

    @BindView(R.id.pokemon_details_activity_pokemon_category)
    View pokemonCategoryView;
    private final DetailElement pokemonCategoryElement = new DetailElement();

    @BindView(R.id.pokemon_details_activity_pokemon_abilities)
    View pokemonAbilitiesView;
    private final DetailElement pokemonAbilitiesElement = new DetailElement();

    @BindView(R.id.pokemon_details_activity_pokemon_name)
    TextView pokemonName;

    @BindView(R.id.pokemon_details_activity_pokemon_description)
    TextView pokemonDescription;

    @BindView(R.id.toolBarPokemonDetails)
    Toolbar toolbar;

    @BindView(R.id.pokemon_details_activity_pokemon_image)
    ImageView image;

    private Pokemon pokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("");

        Intent requestIntent = getIntent();
        pokemon = requestIntent.getParcelableExtra(PokemonListAdapter.POKEMON_DETAILS_KEY);

        pokemonName.setText(pokemon.getName());
        pokemonDescription.setText(pokemon.getDescription());

        initializeDetails();
    }

    private void initializeDetails() {
        ButterKnife.bind(pokemonHeightElement, pokemonHeightView);
        ButterKnife.bind(pokemonWeightElement, pokemonWeightView);
        ButterKnife.bind(pokemonCategoryElement, pokemonCategoryView);
        ButterKnife.bind(pokemonAbilitiesElement, pokemonAbilitiesView);

        initializeContent();
    }

    private void initializeContent() {
        pokemonHeightElement.detailTitle.setText("Height");
        pokemonHeightElement.detailContent.setText(pokemon.getHeight());

        pokemonWeightElement.detailTitle.setText("Weight");
        pokemonWeightElement.detailContent.setText(pokemon.getWeight());

        pokemonCategoryElement.detailTitle.setText("Category");
        pokemonCategoryElement.detailContent.setText(pokemon.getCategory());

        pokemonAbilitiesElement.detailTitle.setText("Abilities");
        pokemonAbilitiesElement.detailContent.setText(pokemon.getAbilities());

        if(PokemonResourcesUtil.imageFileExists(pokemon.getImageSource(), this)) {
            Picasso.with(this).load(pokemon.getImageSource()).into(image);
        } else {
            Picasso.with(this).load(R.drawable.ic_person).into(image);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    class DetailElement {

        @BindView(R.id.pokemon_detail_title)
        TextView detailTitle;

        @BindView(R.id.pokemon_detail_content)
        TextView detailContent;

    }
}