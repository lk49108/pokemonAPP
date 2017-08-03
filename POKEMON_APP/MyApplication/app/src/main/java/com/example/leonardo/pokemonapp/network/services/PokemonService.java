package com.example.leonardo.pokemonapp.network.services;

import com.example.leonardo.pokemonapp.network.resources.Pokemon;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by leonardo on 24/07/17.
 */

public interface PokemonService {

    @GET("/api/v1/pokemons")
    Call<Pokemon[]> getAllPokemons(@Header("Authorization") String authHeader);

    @Multipart
    @POST("/api/v1/pokemons")
    Call<Pokemon> createPokemon(@Header("Authorization") String authHeader,
                                @Part(value = "data[attributes][name]", encoding = "text/plain") String name,
                                @Part("data[attributes][height]") double height,
                                @Part("data[attributes][weight]") double weight,
                                @Part("data[attributes][is_default]") boolean isDefault,
                                @Part("data[attributes][gender_id]") int genderId,
                                @Part(value = "data[attributes][description]", encoding = "text/plain") String description,
                                @Part("data[attributes][type_ids][]") int[] category,
                                @Part("data[attributes][move_ids][]") int[] moves,
                                @Part("data[attributes][image]\"; filename=\"pokemon.jpg") RequestBody image);
}

