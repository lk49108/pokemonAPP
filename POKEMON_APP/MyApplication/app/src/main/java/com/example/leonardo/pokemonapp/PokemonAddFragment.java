package com.example.leonardo.pokemonapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.util.PokemonResourcesUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by leonardo on 20/07/17.
 */

public class PokemonAddFragment extends Fragment {

    private static final int PERMISSION_STORAGE_REQUEST_CODE = 22;
    private static final int SET_POKEMON_PICTURE_FROM_STORAGE_REQUEST_CODE = 40;
    private static final int SET_POKEMON_PICTURE_WITH_CAMERA_REQUEST_CODE = 41;

    @BindString(R.string.chooseBetweenCameraAndStorage)
    String chooseString;

    @BindView(R.id.fragment_pokemon_add_image_pokemon)
    ImageView fragmentPokemonAddImagePokemon;

    @BindView(R.id.fragment_pokemon_add_option_name)
    EditText fragmentPokemonAddOptionName;

    @BindView(R.id.fragment_pokemon_add_option_height)
    EditText fragmentPokemonAddOptionHeight;

    @BindView(R.id.fragment_pokemon_add_option_weight)
    EditText fragmentPokemonAddOptionWeight;

    @BindView(R.id.fragment_pokemon_add_option_category)
    EditText fragmentPokemonAddOptionCategory;

    @BindView(R.id.fragment_pokemon_add_option_abilities)
    EditText fragmentPokemonAddOptionAbilities;

    @BindView(R.id.fragment_pokemon_add_option_description)
    EditText fragmentPokemonAddOptionDescription;

    @BindView(R.id.fragment_pokemon_add_button_save)
    Button fragmentPokemonAddButtonSave;

    @BindView(R.id.fragment_pokemon_add_button_floating)
    android.support.design.widget.FloatingActionButton fragmentPokemonAddButtonFloating;

    public interface PokemonAddFragmentListener {

        void pokemonAdded(Pokemon pokemon);

        void pokemonCreationCanceled();
    }

    private PokemonAddFragmentListener listener;

    private Uri pokemonImageUri;

    public PokemonAddFragment(){
    }

    public static PokemonAddFragment newInstance() {
        return new PokemonAddFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_add, container, false);

