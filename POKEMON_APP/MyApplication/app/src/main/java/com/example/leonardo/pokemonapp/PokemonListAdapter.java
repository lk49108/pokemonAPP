package com.example.leonardo.pokemonapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.util.PokemonResourcesUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by leonardo on 13/07/17.
 */

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.ViewHolder> {

    public static final String POKEMON_DETAILS_KEY = "pokemon.details.key";

    private final List<Pokemon> pokemons = new ArrayList<>();
    private Context context;

    public PokemonListAdapter(Context context) {
        this.context = context;
    }

    public int size() {
        return pokemons.size();
    }

    public boolean isEmpty() {
        return pokemons.isEmpty();
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pokemon_list_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pokemon pokemon = pokemons.get(position);
        holder.pokemonNameTextView.setText(pokemon.getName());

        try {
            if(PokemonResourcesUtil.imageFileExists(pokemon.getImageSource(), context)) {
                Picasso.with(context).load(pokemons.get(position).getImageSource()).transform(new CircleTransformation()).into(holder.pokemonRoundImageView);
            } else {
                Picasso.with(context).load(R.drawable.ic_person).transform(new CircleTransformation()).into(holder.pokemonRoundImageView);
            }
        } catch (NullPointerException ex) {}
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    public void addPokemon(Pokemon newPokemon) {
        pokemons.add(newPokemon);
        notifyItemInserted(pokemons.size());
    }

    public void addAll(Pokemon[] pokemonsNew) {
        int startPostion = pokemons.size();
        for (Pokemon pokemon : pokemonsNew) {
            pokemons.add(pokemon);
        }

        notifyItemRangeInserted(startPostion, pokemonsNew.length);
    }

    public Pokemon getPokemon(int i) {
        return pokemons.get(i);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pokemon_name_view)
        TextView pokemonNameTextView;

        @BindView(R.id.pokemon_image_view)
        ImageView pokemonRoundImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.pokemon_name_view)
        public void onPokemonNameClick() {
            Pokemon selectedPokemon = pokemons.get(getAdapterPosition());

            Intent intent = new Intent(context, PokemonDetailsActivity.class);
            intent.putExtra(POKEMON_DETAILS_KEY, selectedPokemon);

            context.startActivity(intent);
        }
    }
}
