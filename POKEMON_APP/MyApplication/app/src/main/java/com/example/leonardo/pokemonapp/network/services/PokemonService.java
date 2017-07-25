package com.example.leonardo.pokemonapp.network.services;

import com.example.leonardo.pokemonapp.network.resources.Pokemon;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by leonardo on 24/07/17.
 */

public interface PokemonService {

    @GET("api/v1/pokemons")
    Call<Pokemon[]> getAllPokemons(@Header("Authorization") String authHeader);

    @POST("api/v1/pokemons")
    Call<Pokemon> createPokemon(@Body Pokemon pokemon, @Header("Authorization") String authHeader);

}
