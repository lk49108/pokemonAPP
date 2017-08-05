package com.example.leonardo.pokemonapp.UI.register.signUp;

import android.support.v4.app.Fragment;

import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.base.baseFragment.BaseFragmentMVP;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;

/**
 * Created by leonardo on 04/08/17.
 */

public interface SignUpMVP {

    interface View extends BaseFragmentMVP.View {

        void setListener(SignUpFragment.SignUpFragmentListener listener);

        void setEmail(String email);

        void setUserName(String userName);

        void setPassword(String password);

        void setConfirmationPassword(String passwordConfirmation);

        void navigateToPokemonListScreen();

    }

    interface Presenter extends BaseFragmentMVP.Presenter {

        void onSignUpClicked();

        void onEmailChanged(String email);

        void onUserNameChanged(String username);

        void onPasswordChanged(String password);

        void onConfirmationPasswordChanged(String confPassword);

    }

    interface State extends BaseMVP.State {}

    interface Interactor extends BaseMVP.Interactor {

        void SignUp(String email, String username, String password, String confPassword, CallbackInt callback);

    }


}
