package com.example.leonardo.pokemonapp.network.executor;

import android.widget.Toast;

import com.example.leonardo.pokemonapp.LogInFragment_ViewBinding;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.network.resources.User;
import com.example.leonardo.pokemonapp.network.services.PokemonService;
import com.example.leonardo.pokemonapp.network.services.UserService;
import com.example.leonardo.pokemonapp.util.UserUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by leonardo on 23/07/17.
 */

public class NetworkExecutor {

    private static NetworkExecutor networkExecutor;

    private NetworkExecutor(){}

    private static Call<?> pendingCall;

    Object responseValue;

    public static NetworkExecutor getInstance() {
        if(networkExecutor == null) {
            networkExecutor = new NetworkExecutor();
        }

        return networkExecutor;
    }

    public void destroyAnyPendingTransactions() {
        if(pendingCall != null) {
            pendingCall.cancel();
        }
    }

    public User signUp(User user) {

        UserService userService = ServiceCreator.getUserService();

        Call<User> call = userService.createUser(user);
        pendingCall = call;

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    User responseUser = response.body();
                    UserUtil.logInUser(responseUser);

                    responseValue = responseUser;
                } else {
                    responseValue = null;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if(!call.isCanceled()) {
                    responseValue = null;
                }
            }
        });

        return (User)responseValue;
    }

    public User logIn(User user) {
        UserService userService = ServiceCreator.getUserService();

        Call<User> call = userService.loginUser(user);
        pendingCall = call;

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    User responseUser = response.body();
                    UserUtil.logInUser(responseUser);

                    responseValue = responseUser;
                } else {
                    responseValue = null;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if(!call.isCanceled()) {
                    responseValue = null;
                }
            }
        });

        return (User)responseValue;
    }

    public boolean logOut() {
        UserService userService = ServiceCreator.getUserService();

        final String authToken = UserUtil.getLoggedInUser().getAuthanticationToken();
        final String email = UserUtil.getLoggedInUser().getEmail();
        final String authentication = "Token token=" + authToken + ", email=" + email;

        Call<Void> call = userService.logoutUser(authentication);
        pendingCall = call;

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    responseValue = true;
                } else {
                    responseValue = false;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                responseValue = false;
            }
        });

        return (Boolean)responseValue;
    }

    public Pokemon[] getAllPokemons() {
        PokemonService pokeService = ServiceCreator.getPokemonService();

        Call<Pokemon[]> call = pokeService.getAllPokemons();
        pendingCall = call;

        call.enqueue(new Callback<Pokemon[]>() {
            @Override
            public void onResponse(Call<Pokemon[]> call, Response<Pokemon[]> response) {
                if(response.isSuccessful()) {
                    responseValue = response.body();
                } else {
                    responseValue = null;
                }
            }

            @Override
            public void onFailure(Call<Pokemon[]> call, Throwable t) {
                if(!call.isCanceled()) {
                    responseValue = null;
                }
            }
        });

        return (Pokemon[]) responseValue;
    }

    public Pokemon createPokemon(Pokemon pokemon) {
        PokemonService pokeService = ServiceCreator.getPokemonService();

        Call<Pokemon> call = pokeService.createPokemon(pokemon);
        pendingCall = call;

        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if(response.isSuccessful()) {
                    responseValue = response.body();
                } else {
                    responseValue = null;
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                if(!call.isCanceled()) {
                    responseValue = null;
                }
            }
        });

        return (Pokemon) responseValue;
    }
}
