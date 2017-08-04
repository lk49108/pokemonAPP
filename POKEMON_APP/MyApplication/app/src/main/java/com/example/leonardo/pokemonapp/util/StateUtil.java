package com.example.leonardo.pokemonapp.util;

import android.os.Bundle;

import com.example.leonardo.pokemonapp.UI.register.logIn.LogInState;
import com.example.leonardo.pokemonapp.base.BaseMVP;

/**
 * Created by leonardo on 04/08/17.
 */

public class StateUtil {

    private static final String EMAIL_KEY = "log.in.email.key";
    private static final String PASSWORD_KEY = "log.in.password.key";
    private static final String MILLISECONDS_KEY = "log.in.milliseconds.key";
    private static final String PASSWORD_VISIBILITY_KEY = "log.in.milliseconds.key";

    public static void writeToBundle(Bundle bundle, BaseMVP.State state) {

        if(state instanceof LogInState) {
            LogInState logInState = (LogInState) state;
            bundle.putString(EMAIL_KEY, logInState.getEmail());
            bundle.putString(PASSWORD_KEY, logInState.getPassword());
            bundle.putLong(MILLISECONDS_KEY, logInState.getMillisecondsFromAnimationStart());
            bundle.putBoolean(PASSWORD_VISIBILITY_KEY, ((LogInState) state).isPasswordVisible());
        }

    }

    public static LogInState readFromBundle(Bundle bundle) {
        return new LogInState(
                bundle.getString(EMAIL_KEY, null),
                bundle.getString(PASSWORD_KEY, null),
                bundle.getLong(MILLISECONDS_KEY, -1),
                bundle.getBoolean(PASSWORD_VISIBILITY_KEY, false)
        );
    }

}
