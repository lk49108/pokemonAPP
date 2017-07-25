package com.example.leonardo.pokemonapp;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.executor.NetworkExecutor;
import com.example.leonardo.pokemonapp.network.resources.User;
import com.example.leonardo.pokemonapp.util.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements LogInFragment.LogInFragmentListener, SignUpFragment.SignUpFragmentListener {

    @BindView(R.id.activity_register_toolbar)
    Toolbar toolbar;

    @BindView(R.id.acivity_register_fragment_container)
    FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(UserUtil.loggedIn()) {
            Intent intent = new Intent(this, PokemonMainActivity.class);
            startActivity(intent);
            finish();
        }

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if(savedInstanceState == null) {
            initializeLayout();
        }
    }

    private void initializeLayout() {
        getSupportActionBar().hide();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer.getId(), LogInFragment.newInstance()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStackImmediate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void logIn(User user) {
        NetworkExecutor.getInstance().logIn(user, new CallbackInt() {
            @Override
            public void onSuccess(Object object) {
                Intent intent = new Intent(RegisterActivity.this, PokemonMainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(RegisterActivity.this, "Error when trying to log in", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void signUpInsteadOfLogIn() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer.getId(), SignUpFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void signUp(User user) {
        NetworkExecutor.getInstance().signUp(user, new CallbackInt() {
            @Override
            public void onSuccess(Object object) {
                Intent intent = new Intent(RegisterActivity.this, PokemonMainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(RegisterActivity.this, "Error when trying to log in", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkExecutor.getInstance().destroyAnyPendingTransactions();
    }
}
