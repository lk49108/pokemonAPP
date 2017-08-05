package com.example.leonardo.pokemonapp.UI.register.signUp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.UI.customViews.PasswordMatcherView;
import com.example.leonardo.pokemonapp.base.BaseFragment;
import com.example.leonardo.pokemonapp.util.StateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leonardo on 23/07/17.
 */

public class SignUpFragment extends BaseFragment implements SignUpMVP.View {

    private SignUpMVP.Presenter presenter;

    @BindView(R.id.fragment_sign_up_email_edit_text)
    EditText fragmentSignUpEmailEditText;
    @BindView(R.id.fragment_sign_up_username_edit_text)
    EditText fragmentSignUpUsernameEditText;
    @BindView(R.id.fragment_sign_up_password_matcher)
    PasswordMatcherView passwordMatcher;
    @BindView(R.id.fragment_sign_up_button)
    Button fragmentSignUpButton;

    @OnClick(R.id.fragment_sign_up_button)
    void signUp() {
        presenter.onSignUpClicked();
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setListener(SignUpFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void setEmail(String email) {
        fragmentSignUpEmailEditText.setText(email);
    }

    @Override
    public void setUserName(String userName) {
        fragmentSignUpUsernameEditText.setText(userName);
    }

    @Override
    public void setPassword(String password) {
        passwordMatcher.setPasswordText(password);
    }

    @Override
    public void setConfirmationPassword(String passwordConfirmation) {
        passwordMatcher.setPasswordConfirmationText(passwordConfirmation);
    }

    @Override
    public void navigateToPokemonListScreen() {
        listener.navigateToPokemonListScreen();
    }

    public interface SignUpFragmentListener {

        void navigateToPokemonListScreen();

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

        presenter = new SignUpPresenterImpl(this);
        presenter.subscribe(savedInstanceState == null ? null : StateUtil.readFromSignUpBundle(savedInstanceState));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        presenter = new SignUpPresenterImpl(this);
        presenter.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

       presenter.unsubscribe();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        StateUtil.writeToBundle(outState, presenter.getState());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        presenter.cancelCall();
    }
}
