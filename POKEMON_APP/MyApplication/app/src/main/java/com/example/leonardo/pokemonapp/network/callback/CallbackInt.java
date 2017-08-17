package com.example.leonardo.pokemonapp.network.callback;

/**
 * Created by leonardo on 25/07/17.
 */

public interface CallbackInt {
    void onSuccess(Object object);
    void onFailure(String message);
    void onCancel();
}
