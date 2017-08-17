package com.example.leonardo.pokemonapp.util;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.UI.pokemon.pokemonAdd.PokemonAddState;
import com.example.leonardo.pokemonapp.UI.pokemon.pokemonComments.PokemonCommentsState;
import com.example.leonardo.pokemonapp.UI.pokemon.pokemonDetails.PokemonDetailsState;
import com.example.leonardo.pokemonapp.UI.pokemon.pokemonList.PokemonListState;
import com.example.leonardo.pokemonapp.UI.register.logIn.LogInState;
import com.example.leonardo.pokemonapp.UI.register.signUp.SignUpFragment;
import com.example.leonardo.pokemonapp.UI.register.signUp.SignUpState;
import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.network.resources.Comment;
import com.example.leonardo.pokemonapp.network.resources.Move;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.network.resources.Type;
import com.example.leonardo.pokemonapp.network.resources.enumerations.Gender;

import java.util.ArrayList;

/**
 * Created by leonardo on 04/08/17.
 */

public class StateUtil {

    private static final String LOG_IN_EMAIL_KEY = "log.in.email.key";
    private static final String LOG_IN_PASSWORD_KEY = "log.in.password.key";
    private static final String LOG_IN_PASSWORD_VISIBILITY_DRAWABLE_KEY = "log.in.password.visibility.drawable.key";

    private static final String SIGN_UP_EMAIL_KEY = "sign.up.email.key";
    private static final String SIGN_UP_USERNAME_KEY = "sign.up.username.key";
    private static final String SIGN_UP_PASSWORD_KEY = "sign.up.password.key";
    private static final String SIGN_UP_CONFIRMATION_PASSWORD = "sign.up.confirmation.password.key";

    private static final String POKEMON_LIST_POKEMON_LIST = "pokemon.list.pokemon.list";

    private static final String POKEMON_DETAILS_POKEMON_KEY = "pokemon.details.pokemon.key";
    private static final String POKEMON_DETAILS_COMMENT_KEY = "pokemon.details.comment.key";

    private static final String POKEMON_ADD_NAME_KEY = "pokemon.add.name.key";
    private static final String POKEMON_ADD_DESCRIPTION_KEY = "pokemon.add.description.key";
    private static final String POKEMON_ADD_WEIGHT_KEY = "pokemon.add.weight.key";
    private static final String POKEMON_ADD_HEIGHT_KEY = "pokemon.add.height.key";
    private static final String POKEMON_ADD_TYPES_KEY = "pokemon.add.types.key";
    private static final String POKEMON_ADD_MOVES_KEY = "pokemons.add.moves.key";
    private static final String POKEMON_ADD_GENDER_KEY = "pokemons.add.gender.key";
    private static final String POKEMON_ADD_IMAGE_URI_KEY = "pokemons.add.image.uri.key";

    private static final String POKEMON_COMMENTS_COMMENTS_KEY = "pokemons.comments.comments.key";
    private static final String POKEMON_COMMENTS_POKEMON_NAME_KEY = "pokemons.comments.pokemon.name.key";

