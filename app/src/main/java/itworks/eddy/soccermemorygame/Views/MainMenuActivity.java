package itworks.eddy.soccermemorygame.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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

    MediaPlayer soundPlayer;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    Boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //init sound
        soundPlayer = MediaPlayer.create(this, R.raw.wayne_kinos_01_progressions_intro);
        soundPlayer.start();
        isPlaying = soundPlayer.isPlaying();
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

        if (id == R.id.nav_scores) {
            //// TODO: 01/05/2016 high scores fragment (Recycler view)
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().
                    beginTransaction().
                    add(R.id.contentLayout, new SettingsFragment()).
                    commit();
        } else if (id == R.id.nav_about) {
            //// TODO: 01/05/2016 about fragment
            if (isPlaying) {
                soundPlayer.stop();
                isPlaying = soundPlayer.isPlaying();
                soundPlayer.reset();
                soundPlayer.release();
            }
            else {
                soundPlayer = MediaPlayer.create(this, R.raw.wayne_kinos_01_progressions_intro);
                soundPlayer.start();
                isPlaying = soundPlayer.isPlaying();
            }
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
}
