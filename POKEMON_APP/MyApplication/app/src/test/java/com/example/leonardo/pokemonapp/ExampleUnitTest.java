package com.example.leonardo.pokemonapp;

import android.util.Log;

import com.example.leonardo.pokemonapp.network.adapters.PokemonGenderAdapter;
import com.example.leonardo.pokemonapp.network.adapters.UriAdapter;
import com.example.leonardo.pokemonapp.network.resources.Comment;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.network.resources.User;
import com.example.leonardo.pokemonapp.network.services.PokemonService;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.junit.Test;

import moe.banana.jsonapi2.HasMany;
import moe.banana.jsonapi2.JsonApiConverterFactory;
import moe.banana.jsonapi2.ResourceAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
       try {
           throw new Exception();
       } catch (Exception ex) {
           System.out.print("a");
       }

        System.out.print("c");
    }


}