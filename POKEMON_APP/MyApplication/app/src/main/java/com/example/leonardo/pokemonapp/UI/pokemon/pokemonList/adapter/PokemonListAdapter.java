package com.example.leonardo.pokemonapp.UI.pokemon.pokemonList.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leonardo.pokemonapp.util.CircleTransformation;
import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.UI.pokemon.pokemonList.PokemonListFragment;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.util.PokemonResourcesUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moe.banana.jsonapi2.ResourceIdentifier;


/**
 * Created by leonardo on 13/07/17.
 */

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.ViewHolder> {

    private final ArrayList<Pokemon> pokemons = new ArrayList<>();
    private PokemonListFragment fragment;

    public PokemonListAdapter(PokemonListFragment fragment) {
        this.fragment = fragment;
    }

    public PokemonListAdapter(PokemonListFragment fragment, List<Pokemon> pokemons) {
        this.fragment = fragment;
        this.pokemons.addAll(pokemons);
        notifyItemRangeInserted(0, pokemons.size());
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
        View view = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.pokemon_list_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pokemon pokemon = pokemons.get(position);
        holder.pokemonNameTextView.setText(pokemon.getName());

        try {
            if(PokemonResourcesUtil.imageFileExists(Uri.parse(pokemon.getImageSource()), fragment.getActivity()) ||
                pokemon.getImageSource().toString().startsWith("http")) {
                Picasso.with(fragment.getActivity()).load(pokemons.get(position).getImageSource()).transform(new CircleTransformation()).into(holder.pokemonRoundImageView);
            } else {
                Picasso.with(fragment.getActivity()).load(R.drawable.ic_person).transform(new CircleTransformation()).into(holder.pokemonRoundImageView);
            }
        } catch (NullPointerException ex) {
            Picasso.with(fragment.getActivity()).load(R.drawable.ic_person).transform(new CircleTransformation()).into(holder.pokemonRoundImageView);
        }
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    public void addPokemon(Pokemon newPokemon) {
        if(!pokemons.contains(newPokemon)) {
            pokemons.add(0, newPokemon);
            notifyItemInserted(0);
        }
    }

    public void addAll(List<Pokemon> pokemonsNew) {
        int startPostion = pokemons.size();
        for(Pokemon pokemonNew : pokemonsNew) {
            pokemons.add(pokemonNew);
        }

        notifyItemRangeInserted(startPostion, pokemonsNew.size());
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
            fragment.getListener().pokemonSelected(pokemons.get(getAdapterPosition()));
        }
    }
}
