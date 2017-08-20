package com.example.leonardo.pokemonapp.UI.pokemon.pokemonAdd.spinner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.network.resources.Type;

import java.util.List;

/**
 * Created by leonardo on 09/08/17.
 */

public class PokemonTypeMultipleChoiceSpinner extends android.support.v7.widget.AppCompatSpinner implements DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private static final String DEFAULT_TYPE_TEXT = "Not assigned";

    private List<Type> items = Type.getTypes();
    private boolean[] selected = new boolean[items.size()];

    private PokemonTypeMultipleChoiceSpinner.PokemonTypeListener listener;

    public PokemonTypeMultipleChoiceSpinner(Context context) {
        super(context);
        init();
    }

    public PokemonTypeMultipleChoiceSpinner(Context context, int mode) {
        super(context, mode);
        init();
    }

    public PokemonTypeMultipleChoiceSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PokemonTypeMultipleChoiceSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PokemonTypeMultipleChoiceSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        init();
    }

    public PokemonTypeMultipleChoiceSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
        init();
    }

    public interface PokemonTypeListener {
        void onTypesSelected(Type[] selectedTypes);
    }

    private void init() {
        setAdapter(new ArrayAdapter<String>(getContext(), R.layout.pokemon_type_spinner_layout, R.id.pokemon_type_spinner_layout_list, new String[]{DEFAULT_TYPE_TEXT}));
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if(which >= 0 && which < items.size()) {
            selected[which] = isChecked;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        String spinnerText = createSpinnerText();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.pokemon_type_spinner_layout, R.id.pokemon_type_spinner_layout_list, new String[]{spinnerText});
        setAdapter(adapter);

        if(listener != null) {
            listener.onTypesSelected(getSelectedTypes());
        }
    }

    private Type[] getSelectedTypes() {
        int numOfSelected = 0;
        for(int i = 0; i < selected.length; i++) {
            if(selected[i]) {
                numOfSelected++;
            }
        }

        Type[] selectedTypes = new Type[numOfSelected];
        int i = 0;
        int j = 0;
        while(i < items.size()) {
            if(selected[i]) {
                selectedTypes[j] = items.get(i);
                j++;
            }

            i++;
        }

        for(int s = 0; s < selectedTypes.length; s++) {
            Log.d("provjera", selectedTypes[s].getName());
        }

        return selectedTypes;
    }

    private String createSpinnerText() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < items.size(); i++) {
            if(selected[i]) {
                builder.append(items.get(i).getName() + ", ");
            }
        }

        if(builder.length() == 0) {
            return DEFAULT_TYPE_TEXT;
        }

        String text = builder.toString();
        return text.substring(0, text.length() - 2);
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Types");
        builder.setMultiChoiceItems(getItemsStringArray(), selected, this);
        builder.setPositiveButton("Done", (dialog, which) -> {
            dialog.cancel();
        });
        builder.setOnCancelListener(this);
        builder.create().show();

        return true;
    }

    private String[] getItemsStringArray() {
        String[] array = new String[items.size()];
        for(int i = 0; i < array.length; i++) {
            array[i] = items.get(i).getName();
        }

        return array;
    }

    public void setListener(PokemonTypeMultipleChoiceSpinner.PokemonTypeListener listener) {
        this.listener = listener;
    }

    public void setSelected(Type...selectedTypes) {
        if(selectedTypes == null || selectedTypes.length == 0) {
            return;
        }

        for(int i = 0; i < selectedTypes.length; i++) {
            if(items.contains(selectedTypes[i])) {
                int position = items.indexOf(selectedTypes[i]);
                selected[position] = true;
            }
        }

        //Refresh layout...
        onCancel(null);
    }
}
