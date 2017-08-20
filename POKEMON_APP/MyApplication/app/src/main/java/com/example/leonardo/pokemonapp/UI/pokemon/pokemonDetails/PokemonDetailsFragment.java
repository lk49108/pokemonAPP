package com.example.leonardo.pokemonapp.UI.pokemon.pokemonDetails;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.base.BaseFragment;
import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.network.resources.Comment;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.util.PokemonResourcesUtil;
import com.example.leonardo.pokemonapp.util.StateUtil;
import com.example.leonardo.pokemonapp.util.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leonardo on 20/07/17.
 */

public class PokemonDetailsFragment extends BaseFragment implements PokemonDetailsMVP.View {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy");

    public interface PokemonDetailsFragmentListener {

        void onShowAllPokemonsClicked(Comment[] comments, String pokemonName);

    }

    private PokemonDetailsFragmentListener listener;

    @BindView(R.id.fragment_pokemon_details_comment_edit_view)
    EditText commentEditView;
    @BindView(R.id.fragment_pokemon_details_comment_button)
    ImageView executePendingCommentButton;
    @BindView(R.id.fragment_pokemon_details_show_all_comments_button)
    Button showAllCommentsButton;
    @BindView(R.id.fragment_pokemon_details_comment_one_username)
    TextView commentUsernameOneView;
    @BindView(R.id.fragment_pokemon_details_comment_two_username)
    TextView commentUsernameTwoView;
    @BindView(R.id.fragment_pokemon_details_comment_one_date)
    TextView commentDateOneView;
    @BindView(R.id.fragment_pokemon_details_comment_two_date)
    TextView commentDateTwoView;
    @BindView(R.id.fragment_pokemon_details_comment_one_content)
    TextView commentContentOneView;
    @BindView(R.id.fragment_pokemon_details_comment_two_content)
    TextView commentContentTwoView;
    @BindView(R.id.fragment_pokemon_details_image_like)
    ImageView likeView;
    @BindView(R.id.fragment_pokemon_details_image_dislike)
    ImageView dislikeView;
    @BindView(R.id.fragment_pokemon_details_text_gender_content)
    TextView pokemonGenderContent;
    @BindView(R.id.fragment_pokemon_details_text_height_content)
    TextView pokemonHeightContent;
    @BindView(R.id.fragment_pokemon_details_text_weight_content)
    TextView pokemonWeightContent;
    @BindView(R.id.fragment_pokemon_details_text_types_content)
    TextView pokemonTypesContent;
    @BindView(R.id.fragment_pokemon_details_text_moves_content)
    TextView pokemonMovesContent;
    @BindView(R.id.fragment_pokemon_details_text_name_pokemon)
    TextView pokemonNameContent;
    @BindView(R.id.fragment_pokemon_details_text_description_pokemon)
    TextView pokemonDescriptionContent;
    @BindView(R.id.fragment_pokemon_details_image_pokemon)
    ImageView pokemonImageContent;

    private Pokemon pokemon;

    private PokemonDetailsMVP.Presenter presenter;

    public PokemonDetailsFragment() {
    }

    public static PokemonDetailsFragment newInstance(Pokemon pokemon) {
        PokemonDetailsFragment instance = new PokemonDetailsFragment();
        instance.pokemon = pokemon;

        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        presenter = new PokemonDetailsPresenterImpl(this, pokemon);

        presenter.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_details, container, false);

        ButterKnife.bind(this, view);

