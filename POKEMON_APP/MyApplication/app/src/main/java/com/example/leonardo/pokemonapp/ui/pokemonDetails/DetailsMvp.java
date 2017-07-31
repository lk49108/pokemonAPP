package com.example.leonardo.pokemonapp.ui.pokemonDetails;

import com.example.leonardo.pokemonapp.network.resources.Pokemon;

/**
 * Created by leonardo on 31/07/17.
 */

public interface DetailsMvp {

    interface View {

        void setData(Pokemon pokemon);

    }

    interface Presenter {

        void validateData(Pokemon pokemon);

        void setView(DetailsMvp.View view);

    }
}
