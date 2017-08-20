package com.example.leonardo.pokemonapp.UI.pokemon.pokemonAdd;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.UI.pokemon.pokemonAdd.spinner.PokemonMovesMultipleChoiceSpinner;
import com.example.leonardo.pokemonapp.UI.pokemon.pokemonAdd.spinner.PokemonTypeMultipleChoiceSpinner;
import com.example.leonardo.pokemonapp.base.BaseFragment;
import com.example.leonardo.pokemonapp.network.resources.Move;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.network.resources.Type;
import com.example.leonardo.pokemonapp.network.resources.enumerations.Gender;
import com.example.leonardo.pokemonapp.util.StateUtil;
import com.example.leonardo.pokemonapp.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by leonardo on 20/07/17.
 */

public class PokemonAddFragment extends BaseFragment implements PokemonAddMVP.View, PokemonTypeMultipleChoiceSpinner.PokemonTypeListener, PokemonMovesMultipleChoiceSpinner.PokemonMovesListener {

    public interface PokemonAddFragmentListener {

        void pokemonAdded(Pokemon pokemon);

        void pokemonCreationCanceled();
    }

    private PokemonAddFragmentListener listener;

    private PokemonAddMVP.Presenter presenter;

    @BindString(R.string.chooseBetweenCameraAndStorage)
    String chooseString;

    @BindView(R.id.fragment_pokemon_add_move_spinner)
    PokemonMovesMultipleChoiceSpinner moveSpinner;
    @BindView(R.id.fragment_pokemon_add_type_spinner)
    PokemonTypeMultipleChoiceSpinner typeSpinner;
    @BindView(R.id.fragment_pokemon_add_image_pokemon)
    ImageView fragmentPokemonAddImagePokemon;
    @BindView(R.id.fragment_pokemon_add_option_name)
    EditText fragmentPokemonAddOptionName;
    @BindView(R.id.fragment_pokemon_add_option_height)
    EditText fragmentPokemonAddOptionHeight;
    @BindView(R.id.fragment_pokemon_add_option_weight)
    EditText fragmentPokemonAddOptionWeight;
    @BindView(R.id.fragment_pokemon_add_option_description)
    EditText fragmentPokemonAddOptionDescription;
    @BindView(R.id.fragment_pokemon_add_button_save)
    Button fragmentPokemonAddButtonSave;
    @BindView(R.id.fragment_pokemon_add_button_floating)
    android.support.design.widget.FloatingActionButton fragmentPokemonAddButtonFloating;
    @BindView(R.id.fragment_pokemon_add_radio_button_f)
    RadioButton radioButtonFemale;
    @BindView(R.id.fragment_pokemon_add_radio_button_m)
    RadioButton radioButtonMale;

    @OnClick(R.id.fragment_pokemon_add_radio_button_f)
    void onRadioButtonFemaleClick() {
        presenter.onPokemonGenderChanged(Gender.FEMALE);
    }

    @OnClick(R.id.fragment_pokemon_add_radio_button_m)
    void onRadioButtonMaleClick() {
        presenter.onPokemonGenderChanged(Gender.MALE);
    }

    @OnTextChanged(R.id.fragment_pokemon_add_option_name)
    void onNameChanged() {
        presenter.onPokemonNameChanged(fragmentPokemonAddOptionName.getText().toString());
    }

    @OnTextChanged(R.id.fragment_pokemon_add_option_height)
    void onHeightChanged() {
        presenter.onPokemonHeightChanged(fragmentPokemonAddOptionHeight.getText().toString());
    }

    @OnTextChanged(R.id.fragment_pokemon_add_option_weight)
    void onWeightChanged() {
        presenter.onPokemonWeightChanged(fragmentPokemonAddOptionWeight.getText().toString());
    }

    @OnTextChanged(R.id.fragment_pokemon_add_option_description)
    void onDescriptionChanged() {
        presenter.onPokemonDescriptionChanged(fragmentPokemonAddOptionDescription.getText().toString());
    }

