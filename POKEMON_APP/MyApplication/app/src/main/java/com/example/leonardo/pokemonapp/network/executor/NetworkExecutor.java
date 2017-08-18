package com.example.leonardo.pokemonapp.network.executor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.executor.errorHandler.ErrorHandler;
import com.example.leonardo.pokemonapp.network.resources.Comment;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.network.resources.Type;
import com.example.leonardo.pokemonapp.network.resources.User;
import com.example.leonardo.pokemonapp.network.services.PokemonService;
import com.example.leonardo.pokemonapp.network.services.UserService;
import com.example.leonardo.pokemonapp.util.PokemonResourcesUtil;
import com.example.leonardo.pokemonapp.util.UserUtil;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import moe.banana.jsonapi2.Document;
import moe.banana.jsonapi2.JsonBuffer;
import moe.banana.jsonapi2.Resource;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

/**
 * Created by leonardo on 23/07/17.
 */

public class NetworkExecutor {

    private static NetworkExecutor networkExecutor;

    private NetworkExecutor(){}

    private volatile List<Call<?>> calls;

    {
        calls = new ArrayList<>();
    }

    public static NetworkExecutor getInstance() {
        if(networkExecutor == null) {
            networkExecutor = new NetworkExecutor();
        }

        return networkExecutor;
    }

