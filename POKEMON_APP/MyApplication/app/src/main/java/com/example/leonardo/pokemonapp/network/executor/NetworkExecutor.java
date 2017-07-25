package com.example.leonardo.pokemonapp.network.executor;

import android.widget.Toast;

import com.example.leonardo.pokemonapp.LogInFragment_ViewBinding;
import com.example.leonardo.pokemonapp.PermissionApp;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
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

    private String getAuthenticationString() {
        final String authToken = UserUtil.getLoggedInUser().getAuthanticationToken();
        final String email = UserUtil.getLoggedInUser().getEmail();
        return "Token token=" + authToken + ", email=" + email;
    }

    public void signUp(User user, CallbackInt callBack) {

        UserService userService = ServiceCreator.getUserService();

        Call<User> call = userService.createUser(user);
        pendingCall = call;

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    User responseUser = response.body();
                    UserUtil.logInUser(responseUser);

                    callBack.onSuccess((User) responseUser);
                } else {
                    callBack.onFailure("Response was not valid.");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if(!call.isCanceled()) {
                    callBack.onFailure("Failure");
                }
            }
        });

    }

    public void logIn(User user, CallbackInt callBack) {
        UserService userService = ServiceCreator.getUserService();

        Call<User> call = userService.loginUser(user);
        pendingCall = call;

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    User responseUser = response.body();
                    UserUtil.logInUser(responseUser);

                    callBack.onSuccess((User) responseUser);
                } else {
                    callBack.onFailure("Response was not valid.");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callBack.onFailure("Failure");
            }
        });

    }

    public void logOut(CallbackInt callBack) {
        UserService userService = ServiceCreator.getUserService();

        Call<Void> call = userService.logoutUser(getAuthenticationString());
        pendingCall = call;

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    callBack.onSuccess(null);
                } else {
                    callBack.onFailure("Response was not valid.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callBack.onFailure("Failure");
            }
        });

    }

    public void getAllPokemons(CallbackInt callBack) {
        PokemonService pokeService = ServiceCreator.getPokemonService();


        Call<Pokemon[]> call = pokeService.getAllPokemons(getAuthenticationString());
        pendingCall = call;

        call.enqueue(new Callback<Pokemon[]>() {
            @Override
            public void onResponse(Call<Pokemon[]> call, Response<Pokemon[]> response) {
                if(response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure("Response was not valid.");
                }
            }

            @Override
            public void onFailure(Call<Pokemon[]> call, Throwable t) {
                callBack.onFailure("Failure");
            }
        });

    }

    public void createPokemon(Pokemon pokemon, CallbackInt callBack) {
        PokemonService pokeService = ServiceCreator.getPokemonService();

        Call<Pokemon> call = pokeService.createPokemon(pokemon, getAuthenticationString());
        pendingCall = call;

        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if(response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure("Response was not valid.");
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                if(!call.isCanceled()) {
                    callBack.onFailure("Failure");
                }
            }
        });

    }
}
