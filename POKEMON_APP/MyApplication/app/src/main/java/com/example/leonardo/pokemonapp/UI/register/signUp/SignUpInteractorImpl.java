package com.example.leonardo.pokemonapp.UI.register.signUp;

import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.executor.NetworkExecutor;
import com.example.leonardo.pokemonapp.network.resources.User;

/**
 * Created by leonardo on 05/08/17.
 */

public class SignUpInteractorImpl implements SignUpMVP.Interactor {
    @Override
    public void cancel() {
        NetworkExecutor.getInstance().destroyAnyPendingTransactions();
    }

    @Override
    public void SignUp(String email, String username, String password, String confPassword, CallbackInt callback) {
        User user = new User();
        user.setEmail(email);
        user.setUserName(username);
        user.setPassword(password);
        user.setConfirmationPassword(confPassword);

        NetworkExecutor.getInstance().signUp(user, callback);
    }
}
