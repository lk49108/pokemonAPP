package com.example.leonardo.pokemonapp.base;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.base.baseFragment.BaseFragmentMVP;

/**
 * Created by leonardo on 04/08/17.
 */

public class BaseFragment extends Fragment implements BaseFragmentMVP.View {

    private ProgressDialog progressDialog;

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress(String title, String message) {
        if(progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(
                    getActivity(),
                    title,
                    message,
                    true,
                    false
            );
        }
    }

    @Override
    public void hideProgress() {
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
