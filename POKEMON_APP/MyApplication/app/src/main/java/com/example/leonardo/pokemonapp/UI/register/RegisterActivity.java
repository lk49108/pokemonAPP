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
import com.example.leonardo.pokemonapp.PokemonMainActivity;
import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.UI.register.signUp.SignUpFragment;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.executor.NetworkExecutor;
import com.example.leonardo.pokemonapp.network.resources.User;

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

        if(savedInstanceState == null) {
            initializeLayout();
        }
    }

    private void initializeLayout() {
        getSupportActionBar().hide();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer.getId(), LogInFragment.newInstance(false)).commit();

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
    public void logIn(User user) {
        NetworkExecutor.getInstance().logIn(user, new CallbackInt() {
            @Override
            public void onSuccess(Object object) {
                Intent intent = new Intent(RegisterActivity.this, PokemonMainActivity.class);
                startActivityForResult(intent, 20);
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
    public void finishActivity() {
        Log.d("logIn", "finishing");
        Intent intent = new Intent(this, PokemonMainActivity.class);
        startActivityForResult(intent, 20);
    }


    @Override
    public void signUp(User user) {
        NetworkExecutor.getInstance().signUp(user, new CallbackInt() {
            @Override
            public void onSuccess(Object object) {
                Log.d("logIn", "signup");
                Intent intent = new Intent(RegisterActivity.this, PokemonMainActivity.class);
                startActivityForResult(intent, 20);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(RegisterActivity.this, "Error when trying to sign up", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkExecutor.getInstance().destroyAnyPendingTransactions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CANCELED) {
            finish();
        } else if (resultCode == RESULT_OK) {
            //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //transaction.replace(fragmentContainer.getId(), LogInFragment.newInstance(data.getBooleanExtra("logOutPressed", false))).commit();
        }
    }
}