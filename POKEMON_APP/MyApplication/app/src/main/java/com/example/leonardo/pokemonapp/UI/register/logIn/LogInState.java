package com.example.leonardo.pokemonapp.UI.register.logIn;

/**
 * Created by leonardo on 04/08/17.
 */

public class LogInState implements LogInMVP.State {

    private final String email;

    private final String password;

    private final int passwordVisibilityDrawable;

    public LogInState(String email, String password, int passwordVisibilityDrawable) {
        this.email = email;
        this.password = password;
        this.passwordVisibilityDrawable = passwordVisibilityDrawable;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getPasswordVisibilityDrawable() {
        return passwordVisibilityDrawable;
    }

}
