package com.example.leonardo.pokemonapp.base.baseFragment;

import com.example.leonardo.pokemonapp.base.BaseMVP;

/**
 * Created by leonardo on 04/08/17.
 */

public interface BaseActivityMVP {

    interface Presenter extends BaseMVP.View {

        void initLayout(BaseMVP.State savedInstanceState);

    }

    interface View extends BaseMVP.View {}

}
