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

public class AnimationUtil {

    public static AnimatorSet getLogInAnimation(LogInFragment view, LogInPresenterImpl logInPresenter, long timeFromBegginingOfAnimation) {
        int height = getScreenHeight(view);

        ObjectAnimator pokemonLogoAnimator = ObjectAnimator.ofFloat(view.pokemonLogo, "translationY", height / 1.2f, 0);
        pokemonLogoAnimator.setDuration(1500);

        ObjectAnimator pokeBallAnimatorX = ObjectAnimator.ofFloat(view.pokeBall, "scaleX", 0f, 1f);
        ObjectAnimator pokeBallAnimatorY = ObjectAnimator.ofFloat(view.pokeBall, "scaleY", 0f, 1f);

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
                view.switchPokeBallVisibility(View.VISIBLE);
            }
        });

        pokeBallAnimatorX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(UserUtil.loggedIn()) {
                    listener.finishActivity();
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

    private static int getScreenHeight(LogInFragment view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        view.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

}
