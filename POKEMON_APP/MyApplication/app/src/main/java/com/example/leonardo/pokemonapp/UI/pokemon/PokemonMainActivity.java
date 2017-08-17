package com.example.leonardo.pokemonapp.UI.pokemon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonardo.pokemonapp.UI.pokemon.pokemonAdd.PokemonAddFragment;
import com.example.leonardo.pokemonapp.UI.pokemon.pokemonComments.PokemonCommentsFragment;
import com.example.leonardo.pokemonapp.UI.pokemon.pokemonDetails.PokemonDetailsFragment;
import com.example.leonardo.pokemonapp.UI.pokemon.pokemonList.PokemonListFragment;
import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.UI.register.RegisterActivity;
import com.example.leonardo.pokemonapp.UI.pokemon.fragmentHandlers.FragmentMainActivityHandlerHorizontalTablet;
import com.example.leonardo.pokemonapp.UI.pokemon.fragmentHandlers.FragmentMainActivityHandlerInt;
import com.example.leonardo.pokemonapp.UI.pokemon.fragmentHandlers.FragmentMainActivityHandlerRegular;
import com.example.leonardo.pokemonapp.network.callback.CallbackInt;
import com.example.leonardo.pokemonapp.network.executor.NetworkExecutor;
import com.example.leonardo.pokemonapp.network.resources.Comment;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.network.resources.User;
import com.example.leonardo.pokemonapp.util.UserUtil;
import com.example.leonardo.pokemonapp.util.Util;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PokemonMainActivity extends AppCompatActivity implements PokemonListFragment.PokemonListFragmentListener,
        PokemonAddFragment.PokemonAddFragmentListener, NavigationView.OnNavigationItemSelectedListener, PokemonDetailsFragment.PokemonDetailsFragmentListener {

    @BindString(R.string.pokemon_list_activity_title)
    String title;

    @BindView(R.id.activity_pokemon_main_fragment_container_main)
    public FrameLayout pokemonMainFragmentContainer;
    @BindView(R.id.activity_pokemon_main_toolbar)
    Toolbar pokemonMainActivityToolbar;
    @BindView(R.id.activity_pokemon_main_fragment_container_pokemon_add_details)
    @Nullable
    public FrameLayout pokemonAddDetailFragmentContainer;
    @BindView(R.id.activity_main_drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.activity_main_navigation_view)
    NavigationView navigationView;

    private TextView emailTextView;
    private TextView usernameTextView;
    private View navigationViewHeader;

    FragmentMainActivityHandlerInt fragmentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_pokemon);

        ButterKnife.bind(this);

        initializeDrawerBehaviour();

        setSupportActionBar(pokemonMainActivityToolbar);
        invalidateOptionsMenu();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fragmentHandler = pokemonAddDetailFragmentContainer != null ? new FragmentMainActivityHandlerHorizontalTablet(this) : new FragmentMainActivityHandlerRegular(this);

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

    private void initializeDrawerBehaviour() {

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                pokemonMainActivityToolbar,
                R.string.drawer_open,
                R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationViewHeader = (View) navigationView.getHeaderView(0);
        emailTextView = (TextView) navigationViewHeader.findViewById(R.id.nav_header_main_activity_email);
        usernameTextView = (TextView) navigationViewHeader.findViewById(R.id.nav_header_main_activity_username);

        User loggedInUser = UserUtil.getLoggedInUser();
        emailTextView.setText(loggedInUser.getEmail());
        usernameTextView.setText(loggedInUser.getUserName());
    }

    public void logOut() {

        boolean internetActionActive = Util.internetConnectionActive();

        if(!internetActionActive) {
            Toast.makeText(this, "Offline mode, you can not log out from the server.", Toast.LENGTH_LONG).show();
            return;
        }

        NetworkExecutor.getInstance().logOut(new CallbackInt() {
            @Override
            public void onSuccess(Object object) {
                UserUtil.logOutUser();
                Intent intent = new Intent(PokemonMainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(PokemonMainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {}
        });
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

        //pokemon list is rendered if true
        if(!verticalAndOnScreenAddPokemonFragment() && !verticalAndOnScreenPokemonDetails() && !verticalAndOnScreenPokemonComments()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.pokemon_list_activity_menu, menu);
            getSupportActionBar().setTitle("Pokemon");
        } else if(verticalAndOnScreenPokemonComments()) {
            PokemonCommentsFragment pokemonCommentsFragment = (PokemonCommentsFragment) getSupportFragmentManager().findFragmentByTag("pokemonCommentsFragment");
            getSupportActionBar().setTitle(pokemonCommentsFragment.getPokemonName() + " comments");
        } else if(verticalAndOnScreenPokemonDetails()) {
            getSupportActionBar().setTitle("Details");
        } else {
            getSupportActionBar().setTitle("Add New Pokemon");
        }

        return super.onCreateOptionsMenu(menu);
    }

    private boolean verticalAndOnScreenPokemonComments() {
        Fragment pokemonCommentsFragment = getSupportFragmentManager().findFragmentByTag("pokemonCommentsFragment");
        if(pokemonCommentsFragment != null && pokemonCommentsFragment.isVisible() && verticalOrientation()) {
            return true;
        }

        return false;
    }

    public boolean verticalAndOnScreenAddPokemonFragment() {
        Fragment addPokemonFragment = getSupportFragmentManager().findFragmentByTag("addPokemonFragment");
        if(addPokemonFragment != null && addPokemonFragment.isVisible() && verticalOrientation()) {
            return true;
        }

        return false;
    }

    private boolean verticalAndOnScreenPokemonDetails() {
        Fragment pokemonDetailsFragment = getSupportFragmentManager().findFragmentByTag("pokemonDetailsFragment");
        if(pokemonDetailsFragment != null && pokemonDetailsFragment.isVisible() && verticalOrientation()) {
            return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_add_pokemon:
                if(!Util.internetConnectionActive()) {
                    Toast.makeText(this, "No internet connection, you can not add new pokemon", Toast.LENGTH_LONG).show();
                    return true;
                }

                insertPokemonAddFragmentToActivity();
                return true;
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
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
    public void onBackPressed() {
        if(!fragmentHandler.onBackPressed()) {
            moveTaskToBack(true);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawer.closeDrawers();

        int id = item.getItemId();

        switch (id) {
            case R.id.menu_nav_home:
                if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    onBackPressed();
                }
                return true;
            case R.id.menu_nav_log_out:
                logOut();
                return true;
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkExecutor.getInstance().destroyAnyPendingTransactions();
    }

    @Override
    public void onShowAllPokemonsClicked(Comment[] comments, String pokemonName) {
        fragmentHandler.onShowAllCommentsClicked(comments, pokemonName);
    }

    public boolean onScreenCommentsScreen() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("pokemonCommentsFragment");
        if(fragment != null && fragment.isVisible()) {
            return true;
        }

        return false;
    }

    public boolean onScreenAddPokemonFragment() {
        Fragment addPokemonFragment = getSupportFragmentManager().findFragmentByTag("addPokemonFragment");
        if(addPokemonFragment != null && addPokemonFragment.isVisible()) {
            return true;
        }

        return false;
    }
}