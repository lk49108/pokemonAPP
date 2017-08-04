package com.example.leonardo.pokemonapp.base.baseFragment;

import android.content.Context;

import com.example.leonardo.pokemonapp.base.BaseMVP;

/**
 * Created by leonardo on 04/08/17.
 */

public interface BaseFragmentMVP {

    interface View extends BaseMVP.View {

        void setListener(Context context);

        void showProgress(String title, String message);

        void hideProgress();

    }

    interface Presenter extends BaseMVP.Presenter {

        void onAttach(Context context);

        void subscribe(BaseMVP.State state);

        void unsubscribe();

        BaseMVP.State getState();

    }

}
