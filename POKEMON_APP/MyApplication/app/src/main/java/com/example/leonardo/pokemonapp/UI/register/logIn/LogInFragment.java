package com.example.leonardo.pokemonapp.UI.register.logIn;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.UI.customViews.EditTextWithSwitchingTextVisibility;
import com.example.leonardo.pokemonapp.base.BaseFragment;
import com.example.leonardo.pokemonapp.util.LogInAnimation;
import com.example.leonardo.pokemonapp.util.StateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by leonardo on 23/07/17.
 */

public class LogInFragment extends BaseFragment implements LogInMVP.View, EditTextWithSwitchingTextVisibility.OnDrawableClickedListener {

    private LogInMVP.Presenter presenter;

    @BindView(R.id.fragment_log_in_ll2)
    LinearLayout ll2;
    @BindView(R.id.fragment_log_in_ll)
    LinearLayout ll;
    @BindView(R.id.fragment_log_in_email_edit_text)
    EditText emailView;
    @BindView(R.id.fragment_log_in_password_edit_text)
    EditTextWithSwitchingTextVisibility passwordView;
    @BindView(R.id.fragment_log_in_log_in_button)
    Button logInButton;
    @BindView(R.id.fragment_log_in_sign_up_button)
    Button signUpButton;
    @BindView(R.id.fragment_log_in_pokemon_logo)
    public ImageView pokemonLogo;
    @BindView(R.id.fragment_log_in_poke_ball)
    public ImageView pokeBall;

    @OnClick(R.id.fragment_log_in_log_in_button)
    void logInButton() {
        presenter.onLogInPressed();
    }

    @OnClick(R.id.fragment_log_in_sign_up_button)
    void signUpButton() {
        listener.signUpInsteadOfLogIn();
    }

    @OnTextChanged(R.id.fragment_log_in_email_edit_text)
    void onEmailTextChanged() {
        presenter.onEmailTextChanged(emailView.getText().toString());
    }

    @OnTextChanged(R.id.fragment_log_in_password_edit_text)
    void onPasswordTextChanged() {
        presenter.onPasswordTextChange(passwordView.getText().toString());
    }

    @Override
    public void setListener(LogInFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void showAnimation() {
        LogInAnimation.getInstance(this, (LogInAnimation.LogInAnimationListener) presenter).startAnimation();
    }

    @Override
    public void stopAnimation() {
        LogInAnimation.getInstance(this, (LogInAnimation.LogInAnimationListener) presenter).stopAnimation();
    }

    @Override
    public void setEmail(String email) {
        emailView.setText(email);
    }

    @Override
    public void setPassword(String password) {
        passwordView.setText(password);
    }

    @Override
    public void switchlayoutVisibility(int visibility) {
        signUpButton.setVisibility(visibility);
        logInButton.setVisibility(visibility);
        ll.setVisibility(visibility);
        ll2.setVisibility(visibility);

    }

    @Override
    public void switchPokeBallVisibility(int visibility) {
        pokeBall.setVisibility(visibility);
    }

    @Override
    public void switchPokemonLogoVisibility(int visibility) {
        pokemonLogo.setVisibility(visibility);
    }

    @Override
    public void navigateToPokemonListScreen() {
        listener.navigateToPokemonListScreen();
    }

    @Override
    public void setPasswordVisibilityDrawable(int visibilityDrawable) {
        passwordView.setPasswordVisibilityDrawable(visibilityDrawable);
    }

    @Override
    public void onPasswordVissibilityChanged(int drawableId) {
        presenter.onPasswordVisiblityChange(drawableId);
    }

    public interface LogInFragmentListener {

        void signUpInsteadOfLogIn();

        void navigateToPokemonListScreen();

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

        presenter = new LogInPresenterImpl(this);
        presenter.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        ButterKnife.bind(this, view);

        passwordView.setListener(this);

        presenter.subscribe(savedInstanceState == null ? null : StateUtil.readFromLogInBundle(savedInstanceState));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        StateUtil.writeToBundle(outState, presenter.getState());
    }

    @Override
    public void onStop() {
        presenter.cancelCall();

        super.onStop();
    }

    @Override
    public void onDestroy() {
        presenter.cancelCall();

        super.onDestroy();
    }
}