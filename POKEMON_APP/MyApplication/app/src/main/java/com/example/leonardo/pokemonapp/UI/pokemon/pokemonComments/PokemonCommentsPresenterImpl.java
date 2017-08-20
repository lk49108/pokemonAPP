package com.example.leonardo.pokemonapp.UI.pokemon.pokemonComments;

import android.content.Context;

import com.example.leonardo.pokemonapp.UI.pokemon.pokemonComments.adapter.PokemonCommentsListAdapter;
import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.network.resources.Comment;

/**
 * Created by leonardo on 11/08/17.
 */

public class PokemonCommentsPresenterImpl implements PokemonCommentsMVP.Presenter {

    private String pokemonName;
    private Comment[] comments;

    private final PokemonCommentsMVP.View view;

    public PokemonCommentsPresenterImpl(PokemonCommentsMVP.View view, Comment[] comments, String pokemonName) {
        this.view = view;
        this.comments = comments;
        this.pokemonName = pokemonName;
    }

    @Override
    public void cancelCall() {}

    @Override
    public void onAttach(Context context) {}

    @Override
    public void subscribe(BaseMVP.State state) {
        if(state == null) {
            view.showCommentsList(new PokemonCommentsListAdapter((PokemonCommentsFragment)view, comments));
            return;
        }

        PokemonCommentsState pokemonCommentsState = (PokemonCommentsState) state;
        comments = pokemonCommentsState.getComments();
        pokemonName = pokemonCommentsState.getPokemonName();

        view.showCommentsList(new PokemonCommentsListAdapter((PokemonCommentsFragment)view, comments));
    }

    @Override
    public void unsubscribe() {
        comments = null;
        pokemonName = null;
    }

    @Override
    public BaseMVP.State getState() {
        return new PokemonCommentsState(comments, pokemonName);
    }

    @Override
    public String getPokemonName() {
        return pokemonName;
    }
}
