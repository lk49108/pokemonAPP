package com.example.leonardo.pokemonapp.network.executor;

import android.util.Log;

import com.example.leonardo.pokemonapp.network.adapters.DateAdapter;
import com.example.leonardo.pokemonapp.network.adapters.PokemonGenderAdapter;
import com.example.leonardo.pokemonapp.network.adapters.UriAdapter;
import com.example.leonardo.pokemonapp.network.adapters.VoteAdapter;
import com.example.leonardo.pokemonapp.network.resources.Comment;
import com.example.leonardo.pokemonapp.network.resources.Move;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.network.resources.Type;
import com.example.leonardo.pokemonapp.network.resources.User;
import com.example.leonardo.pokemonapp.network.services.PokemonService;
import com.example.leonardo.pokemonapp.network.services.UserService;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import moe.banana.jsonapi2.JsonApiConverterFactory;
import moe.banana.jsonapi2.ResourceAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by leonardo on 23/07/17.
 */

public class ServiceCreator {

    public static final String API_ENDPOINT = "https://pokeapi.infinum.co";

    private static Retrofit restAdapter;

    public static UserService getUserService() {
        if (restAdapter == null) {
            createRestAdapter();
        }

        return restAdapter.create(UserService.class);
    }

    public static PokemonService getPokemonService() {
        if (restAdapter == null) {
            createRestAdapter();
        }

        return restAdapter.create(PokemonService.class);
    }

    private static void createRestAdapter() {

        final HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(
                        (message) -> {
                            Log.d("logging_interceptor", message);
                        }
                ).setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient client
                = new OkHttpClient.Builder()
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();

        JsonAdapter.Factory jsonApiAdapterFactory =  ResourceAdapterFactory.builder()
                .add(User.class)
                .add(Pokemon.class)
                .add(Move.class)
                .add(Type.class)
                .add(Comment.class)
                .build();

        final Moshi moshi = new Moshi.Builder()
                .add(new PokemonGenderAdapter())
                .add(new DateAdapter())
                .add(new VoteAdapter())
                .add(jsonApiAdapterFactory)
                .build();


        restAdapter = new Retrofit.Builder()
                .baseUrl(API_ENDPOINT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JsonApiConverterFactory.create(moshi))
                .client(client).build();

    }

}
