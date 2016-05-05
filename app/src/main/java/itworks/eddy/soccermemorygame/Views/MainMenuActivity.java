package itworks.eddy.soccermemorygame.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AsyncPlayer;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import itworks.eddy.soccermemorygame.R;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    BackgroundMusic backgroundMusic = new BackgroundMusic("Background Music");
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    Menu navMenu;
    String resourcePath;
    Uri musicUri;
    Boolean isPlaying;

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
        getSupportFragmentManager().
                beginTransaction().
                add(R.id.contentLayout, new SelectLevelFragment()).
                commit();
        navMenu = navigationView.getMenu();
        navMenu.findItem(R.id.nav_select_level).setVisible(false);
        //init music
        resourcePath = "android.resource://" + getPackageName() + "/";
        musicUri = Uri.parse(resourcePath + R.raw.wayne_kinos_01_progressions_intro);
        //backgroundMusic.execute();
        backgroundMusic.play(getApplicationContext(), musicUri, true, AudioManager.STREAM_MUSIC);
        isPlaying = true;
    }

    @Override
    protected void onPostResume() {
        Log.d("onPostResume", "!");
        backgroundMusic.play(getApplicationContext(), musicUri, true, AudioManager.STREAM_MUSIC);
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        Log.d("Pause", "!");
        backgroundMusic.stop();
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
        // Handle navigation view item clicks here.
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
            //// TODO: 01/05/2016 high scores fragment (Recycler view)
            toolbar.setTitle("High Scores");
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
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //clear shared preferences and start welcome activity
                SharedPreferences appPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = appPreferences.edit();
                editor.remove("username");
                editor.apply();
                Log.d("logout", "OK");
                Log.d("SharedPrefers cleared", "key:username");
                Intent intent = new Intent(MainMenuActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("stay", "cancel");
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class BackgroundMusic extends AsyncPlayer{
        @Override
        public void play(Context context, Uri uri, boolean looping, AudioAttributes attributes) throws IllegalArgumentException {
            super.play(context, uri, looping, attributes);
        }

        public BackgroundMusic(String tag) {
            super(tag);
        }

        @Override
        public void stop() {
            super.stop();
        }
    }

    /*public class BackgroundMusic extends AsyncTask {

        MediaPlayer soundPlayer;
        @Override
        protected Object doInBackground(Object[] params) {
            soundPlayer = MediaPlayer.create(MainMenuActivity.this, R.raw.wayne_kinos_01_progressions_intro);
            soundPlayer.setLooping(true);
            soundPlayer.start();
            return null;
        }

        public void pause(){
            soundPlayer.pause();
        }
    }*/
}
