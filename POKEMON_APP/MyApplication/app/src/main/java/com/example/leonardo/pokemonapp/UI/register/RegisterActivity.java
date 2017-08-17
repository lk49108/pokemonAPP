package com.example.leonardo.pokemonapp.UI.register;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.UI.register.logIn.LogInFragment;
import com.example.leonardo.pokemonapp.UI.pokemon.PokemonMainActivity;
import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.UI.register.signUp.SignUpFragment;

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

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign up");

        if(savedInstanceState == null) {
            initializeLayout();
        } else {
            if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
                getSupportActionBar().hide();
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            } else {
                getSupportActionBar().show();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void initializeLayout() {
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer.getId(), LogInFragment.newInstance()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
            getSupportActionBar().hide();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public void signUpInsteadOfLogIn() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer.getId(), SignUpFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();

        getSupportActionBar().show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void navigateToPokemonListScreen() {
        Intent intent = new Intent(this, PokemonMainActivity.class);
        startActivity(intent);
        finish();
    }

}