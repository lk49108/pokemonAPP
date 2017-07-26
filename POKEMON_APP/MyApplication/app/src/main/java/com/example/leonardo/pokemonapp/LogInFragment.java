package com.example.leonardo.pokemonapp;

import android.content.Context;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.network.executor.NetworkExecutor;
import com.example.leonardo.pokemonapp.network.resources.User;
import com.example.leonardo.pokemonapp.util.UserUtil;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moe.banana.jsonapi2.Document;
import moe.banana.jsonapi2.ResourceAdapterFactory;

/**
 * Created by leonardo on 23/07/17.
 */

public class LogInFragment extends Fragment {

    @BindView(R.id.fragment_log_in_email_edit_text)
    EditText emailView;
    @BindView(R.id.fragment_log_in_password_edit_text)
    EditText passwordView;
    @BindView(R.id.fragment_log_in_log_in_button)
    Button logInButton;
    @BindView(R.id.fragment_log_in_sign_up_button)
    Button signUpButton;

    private String errorMessage;

    @OnClick(R.id.fragment_log_in_log_in_button)
    void logInButton() {

        boolean valid = checkValidityLocaly();

        if(!valid) {
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            return;
        }

        if(!UserUtil.internetConnectionActive()) {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
            return;
        }

        attemptToLogin();
    }

    private void attemptToLogin() {
        User user = new User();
        user.setEmail(emailView.getText().toString());
        user.setPassword(passwordView.getText().toString());

        listener.logIn(user);
    }

    private boolean checkValidityLocaly() {
        final String email = emailView.getText().toString();
        final String password = passwordView.getText().toString();

        if(email.isEmpty() || password.isEmpty()) {
            errorMessage = "No empty fields allowed";
            return false;
        }

        if(!UserUtil.validEmail(email)) {
            errorMessage = "Email is not valid";
            return false;
        }

        return true;
    }

    @OnClick(R.id.fragment_log_in_sign_up_button)
    void signUpButton() {
        listener.signUpInsteadOfLogIn();
    }

    public interface LogInFragmentListener {

        void logIn(User user);

        void signUpInsteadOfLogIn();

    }

    private LogInFragmentListener listener;

    public LogInFragment() {
    }

    public static LogInFragment newInstance() {
        return new LogInFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof RegisterActivity) {
            listener = (LogInFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LogInFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        ButterKnife.bind(this, view);

        if(savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }

        return view;
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        emailView.setText(savedInstanceState.getString("email"));
        passwordView.setText(savedInstanceState.getString("password"));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("email", emailView.getText().toString());
        outState.putString("password", passwordView.getText().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        NetworkExecutor.getInstance().destroyAnyPendingTransactions();
    }
}
