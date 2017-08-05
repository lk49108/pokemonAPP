package com.example.leonardo.pokemonapp.UI.register.signUp;

import android.content.Context;

import com.example.leonardo.pokemonapp.UI.register.RegisterActivity;
import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.resources.User;
import com.example.leonardo.pokemonapp.util.StateUtil;
import com.example.leonardo.pokemonapp.util.UserUtil;

/**
 * Created by leonardo on 05/08/17.
 */

public class SignUpPresenterImpl implements SignUpMVP.Presenter {

    private SignUpMVP.View view;
    private SignUpMVP.Interactor interactor;

    private String email;
    private String username;
    private String password;
    private String confPassword;

    public SignUpPresenterImpl(SignUpMVP.View view) {
        this.view = view;
        interactor = new SignUpInteractorImpl();
    }

    @Override
    public void cancelCall() {
        interactor.cancel();
    }

    @Override
    public void onSignUpClicked() {

        if(email.isEmpty() || username.isEmpty() || password.isEmpty() || confPassword.isEmpty()) {
            view.showError("No empty fields allowed");
            return;
        }

        if(!UserUtil.validEmail(email)) {
            view.showError("Email is not valid");
            return;
        }

        if (!password.equals(confPassword)) {
            view.showError("Passwords do not match");
            return;
        }

        if(password.length() < 8) {
            view.showError("Password should be at least 8 characters long");
            return;
        }

        view.showProgress("Sign up progress", "Signing up onto server...");
        interactor.SignUp(email, username, password, confPassword, new CallbackInt() {
            @Override
            public void onSuccess(Object object) {
                User createdUser = (User) object;
                UserUtil.logInUser(createdUser);

                view.hideProgress();
                view.navigateToPokemonListScreen();
            }

            @Override
            public void onFailure(String message) {
                view.hideProgress();
                view.showError(message);
            }
        });
    }

    @Override
    public void onEmailChanged(String email) {
        this.email = email;
    }

    @Override
    public void onUserNameChanged(String username) {
        this.username = username;
    }

    @Override
    public void onPasswordChanged(String password) {
        this.password = password;
    }

    @Override
    public void onConfirmationPasswordChanged(String confPassword) {
            this.confPassword = confPassword;
    }

    @Override
    public void onAttach(Context context) {
        if(context instanceof SignUpFragment.SignUpFragmentListener) {
            view.setListener((SignUpFragment.SignUpFragmentListener) context);
        } else {
            throw new RuntimeException(context.toString() + " must implement SignUpFragmentListener");
        }
    }

    @Override
    public void subscribe(BaseMVP.State state) {
        if(state == null) {
            email = "";
            username = "";
            password = "";
            confPassword = "";
            return;
        }

        SignUpState signUpState = (SignUpState) state;

        email = signUpState.getEmail();
        username = signUpState.getUsername();
        password = signUpState.getPassword();
        confPassword = signUpState.getConfPassword();

        view.setEmail(email);
        view.setUserName(username);
        view.setPassword(password);
        view.setConfirmationPassword(confPassword);

    }

    @Override
    public void unsubscribe() {
        email = null;
        username = null;
        password = null;
        confPassword = null;
    }

    @Override
    public BaseMVP.State getState() {
        return new SignUpState(email, username, password, confPassword);
    }
}
