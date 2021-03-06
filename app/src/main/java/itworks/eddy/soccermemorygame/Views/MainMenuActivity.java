package itworks.eddy.soccermemorygame.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import itworks.eddy.soccermemorygame.BackgroundMusic;
import itworks.eddy.soccermemorygame.R;

/** MainMenuActivity - this is the main menu, it is the second activity that the user sees
 *  this activity handles navigation across different menu items using fragments.
 *  background music player will be instantiated in the onCreate method if users settings permit background music
 */

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    Menu navMenu;
    Boolean allowMusic;
    float volume = 0.75f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //first fragment to be displayed is level selection
        getSupportFragmentManager().
                beginTransaction().
                add(R.id.contentLayout, new SelectLevelFragment()).
                commit();

        navMenu = navigationView.getMenu();
        navMenu.findItem(R.id.nav_select_level).setVisible(false); //hide select level menu item
        setVolumeControlStream(AudioManager.STREAM_MUSIC); //is it necessary? need to check how volume change behaves on media player
        Bundle extras = getIntent().getExtras(); //get users settings from bundle
        if (extras != null){
            allowMusic = extras.getBoolean("music");
            volume = extras.getFloat("volume");
        }
        if (allowMusic){ //init and start background music player
            BackgroundMusic.initPlayer(this, allowMusic, volume);
            BackgroundMusic.start();
        }
    }

    @Override
    protected void onPostResume() {
        if (BackgroundMusic.isAllowed()){
            BackgroundMusic.start(); //resume music
        }
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        if (BackgroundMusic.isAllowed()){
            BackgroundMusic.pause(); //pause music
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            if (BackgroundMusic.isAllowed()){
                BackgroundMusic.releasePlayer(); //release MediaPlayer's resources when closing the app
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here, level selection item is shown only when user is in other fragments
        int id = item.getItemId();
        navMenu.findItem(R.id.nav_select_level).setVisible(true);
        if (id == R.id.nav_select_level) {
            toolbar.setTitle(R.string.app_name);
            navMenu.findItem(R.id.nav_select_level).setVisible(false);
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.contentLayout, new SelectLevelFragment()).
                    commit();
        } else if (id == R.id.nav_scores) {
            toolbar.setTitle("High Scores");
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.contentLayout, new ScoresFragment()).
                    commit();
        } else if (id == R.id.nav_settings) {
            toolbar.setTitle("Settings");
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.contentLayout, new SettingsFragment()).
                    commit();
        } else if (id == R.id.nav_about) {
            toolbar.setTitle("About");
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.contentLayout, new AboutFragment()).
                    commit();
        } else if (id == R.id.nav_logout) {
            displayLogoutDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayLogoutDialog() {
        //display logout dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setTitle("Log out");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //clear shared preferences and start welcome activity
                performLocalLogout();
                Intent intent = new Intent(MainMenuActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void performLocalLogout(){ //removes users details and setting from shared preferences
        SharedPreferences appPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.remove("username");
        editor.remove("music");
        editor.remove("volume");
        editor.remove("lvl1");
        editor.remove("lvl2");
        editor.remove("lvl3");
        editor.apply();
    }
}
