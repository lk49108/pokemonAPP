package com.example.leonardo.pokemonapp.base;

/**
 * Created by leonardo on 04/08/17.
 */

public interface BaseMVP {

    interface View {

        void showError(String errorMessage);

    }

    interface State {}

    interface Presenter {

        void cancelCall();

    }

    interface Interactor {

        void cancel();

    }

}
