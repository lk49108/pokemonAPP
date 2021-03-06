package com.example.leonardo.pokemonapp.UI.register.logIn;

import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.executor.NetworkExecutor;
import com.example.leonardo.pokemonapp.network.resources.User;

/**
 * Created by leonardo on 04/08/17.
 */

public class LogInInteractorImpl implements LogInMVP.Interactor {

    @Override
    public void cancel() {
        NetworkExecutor.getInstance().destroyAnyPendingTransactions();
    }

    @Override
    public void logIn(String email, String password, CallbackInt callback) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        NetworkExecutor.getInstance().logIn(user, callback);
    }

}
