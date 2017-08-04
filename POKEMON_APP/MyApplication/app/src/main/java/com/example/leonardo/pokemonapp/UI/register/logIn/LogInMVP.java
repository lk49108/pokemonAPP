package com.example.leonardo.pokemonapp.UI.register.logIn;

import android.animation.AnimatorSet;

import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.base.baseFragment.BaseFragmentMVP;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;

/**
 * Created by leonardo on 04/08/17.
 */

public interface LogInMVP {

    interface View extends BaseFragmentMVP.View {

        void showAnimation(AnimatorSet set);

        void setEmail(String email);

        void setPassword(String password);

        void switchlayoutVisibility(int visibility);

        void switchPokeBallVisibility(int visibility);

        void switchPokemonLogoVisibility(int visibility);

        void navigateToPokemonListScreen();

    }

    interface State extends BaseMVP.State{}

    interface Presenter extends BaseFragmentMVP.Presenter {

        void onLogInPressed(String email, String password);

        void onResume();

    }

    interface Interactor extends BaseMVP.Interactor {

        void logIn(String email, String password, CallbackInt callback);

    }

}
