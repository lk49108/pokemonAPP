package com.example.leonardo.pokemonapp.UI.pokemon.pokemonComments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.UI.pokemon.pokemonComments.adapter.PokemonCommentsListAdapter;
import com.example.leonardo.pokemonapp.base.BaseFragment;
import com.example.leonardo.pokemonapp.network.resources.Comment;
import com.example.leonardo.pokemonapp.util.StateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leonardo on 11/08/17.
 */

public class PokemonCommentsFragment extends BaseFragment implements PokemonCommentsMVP.View {

    @BindView(R.id.fragment_pokemon_comments_recycler_view)
    RecyclerView recyclerView;

    private Comment[] comments;
    private PokemonCommentsMVP.Presenter presenter;
    private String pokemonName;

    public static PokemonCommentsFragment newInstance(Comment[] comments, String pokemonName) {
        PokemonCommentsFragment instance = new PokemonCommentsFragment();
        instance.comments = comments;
        instance.pokemonName = pokemonName;

        return instance;
    }

    public String getPokemonName() {
        return presenter.getPokemonName();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        presenter = new PokemonCommentsPresenterImpl(this, comments, pokemonName);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_pokemon_comments, container, false);
        ButterKnife.bind(this, view);

        presenter.subscribe(savedInstanceState == null ? null : StateUtil.readFromPokemonCommentsBundle(savedInstanceState));

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        StateUtil.writeToBundle(outState, presenter.getState());
    }

    @Override
    public void onDestroy() {
        presenter.unsubscribe();

        super.onDestroy();
    }

    @Override
    public void showCommentsList(PokemonCommentsListAdapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

}
