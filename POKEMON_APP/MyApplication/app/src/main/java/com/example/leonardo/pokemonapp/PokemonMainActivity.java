package com.example.leonardo.pokemonapp;

import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.fragmentHandler.FragmentMainActivityHandlerHorizontalTablet;
import com.example.leonardo.pokemonapp.fragmentHandler.FragmentMainActivityHandlerInt;
import com.example.leonardo.pokemonapp.fragmentHandler.FragmentMainActivityHandlerRegular;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PokemonMainActivity extends AppCompatActivity implements PokemonListFragment.PokemonListFragmentListener, PokemonAddFragment.PokemonAddFragmentListener {

    @BindString(R.string.pokemon_list_activity_title)
    String title;

    @BindView(R.id.activity_pokemon_main_fragment_container_main)
    public FrameLayout pokemonMainFragmentContainer;
    @BindView(R.id.activity_pokemon_main_toolbar)
    Toolbar pokemonMainActivityToolbar;
    @BindView(R.id.activity_pokemon_main_fragment_container_pokemon_add_details)
    @Nullable
    public FrameLayout pokemonAddDetailFragmentContainer;

    FragmentMainActivityHandlerInt fragmentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pokemon);

        ButterKnife.bind(this);

        setSupportActionBar(pokemonMainActivityToolbar);
        getSupportActionBar().setTitle(title);

        if(pokemonAddDetailFragmentContainer != null) {
            fragmentHandler = new FragmentMainActivityHandlerHorizontalTablet(this);
        } else {
            fragmentHandler = new FragmentMainActivityHandlerRegular(this);
        }

        if(savedInstanceState == null) {
            initializeFragments();
        } else {
            boolean landIntoVertOnTablet = savedInstanceState.getString("promjenaOrijentacijeIzHorizontalneUVertikalnu") != null && verticalOrientation();
            boolean vertIntoLandTablet = savedInstanceState.getString("promjenaOrijentacijeIzVertikalneUHorizontalnu") != null && !verticalOrientation();
            if(landIntoVertOnTablet || vertIntoLandTablet) {
                fragmentHandler.fromOtherConfigurationInitialization();
            }
        }
    }

    public boolean verticalOrientation() {
        return fragmentHandler instanceof FragmentMainActivityHandlerRegular;
    }

    private void initializeFragments() {
        fragmentHandler.initializeFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportFragmentManager().executePendingTransactions();

        if(!onScreenAddPokemonFragment() && !verticalAndOnScreenPokemonDetails()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.pokemon_list_activity_menu, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onScreenAddPokemonFragment() {
        Fragment addPokemonFragment = getSupportFragmentManager().findFragmentByTag("addPokemonFragment");
        if(addPokemonFragment != null && addPokemonFragment.isVisible()) {
            return true;
        }

        return false;
    }

    private boolean verticalAndOnScreenPokemonDetails() {
        Fragment pokemonDetailsFragment = getSupportFragmentManager().findFragmentByTag("pokemonDetailsFragment");
        if(pokemonDetailsFragment != null && pokemonDetailsFragment.isVisible() && (fragmentHandler instanceof FragmentMainActivityHandlerRegular)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_add_pokemon:
                insertPokemonAddFragmentToActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void insertPokemonAddFragmentToActivity() {
        fragmentHandler.insertPokemonAddFragmentToActivity();
    }

    @Override
    public void pokemonSelected(Pokemon pokemon) {
        fragmentHandler.pokemonSelected(pokemon);
    }

    @Override
    public void pokemonAdded(Pokemon pokemon) {
        fragmentHandler.pokemonAdded(pokemon);
    }

    @Override
    public void pokemonCreationCanceled() {
       fragmentHandler.pokemonCreationCanceled();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
       if(!fragmentHandler.onBackPressed()) {
           super.onBackPressed();
       }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(!verticalOrientation()) {
            outState.putString("promjenaOrijentacijeIzHorizontalneUVertikalnu", "promjena");
        } else {
            outState.putString("promjenaOrijentacijeIzVertikalneUHorizontalnu", "promjena");
        }

    }

}