        presenter.subscribe(savedInstanceState == null ? null : StateUtil.readFromPokemonDetailsBundle(savedInstanceState));

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        presenter.setComment(commentEditView.getText().toString());
        StateUtil.writeToBundle(outState, presenter.getState());
    }

    @Override
    public void onDestroy() {
        presenter.unsubscribe();

        super.onDestroy();
    }

    @Override
    public void onStop() {
        presenter.cancelCall();

        super.onStop();
    }

    @Override
    public void setListener(PokemonDetailsFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void showHeight(String height) {
        pokemonHeightContent.setText(height);
    }

    @Override
    public void showWeight(String weight) {
        pokemonWeightContent.setText(weight);
    }

    @Override
    public void showGender(String gender) {
        pokemonGenderContent.setText(gender);
    }

    @Override
    public void showTypes(String types) {
        pokemonTypesContent.setText(types);
    }

    @Override
    public void showMoves(String moves) {
        pokemonMovesContent.setText(moves);
    }

    @Override
    public void showName(String name) {
        pokemonNameContent.setText(name);
    }

    @Override
    public void showDescription(String description) {
        pokemonDescriptionContent.setText(description);
    }

    @Override
    public void showPokemonImage(int drawable) {
        Picasso.with(getActivity()).load(drawable).into(pokemonImageContent);
    }

    @Override
    public void showPokemonImage(Uri imageUri) {
        Picasso.with(getActivity()).load(imageUri).into(pokemonImageContent);
    }

    @Override
    public void showLikeIcon(int drawable) {
        Picasso.with(getActivity()).load(drawable).into(likeView);
        likeView.setTag(drawable);
    }

    @Override
    public void showDislikeIcon(int drawable) {
        Picasso.with(getActivity()).load(drawable).into(dislikeView);
        dislikeView.setTag(drawable);
    }

    @Override
    public void showOneComment(Comment comment) {
        String commentContent = comment.getContent();
        Date commentDate = comment.getDate();
        String authorUsername = comment.getAuthor().getUserName();

        setFirstCommentVisibility(View.VISIBLE);

        commentContentOneView.setText(commentContent);
        commentDateOneView.setText(DATE_FORMAT.format(commentDate));
        commentUsernameOneView.setText(authorUsername);

        setSecondCommentVisibbility(View.GONE);
    }

    @Override
    public void showTwoComments(Comment commentOne, Comment commentTwo) {
        String commentOneContent = commentOne.getContent();
        Date commentOneDate = commentOne.getDate();
        String commentOneAuthorUsername = commentOne.getAuthor().getUserName();

        setFirstCommentVisibility(View.VISIBLE);

        commentContentOneView.setText(commentOneContent);
        commentDateOneView.setText(DATE_FORMAT.format(commentOneDate));
        commentUsernameOneView.setText(commentOneAuthorUsername);

        String commentTwoContent = commentTwo.getContent();
        Date commentTwoDate = commentTwo.getDate();
        String commentTwoAuthorUsername = commentTwo.getAuthor().getUserName();

        setSecondCommentVisibbility(View.VISIBLE);

        commentContentTwoView.setText(commentTwoContent);
        commentDateTwoView.setText(DATE_FORMAT.format(commentTwoDate));
        commentUsernameTwoView.setText(commentTwoAuthorUsername);
    }

    @Override
    public void showShowAllCommentsButton() {
        showAllCommentsButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideShowAllCommentsButton() {
        showAllCommentsButton.setVisibility(View.GONE);
    }

    void setFirstCommentVisibility(int visibility) {
        commentContentOneView.setVisibility(visibility);
        commentUsernameOneView.setVisibility(visibility);
        commentDateOneView.setVisibility(visibility);
    }

    void setSecondCommentVisibbility(int visibility) {
        commentContentTwoView.setVisibility(visibility);
        commentDateTwoView.setVisibility(visibility);
        commentUsernameTwoView.setVisibility(visibility);
    }

    @Override
    public void hideComments() {
        setFirstCommentVisibility(View.GONE);
        setSecondCommentVisibbility(View.GONE);
        hideShowAllCommentsButton();
    }

    @Override
    public void setPendingCommentText(String comment) {
        commentEditView.setText(comment);
    }

    @OnClick(R.id.fragment_pokemon_details_image_like)
    void onLikeStatusChanged() {
        presenter.onLikePressed((int)likeView.getTag());
    }

    @OnClick(R.id.fragment_pokemon_details_image_dislike)
    void onDislikeStatusChanged() {
        presenter.onDislikePressed((int)dislikeView.getTag());
    }

    @OnClick(R.id.fragment_pokemon_details_show_all_comments_button)
    void onShowAllCommentsButtonClicked() {
        listener.onShowAllPokemonsClicked(presenter.onShowAllCommentsClicked(), presenter.getPokemonName());
    }

    @OnClick(R.id.fragment_pokemon_details_comment_button)
    void onCommentPokemonClicked() {
        presenter.onCommentPokemonClicked(commentEditView.getText().toString());
    }

}