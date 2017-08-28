package com.example.leonardo.pokemonapp.network.services;

import com.example.leonardo.pokemonapp.network.resources.User;

import org.xml.sax.helpers.DefaultHandler;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by leonardo on 23/07/17.
 */

public interface UserService {

    @POST("/api/v1/users")
    Call<User> createUser(@Body User user);

    @POST("/api/v1/users/login")
    Call<User> loginUser(@Body User user);

    @DELETE("/api/v1/users/logout")
    Call<Void> logoutUser(@Header("Authorization") String authHeader);

    @GET("/api/v1/users/{id}")
    Call<User>  getUserById(@Header("Authorization") String authHeader, @Path("id") int userId);
}