    public static void writeToBundle(Bundle bundle, BaseMVP.State state) {

        if(state instanceof LogInState) {
            LogInState logInState = (LogInState) state;
            bundle.putString(LOG_IN_EMAIL_KEY, logInState.getEmail());
            bundle.putString(LOG_IN_PASSWORD_KEY, logInState.getPassword());
            bundle.putInt(LOG_IN_PASSWORD_VISIBILITY_DRAWABLE_KEY, ((LogInState) state).getPasswordVisibilityDrawable());
        } else if(state instanceof SignUpState) {
            SignUpState signUpState = (SignUpState) state;
            bundle.putString(SIGN_UP_EMAIL_KEY, signUpState.getEmail());
            bundle.putString(SIGN_UP_USERNAME_KEY, signUpState.getUsername());
            bundle.putString(SIGN_UP_PASSWORD_KEY, signUpState.getPassword());
            bundle.putString(SIGN_UP_CONFIRMATION_PASSWORD, signUpState.getConfPassword());
        } else if(state instanceof PokemonListState) {
            //do nothing
        } else if(state instanceof PokemonDetailsState) {
            PokemonDetailsState pokemonDetailsState = (PokemonDetailsState) state;
            bundle.putSerializable(POKEMON_DETAILS_POKEMON_KEY, pokemonDetailsState.getPokemon());
            bundle.putString(POKEMON_DETAILS_COMMENT_KEY, pokemonDetailsState.getPendingComment());
        } else if(state instanceof PokemonAddState) {
            PokemonAddState pokemonAddState = (PokemonAddState) state;
            bundle.putString(POKEMON_ADD_NAME_KEY, pokemonAddState.getName());
            bundle.putString(POKEMON_ADD_DESCRIPTION_KEY, pokemonAddState.getDescription());
            bundle.putString(POKEMON_ADD_WEIGHT_KEY, pokemonAddState.getWeight());
            bundle.putString(POKEMON_ADD_HEIGHT_KEY, pokemonAddState.getHeight());
            bundle.putSerializable(POKEMON_ADD_TYPES_KEY, pokemonAddState.getTypes());
            bundle.putSerializable(POKEMON_ADD_MOVES_KEY, pokemonAddState.getMoves());
            bundle.putSerializable(POKEMON_ADD_GENDER_KEY, pokemonAddState.getGender());
            bundle.putSerializable(POKEMON_ADD_IMAGE_URI_KEY, pokemonAddState.getImageUri());
        } else if(state instanceof PokemonCommentsState) {
            PokemonCommentsState pokemonCommentsState = (PokemonCommentsState) state;
            bundle.putSerializable(POKEMON_COMMENTS_COMMENTS_KEY, pokemonCommentsState.getComments());
            bundle.putString(POKEMON_COMMENTS_POKEMON_NAME_KEY, pokemonCommentsState.getPokemonName());
        }

    }

    public static LogInState readFromLogInBundle(Bundle bundle) {
        return new LogInState(
                bundle.getString(LOG_IN_EMAIL_KEY, ""),
                bundle.getString(LOG_IN_PASSWORD_KEY, ""),
                bundle.getInt(LOG_IN_PASSWORD_VISIBILITY_DRAWABLE_KEY, R.drawable.ic_visibility_off)
        );
    }

    public static SignUpState readFromSignUpBundle(Bundle bundle) {
        return new SignUpState(
                bundle.getString(SIGN_UP_EMAIL_KEY, ""),
                bundle.getString(SIGN_UP_USERNAME_KEY, ""),
                bundle.getString(SIGN_UP_PASSWORD_KEY, ""),
                bundle.getString(SIGN_UP_CONFIRMATION_PASSWORD, "")
        );
    }

    public static PokemonListState readFromPokemonListBundle(Bundle bundle) {
        return new PokemonListState();
    }

    public static PokemonDetailsState readFromPokemonDetailsBundle(Bundle bundle) {
        return new PokemonDetailsState(
                (Pokemon)bundle.getSerializable(POKEMON_DETAILS_POKEMON_KEY),
                bundle.getString(POKEMON_DETAILS_COMMENT_KEY)
        );
    }

    public static PokemonAddState readFromPokemonAddBundle(Bundle bundle) {
        return new PokemonAddState(
                bundle.getString(POKEMON_ADD_NAME_KEY, ""),
                bundle.getString(POKEMON_ADD_DESCRIPTION_KEY, ""),
                (Type[]) bundle.getSerializable(POKEMON_ADD_TYPES_KEY),
                bundle.getString(POKEMON_ADD_HEIGHT_KEY, ""),
                bundle.getString(POKEMON_ADD_WEIGHT_KEY, ""),
                (Move[]) bundle.getSerializable(POKEMON_ADD_MOVES_KEY),
                (Gender) bundle.getSerializable(POKEMON_ADD_GENDER_KEY),
                (String) bundle.getSerializable(POKEMON_ADD_IMAGE_URI_KEY)
        );
    }

    public static PokemonCommentsState readFromPokemonCommentsBundle(Bundle bundle) {
        return new PokemonCommentsState(
                (Comment[]) bundle.getSerializable(POKEMON_COMMENTS_COMMENTS_KEY),
                bundle.getString(POKEMON_COMMENTS_POKEMON_NAME_KEY, "Pokemon")
        );
    }
}
