package com.example.leonardo.pokemonapp.UI.pokemon.pokemonAdd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.executor.NetworkExecutor;
import com.example.leonardo.pokemonapp.network.resources.Move;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.network.resources.Type;
import com.example.leonardo.pokemonapp.network.resources.enumerations.Gender;
import com.example.leonardo.pokemonapp.util.Util;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.DoubleSummaryStatistics;

import moe.banana.jsonapi2.HasMany;

import static android.app.Activity.RESULT_OK;

/**
 * Created by leonardo on 06/08/17.
 */

public class PokemonAddPresenterImpl implements PokemonAddMVP.Presenter {

    private static final int SET_POKEMON_PICTURE_FROM_STORAGE_REQUEST_CODE = 40;
    private static final int SET_POKEMON_PICTURE_WITH_CAMERA_REQUEST_CODE = 41;
    private static final int PERMISSION_STORAGE_REQUEST_CODE = 22;

    private String name;
    private String description;
    private String height;
    private String weight;
    private Gender gender;
    private Type[] types;
    private Move[] moves;
    private String imageUri;

    private PokemonAddMVP.View view;
    private PokemonAddMVP.Interactor interactor;

    public PokemonAddPresenterImpl(PokemonAddMVP.View view) {
        this.view = view;
        interactor = new PokemonAddInteractorImpl();
    }

    @Override
    public void cancelCall() {
        interactor.cancel();
    }

    @Override
    public void onAttach(Context context) {
        if(context instanceof PokemonAddFragment.PokemonAddFragmentListener) {
            view.setListener((PokemonAddFragment.PokemonAddFragmentListener) context);
        } else {
            throw new RuntimeException(context.toString() + " must implement PokemonAddFragmentListener");
        }

    }

    @Override
    public void subscribe(BaseMVP.State state) {

        if(state == null) {
            if(name == null) {
                name = "";
            }
            if(description == null) {
                description = "";
            }
            if(types == null) {
                types = new Type[0];
            }
            if(moves == null) {
                moves = new Move[0];
            }
            if(gender == null) {
                gender = Gender.UNKNOWN;
            }
            if(weight == null) {
                weight = "";
            }
            if(height == null) {
                height = "";
            }
            return;
        }

        PokemonAddState pokemonAddState = (PokemonAddState) state;
        name = pokemonAddState.getName();
        description = pokemonAddState.getDescription();
        height = pokemonAddState.getHeight();
        weight = pokemonAddState.getWeight();
        types = pokemonAddState.getTypes();
        moves = pokemonAddState.getMoves();
        gender = pokemonAddState.getGender();
        imageUri = pokemonAddState.getImageUri();

        view.setPokemonName(name);
        view.setPokemonDescription(description);
        view.setPokemonHeight(height);
        view.setPokemonWeight(weight);
        if(imageUri == null) {
            view.setPokemonImage(R.drawable.ic_person);
        } else {
            view.setPokemonImage(Uri.parse(imageUri));
        }
        view.setPokemonMoves(moves);
        view.setPokemonTypes(types);

    }

    @Override
    public void unsubscribe() {
        name = null;
        description = null;
        gender = null;
        types = null;
        moves = null;
    }

    @Override
    public BaseMVP.State getState() {
        return new PokemonAddState(name, description, types, height, weight, moves, gender, imageUri);
    }

    @Override
    public void onPokemonNameChanged(String name) {
        this.name = name;
    }

    @Override
    public void onPokemonDescriptionChanged(String description) {
        this.description = description;
    }

    @Override
    public void onPokemonWeightChanged(String weight) {
        this.weight = weight;
    }

    @Override
    public void onPokemonHeightChanged(String height) {
        this.height = height;
    }

    @Override
    public void onPokemonMovesChangedChanged(Move[] moves) {
        this.moves = moves;
    }

    @Override
    public void onPokemonTypesChanged(Type[] types) {
        this.types = types;
    }

    @Override
    public void onPokemonGenderChanged(Gender gender) {
        this.gender = gender;
    }

