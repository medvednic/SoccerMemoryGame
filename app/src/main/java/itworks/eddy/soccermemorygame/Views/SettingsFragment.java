package itworks.eddy.soccermemorygame.Views;


import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    //BackgroundMusic backgroundMusic;
    SharedPreferences appPreferences;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = this.getArguments();
        if (bundle != null){
            switchMusic.setChecked(bundle.getBoolean("music", true));
        }
        final AudioManager audioManager = (AudioManager) getContext().getSystemService(getContext().AUDIO_SERVICE);
        seekBarVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        seekBarVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                Log.d(Integer.toString(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC))+" ", Integer.toString(progress));
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
                appPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = appPreferences.edit();
                if (isChecked) {
                    BackgroundMusic.initPlayer(getContext(), isChecked);
                    BackgroundMusic.start();

                }else {
                    BackgroundMusic.releasePlayer();
                }
                Log.d("allowed->", String.valueOf(isChecked));
                editor.putBoolean("music", isChecked);
                editor.apply();

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
