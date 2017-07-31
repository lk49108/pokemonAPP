package com.example.leonardo.pokemonapp.ui.pokemonDetails;

import android.net.Uri;

import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.util.PokemonResourcesUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by leonardo on 31/07/17.
 */

public class DetailsPresenter implements DetailsMvp.Presenter {

    DetailsMvp.View view;

    public void setView(DetailsMvp.View view) {
        this.view = view;
    }

    @Override
    public void validateData(Pokemon pokemon) {
        if(pokemon != null) {
            if(pokemon.getName().isEmpty()) {
                pokemon.setName("NAN");
            }
            if(pokemon.getDescription().isEmpty()) {
                pokemon.setDescription("NAN");
            }
            if(pokemon.getAbilities().isEmpty()) {
                pokemon.setAbilities("NAN");
            }
            //if(pokemon.getImageSource() == null) {
              //  pokemon.setImageSource();
            //}
            if(pokemon.getCategory().isEmpty()) {
                pokemon.setCategory("NAN");
            }
            if(pokemon.getHeight().isEmpty()) {
                pokemon.setHeight("NAN");
            }
            if(pokemon.getWeight().isEmpty()) {
                pokemon.setWeight("NAN");
            }

        } else {
            pokemon = new Pokemon("NAN", "NAN", "NAN", "NAN", "NAN", "NAN", null);
        }

        view.setData(pokemon);
    }
}
