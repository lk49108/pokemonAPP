package com.example.leonardo.pokemonapp.sharedPref;

import com.example.leonardo.pokemonapp.network.resources.User;

/**
 * Created by leonardo on 05/08/17.
 */

public interface SharedPreferencesInt {

    String SHARED_PREFS_NAME = "shared.prefs.name";
    String SHARED_PREFS_EMAIL_KEY = "shared.prefs.email.key";
    String SHARED_PREFS_USERNAME_KEY = "shared.prefs.username.key";
    String SHARED_PREFS_TOKEN_KEY = "shared.prefs.token.key";

    String getEmail();
    String getUsername();
    String getToken();

    public void saveUser(User user);
    public boolean isLoggedIn();
    public void logOutUser();
    public void logInUser(String email, String authToken, String username);
    public User getLoggedInUser();

}