    @Override
    public void setListener(PokemonAddFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void setPokemonName(String name) {
        fragmentPokemonAddOptionName.setText(name);
    }

    @Override
    public void setPokemonDescription(String description) {
        fragmentPokemonAddOptionDescription.setText(description);
    }

    @Override
    public void setPokemonWeight(String weight) {
        fragmentPokemonAddOptionWeight.setText(weight);
    }

    @Override
    public void setPokemonHeight(String height) {
        fragmentPokemonAddOptionHeight.setText(height);
    }

    @Override
    public void showRequestPermissionRationale(int message, String permissionGroupToBeAskedFor) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage(R.string.explanation_for_storage_read_permission)
                .setPositiveButton(R.string.close, (dialogInterface, i) -> {

                    presenter.requestPermission(permissionGroupToBeAskedFor);
                    dialogInterface.cancel();
                });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void showDialogChooser() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.pokemon_add_photo_dialog, null);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        Button takePhoto = (Button) dialogView.findViewById(R.id.pokemon_add_photo_dialog_button_take_photo);
        Button choosePhoto = (Button) dialogView.findViewById(R.id.pokemon_add_photo_dialog_button_choose_photo);

        takePhoto.setOnClickListener((v) -> {
            if(!Util.storageMounted()) {
                alertDialog.dismiss();
                showError("Storage not mounted");
                return;
            }

            presenter.onTakePhoto();
            alertDialog.dismiss();
        });

        choosePhoto.setOnClickListener((v) -> {
            if(!Util.storageMounted()) {
                alertDialog.dismiss();
                showError("Storage not mounted");
                return;
            }

            presenter.onGetImageFromGallery();
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    @Override
    public void setPokemonImage(Uri imageUri) {
        Picasso.with(getActivity()).load(imageUri).into(fragmentPokemonAddImagePokemon);
    }

    @Override
    public void setPokemonImage(int drawable) {
        Picasso.with(getActivity()).load(drawable).into(fragmentPokemonAddImagePokemon);
    }

    @Override
    public void pokemonAdded(Pokemon pokemon) {
        listener.pokemonAdded(pokemon);
    }

    @Override
    public void setPokemonTypes(Type[] types) {
        typeSpinner.setSelected(types);
    }

    @Override
    public void setPokemonMoves(Move[] moves) {
        moveSpinner.setSelected(moves);
    }

    @Override
    public void setFemaleGender() {
        radioButtonFemale.setSelected(true);
    }

    @Override
    public void setMaleGender() {
        radioButtonMale.setSelected(true);
    }

    public PokemonAddFragment(){
    }

    public static PokemonAddFragment newInstance() {
        return new PokemonAddFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        presenter = new PokemonAddPresenterImpl(this);
        presenter.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_add, container, false);

        ButterKnife.bind(this, view);

        typeSpinner.setListener(this);
        moveSpinner.setListener(this);

        presenter.subscribe(savedInstanceState == null ? null : StateUtil.readFromPokemonAddBundle(savedInstanceState));

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        presenter.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
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
    public void onStop() {
        presenter.cancelCall();

        super.onStop();
    }

    @OnClick(R.id.fragment_pokemon_add_button_save)
    void savePokemon(){
        presenter.onButtonSaveCLicked();
    }

    @OnClick(R.id.fragment_pokemon_add_button_floating)
    void getPokemonImage() {
        presenter.onAddPokemonImageButtonClicked();
    }

    public void makeDialog() {
            AlertDialog.Builder dialoBuilder = new AlertDialog.Builder(getActivity());
            dialoBuilder.setMessage(R.string.save_changes_dialog)
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                        presenter.onButtonSaveCLicked();

                        dialogInterface.cancel();
                    })
                    .setNegativeButton(R.string.no, ((dialogInterface, i) -> {
                        listener.pokemonCreationCanceled();

                        dialogInterface.cancel();
                    }));

            AlertDialog dialog = dialoBuilder.create();
            dialog.show();
    }

    public boolean shouldShowSavePokemonDialog() {
        return presenter.checkDataValidity();
    }

    @Override
    public void onTypesSelected(Type[] selectedTypes) {
        presenter.onPokemonTypesChanged(selectedTypes);
    }

    @Override
    public void onMovesSelected(Move[] selectedMoves) {
        presenter.onPokemonMovesChangedChanged(selectedMoves);
    }


}
