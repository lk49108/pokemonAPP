package com.example.leonardo.pokemonapp.drawer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leonardo.pokemonapp.R;

/**
 * Created by leonardo on 24/07/17.
 */

public class DrawerFragment extends Fragment {

    public DrawerFragment() {
    }

    public static DrawerFragment newInstance() {
        return new DrawerFragment();
    }

    private Context listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = context;
    }
}
