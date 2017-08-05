package com.example.leonardo.pokemonapp.util;

import android.os.Bundle;
import android.view.View;

import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.UI.register.logIn.LogInState;
import com.example.leonardo.pokemonapp.UI.register.signUp.SignUpFragment;
import com.example.leonardo.pokemonapp.UI.register.signUp.SignUpState;
import com.example.leonardo.pokemonapp.base.BaseMVP;

/**
 * Created by leonardo on 04/08/17.
 */

public class StateUtil {

    private static final String LOG_IN_EMAIL_KEY = "log.in.email.key";
    private static final String LOG_IN_PASSWORD_KEY = "log.in.password.key";
    private static final String LOG_IN_PASSWORD_VISIBILITY_DRAWABLE_KEY = "log.in.password.visibility.drawable.key";

    private static final String SIGN_UP_EMAIL_KEY = "sign.up.email.key";
    private static final String SIGN_UP_USERNAME_KEY = "sign.up.username.key";
    private static final String SIGN_UP_PASSWORD_KEY = "sign.up.password.key";
    private static final String SIGN_UP_CONFIRMATION_PASSWORD = "sign.up.confirmation.password.key";

    public static void writeToBundle(Bundle bundle, BaseMVP.State state) {

        if(state instanceof LogInState) {
            LogInState logInState = (LogInState) state;
            bundle.putString(LOG_IN_EMAIL_KEY, logInState.getEmail());
            bundle.putString(LOG_IN_PASSWORD_KEY, logInState.getPassword());
            bundle.putInt(LOG_IN_PASSWORD_VISIBILITY_DRAWABLE_KEY, ((LogInState) state).getPasswordVisibilityDrawable());
        } else if(state instanceof SignUpState) {
            SignUpState signUpState = (SignUpState) state;
            bundle.putString(SIGN_UP_EMAIL_KEY, signUpState.getEmail());
            bundle.putString(SIGN_UP_USERNAME_KEY, signUpState.getUsername());
            bundle.putString(SIGN_UP_PASSWORD_KEY, signUpState.getPassword());
            bundle.putString(SIGN_UP_CONFIRMATION_PASSWORD, signUpState.getConfPassword());
        }

    }

    public static LogInState readFromLogInBundle(Bundle bundle) {
        return new LogInState(
                bundle.getString(LOG_IN_EMAIL_KEY, ""),
                bundle.getString(LOG_IN_PASSWORD_KEY, ""),
                bundle.getInt(LOG_IN_PASSWORD_VISIBILITY_DRAWABLE_KEY, R.drawable.ic_visibility_off)
        );
    }

    public static SignUpState readFromSignUpBundle(Bundle bundle) {
        return new SignUpState(
                bundle.getString(SIGN_UP_EMAIL_KEY, ""),
                bundle.getString(SIGN_UP_USERNAME_KEY, ""),
                bundle.getString(SIGN_UP_PASSWORD_KEY, ""),
                bundle.getString(SIGN_UP_CONFIRMATION_PASSWORD, "")
        );
    }

}
