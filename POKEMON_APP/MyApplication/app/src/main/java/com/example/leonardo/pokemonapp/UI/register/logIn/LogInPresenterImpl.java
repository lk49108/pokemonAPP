package com.example.leonardo.pokemonapp.UI.register.logIn;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.UI.register.RegisterActivity;
import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.util.AnimationUtil;
import com.example.leonardo.pokemonapp.util.StateUtil;
import com.example.leonardo.pokemonapp.util.UserUtil;

/**
 * Created by leonardo on 04/08/17.
 */

public class LogInPresenterImpl implements LogInMVP.Presenter {

    private LogInMVP.View view;
    private LogInMVP.Interactor interactor;

    private String email;
    private String password;
    private long millisecondsFromAnimationBeggining;
    private boolean passwordVisible;

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
        if(context instanceof RegisterActivity) {
            view.setListener(context);
        } else {
            throw new RuntimeException(context.toString() + " must implement LogInFragmentListener");
        }
    }

    @Override
    public void subscribe(BaseMVP.State savedInstanceState) {

        if(savedInstanceState == null) {
            return;
        }

        LogInState state = (LogInState) savedInstanceState;
        email = state.getEmail();
        password = state.getPassword();
        millisecondsFromAnimationBeggining = state.getMillisecondsFromAnimationStart();
        passwordVisible = state.isPasswordVisible();

        //TODO
        if(millisecondsFromAnimationBeggining < )
        view.setEmail(email);
        view.setPassword(password);
    }

    @Override
    public void unsubscribe() {
        email = null;
        password = null;
    }

    @Override
    public BaseMVP.State getState() {
        return new LogInState(email, password, millisecondsFromAnimationBeggining, passwordVisible);
    }

    @Override
    public void onLogInPressed(String email, String password) {
        interactor.logIn(email, password, new CallbackInt() {

            @Override
            public void onSuccess(Object object) {
                view.navigateToPokemonListScreen();
            }

            @Override
            public void onFailure(String message) {
                view.showError(message);
            }

        });
    }

    @Override
    public void onResume() {
        if(millisecondsFromAnimationBeggining > 2500 ) {
            view.switchPokeBallVisibility(View.VISIBLE);
            view.switchPokemonLogoVisibility(View.VISIBLE);
            view.switchlayoutVisibility(View.VISIBLE);
            return;
        }

        AnimatorSet animation = AnimationUtil.getLogInAnimation((LogInFragment)view, this, millisecondsFromAnimationBeggining);

    }

}
