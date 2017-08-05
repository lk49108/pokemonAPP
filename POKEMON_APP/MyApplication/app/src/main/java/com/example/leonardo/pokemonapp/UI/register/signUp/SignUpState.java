package com.example.leonardo.pokemonapp.UI.register.signUp;

/**
 * Created by leonardo on 05/08/17.
 */

public class SignUpState implements SignUpMVP.State {

    private final String email;

    private final String username;

    private final String password;

    private final String confPassword;

    public SignUpState(String email, String username, String password, String confPassword) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confPassword = confPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfPassword() {
        return confPassword;
    }
}