    public void destroyAnyPendingTransactions() {
        for(Call<?> call : calls) {
            call.cancel();
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

        calls.add(call);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                calls.remove(call);

                if(response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure(ErrorHandler.getErrorResponse(response).toString());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                calls.remove(call);
                if(call.isCanceled()) {
                    callBack.onCancel();
                } else {
                    callBack.onFailure("Failure");
                }
            }
        });

    }

    public void logIn(User user, CallbackInt callBack) {
        UserService userService = ServiceCreator.getUserService();

        Call<User> call = userService.loginUser(user);

        calls.add(call);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                calls.remove(call);
                if(response.isSuccessful()) {
                    User responseUser = response.body();

                    callBack.onSuccess((User) responseUser);
                } else {
                    callBack.onFailure(ErrorHandler.getErrorResponse(response).toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                calls.remove(call);
                if(call.isCanceled()) {
                    callBack.onCancel();
                } else {
                    callBack.onFailure("Failure");
                }
            }
        });

    }

    public void logOut(CallbackInt callBack) {
        UserService userService = ServiceCreator.getUserService();

        Call<Void> call = userService.logoutUser(getAuthenticationString());

        calls.add(call);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                calls.remove(call);
                if(response.isSuccessful()) {
                    callBack.onSuccess(null);
                } else {
                    callBack.onFailure(ErrorHandler.getErrorResponse(response).toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                calls.remove(call);
                if(call.isCanceled()) {
                    callBack.onCancel();
                } else {
                    callBack.onFailure("Failure");
                }
            }
        });


    }

    public void getAllPokemons(CallbackInt callBack) {
        PokemonService pokeService = ServiceCreator.getPokemonService();


        Call<Pokemon[]> call = pokeService.getAllPokemons(getAuthenticationString());

        calls.add(call);

        call.enqueue(new Callback<Pokemon[]>() {
            @Override
            public void onResponse(Call<Pokemon[]> call, Response<Pokemon[]> response) {
                calls.remove(call);
                if(response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure(ErrorHandler.getErrorResponse(response).toString());
                }
            }

            @Override
            public void onFailure(Call<Pokemon[]> call, Throwable t) {
                calls.remove(call);
                if(call.isCanceled()) {
                    callBack.onCancel();
                } else {
                    callBack.onFailure("Failure");
                }
            }
        });

    }

    public void createPokemon(Pokemon pokemon, CallbackInt callBack, Context context) {
        PokemonService pokeService = ServiceCreator.getPokemonService();

        RequestBody requestBody = createRequestBodyWithImageToBeSent(pokemon, context);
        Call<Pokemon> call = pokeService
                .createPokemon(getAuthenticationString(), pokemon.getName(),
                        pokemon.getHeight(), pokemon.getWeight(), false, pokemon.getGender().getId(), pokemon.getDescription(),
                        pokemon.getTypeIds(), pokemon.getMoveIds(), requestBody);

        calls.add(call);

        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                calls.remove(call);
                if(response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure(ErrorHandler.getErrorResponse(response).toString());
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                calls.remove(call);
                if(call.isCanceled()) {
                    callBack.onCancel();
                } else {
                    callBack.onFailure("Failure");
                }
            }
        });


    }

    private RequestBody createRequestBodyWithImageToBeSent(Pokemon pokemon, Context context) {
        String imageUriString = pokemon.getImageSource();
        Log.d("uri....ffs", imageUriString);
        if(imageUriString == null) {
            return null;
        }

        Uri imageUri = Uri.parse(imageUriString);

        String filePathString = getPath(imageUri, context);
        if(filePathString == null) {
            Log.d("null je", "null");
            filePathString = imageUriString;
        }

        File file = new File(filePathString);
        Log.d("null je", String.valueOf(file.exists()));

        RequestBody filePart = RequestBody.create(MediaType.parse("image/*"), file);

        return filePart;
    }

    private String getPath(Uri imageUri, Context context) {
        Log.d("image uri", imageUri.getPath());
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(imageUri, proj, null, null, null);
        if(cursor == null) {
            return null;
        }
        int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        if(columnIndex == -1) {
            return null;
        }
        cursor.moveToFirst();
        return  cursor.getString(columnIndex);
    }

    public void getPokemon(int id, CallbackInt callBack) {
        PokemonService pokeService = ServiceCreator.getPokemonService();


        Call<Pokemon> call = pokeService.getPokemon(getAuthenticationString(), id);
        calls.add(call);

        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                calls.remove(call);
                if(response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure(ErrorHandler.getErrorResponse(response).toString());
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                calls.remove(call);
                if(call.isCanceled()) {
                    callBack.onCancel();
                } else {
                    callBack.onFailure("Failure");
                }
            }
        });
    }

    public void downVotePokemon(int pokemonId, CallbackInt callBack) {
        PokemonService pokeService = ServiceCreator.getPokemonService();

        Call<Pokemon> call = pokeService.downVotePokemon(getAuthenticationString(), pokemonId);
        calls.add(call);

        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                calls.remove(call);
                if(response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure(ErrorHandler.getErrorResponse(response).toString());
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                calls.remove(call);
                if(call.isCanceled()) {
                    callBack.onCancel();
                } else {
                    callBack.onFailure("Failure");
                }
            }
        });

    }

    public void upVotePokemon(int pokemonId, CallbackInt callBack) {
        PokemonService pokeService = ServiceCreator.getPokemonService();

        Call<Pokemon> call = pokeService.upVotePokemon(getAuthenticationString(), pokemonId);
        calls.add(call);

        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                calls.remove(call);
                if(response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure(ErrorHandler.getErrorResponse(response).toString());
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                calls.remove(call);
                if(call.isCanceled()) {
                    callBack.onCancel();
                } else {
                    callBack.onFailure("Failure");
                }
            }
        });
    }

    public void getCommentsForPokemon(int pokemonId, CallbackInt callBack) {
        PokemonService pokeService = ServiceCreator.getPokemonService();

        Call<Document> call = pokeService.getCommentsForPokemon(getAuthenticationString(), pokemonId);
        calls.add(call);

        call.enqueue(new Callback<Document>() {
            @Override
            public void onResponse(Call<Document> call, Response<Document> response) {
                calls.remove(call);
                if(response.isSuccessful()) {
                    Document document = response.body();
                    JsonBuffer<Object> buffer = document.getLinks();

                    Object object = buffer.get(new JsonAdapter<Object>() {
                        @Nullable
                        @Override
                        public Object fromJson(JsonReader reader) throws IOException {
                            return reader.readJsonValue();
                        }

                        @Override
                        public void toJson(JsonWriter writer, Object value) throws IOException {}

                    });

                    int[] lastPageAndPageSize = getLastPageAndPageSize(object);
                    getComments(pokemonId, lastPageAndPageSize[0], lastPageAndPageSize[1],
                            new CallbackInt() {
                                @Override
                                public void onSuccess(Object object) {
                                    callBack.onSuccess(object);
                                }

                                @Override
                                public void onFailure(String message) {
                                    callBack.onFailure(message);
                                }

                                @Override
                                public void onCancel() {
                                    callBack.onCancel();
                                }
                            });

                } else {
                    callBack.onFailure(ErrorHandler.getErrorResponse(response).toString());
                }
            }

            @Override
            public void onFailure(Call<Document> call, Throwable t) {
                calls.remove(call);
                if(call.isCanceled()) {
                    callBack.onCancel();
                } else {
                    callBack.onFailure("Failure");
                }
            }
        });
    }

    private int[] getLastPageAndPageSize(Object object) {
        int[] returnValues = new int[]{1, 10};
        if(object instanceof Map) {
            Map<String, Object> linksMap = (Map<String, Object>) object;

            Object lastPageLink = linksMap.get("last");
            if(lastPageLink != null && lastPageLink instanceof String) {
                String lastPage = (String) lastPageLink;

                Pattern p = Pattern.compile("https://pokeapi\\.infinum\\.co/api/v1/pokemons/[0-9]+/comments\\?page%5Bnumber%5D\\=([0-9]+)&page%5Bsize%5D\\=([0-9]+)");
                Matcher m = p.matcher(lastPage);
                if(m.matches()) {
                    returnValues[0] = Integer.parseInt(m.group(1));
                    returnValues[1] = Integer.parseInt(m.group(2));
                }
            }
        }

        return returnValues;
    }

    private int sum;
    boolean failed;
    private void getComments(int pokemonId, int lastPageNum, int pageSize, CallbackInt callBack) {
        PokemonService pokeService = ServiceCreator.getPokemonService();

        List<Comment> comments = new ArrayList<>();
        sum = lastPageNum * (lastPageNum + 1) / 2;
        failed = false;
        for(int i = 1; i <= lastPageNum; i++) {
            Call<Comment[]> commentsCall = pokeService.getCommentsForPokemon(getAuthenticationString(), pokemonId, String.valueOf(i), String.valueOf(pageSize));
            calls.add(commentsCall);

            int finalI = i;
            commentsCall.enqueue(new Callback<Comment[]>() {
                @Override
                public void onResponse(Call<Comment[]> call, Response<Comment[]> response) {
                    calls.remove(call);
                    if(response.isSuccessful()) {
                        Comment[] responseComments = response.body();

                        for(int j = 0; j < responseComments.length; j++) {
                            comments.add(responseComments[j]);
                        }

                        sum -= finalI;
                        if(sum == 0) {
                            Comment[] commentsArray = comments.toArray(new Comment[comments.size()]);
                            callBack.onSuccess(commentsArray);
                        }
                    } else {
                        if(!failed) {
                            failed = true;
                            callBack.onFailure(ErrorHandler.getErrorResponse(response).toString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Comment[]> call, Throwable t) {
                    calls.remove(call);
                    if(call.isCanceled()) {
                        callBack.onCancel();
                    } else {
                        callBack.onFailure("Failure");
                    }
                }
            });
        }
    }


    public void getUserById(int userId, CallbackInt callBack) {

        UserService service = ServiceCreator.getUserService();

        Call<User> call = service.getUserById(getAuthenticationString(), userId);
        calls.add(call);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                calls.remove(call);
                if(response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure(ErrorHandler.getErrorResponse(response).toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                calls.remove(call);
                if(call.isCanceled()) {
                    callBack.onCancel();
                } else {
                    callBack.onFailure("Failure");
                }
            }
        });

    }

    public void postPokemonComment(int pokemonId, Comment comment, CallbackInt callBack) {
        PokemonService pokeService = ServiceCreator.getPokemonService();

        Call<Comment> call = pokeService.commentPokemon(getAuthenticationString(), pokemonId, comment);
        calls.add(call);

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                calls.remove(call);
                if(response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure(ErrorHandler.getErrorResponse(response).toString());
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                calls.remove(call);
                if(call.isCanceled()) {
                    callBack.onCancel();
                } else {
                    callBack.onFailure("Failure");
                }
            }
        });
    }
}
