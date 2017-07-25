package com.example.leonardo.pokemonapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.network.executor.NetworkExecutor;
import com.example.leonardo.pokemonapp.network.resources.User;
import com.example.leonardo.pokemonapp.util.UserUtil;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leonardo on 23/07/17.
 */

public class SignUpFragment extends Fragment {

    private String errorMessage;

    @BindView(R.id.fragment_sign_up_email_edit_text)
    EditText fragmentSignUpEmailEditText;
    @BindView(R.id.fragment_sign_up_username_edit_text)
    EditText fragmentSignUpUsernameEditText;
    @BindView(R.id.fragment_sign_up_password_edit_text)
    EditText fragmentSignUpPasswordEditText;
    @BindView(R.id.fragment_sign_up_password_confirmation_edit_text)
    EditText fragmentSignUpPasswordConfirmationEditText;
    @BindView(R.id.fragment_sign_up_button)
    Button fragmentSignUpButton;

    @OnClick(R.id.fragment_sign_up_button)
    void signUp() {
        boolean valid = checkDataValidityLocaly();

        if(!valid) {
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            return;
        }

        if(!UserUtil.internetConnectionActive()) {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
            return;
        }

        attemptToSignUp();
    }

    private void attemptToSignUp() {
        User user = new User();
        user.setEmail(fragmentSignUpEmailEditText.getText().toString());
        user.setUserName(fragmentSignUpUsernameEditText.getText().toString());
        user.setPassword(fragmentSignUpPasswordEditText.getText().toString());
        user.setConfirmationPassword(fragmentSignUpPasswordConfirmationEditText.getText().toString());

        listener.signUp(user);
    }

    private boolean checkDataValidityLocaly() {
        final String email = fragmentSignUpEmailEditText.getText().toString();
        final String userName = fragmentSignUpUsernameEditText.getText().toString();

        if(email.isEmpty() || userName.isEmpty()) {
            errorMessage = "No empty fields allowed";
            return false;
        }

        if(!UserUtil.validEmail(email)) {
            errorMessage = "Email is not valid";
            return false;
        }

        final String password = fragmentSignUpPasswordEditText.getText().toString();
        final String confPassword = fragmentSignUpPasswordConfirmationEditText.getText().toString();

        if(password.isEmpty()) {
            errorMessage = "Password can not be mepty";
            return false;
        }

        if(password.length() < 8) {
            errorMessage = "Password should be at least 8 characters long";
            return false;
        }

        if (!password.equals(confPassword)) {
            errorMessage = "Passwords do not match";
            return false;
        }

        return true;
    }

    public interface SignUpFragmentListener {

        void signUp(User user);

    }

    private SignUpFragmentListener listener;

    public SignUpFragment() {
    }

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        ButterKnife.bind(this, view);

        RegisterActivity activity = (RegisterActivity) getActivity();
        activity.getSupportActionBar().show();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof RegisterActivity) {
            listener = (SignUpFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SignUpFragmentListener");
        }

    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        fragmentSignUpEmailEditText.setText(savedInstanceState.getString("email"));
        fragmentSignUpUsernameEditText.setText(savedInstanceState.getString("userName"));
        fragmentSignUpPasswordEditText.setText(savedInstanceState.getString("password"));
        fragmentSignUpPasswordConfirmationEditText.setText(savedInstanceState.getString("confirmationPassword"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        RegisterActivity activity = (RegisterActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().hide();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("email", fragmentSignUpEmailEditText.getText().toString());
        outState.putString("userName", fragmentSignUpUsernameEditText.getText().toString());
        outState.putString("password", fragmentSignUpPasswordEditText.getText().toString());
        outState.putString("confirmationPassword", fragmentSignUpPasswordConfirmationEditText.getText().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        NetworkExecutor.getInstance().destroyAnyPendingTransactions();
    }
}