    @Override
    public void onButtonSaveCLicked() {

        if(!Util.internetConnectionActive()) {
            view.showError("No active internet connection.");
            return;
        }

        if(!dataValid()) {
            view.showError("Invalid data");
            return;
        }

        Pokemon pokemon = new Pokemon();
        pokemon.setName(name);
        pokemon.setDescription(description);
        pokemon.setWeight(Double.parseDouble(weight));
        pokemon.setHeight(Double.parseDouble(height));
        pokemon.setTypes(new HasMany<>(types));
        pokemon.setMoves(new HasMany<>(moves));
        pokemon.setGender(gender);
        pokemon.setImageSource(imageUri);

        view.showProgress("Saving pokemon", "In progress..");
        interactor.savePokemonToServer(pokemon, new CallbackInt() {
            @Override
            public void onSuccess(Object object) {
                Pokemon createdPokemon = (Pokemon) object;

                interactor.savePokemonToDatabase(createdPokemon);
                view.pokemonAdded(createdPokemon);

                view.hideProgress();
            }

            @Override
            public void onFailure(String message) {
                view.hideProgress();
                if(message.equals("Wrong credentials not authenticated")) {
                    view.showError("Failed to post pokemon, try to relogin.");
                } else {
                    view.showError(message);
                }
            }

            @Override
            public void onCancel() {
                view.hideProgress();
            }
        });
    }

    private boolean dataValid() {
        try {
            Double.parseDouble(height);
            Double.parseDouble(weight);
        } catch (Exception ex) {
            return false;
        }

        if(name.isEmpty() || description.isEmpty() || gender.equals(Gender.UNKNOWN)) {
            return false;
        }
        return true;
    }

    @Override
    public void onAddPokemonImageButtonClicked() {
        checkForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void requestPermission(String permission) {
        ((PokemonAddFragment) view).requestPermissions(new String[] {permission},
                PERMISSION_STORAGE_REQUEST_CODE
        );
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_STORAGE_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    view.showDialogChooser();
                }
                return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {

            if(requestCode == SET_POKEMON_PICTURE_FROM_STORAGE_REQUEST_CODE && data != null) {
                imageUri = data.getData().toString();
            }

            view.setPokemonImage(Uri.parse(imageUri));
        }
    }

    @Override
    public void onTakePhoto() {
        Activity context = ((PokemonAddFragment) view).getActivity();

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = Util.createImageFile();
            } catch (IOException ex) {
                view.showError("Can not make photo file.");
            }

            final String authorities = context.getApplicationContext().getPackageName() + ".fileprovider";
            if(photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(context,
                        authorities,
                        photoFile);

                imageUri = photoUri.toString();

                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                ((PokemonAddFragment) view).startActivityForResult(takePictureIntent, SET_POKEMON_PICTURE_WITH_CAMERA_REQUEST_CODE);
            } else {
                view.showError("Can not make photo file.");
            }

        }
    }

    @Override
    public void onGetImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        Activity context = ((PokemonAddFragment) view).getActivity();

        if(intent.resolveActivity(context.getPackageManager()) != null) {
            ((PokemonAddFragment) view).startActivityForResult(intent, SET_POKEMON_PICTURE_FROM_STORAGE_REQUEST_CODE);
        } else {
            view.showError("No app which would allow yout to choose image from gallery");
        }
    }

    @Override
    public boolean checkDataValidity() {
        return dataValid();
    }

    private void checkForPermission(String permission) {
        Activity context = ((PokemonAddFragment) view).getActivity();

        if(ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {

                showExplanationAndReqestPermission(permission);

            } else {

                requestPermission(permission);

            }
        } else {

            view.showDialogChooser();

        }
    }

    private void showExplanationAndReqestPermission(String permissionGroupToBeExplainedToTheUser) {

        switch(permissionGroupToBeExplainedToTheUser) {
            case android.Manifest.permission.WRITE_EXTERNAL_STORAGE:
                view.showRequestPermissionRationale(R.string.explanation_for_storage_read_permission, permissionGroupToBeExplainedToTheUser);
                break;
            default:
                view.showError("Nepostojeci explanation");
        }
    }
}
