package itworks.eddy.soccermemorygame.Views;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itworks.eddy.soccermemorygame.BackgroundMusic;
import itworks.eddy.soccermemorygame.Models.ServerResponse;
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
 * SettingsFragment - this is the settings fragment
 * the user can control music/sound settings from here
 * the user can reset his/her scores
 * the user can deactivate the account
 */
public class SettingsFragment extends Fragment {


    @Bind(R.id.switchEffects)
    Switch switchEffects;
    @Bind(R.id.switchMusic)
    Switch switchMusic;
    @Bind(R.id.seekBarVolume)
    SeekBar seekBarVolume;
    @Bind(R.id.btResetScores)
    Button btResetScores;
    @Bind(R.id.btDeleteUser)
    Button btDeleteUser;
    @Bind(R.id.tbVerification)
    EditText tbVerification;
    @Bind(R.id.btnConfirm)
    Button btnConfirm;
    @Bind(R.id.btCancel)
    Button btCancel;
    apiServices api;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        initViews();
        apiServiceInit();
        return view;
    }

    private void initViews() { //init seekbar and switches according to users settings
        seekBarVolume.setProgress(BackgroundMusic.getVolumeInt());
        seekBarVolume.setMax(100);
        if (!BackgroundMusic.isAllowed()) {
            switchMusic.setChecked(false);
            seekBarVolume.setEnabled(false);
        }

        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BackgroundMusic.setVolume(progress / 100f); //adjust background music volume according to slider
                saveSettings("volume");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        switchMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //enable or disable background music
                if (isChecked) {
                    BackgroundMusic.initPlayer(getContext(), isChecked, BackgroundMusic.getVolume());
                    BackgroundMusic.start();

                } else {
                    BackgroundMusic.releasePlayer();
                }
                seekBarVolume.setEnabled(isChecked);
                saveSettings("music");
            }
        });
        tbVerification.addTextChangedListener(new NoSpaceTextWatcher(tbVerification));
    }

    private void saveSettings(String option) { //save user settings to shared preferences
        SharedPreferences appPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = appPreferences.edit();
        switch (option) {
            case "volume":
                editor.putFloat("volume", BackgroundMusic.getVolume());
                editor.apply();
                break;
            case "music":
                editor.putBoolean("music", BackgroundMusic.isAllowed());
                editor.apply();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btResetScores, R.id.btDeleteUser, R.id.btnConfirm, R.id.btCancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btResetScores:
                showRestDialog();
                break;
            case R.id.btDeleteUser:
                showViews(View.VISIBLE);
                btDeleteUser.setEnabled(false);
                btResetScores.setEnabled(false);
                break;
            case R.id.btnConfirm:
                enableViews(false);
                deleteAccount();
                break;
            case R.id.btCancel:
                tbVerification.setText("");
                showViews(View.INVISIBLE);
                btDeleteUser.setEnabled(true);
                btResetScores.setEnabled(true);
                break;
        }
    }

    private void showRestDialog() {
        //display reset scores dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure?").setTitle("Reset scores");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btResetScores.setEnabled(true);
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //reset scores in database and locally
                btResetScores.setEnabled(false);
                resetScores();
                resetInPreferences();
            }
        });
        builder.setCancelable(false);
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

    private void resetScores() { //reset users scores
        if (!Session.currentUser.allZeros()) {
            Call<ServerResponse> register = api.resetScores(Session.currentUser.getUsername());
            register.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    btResetScores.setEnabled(true);
                    if (response.isSuccessful()) { //if scores were reset
                        Toast.makeText(getContext(), "Scores reset", Toast.LENGTH_SHORT).show();
                    } else {    //server returned an error
                        Toast.makeText(getContext(), "Error, scores not changed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                }
            });
        }
    }

    private void resetInPreferences() { //reset local saved scores
        SharedPreferences appPreferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putInt("lvl1", 0);
        editor.putInt("lvl2", 0);
        editor.putInt("lvl3", 0);
        editor.apply();
        Session.currentUser.resetScores();
    }

    private void deleteAccount() { //delete users account
        String password = hashPassword();
        Call<ServerResponse> deleteUser = api.deleteUser(Session.currentUser.getUsername(), password);
        deleteUser.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) { //account was deleted
                    Toast.makeText(getContext(), "Account deleted", Toast.LENGTH_SHORT).show();
                    deletePreferences();
                    Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else { //server returned error
                    if (response.code() == 400) { //will return if password is incorrect
                        Toast.makeText(getContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error: " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    }
                    enableViews(true);
                    btResetScores.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }

    private void deletePreferences() {
        SharedPreferences appPreferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.remove("username");
        editor.remove("music");
        editor.remove("volume");
        editor.remove("lvl1");
        editor.remove("lvl2");
        editor.remove("lvl3");
        editor.apply();
    }

    private String hashPassword() {
        String base = tbVerification.getText().toString();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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
        return hexString.toString();
    }

    private void showViews(int visibility) {
        tbVerification.setVisibility(visibility);
        btnConfirm.setVisibility(visibility);
        btCancel.setVisibility(visibility);
    }

    private void enableViews(Boolean state){
        btnConfirm.setEnabled(state);
        btCancel.setEnabled(state);
        tbVerification.setEnabled(state);
        tbVerification.setClickable(state);
    }
}
