package com.example.leonardo.pokemonapp;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

    private boolean pokemonLogoAnimatorFinished;
    private boolean pokeBallAnimatorFinished;
    private boolean finished;

    ObjectAnimator pokemonLogoAnimator;
    ObjectAnimator pokeBallAnimatorX;
    ObjectAnimator pokeBallAnimatorY;

    private boolean firstTime = true;

    @BindView(R.id.log_in_fragment_scroll_view)
    ScrollView scrollView;
    @BindView(R.id.fragment_log_in_root_element)
    View root;
    @BindView(R.id.fragment_log_in_ll2)
    LinearLayout ll2;
    @BindView(R.id.fragment_log_in_ll)
    LinearLayout ll;
    @BindView(R.id.fragment_log_in_email_edit_text)
    EditText emailView;
    @BindView(R.id.fragment_log_in_password_edit_text)
    EditText passwordView;
    @BindView(R.id.fragment_log_in_log_in_button)
    Button logInButton;
    @BindView(R.id.fragment_log_in_sign_up_button)
    Button signUpButton;
    @BindView(R.id.fragment_log_in_pokemon_logo)
    ImageView pokemonLogo;
    @BindView(R.id.fragment_log_in_poke_ball)
    ImageView pokeBall;

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

        void finish();

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

        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(() -> {
            if(firstTime) {
                firstTime = false;
                animate(savedInstanceState);
            }
        });

        if(savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }

        return view;
    }

    private void animate(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            if(savedInstanceState.getBoolean("finished")) {
                return;
            }
        }
            int[] location = new int[2];

        pokemonLogoAnimator = ObjectAnimator.ofFloat(pokemonLogo, "translationY", root.getHeight() / 1.7f, 0);
        pokeBallAnimatorX = ObjectAnimator.ofFloat(pokeBall, "scaleX", 0f, 1f);
        pokeBallAnimatorY = ObjectAnimator.ofFloat(pokeBall, "scaleY", 0f, 1f);

        pokemonLogoAnimator.setDuration(1500);
        pokeBallAnimatorX.setDuration(1000);
        pokeBallAnimatorY.setDuration(1000);

        if(savedInstanceState != null) {
            pokemonLogoAnimator.setCurrentPlayTime(savedInstanceState.getLong("pokemonLogoAnimationStatus") == -1 ? 3500 : savedInstanceState.getLong("pokemonLogoAnimationStatus"));
            pokeBallAnimatorX.setCurrentPlayTime(savedInstanceState.getLong("pokeBallAnimationStatus") == -1 ? 3000 : savedInstanceState.getLong("pokeBallAnimationStatus"));
            pokeBallAnimatorY.setCurrentPlayTime(savedInstanceState.getLong("pokeBallAnimationStatus") == -1 ? 3000 : savedInstanceState.getLong("pokeBallAnimationStatus"));
        }

        pokemonLogoAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                pokeBall.setVisibility(View.VISIBLE);
                pokemonLogoAnimatorFinished = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        pokeBallAnimatorX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(UserUtil.loggedIn()) {
                    listener.finish();
                    return;
                }
                scrollView.setScrollContainer(true);
                pokeBallAnimatorFinished = true;
                finished = true;
                ll2.setVisibility(View.VISIBLE);
                logInButton.setVisibility(View.VISIBLE);
                signUpButton.setVisibility(View.VISIBLE);
                ll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(pokeBallAnimatorX).with(pokeBallAnimatorY).after(pokemonLogoAnimator);
        animSet.start();
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        emailView.setText(savedInstanceState.getString("email"));
        passwordView.setText(savedInstanceState.getString("password"));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("email", emailView.getText().toString());
        outState.putString("password", passwordView.getText().toString());
        outState.putLong("pokemonLogoAnimationStatus", pokemonLogoAnimatorFinished ? -1 : pokemonLogoAnimator.getCurrentPlayTime());
        outState.putLong("pokeBallAnimationStatus", pokeBallAnimatorFinished ? -1 : pokeBallAnimatorX.getCurrentPlayTime());
        outState.putBoolean("animationFinished", finished);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        NetworkExecutor.getInstance().destroyAnyPendingTransactions();
    }
}
