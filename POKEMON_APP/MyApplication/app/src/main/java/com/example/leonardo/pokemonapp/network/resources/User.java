package com.example.leonardo.pokemonapp.network.resources;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonQualifier;

import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by leonardo on 23/07/17.
 */
@JsonApi(type = "users")
public class User extends Resource {

    @Json(name = "username")
    private String userName;

    @Json(name = "email")
    private String email;

    @Json(name = "password")
    private String password;

    @Json(name = "password_confirmation")
    private String confirmationPassword;

    @Json(name = "auth-token")
    private String authanticationToken;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }

    public String getAuthanticationToken() {
        return authanticationToken;
    }

    public void setAuthanticationToken(String authanticationToken) {
        this.authanticationToken = authanticationToken;
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}