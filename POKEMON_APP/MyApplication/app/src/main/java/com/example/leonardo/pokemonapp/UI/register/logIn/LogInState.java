package com.example.leonardo.pokemonapp.UI.register.logIn;

/**
 * Created by leonardo on 04/08/17.
 */

public class LogInState implements LogInMVP.State {

    private final String email;

    private final String password;

    private final long millisecondsFromAnimationStart;

    private final boolean passwordVisible;

    public LogInState(String email, String password, long millisecondsFromAnimationStart, boolean passwordVisible) {
        this.email = email;
        this.password = password;
        this.millisecondsFromAnimationStart = millisecondsFromAnimationStart;
        this.passwordVisible = passwordVisible;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isPasswordVisible() {
        return passwordVisible;
    }

    public long getMillisecondsFromAnimationStart() {
        return millisecondsFromAnimationStart;
    }
}
