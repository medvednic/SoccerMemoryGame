package itworks.eddy.soccermemorygame.Views;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import butterknife.Bind;
import butterknife.ButterKnife;
import itworks.eddy.soccermemorygame.BackgroundMusic;
import itworks.eddy.soccermemorygame.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    @Bind(R.id.switchEffects)
    Switch switchEffects;
    @Bind(R.id.switchMusic)
    Switch switchMusic;
    @Bind(R.id.seekBarVolume)
    SeekBar seekBarVolume;
    @Bind(R.id.btRestartScore)
    Button btRestartScore;
    @Bind(R.id.btDeleteUser)
    Button btDeleteUser;

    /** SettingsFragment - this is the settings fragment
     *  the user can control music/sound settings from here
     *  the user can reset his/her scores
     *  the user can deactivate the account
     */

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
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BackgroundMusic.setVolume(progress/100f); //adjust background music volume according to slider
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

                }else {
                    BackgroundMusic.releasePlayer();
                }
                seekBarVolume.setEnabled(isChecked);
                saveSettings("music");
            }
        });

        return view;
    }

    private void initViews() { //init seekbar and switches according to users settings
        seekBarVolume.setProgress(BackgroundMusic.getVolumeInt());
        seekBarVolume.setMax(100);
        if (!BackgroundMusic.isAllowed()){
            switchMusic.setChecked(false);
            seekBarVolume.setEnabled(false);
        }
    }

    private void saveSettings(String option) { //save user settings to shared preferences
        SharedPreferences appPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = appPreferences.edit();
        switch (option){
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
}
