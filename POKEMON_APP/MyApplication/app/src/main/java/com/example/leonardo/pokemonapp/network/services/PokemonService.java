package com.example.leonardo.pokemonapp.network.services;

import com.example.leonardo.pokemonapp.network.resources.Comment;
import com.example.leonardo.pokemonapp.network.resources.Move;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;

import java.util.List;

import moe.banana.jsonapi2.Document;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by leonardo on 24/07/17.
 */

public interface PokemonService {

    @GET("/api/v1/pokemons")
    Call<Pokemon[]> getAllPokemons(@Header("Authorization") String authHeader);

    @GET("/api/v1/pokemons/{id}")
    Call<Pokemon> getPokemon(@Header("Authorization") String authHeader, @Path("id") int id);

    @Multipart
    @POST("/api/v1/pokemons")
    Call<Pokemon> createPokemon(@Header("Authorization") String authHeader,
                                @Part(value = "data[attributes][name]", encoding = "text/plain") String name,
                                @Part("data[attributes][height]") double height,
                                @Part("data[attributes][weight]") double weight,
                                @Part("data[attributes][is_default]") boolean isDefault,
                                @Part("data[attributes][gender_id]") int genderId,
                                @Part(value = "data[attributes][description]", encoding = "text/plain") String description,
                                @Part("data[attributes][type_ids][]") int[] type,
                                @Part("data[attributes][move_ids][]") int[] moves,
                                @Part("data[attributes][image]\"; filename=\"pokemon.jpg") RequestBody image);

    @GET("/api/v1/pokemons/{pokemon_id}/comments")
    Call<Document> getCommentsForPokemon(@Header("Authorization") String authHeader, @Path("pokemon_id") int pokemonId);

    @GET("/api/v1/pokemons/{pokemon_id}/comments")
    Call<Comment[]> getCommentsForPokemon(@Header("Authorization") String authHeader, @Path("pokemon_id") int pokemonId, @Query(value = "page%5Bnumber%5D", encoded = true) String pageNumber, @Query(value = "page%5Bsize%5D", encoded = true) String pageSize);

    @POST("/api/v1/pokemons/{id}/upvote")
    Call<Pokemon> upVotePokemon(@Header("Authorization") String authHeader, @Path("id") int pokemonId);

    @POST("/api/v1/pokemons/{id}/downvote")
    Call<Pokemon> downVotePokemon(@Header("Authorization") String authHeader, @Path("id") int pokemonId);

    @POST("/api/v1/pokemons/{pokemon_id}/comments")
    Call<Comment> commentPokemon(@Header("Authorization") String authHeader, @Path("pokemon_id") int pokemonId, @Body Comment comment);
}

