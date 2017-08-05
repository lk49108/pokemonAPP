package com.example.leonardo.pokemonapp.network.executor;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.network.resources.User;
import com.example.leonardo.pokemonapp.network.services.PokemonService;
import com.example.leonardo.pokemonapp.network.services.UserService;
import com.example.leonardo.pokemonapp.util.PokemonResourcesUtil;
import com.example.leonardo.pokemonapp.util.UserUtil;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

/**
 * Created by leonardo on 23/07/17.
 */

public class NetworkExecutor {

    public static File imageFile;

    private static NetworkExecutor networkExecutor;

    private NetworkExecutor(){}

    private volatile static Call<?> pendingCall;

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
                pendingCall = null;
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
                pendingCall = null;
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
                pendingCall = null;
                if(response.isSuccessful()) {
                    User responseUser = response.body();

                    callBack.onSuccess((User) responseUser);
                } else {
                    callBack.onFailure("Response was not valid.");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                pendingCall = null;
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
                pendingCall = null;
                if(response.isSuccessful()) {
                    callBack.onSuccess(null);
                } else {
                    callBack.onFailure("Response was not valid.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                pendingCall = null;
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
                pendingCall = null;
                if(response.isSuccessful()) {
                    Log.d("successs", "jes :D");
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure("Response was not valid.");
                }
            }

            @Override
            public void onFailure(Call<Pokemon[]> call, Throwable t) {
                pendingCall = null;
                callBack.onFailure("Failure");
            }
        });

    }

    public void createPokemon(Pokemon pokemon, CallbackInt callBack, Context context) {
        PokemonService pokeService = ServiceCreator.getPokemonService();

        RequestBody requestBody = createRequestBodyWithImageToBeSent(pokemon, context);
        Call<Pokemon> call = pokeService
                .createPokemon(getAuthenticationString(), pokemon.getName(),
                        12, 12, false, 1, pokemon.getDescription(),
                        new int[0], new int[0], requestBody);

        pendingCall = call;

        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                pendingCall = null;
                if(response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure("Response was not valid.");
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                pendingCall = null;
                if(!call.isCanceled()) {
                    callBack.onFailure("Failure");
                }
            }
        });


    }

    private RequestBody createRequestBodyWithImageToBeSent(Pokemon pokemon, Context context) {
        Uri imageUri = pokemon.getImageSource();
        if(imageUri == null) {
            return null;
        }

        String filePathString = getPath(imageUri, context);
        if(filePathString == null) {
            return null;
        }

        File file = new File(filePathString);

        RequestBody filePart = RequestBody.create(MediaType.parse("image/*"), file);

        return filePart;
    }

    private String getPath(Uri imageUri, Context context) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(imageUri, proj, null, null, null);
        int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        if(columnIndex == -1) {
            return null;
        }
        cursor.moveToFirst();
        return  cursor.getString(columnIndex);
    }

}
