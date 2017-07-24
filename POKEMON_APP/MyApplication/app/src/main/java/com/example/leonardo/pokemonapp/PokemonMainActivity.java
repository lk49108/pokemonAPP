package com.example.leonardo.pokemonapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.fragmentHandler.FragmentMainActivityHandlerHorizontalTablet;
import com.example.leonardo.pokemonapp.fragmentHandler.FragmentMainActivityHandlerInt;
import com.example.leonardo.pokemonapp.fragmentHandler.FragmentMainActivityHandlerRegular;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PokemonMainActivity extends AppCompatActivity implements PokemonListFragment.PokemonListFragmentListener, PokemonAddFragment.PokemonAddFragmentListener {

    private DrawerLayout drawerLayout;
    private RecyclerView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private FrameLayout drawerFragmentContainer;

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

        /*
        drawerLayout  = (DrawerLayout) LayoutInflater.from(this).inflate(R.layout.drawer_layout, null, false);
        drawerList = (RecyclerView) drawerLayout.findViewById(R.id.drawer_layout_left_drawer);
        drawerFragmentContainer = (FrameLayout) drawerLayout.findViewById(R.id.drawer_layout_fragment_container);
        initializeDrawerBehaviour();
        */

        setSupportActionBar(pokemonMainActivityToolbar);
        getSupportActionBar().setTitle(title);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

/*
    private void initializeDrawerBehaviour() {

        drawerList.setAdapter(new DrawerListAdapter(this));
        drawerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                getSupportActionBar().setTitle("drawer");
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);

        getSupportFragmentManager().beginTransaction()
                .replace(drawerFragmentContainer.getId(), DrawerFragment.newInstance()).commit();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
*/

    public void logOut() {
        Toast.makeText(this, "log out...", Toast.LENGTH_LONG).show();
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

       //if(drawerToggle.onOptionsItemSelected(item)) {
         //  Toast.makeText(this, "sdfasf", Toast.LENGTH_SHORT).show();
           // return true;
        //}

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