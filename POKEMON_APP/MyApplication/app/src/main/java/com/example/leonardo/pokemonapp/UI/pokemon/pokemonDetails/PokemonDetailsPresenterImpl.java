package com.example.leonardo.pokemonapp.UI.pokemon.pokemonDetails;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telecom.Call;
import android.util.ArraySet;
import android.util.Log;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.resources.Comment;
import com.example.leonardo.pokemonapp.network.resources.Move;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.network.resources.Type;
import com.example.leonardo.pokemonapp.network.resources.User;
import com.example.leonardo.pokemonapp.network.resources.enumerations.Vote;
import com.example.leonardo.pokemonapp.util.PokemonResourcesUtil;
import com.example.leonardo.pokemonapp.util.Util;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by leonardo on 06/08/17.
 */

public class PokemonDetailsPresenterImpl implements PokemonDetailsMVP.Presenter {

    private Pokemon pokemon;
    private String pendingComment;

    private PokemonDetailsMVP.View view;
    private PokemonDetailsMVP.Interactor interactor;

    public PokemonDetailsPresenterImpl(PokemonDetailsMVP.View view, Pokemon pokemon) {
        this.view = view;
        interactor = new PokemonDetailsInteractorImpl();
        this.pokemon = pokemon;
    }



    @Override
    public void cancelCall() {
        interactor.cancel();
    }

    @Override
    public void onAttach(Context context) {
        if(context instanceof PokemonDetailsFragment.PokemonDetailsFragmentListener) {
            view.setListener((PokemonDetailsFragment.PokemonDetailsFragmentListener) context);
        } else {
            throw new RuntimeException(context.toString() + " must implement PokemonDetailsFragmentListener");
        }
    }

    @Override
    public void subscribe(BaseMVP.State state) {
        if(state == null) {
            refreshComments();
            showPokemonDetails();
            return;
        }

        PokemonDetailsState pokemonDetailsState = (PokemonDetailsState) state;
        pokemon =  pokemonDetailsState.getPokemon();
        pendingComment = pokemonDetailsState.getPendingComment();

        view.setPendingCommentText(pendingComment);

        refreshComments();
        showPokemonDetails();
    }

    private void refreshComments() {
        if(!Util.internetConnectionActive()) {
            view.hideComments();
            return;
        }

        view.showProgress("Refreshing pokemon", "Fetching pokemon data");
        interactor.getComments(Integer.parseInt(pokemon.getId()), new CallbackInt() {
            @Override
            public void onSuccess(Object object) {
                Comment[] comments = (Comment[]) object;
                if(comments == null) {
                    comments = new Comment[0];
                }

                Arrays.sort(comments);

                pokemon.setPokemonComments(comments);

                if(comments.length == 0) {
                    view.hideComments();
                    view.hideShowAllCommentsButton();
                } else if(comments.length == 1) {
                    view.showOneComment(comments[0]);
                    view.hideShowAllCommentsButton();
                } else if(comments.length == 2) {
                    view.showTwoComments(comments[0], comments[1]);
                    view.hideShowAllCommentsButton();
                } else {
                    view.showTwoComments(comments[0], comments[1]);
                    view.showShowAllCommentsButton();
                }

                view.hideProgress();

            }

            @Override
            public void onFailure(String message) {
                view.hideProgress();
                view.hideComments();
                if(message.equals("Wrong credentials not authenticated")) {
                    view.showError("Failed to download pokemon comments, try to relogin.");
                } else {
                    view.showError("Failed to download pokemon comments");
                }
            }

            @Override
            public void onCancel() {
                view.hideProgress();
            }
        });
    }

    private void showPokemonDetails() {
        showVotes();

        showPokemonImage();

        view.showName(pokemon.getName());
        view.showDescription(pokemon.getDescription());

        view.showHeight(String.valueOf(pokemon.getHeight()));
        view.showWeight(String.valueOf(pokemon.getWeight()));
        view.showGender(pokemon.getGender().getName());

        int[] typeIds = pokemon.getTypeIds();
        if(typeIds == null) {
            view.showTypes("N/A");
        } else {
            String types = getTypesString(typeIds);
            view.showTypes(types);
        }

        int[] moveIds = pokemon.getMoveIds();
        if(moveIds == null) {
            view.showMoves("N/A");
        } else {
            String moves = getMovesString(moveIds);
            view.showMoves(moves);
        }

    }

    private void showVotes() {
        Vote vote = pokemon.getVotedOn();

        if(vote.equals(Vote.NEUTRAL)) {
            view.showLikeIcon(R.drawable.ic_like_off);
            view.showDislikeIcon(R.drawable.ic_dislike_off);
        } else if(vote.equals(Vote.LIKE)) {
            view.showLikeIcon(R.drawable.ic_like_on);
            view.showDislikeIcon(R.drawable.ic_dislike_off);
        } else {
            view.showLikeIcon(R.drawable.ic_like_off);
            view.showDislikeIcon(R.drawable.ic_dislike_on);
        }
    }