        PokemonMainActivity activity = (PokemonMainActivity) listener;
        if(activity.verticalOrientation()) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this, view);

        if(savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }

        return view;
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        String imageURI = savedInstanceState.getString("imageURI");
        if(imageURI != null) {
            pokemonImageUri = Uri.parse(imageURI);
            if(PokemonResourcesUtil.imageFileExists(pokemonImageUri, getActivity())) {
                Picasso.with(getActivity()).load(pokemonImageUri).into(fragmentPokemonAddImagePokemon);
            } else {
                Picasso.with(getActivity()).load(R.drawable.ic_person).into(fragmentPokemonAddImagePokemon);
            }
        }

        fragmentPokemonAddOptionName.setText(savedInstanceState.getString("name"));
        fragmentPokemonAddOptionHeight.setText(savedInstanceState.getString("height"));
        fragmentPokemonAddOptionWeight.setText(savedInstanceState.getString("weight"));
        fragmentPokemonAddOptionCategory.setText(savedInstanceState.getString("category"));
        fragmentPokemonAddOptionAbilities.setText(savedInstanceState.getString("abilities"));
        fragmentPokemonAddOptionDescription.setText(savedInstanceState.getString("description"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        PokemonMainActivity activity = (PokemonMainActivity) listener;
        if(activity.verticalOrientation()) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof PokemonAddFragmentListener) {
            listener = (PokemonAddFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement PokemonAddFragmentListener");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_STORAGE_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showDialogChooser();
                }
                return;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

            if(requestCode == SET_POKEMON_PICTURE_FROM_STORAGE_REQUEST_CODE && data != null) {
                pokemonImageUri = data.getData();
            }

            //do that always
            Picasso.with(getActivity()).load(pokemonImageUri).into(fragmentPokemonAddImagePokemon);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("imageURI", pokemonImageUri == null ? null : pokemonImageUri.toString());
        outState.putString("name", fragmentPokemonAddOptionName.getText().toString());
        outState.putString("height", fragmentPokemonAddOptionHeight.getText().toString());
        outState.putString("weight", fragmentPokemonAddOptionWeight.getText().toString());
        outState.putString("category", fragmentPokemonAddOptionCategory.getText().toString());
        outState.putString("abilities", fragmentPokemonAddOptionAbilities.getText().toString());
        outState.putString("description", fragmentPokemonAddOptionDescription.getText().toString());

    }


    @OnClick(R.id.fragment_pokemon_add_button_save)
    void savePokemon(){
        final String name = fragmentPokemonAddOptionName.getText().toString().trim();

        if(name.isEmpty()) {
            Toast.makeText(getActivity(), "Pokemon has to have a name.", Toast.LENGTH_LONG).show();
            return;
        }
       Pokemon newPokemon  = createNewPokemon();
        listener.pokemonAdded(newPokemon);
    }

    private Pokemon createNewPokemon() {
        final String name =  fragmentPokemonAddOptionName.getText().toString().trim();
        final String description = fragmentPokemonAddOptionDescription.getText().toString().trim();
        final String height = fragmentPokemonAddOptionHeight.getText().toString().trim();
        final String weight = fragmentPokemonAddOptionWeight.getText().toString().trim();
        final String category = fragmentPokemonAddOptionCategory.getText().toString().trim();
        final String abilities = fragmentPokemonAddOptionAbilities.getText().toString().trim();

        Pokemon newPokemon = new Pokemon(name, description, height, weight, category, abilities, pokemonImageUri);

        return newPokemon;
    }

    @OnClick(R.id.fragment_pokemon_add_button_floating)
    void getPokemonImage() {
        checkForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void checkForPermission(String permission) {
        if(ContextCompat.checkSelfPermission(getActivity(), permission)
                != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {

                showExplanationAndReqestPermission(permission);

            } else {

                requestPermission(permission);

            }
        } else {

            showDialogChooser();

        }
    }

    private void showExplanationAndReqestPermission(String permissionGroupToBeExplainedToTheUser) {
        switch(permissionGroupToBeExplainedToTheUser) {
            case android.Manifest.permission.WRITE_EXTERNAL_STORAGE:

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                dialogBuilder.setMessage(R.string.explanation_for_storage_read_permission)
                        .setPositiveButton(R.string.close, (dialogInterface, i) -> {

                            requestPermission(permissionGroupToBeExplainedToTheUser);
                            dialogInterface.cancel();

                        });

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                break;
            default:
                Toast.makeText(getActivity(), "Nepostojeci explanation", Toast.LENGTH_LONG).show();
        }
    }

    private void requestPermission(String permissionToBeGranted) {
        ActivityCompat.requestPermissions(getActivity(), new String[] {permissionToBeGranted},
                PERMISSION_STORAGE_REQUEST_CODE
        );
    }

    private void showDialogChooser() {
        final CharSequence[] options = { "Take photo", "Choose from gallery", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(chooseString)
                .setItems(options, (dialogInterface, item) -> {
                    String storageState = Environment.getExternalStorageState();
                    if(!Environment.MEDIA_MOUNTED.equals(storageState)) {
                        dialogInterface.cancel();
                        Toast.makeText(getActivity(), "Storage not mounted", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (options[item].equals("Take photo")) {
                        takePhoto();
                    } else if(options[item].equals("Choose from gallery")) {
                        getImageFromGallery();
                    }

                    dialogInterface.cancel();
                });

        builder.create().show();
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), "Can not make photo file", Toast.LENGTH_LONG).show();
            }

            if(photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(getActivity(),
                        "com.pokemon.android.fileprovider",
                        photoFile);
                pokemonImageUri = photoUri;

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, SET_POKEMON_PICTURE_WITH_CAMERA_REQUEST_CODE);
            } else {
                Toast.makeText(getActivity(), "Can not make photo file", Toast.LENGTH_LONG).show();
            }

        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_pokemon.jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File applicationDirectory = new File(storageDir, "/pokemonApp");
        if(!applicationDirectory.exists()) {
            applicationDirectory.mkdir();
        }

        File image = new File(applicationDirectory, imageFileName);

        return image;
    }

    private void getImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, SET_POKEMON_PICTURE_FROM_STORAGE_REQUEST_CODE);
        } else {
            Toast.makeText(getActivity(), "No app which would allow yout to choose image from gallery", Toast.LENGTH_LONG).show();
        }
    }

    public void makeDialog() {
            AlertDialog.Builder dialoBuilder = new AlertDialog.Builder(getActivity());
            dialoBuilder.setMessage(R.string.save_changes_dialog)
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                        Pokemon newPokemon = createNewPokemon();
                        listener.pokemonAdded(newPokemon);

                        dialogInterface.cancel();
                    })
                    .setNegativeButton(R.string.no, ((dialogInterface, i) -> {
                        listener.pokemonCreationCanceled();

                        dialogInterface.cancel();
                    }));

            AlertDialog dialog = dialoBuilder.create();
            dialog.show();
    }

    public void resetLayoutState(){
        pokemonImageUri = null;
        Picasso.with(getActivity()).load(R.drawable.ic_person).into(fragmentPokemonAddImagePokemon);

        fragmentPokemonAddOptionDescription.setText("");
        fragmentPokemonAddOptionAbilities.setText("");
        fragmentPokemonAddOptionName.setText("");
        fragmentPokemonAddOptionCategory.setText("");
        fragmentPokemonAddOptionWeight.setText("");
        fragmentPokemonAddOptionHeight.setText("");
    }

    public boolean shouldShowSavePokemonDialog() {
        if(!fragmentPokemonAddOptionName.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

}
