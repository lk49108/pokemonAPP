package com.example.leonardo.pokemonapp.UI.pokemon.pokemonAdd.spinner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.network.resources.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardo on 08/08/17.
 */

public class PokemonMovesMultipleChoiceSpinner extends android.support.v7.widget.AppCompatSpinner implements DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private static final String DEFAULT_MOVES_TEXT = "Not assigned";

    private List<Move> items = Move.getMoves();

    private boolean[] selected = new boolean[items.size()];

    private PokemonMovesListener listener;

    public PokemonMovesMultipleChoiceSpinner(Context context) {
        super(context);
        init();
    }

    public PokemonMovesMultipleChoiceSpinner(Context context, int mode) {
        super(context, mode);
        init();
    }

    public PokemonMovesMultipleChoiceSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PokemonMovesMultipleChoiceSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PokemonMovesMultipleChoiceSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        init();
    }

    public PokemonMovesMultipleChoiceSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
        init();
    }

    public interface PokemonMovesListener {
        void onMovesSelected(Move[] selectedMoves);
    }

    private void init() {
        setAdapter(new ArrayAdapter<String>(getContext(), R.layout.pokemon_moves_spinner_layout, R.id.pokemon_moves_spinner_layout_list, new String[]{DEFAULT_MOVES_TEXT}));
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.pokemon_moves_spinner_layout, R.id.pokemon_moves_spinner_layout_list, new String[]{spinnerText});
        setAdapter(adapter);

        if(listener != null) {
            listener.onMovesSelected(getSelectedMoves());
        }
    }

    private Move[] getSelectedMoves() {
        int numOfSelected = 0;
        for(int i = 0; i < selected.length; i++) {
            if(selected[i]) {
                numOfSelected++;
            }
        }

        Move[] selectedMoves = new Move[numOfSelected];
        int i = 0;
        int j = 0;
        while(i < items.size()) {
            if(selected[i]) {
                selectedMoves[j] = items.get(i);
                j++;
            }

            i++;
        }

        return selectedMoves;
    }

    private String createSpinnerText() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < items.size(); i++) {
            if(selected[i]) {
                builder.append(items.get(i).getName() + ", ");
            }
        }

        if(builder.length() == 0) {
            return DEFAULT_MOVES_TEXT;
        }

        String text = builder.toString();
        return text.substring(0, text.length() - 2);
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Moves");
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

    public void setListener(PokemonMovesListener listener) {
        this.listener = listener;
    }

    public void setSelected(Move...selectedMoves) {
        if(selectedMoves == null || selectedMoves.length == 0) {
            return;
        }

        for(int i = 0; i < selectedMoves.length; i++) {
            if(items.contains(selectedMoves[i])) {
                int position = items.indexOf(selectedMoves[i]);
                selected[position] = true;
            }
        }

        //Refresh layout...
        onCancel(null);
    }
}