    private String getMovesString(int[] moveIds) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < moveIds.length; i++) {
            builder.append(Move.getFromId(moveIds[i]).getName() + ", ");
        }

        String s = builder.toString();
        return s.substring(0, s.length() - 2);
    }

    private String getTypesString(int[] typeIds) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < typeIds.length; i++) {
            builder.append(Type.getFromId(typeIds[i]).getName() + ", ");
        }

        String s = builder.toString();
        return s.substring(0, s.length() - 2);
    }

    private void showPokemonImage() {
        String pokemonImageSource = pokemon.getImageSource();
        Context context = ((PokemonDetailsFragment) view).getActivity();
        if(pokemonImageSource != null && (PokemonResourcesUtil.imageFileExists(Uri.parse(pokemonImageSource), context) || pokemonImageSource.toString().startsWith("http"))) {
            view.showPokemonImage(Uri.parse(pokemonImageSource));
        } else {
            view.showPokemonImage(R.drawable.ic_person_details);
        }
    }

    @Override
    public void unsubscribe() {
        pokemon = null;
    }

    @Override
    public BaseMVP.State getState() {
        return new PokemonDetailsState(pokemon, pendingComment);
    }

    @Override
    public String getPokemonName() {
        return pokemon.getName();
    }

    @Override
    public void onLikePressed(int drawable) {

        if(!Util.internetConnectionActive()) {
            view.showError("No internet connection");
            return;
        }

        if(pokemon.getVotedOn().equals(Vote.LIKE)) {
            return;
        }

        if(drawable == R.drawable.ic_like_off) {
            if(pokemon.getVotedOn().equals(Vote.DISLIKE)) {
                view.showDislikeIcon(R.drawable.ic_dislike_off);
            }
            view.showLikeIcon(R.drawable.ic_like_on);
            interactor.upVotePokemon(Integer.parseInt(pokemon.getId()), new CallbackInt() {
                @Override
                public void onSuccess(Object object) {
                    pokemon.setVotedOn(Vote.LIKE);
                }

                @Override
                public void onFailure(String message) {
                    showVotes();
                    if(message.equals("Wrong credentials not authenticated")) {
                        view.showError("Failed to like pokemon, try to relogin.");
                    } else {
                        view.showError("Failed to like pokemon");
                    }                }

                @Override
                public void onCancel() {}
            });
        }
    }

    @Override
    public void onDislikePressed(int drawable) {

        if(!Util.internetConnectionActive()) {
            view.showError("No internet connection");
            return;
        }

        if(pokemon.getVotedOn().equals(Vote.DISLIKE)) {
            return;
        }

        if(drawable == R.drawable.ic_dislike_off) {
            if(pokemon.getVotedOn().equals(Vote.LIKE)) {
                view.showLikeIcon(R.drawable.ic_like_off);
            }
            view.showDislikeIcon(R.drawable.ic_dislike_on);
            interactor.downVotePokemon(Integer.parseInt(pokemon.getId()), new CallbackInt() {
                @Override
                public void onSuccess(Object object) {
                    pokemon.setVotedOn(Vote.DISLIKE);
                }

                @Override
                public void onFailure(String message) {
                    showVotes();
                    if(message.equals("Wrong credentials not authenticated")) {
                        view.showError("Failed to dislike pokemon, try to relogin.");
                    } else {
                        view.showError("Failed to dislike pokemon");
                    }
                }

                @Override
                public void onCancel() {}
            });
        }
    }

    @Override
    public Comment[] onShowAllCommentsClicked() {
        if(!Util.internetConnectionActive() && (pokemon.getPokemonComments() == null || pokemon.getPokemonComments().length == 0)) {
            view.showError("Empty pokemon comment list, and no internet connection.");
        }

        return pokemon.getPokemonComments();
    }

    @Override
    public void onCommentPokemonClicked(String comment) {
        if(!Util.internetConnectionActive()) {
            view.showError("No internet connection");
            return;
        }

        if(comment.trim().isEmpty()) {
            view.showError("No empty comments allowed");
            return;
        }

        view.showProgress("Posting comment", "In progress...");
        Comment commentToPost = new Comment();
        commentToPost.setContent(comment);
        interactor.commentPokemon(Integer.parseInt(pokemon.getId()), commentToPost, new CallbackInt() {

            @Override
            public void onSuccess(Object object) {
                view.hideProgress();
                view.setPendingCommentText("");
                refreshComments();
            }

            @Override
            public void onFailure(String message) {
                view.hideProgress();
                if(message.equals("Wrong credentials not authenticated")) {
                    view.showError("Failed to post a comment, try to relogin.");
                } else {
                    view.showError("Failed to post a comment");
                }
            }

            @Override
            public void onCancel() {
                view.hideProgress();
            }
        });
    }

    @Override
    public void setComment(String comment) {
        pendingComment = comment;
    }
}
