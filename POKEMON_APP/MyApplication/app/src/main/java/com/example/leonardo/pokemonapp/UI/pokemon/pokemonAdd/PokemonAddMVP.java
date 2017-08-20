package com.example.leonardo.pokemonapp.UI.pokemon.pokemonAdd;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.base.baseFragment.BaseFragmentMVP;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.resources.Move;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.network.resources.Type;
import com.example.leonardo.pokemonapp.network.resources.enumerations.Gender;

/**
 * Created by leonardo on 06/08/17.
 */

public interface PokemonAddMVP {

    interface View extends BaseFragmentMVP.View {

        void setListener(PokemonAddFragment.PokemonAddFragmentListener listener);

        void setPokemonName(String name);

        void setPokemonDescription(String description);

        void setPokemonWeight(String weight);

        void setPokemonHeight(String height);

        void showRequestPermissionRationale(int message, String permissionGroupToBeAskedFor);

        void showDialogChooser();

        void setPokemonImage(Uri imageUri);

        void setPokemonImage(int drawable);

        void pokemonAdded(Pokemon pokemon);

        void setPokemonTypes(Type[] types);

        void setPokemonMoves(Move[] moves);

        void setFemaleGender();

        void setMaleGender();
    }

    interface Presenter extends BaseFragmentMVP.Presenter {

        void onPokemonNameChanged(String name);

        void onPokemonDescriptionChanged(String description);

        void onPokemonWeightChanged(String weight);

        void onPokemonHeightChanged(String height);

        void onPokemonMovesChangedChanged(Move[] moves);

        void onPokemonTypesChanged(Type[] types);

        void onPokemonGenderChanged(Gender gender);

        void onButtonSaveCLicked();

        void onAddPokemonImageButtonClicked();

        void requestPermission(String permission);

        void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults);

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onTakePhoto();

        void onGetImageFromGallery();

        boolean checkDataValidity();
    }

    interface Interactor extends BaseMVP.Interactor {

        void savePokemonToServer(Pokemon pokemon, CallbackInt callback);

        void savePokemonToDatabase(Pokemon pokemon);

    }

}
