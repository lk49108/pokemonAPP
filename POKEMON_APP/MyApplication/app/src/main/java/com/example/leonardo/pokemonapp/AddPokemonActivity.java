package com.example.leonardo.pokemonapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPokemonActivity extends AppCompatActivity {

    public static final String POKEMON_NAME_KEY = "pokemon.name.key";
    public static final String POKEMON_DESCRIPTION_KEY = "pokemon.description.key";

    private boolean dataChanged = false;

    @BindView(R.id.add_pokemon_activity_pokemon_name_text)
    EditText nameEditTextView;

    @BindView(R.id.add_pokemon_activity_pokemon_description_text)
    EditText descriptionEditTextView;

    @BindView(R.id.add_pokemon_activity_pokemon_save_button)
    Button saveButton;

    @BindView(R.id.toolBarAddPokemon)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pokemon);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameEditTextView.addTextChangedListener(new TextChangedListener());
        descriptionEditTextView.addTextChangedListener(new TextChangedListener());

        saveButton.setOnClickListener((v) -> {
            String name = nameEditTextView.getText().toString().trim();
            String description = descriptionEditTextView.getText().toString().trim();
            if(!dataChanged || name.isEmpty()) {
                Toast.makeText(AddPokemonActivity.this, "Pokemon has to have name.", Toast.LENGTH_LONG).show();
                return;
            }
            Intent resultIntent = new Intent();
            resultIntent.putExtra(POKEMON_NAME_KEY, name);
            resultIntent.putExtra(POKEMON_DESCRIPTION_KEY, description);
            setResult(RESULT_OK, resultIntent);
            AddPokemonActivity.this.finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        final String name = nameEditTextView.getText().toString().trim();

        Toast.makeText(this, String.valueOf(dataChanged), Toast.LENGTH_SHORT).show();
        if(dataChanged && !name.isEmpty()) {
            makeDialog();
            return true;
        }

        Intent resultIntent = new Intent();
        setResult(RESULT_CANCELED, resultIntent);
        finish();
        return true;
    }

    private void makeDialog() {
        AlertDialog.Builder dialoBuilder = new AlertDialog.Builder(this);
        dialoBuilder.setMessage(R.string.save_changes_dialog)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    final String name = nameEditTextView.getText().toString().trim();
                    final String description = descriptionEditTextView.getText().toString().trim();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(POKEMON_NAME_KEY, name);
                    resultIntent.putExtra(POKEMON_DESCRIPTION_KEY, description);
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

    private class TextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            dataChanged = true;
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }
}
