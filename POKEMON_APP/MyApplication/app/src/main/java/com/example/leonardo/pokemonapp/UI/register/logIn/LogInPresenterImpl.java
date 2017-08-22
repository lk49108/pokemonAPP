package com.example.leonardo.pokemonapp.UI.register.logIn;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.PermissionApp;
import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.UI.register.RegisterActivity;
import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.resources.User;
import com.example.leonardo.pokemonapp.sharedPref.SimpleSharedPrefsImpl;
import com.example.leonardo.pokemonapp.util.LogInAnimation;
import com.example.leonardo.pokemonapp.util.UserUtil;
import com.example.leonardo.pokemonapp.util.Util;

/**
 * Created by leonardo on 04/08/17.
 */

public class LogInPresenterImpl implements LogInMVP.Presenter, LogInAnimation.LogInAnimationListener {

    private LogInMVP.View view;
    private LogInMVP.Interactor interactor;

    private String email;
    private String password;
    private int passwordVisibilityDrawable;

    public LogInPresenterImpl(LogInMVP.View view) {
        this.view = view;
        interactor = new LogInInteractorImpl();
    }

    @Override
    public void cancelCall() {
        interactor.cancel();
    }

    @Override
    public void onAttach(Context context) {
        if(context instanceof LogInFragment.LogInFragmentListener) {
            view.setListener((LogInFragment.LogInFragmentListener) context);
        } else {
            throw new RuntimeException(context.toString() + " must implement LogInFragmentListener");
        }
    }

    @Override
    public void subscribe(BaseMVP.State savedInstanceState) {

        if(savedInstanceState == null) {
            if(email == null) {
                email = "";
            } else {
                view.setEmail(email);
            }
            if(password == null) {
                password = "";
            } else {
                view.setPassword(password);
            }
            if(passwordVisibilityDrawable == 0) {
                passwordVisibilityDrawable = R.drawable.ic_visibility_off;
            } else {
                view.setPasswordVisibilityDrawable(passwordVisibilityDrawable);
            }
            return;
        }

        LogInState state = (LogInState) savedInstanceState;
        email = state.getEmail();
        password = state.getPassword();
        passwordVisibilityDrawable = state.getPasswordVisibilityDrawable();

        view.setEmail(email);
        view.setPassword(password);
        view.setPasswordVisibilityDrawable(passwordVisibilityDrawable);

    }

    @Override
    public void unsubscribe() {
        email = null;
        password = null;
    }

    @Override
    public BaseMVP.State getState() {
        view.stopAnimation();
        return new LogInState(email, password, passwordVisibilityDrawable);
    }

    @Override
    public void onLogInPressed() {
        if(!Util.internetConnectionActive()) {
            view.showError("No active internet connection");
            return;
        }

        if(email.isEmpty() || password.isEmpty()) {
            view.showError("Empty fields are not allowed");
            return;
        }

        if(!UserUtil.validEmail(email)) {
            view.showError("Invalid email");
            return;
        }

        if(password.length() < 8) {
            view.showError("Password has to contain at least 8 characters");
            return;
        }

        view.showProgress("Log in progress", "Logging onto server...");
        interactor.logIn(email, password, new CallbackInt() {

            @Override
            public void onSuccess(Object object) {
                User loggedInUser = (User) object;
                UserUtil.logInUser(loggedInUser);

                view.hideProgress();
                view.navigateToPokemonListScreen();
            }

            @Override
            public void onFailure(String message) {
                view.hideProgress();
                view.showError(message);
            }

            @Override
            public void onCancel() {
                view.hideProgress();
            }
        });
    }

    @Override
    public void onResume() {
        if(LogInAnimation.getInstance((LogInFragment) view, this).isAnimationFinished()) {
            view.switchPokeBallVisibility(View.VISIBLE);
            view.switchPokemonLogoVisibility(View.VISIBLE);
            view.switchlayoutVisibility(View.VISIBLE);
            return;
        }

        view.showAnimation();
    }

    @Override
    public void onEmailTextChanged(String email) {
        this.email = email;
    }

    @Override
    public void onPasswordTextChange(String password) {
        this.password = password;
    }

    @Override
    public void onPasswordVisiblityChange(int visibilityDrawable) {
        this.passwordVisibilityDrawable = visibilityDrawable;
    }

    @Override
    public void onAnimationEnd() {
        if(UserUtil.isLoggedIn()) {
            view.navigateToPokemonListScreen();
        } else {
            view.switchlayoutVisibility(View.VISIBLE);
        }
    }
}
