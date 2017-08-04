package com.example.leonardo.pokemonapp.UI.register.signUp;

import com.example.leonardo.pokemonapp.base.BaseMVP;

/**
 * Created by leonardo on 04/08/17.
 */

public interface SignUpMVP {

    interface SignUpView extends BaseMVP.View {

        void setEmail(String email);

        void setUserName(String userName);

        void setPassword(String password);

        void setPasswordConfirmation(String passwordConfirmation);

    }

    interface SignUpPresenter extends BaseMVP.Presenter {

        void onSignUpClicked(String email, String userName, String password, String passwordConfirmation);

    }



}
