package com.example.leonardo.pokemonapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import butterknife.OnTextChanged;

public class AddPokemonActivity extends AppCompatActivity {

    private String fileImagePath;

    private Uri pokemonImageUri;

    private static final int PERMISSION_STORAGE_REQUEST_CODE = 22;
    private static final int SET_POKEMON_PICTURE_FROM_STORAGE_REQUEST_CODE = 40;
    private static final int SET_POKEMON_PICTURE_WITH_CAMERA_REQUEST_CODE = 41;


    public static final String POKEMON_KEY = "pokemon.key";

    private boolean dataChanged = false;

    @BindString(R.string.chooseBetweenCameraAndStorage)
    String chooseString;

    @BindView(R.id.add_pokemon_activity_pokemon_picture)
    ImageView pokemonImage;

    @BindView(R.id.add_pokemon_activity_pokemon_save_button)
    Button saveButton;

    @BindView(R.id.toolBarAddPokemon)
    Toolbar toolbar;

    @BindView(R.id.add_pokemon_activity_floating_button)
    FloatingActionButton addPictureButton;

    @BindView(R.id.add_pokemon_activity_pokemon_name)
    View pokemonNameView;
    private final IncludeViewElements pokemonNameElement = new IncludeViewElements();

    @BindView(R.id.add_pokemon_activity_pokemon_height)
    View pokemonHeightView;
    private final IncludeViewElements pokemonHeightElement = new IncludeViewElements();

    @BindView(R.id.add_pokemon_activity_pokemon_weight)
    View pokemonWeightView;
    private final IncludeViewElements pokemonWeightElement = new IncludeViewElements();

    @BindView(R.id.add_pokemon_activity_pokemon_category)
    View pokemonCategoryView;
    private final IncludeViewElements pokemonCategoryElement = new IncludeViewElements();

    @BindView(R.id.add_pokemon_activity_pokemon_abilities)
    View pokemonAbilitiesView;
    private final IncludeViewElements pokemonAbilitiesElement = new IncludeViewElements();

    @BindView(R.id.add_pokemon_activity_pokemon_description)
    View pokemonDescriptionView;
    private final IncludeViewElements pokemonDescriptionElement = new IncludeViewElements();

    class IncludeViewElements {
        @BindView(R.id.pokemon_option_image_view)
        ImageView imageView;
        @BindView(R.id.pokemon_option_edit_text_view)
        EditText editText;

        @OnTextChanged(value = R.id.pokemon_option_edit_text_view,
                callback = OnTextChanged.Callback.TEXT_CHANGED)
        void onTextChangedMethod() {
            dataChanged = true;
        }
    }

