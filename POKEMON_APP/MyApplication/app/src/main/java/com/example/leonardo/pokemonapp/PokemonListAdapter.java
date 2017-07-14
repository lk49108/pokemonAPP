package com.example.leonardo.pokemonapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by leonardo on 13/07/17.
 */

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.ViewHolder> {

    public static final String POKEMON_DETAILS_NAME_KEY = "pokemon.details.name.key";
    public static final String POKEMON_DETAILS_DESCRIPTION_KEY = "pokemon.details.description.key";


    private List<Pokemon> pokemons = new ArrayList<>();
    private Context context;

    public PokemonListAdapter(Context context) {
        this.context = context;
    }

    public PokemonListAdapter(Context context, List<Pokemon> pokemons) {
        this.context = context;
        this.pokemons = pokemons;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pokemon_list_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.pokemonNameTextView.setText(pokemons.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    public void addPokemon(Pokemon newPokemon) {
        pokemons.add(newPokemon);
        notifyItemInserted(pokemons.size());
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView pokemonNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            pokemonNameTextView = (TextView)itemView.findViewById(R.id.pokemon_name_view);
            pokemonNameTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Pokemon selectedPokemon = pokemons.get(getAdapterPosition());

            Intent intent = new Intent(context, PokemonDetailsActivity.class);
            intent.putExtra(POKEMON_DETAILS_NAME_KEY, selectedPokemon.getName());
            intent.putExtra(POKEMON_DETAILS_DESCRIPTION_KEY, selectedPokemon.getDescription());

            context.startActivity(intent);
        }
    }
}
