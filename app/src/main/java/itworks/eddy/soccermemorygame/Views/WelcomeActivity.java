package itworks.eddy.soccermemorygame.Views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itworks.eddy.soccermemorygame.Models.ServerResponse;
import itworks.eddy.soccermemorygame.Models.User;
import itworks.eddy.soccermemorygame.Models.UsersList;
import itworks.eddy.soccermemorygame.NoSpaceTextWatcher;
import itworks.eddy.soccermemorygame.R;
import itworks.eddy.soccermemorygame.RESTaccess.apiServices;
import itworks.eddy.soccermemorygame.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * WelcomeActivity - this is the first activity that the user sees:
 * 1.  if no network connectivity is available - the user is prompted
 * 2.  shared preferences are accessed in order to check for previous session
 * 3.  if a previous session was detected, users settings are loaded, otherwise default settings
 * 4.  login/registration options are presented to the user if no previous session was detected,
 *     input is validated and then authenticated via the database using asynchronous web access
 * 5.  in both cases the main menu activity will be launched.
 */
public class WelcomeActivity extends AppCompatActivity {

    @Bind(R.id.tbUsername)
    EditText tbUsername;
    @Bind(R.id.tbPassword)
    EditText tbPassword;
    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.btnRegister)
    Button btnRegister;
    @Bind(R.id.tbVerifyPassword)
    EditText tbVerifyPassword;
    @Bind(R.id.btnCancel)
    Button btnCancel;
    @Bind(R.id.ivLogo)
    ImageView ivLogo;
    private Boolean performRegistration = false;
    private String username;
    private String password;
    private String passwordVerify;
    private Boolean musicState;
    private float volume;
    apiServices api;
    SharedPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        //add custom TextChangedListener to EditText views
        tbUsername.addTextChangedListener(new NoSpaceTextWatcher(tbUsername));
        tbPassword.addTextChangedListener(new NoSpaceTextWatcher(tbPassword));
        tbVerifyPassword.addTextChangedListener(new NoSpaceTextWatcher(tbVerifyPassword));

        hideLoginOpts();
        apiServiceInit();
        if (!isNetworkAvailable()){
            networkDialog();
        }
        if (!getLoggedInState()) {
            showLoginOpts();
        } else {
            launchMenu();
        }

    }

    private boolean isNetworkAvailable() {//check for network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null){
            return activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void networkDialog() { //if no connectivity: close or restart app
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Internet connection is unavailable").setTitle("Can't launch game");
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getIntent();
                finish();
                startActivity(intent); //relaunch current activity
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void apiServiceInit() { //init Retrofit
        final String BASE_URL = getString(R.string.api_server_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(apiServices.class);
    }

    private boolean getLoggedInState() { //check if the user has a session on the client
        appPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        username = appPreferences.getString("username", "");
        musicState = appPreferences.getBoolean("music", true);
        volume = appPreferences.getFloat("volume", 0.75f);
        if (!username.equals("")) {
            Session.currentUser = new User(username);
            Session.currentUser.setLvl1(appPreferences.getInt("lvl1", 0));
            Session.currentUser.setLvl2(appPreferences.getInt("lvl2", 0));
            Session.currentUser.setLvl3(appPreferences.getInt("lvl3", 0));
            return true;
        } else {
            return false;
        }
    }

    private void hideLoginOpts() { //hide views
        tbUsername.setVisibility(View.INVISIBLE);
        tbPassword.setVisibility(View.INVISIBLE);
        tbVerifyPassword.setVisibility(View.INVISIBLE);
        btnLogin.setVisibility(View.INVISIBLE);
        btnRegister.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
    }

    private void showLoginOpts() { //show views necessary to login
        tbUsername.setVisibility(View.VISIBLE);
        tbPassword.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
    }

    private void launchMenu() { //launch the main menu by intent
        ivLogo.animate().rotation(-360).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //start main menu activity once the animation ends
                Intent intent = new Intent(WelcomeActivity.this, MainMenuActivity.class);
                //pass user settings to intent
                intent.putExtra("music", musicState);
                intent.putExtra("volume", volume);
                startActivity(intent);
                finish();
            }
        }).start();
    }

    private boolean verifyInput() { //verify user input in login/registration fields
        Boolean verified = true;
        username = tbUsername.getText().toString();
        password = tbPassword.getText().toString();
        if (performRegistration) { //if registration is performed
            passwordVerify = tbVerifyPassword.getText().toString();
        }
        Log.d("entered details", username + " " + password + " " + passwordVerify);
        if (username.length() < 3) { //username length
            verified = false;
            Toast.makeText(getApplicationContext(), "Username must be at least 3 characters long", Toast.LENGTH_SHORT).show();
            Log.d("short username", username);
        }
        if (password.length() < 4) { //password length
            verified = false;
            Toast.makeText(getApplicationContext(), "Password must be at least 4 characters long", Toast.LENGTH_SHORT).show();
            Log.d("short password", password);
        }
        if (performRegistration && !passwordVerify.equals(null) && !password.equals(passwordVerify)) {
            //password verification upon registration
            verified = false;
            Toast.makeText(getApplicationContext(), "Password don't match", Toast.LENGTH_SHORT).show();
            Log.d("password don't match", password + " " + passwordVerify);
        }
        return verified;
    }

    private void registerUser() { //register a new user in the server
        Log.d("Async-->", "Register");
        hashPassword();
        Call<ServerResponse> register = api.register(username, password);
        register.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) { //if user was added, start local session
                    Session.currentUser = new User(username, 0, 0, 0);
                    registerDialog();
                } else {
                    if (response.code() == 400) { //username exists
                        Log.d("username exists", username);
                        Toast.makeText(getApplicationContext(), "Username exists, please enter different username", Toast.LENGTH_SHORT).show();
                    } else { //server returned error
                        String errMsg = response.raw().message();
                        Log.d("Error", errMsg);
                    }
                    btnRegister.setEnabled(true);
                    btnCancel.setEnabled(true);
                    SetEditText(true);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }

    private void login() { //login, verify user existence in db
        hashPassword();
        //User user = new User(username, password); //needed for post form based registration
        Call<UsersList> login = api.login(username, password);
        Log.d("Async-->", "Login");
        login.enqueue(new Callback<UsersList>() {
            @Override
            public void onResponse(Call<UsersList> call, Response<UsersList> response) {
                Log.d("login", "res");
                if (response.isSuccessful()) { //if server returned no error, start local session
                    Session.currentUser = response.body().getUser().get(0);
                    if (Session.currentUser != null) {
                        performLocalLogin();
                        launchMenu();
                    }
                } else { //server returned error
                    if (response.code() == 400) { //will return if username exists but password is incorrect
                        Log.d("wrong password", username);
                        Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                    }
                    String errMsg = response.raw().message();
                    Log.d("Error", errMsg);
                    btnLogin.setEnabled(true);
                    btnRegister.setEnabled(true);
                    SetEditText(true);
                }
            }

            @Override
            public void onFailure(Call<UsersList> call, Throwable t) {

            }
        });

    }

    private void hashPassword() { //hash user provided password
        String base = password;
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.d("error->", "Error initializing message digest");
        }
        byte[] hash = new byte[0];
        try {
            hash = digest.digest(base.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        password = hexString.toString();
        Log.d("Password Hashed-->", password);
    }

    private void registerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Welcome " + username).setTitle("Successful Registration");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                performLocalLogin();
                launchMenu();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void performLocalLogin() { //save username and scores in shared preferences
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putString("username", Session.currentUser.getUsername());
        editor.putInt("lvl1", Session.currentUser.getLvl1());
        editor.putInt("lvl2", Session.currentUser.getLvl2());
        editor.putInt("lvl3", Session.currentUser.getLvl3());
        editor.apply();
    }

    @OnClick({R.id.btnLogin, R.id.btnRegister, R.id.btnCancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                Log.d("Click", "login");
                if (verifyInput()) {
                    Log.d("Verified input", "True");
                    btnLogin.setEnabled(false);
                    btnRegister.setEnabled(false);
                    SetEditText(false);
                    login();
                }
                break;
            case R.id.btnRegister:
                Log.d("Click", "register");
                performRegistration = true;
                btnLogin.setVisibility(View.INVISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                tbVerifyPassword.setVisibility(View.VISIBLE);
                if (verifyInput()) {
                    Log.d("Verified input", "true");
                    btnRegister.setEnabled(false);
                    btnCancel.setEnabled(false);
                    SetEditText(false);
                    registerUser();
                }
                break;
            case R.id.btnCancel: //cancel registration - will clear fields and hide them
                tbUsername.setText("");
                tbPassword.setText("");
                tbVerifyPassword.setText("");
                btnLogin.setVisibility(View.VISIBLE);
                tbVerifyPassword.setVisibility(View.INVISIBLE);
                btnCancel.setVisibility(View.INVISIBLE);
                performRegistration = false;
                break;
        }
    }

    private void SetEditText(Boolean state) {
        //enable or disable EditText views
        tbUsername.setEnabled(state);
        tbUsername.setClickable(state);
        tbPassword.setEnabled(state);
        tbPassword.setClickable(state);
        if (performRegistration) {
            tbVerifyPassword.setEnabled(state);
            tbVerifyPassword.setClickable(state);
        }
    }
}