    @OnClick(R.id.add_pokemon_activity_floating_button)
    void setPokemonImage() {
        checkForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void showDialogChooser() {
        final CharSequence[] options = { "Take photo", "Choose from gallery", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(chooseString)
                .setItems(options, (dialogInterface, item) -> {
                    String storageState = Environment.getExternalStorageState();
                    if(!Environment.MEDIA_MOUNTED.equals(storageState)) {
                        dialogInterface.cancel();
                        Toast.makeText(AddPokemonActivity.this, "Storage not mounted", Toast.LENGTH_LONG).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_STORAGE_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showDialogChooser();
                } else {

                }
                return;
        }
    }

    private void checkForPermission(String permission) {
        if(ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                showExplanationAndReqestPermission(permission);

            } else {

                requestPermission(permission);

            }
        } else {

            showDialogChooser();

        }
    }

    private void requestPermission(String permissionToBeGranted) {
        ActivityCompat.requestPermissions(this, new String[] {permissionToBeGranted},
                PERMISSION_STORAGE_REQUEST_CODE
        );
    }

    private void showExplanationAndReqestPermission(String permissionGroupToBeExplainedToTheUser) {
        switch(permissionGroupToBeExplainedToTheUser) {
            case android.Manifest.permission.WRITE_EXTERNAL_STORAGE:

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setMessage(R.string.explanation_for_storage_read_permission)
                        .setPositiveButton(R.string.close, (dialogInterface, i) -> {

                            requestPermission(permissionGroupToBeExplainedToTheUser);
                            dialogInterface.cancel();

                        });

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                break;
            default:
                Toast.makeText(this, "Nepostojeci explanation", Toast.LENGTH_LONG).show();
        }
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Can not make photo file", Toast.LENGTH_LONG).show();
            }

            if(photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                    "com.pokemon.android.fileprovider",
                    photoFile);
                pokemonImageUri = photoUri;

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, SET_POKEMON_PICTURE_WITH_CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        fileImagePath = image.getAbsolutePath();
        return image;
    }

    private void getImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, SET_POKEMON_PICTURE_FROM_STORAGE_REQUEST_CODE);
        } else {
            Toast.makeText(this, "No app which would allow yout to choose image from gallery", Toast.LENGTH_LONG).show();
        }
    }

    private void initializeViews() {
        ButterKnife.bind(this);
        ButterKnife.bind(pokemonNameElement, pokemonNameView);
        ButterKnife.bind(pokemonHeightElement, pokemonHeightView);
        ButterKnife.bind(pokemonWeightElement, pokemonWeightView);
        ButterKnife.bind(pokemonCategoryElement, pokemonCategoryView);
        ButterKnife.bind(pokemonAbilitiesElement, pokemonAbilitiesView);
        ButterKnife.bind(pokemonDescriptionElement, pokemonDescriptionView);

        initializeIcons();
        initializeHints();
    }

    private void initializeHints() {
        pokemonNameElement.editText.setHint("Name");
        pokemonHeightElement.editText.setHint("Height");
        pokemonWeightElement.editText.setHint("Weight");
        pokemonCategoryElement.editText.setHint("Category");
        pokemonAbilitiesElement.editText.setHint("Abilities");
        pokemonDescriptionElement.editText.setHint("Description");
    }

    private void initializeIcons() {
        pokemonNameElement.imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_person));
        pokemonDescriptionElement.imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_description));
    }

    @OnClick(R.id.add_pokemon_activity_pokemon_save_button)
    void onSave(){
        final String name = pokemonNameElement.editText.getText().toString().trim();

        if(!dataChanged || name.isEmpty()) {
            Toast.makeText(AddPokemonActivity.this, "Pokemon has to have a name.", Toast.LENGTH_LONG).show();
            return;
        }

        Intent resultIntent = createResultPokemonIntent();
        setResult(RESULT_OK, resultIntent);
        AddPokemonActivity.this.finish();
    }

    private Intent createResultPokemonIntent() {
        final String name = pokemonNameElement.editText.getText().toString().trim();
        final String description = pokemonDescriptionElement.editText.getText().toString().trim();
        final String height = pokemonHeightElement.editText.getText().toString().trim();
        final String weight = pokemonWeightElement.editText.getText().toString().trim();
        final String category = pokemonCategoryElement.editText.getText().toString().trim();
        final String abilities = pokemonAbilitiesElement.editText.getText().toString().trim();

        Intent resultIntent = new Intent();
        Pokemon newPokemon = new Pokemon(name, description, height, weight, category, abilities, pokemonImageUri);
        resultIntent.putExtra(POKEMON_KEY, newPokemon);
        return resultIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pokemon);

        initializeViews();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        final String name = pokemonNameElement.editText.getText().toString().trim();

        if(dataChanged && !name.isEmpty()) {
            makeDialog();
            return true;
        }

        Intent resultIntent = new Intent();
        setResult(RESULT_CANCELED, resultIntent);
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

            if(requestCode == SET_POKEMON_PICTURE_FROM_STORAGE_REQUEST_CODE && data != null) {
                pokemonImageUri = data.getData();
            } else if (requestCode == SET_POKEMON_PICTURE_WITH_CAMERA_REQUEST_CODE) {
                pokemonImageUri = Uri.fromFile(new File(fileImagePath));
                //makePhotoAvailableInGallery();
            }

            Picasso.with(this).load(pokemonImageUri).into(pokemonImage);
        }
    }

   /* private void makePhotoAvailableInGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(pokemonImageUri);
        sendBroadcast(mediaScanIntent);
    }
    */

    private void makeDialog() {
        AlertDialog.Builder dialoBuilder = new AlertDialog.Builder(this);
        dialoBuilder.setMessage(R.string.save_changes_dialog)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    Intent resultIntent = createResultPokemonIntent();
                    setResult(RESULT_OK, resultIntent);

                    dialogInterface.cancel();
                    AddPokemonActivity.this.finish();
                })
                .setNegativeButton(R.string.no, ((dialogInterface, i) -> {
                    Intent resultIntent = new Intent();
                    setResult(RESULT_CANCELED, resultIntent);

                    dialogInterface.cancel();
                    AddPokemonActivity.this.finish();
                }));

        AlertDialog dialog = dialoBuilder.create();
        dialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("imageURI", pokemonImageUri == null ? null : pokemonImageUri.toString());
        outState.putString("name", pokemonNameElement.editText.getText().toString());
        outState.putString("height", pokemonHeightElement.editText.getText().toString());
        outState.putString("weight", pokemonWeightElement.editText.getText().toString());
        outState.putString("category", pokemonCategoryElement.editText.getText().toString());
        outState.putString("abilities", pokemonAbilitiesElement.editText.getText().toString());
        outState.putString("description", pokemonDescriptionElement.editText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        String imageURI = savedInstanceState.getString("imageURI");
        if(imageURI != null) {
            Uri imageUri = Uri.parse(imageURI);
            pokemonImageUri = imageUri;
            if(PokemonResourcesUtil.imageFileExists(imageUri, this)) {
                Picasso.with(this).load(imageUri).into(pokemonImage);
            }
        }

        pokemonNameElement.editText.setText(savedInstanceState.getString("name"));
        pokemonHeightElement.editText.setText(savedInstanceState.getString("height"));
        pokemonWeightElement.editText.setText(savedInstanceState.getString("weight"));
        pokemonCategoryElement.editText.setText(savedInstanceState.getString("category"));
        pokemonAbilitiesElement.editText.setText(savedInstanceState.getString("abilities"));
        pokemonDescriptionElement.editText.setText(savedInstanceState.getString("description"));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            onSupportNavigateUp();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}