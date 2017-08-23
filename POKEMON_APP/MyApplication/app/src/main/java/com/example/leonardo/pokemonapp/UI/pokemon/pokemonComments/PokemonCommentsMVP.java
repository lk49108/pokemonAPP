package com.example.leonardo.pokemonapp.UI.pokemon.pokemonComments;

import com.example.leonardo.pokemonapp.UI.pokemon.pokemonComments.adapter.PokemonCommentsListAdapter;
import com.example.leonardo.pokemonapp.base.BaseFragment;
import com.example.leonardo.pokemonapp.base.baseFragment.BaseFragmentMVP;

/**
 * Created by leonardo on 11/08/17.
 */

public interface
PokemonCommentsMVP {

    interface View extends BaseFragmentMVP.View {

        void showCommentsList(PokemonCommentsListAdapter adapter);

    }

    interface Presenter extends BaseFragmentMVP.Presenter {
        String getPokemonName();
    }

}
