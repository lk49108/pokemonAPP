package com.example.leonardo.pokemonapp.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.leonardo.pokemonapp.UI.register.logIn.LogInFragment;
import com.example.leonardo.pokemonapp.UI.register.logIn.LogInMVP;
import com.example.leonardo.pokemonapp.UI.register.logIn.LogInPresenterImpl;

/**
 * Created by leonardo on 04/08/17.
 */

public class LogInAnimation {

    private static LogInAnimation instance;

    private LogInFragment view;

    long timeFromBegginingOfAnimation;

    private boolean firstStageOfAnimationFinished;

    private boolean finished;

    private LogInAnimationListener listener;

    private ObjectAnimator pokemonLogoAnimator;

    private ObjectAnimator pokeBallAnimatorX;

    private ObjectAnimator pokeBallAnimatorY;

    private LogInAnimation(LogInFragment fragment, LogInAnimationListener listener) {
        view = fragment;
        this.listener = listener;
    }

    public static LogInAnimation getInstance(LogInFragment fragment, LogInAnimationListener listener) {
        if(instance == null) {
            instance = new LogInAnimation(fragment, listener);
        } else if(!instance.view.equals(fragment)) {
            instance.view = fragment;
        }

        return instance;
    }

    public boolean isAnimationFinished() {
        return finished;
    }

    public void stopAnimation() {
        if(pokemonLogoAnimator == null) {
            return;
        }

        if(!firstStageOfAnimationFinished) {
            pokemonLogoAnimator.pause();
            timeFromBegginingOfAnimation = pokemonLogoAnimator.getCurrentPlayTime();
            return;
        }

        if(!finished) {
            pokeBallAnimatorX.pause();
            pokeBallAnimatorY.pause();
            timeFromBegginingOfAnimation = 1500 + pokeBallAnimatorX.getCurrentPlayTime();
            return;
        }
    }

    public void startAnimation() {
        if(finished) {
            return;
        }

        int height = getScreenHeight(view);

        pokemonLogoAnimator = ObjectAnimator.ofFloat(view.pokemonLogo, "translationY", height / 1.2f, 0);
        pokemonLogoAnimator.setDuration(1500);

        pokeBallAnimatorX = ObjectAnimator.ofFloat(view.pokeBall, "scaleX", 0f, 1f);
        pokeBallAnimatorY = ObjectAnimator.ofFloat(view.pokeBall, "scaleY", 0f, 1f);

        pokeBallAnimatorX.setDuration(1000);
        pokeBallAnimatorY.setDuration(1000);

        pokemonLogoAnimator.setCurrentPlayTime(Math.min(timeFromBegginingOfAnimation, 1500));
        pokeBallAnimatorX.setCurrentPlayTime(Math.max(0, timeFromBegginingOfAnimation - 1500));
        pokeBallAnimatorY.setCurrentPlayTime(Math.max(0, timeFromBegginingOfAnimation - 1500));

        pokemonLogoAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.switchPokemonLogoVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                firstStageOfAnimationFinished = true;
            }
        });

        pokeBallAnimatorX.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

                view.switchPokeBallVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finished = true;
                listener.onAnimationEnd();
            }
        });

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(pokeBallAnimatorX).with(pokeBallAnimatorY).after(pokemonLogoAnimator);
        animSet.start();
    }

    private int getScreenHeight(LogInFragment view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        view.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    public interface LogInAnimationListener {

        void onAnimationEnd();

    }
}
