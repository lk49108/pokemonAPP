package com.example.leonardo.pokemonapp.UI.pokemon.pokemonDetails;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.example.leonardo.pokemonapp.UI.pokemon.pokemonList.PokemonListFragment;
import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.base.baseFragment.BaseFragmentMVP;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.resources.Comment;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;

import java.util.List;

/**
 * Created by leonardo on 06/08/17.
 */

public interface PokemonDetailsMVP {

    interface View extends BaseFragmentMVP.View {

        void setListener(PokemonDetailsFragment.PokemonDetailsFragmentListener listener);

        void showHeight(String height);

        void showWeight(String weight);

        void showGender(String gender);

        void showTypes(String types);

        void showMoves(String moves);

        void showName(String name);

        void showDescription(String description);

        void showPokemonImage(int drawable);

        void showPokemonImage(Uri imageUri);

        void showLikeIcon(int drawable);

        void showDislikeIcon(int drawable);

        void showOneComment(Comment comment);

        void showTwoComments(Comment commentOne, Comment commentTwo);

        void showShowAllCommentsButton();

        void hideShowAllCommentsButton();

        void hideComments();

        void setPendingCommentText(String comment);

    }

    interface Presenter extends BaseFragmentMVP.Presenter {

        void onLikePressed(int drawable);

        void onDislikePressed(int drawable);

        Comment[] onShowAllCommentsClicked();

        void onCommentPokemonClicked(String s);

        void setComment(String comment);
    }

    interface Interactor extends BaseMVP.Interactor {

        void upVotePokemon(int pokemonId, CallbackInt callback);

        void downVotePokemon(int pokemonId, CallbackInt callback);

        void getComments(int pokemonId, CallbackInt callback);

        void commentPokemon(int id, Comment comment, CallbackInt callbackInt);
    }
}
