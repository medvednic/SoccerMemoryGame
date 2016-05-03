package itworks.eddy.soccermemorygame.Views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;

import butterknife.Bind;
import butterknife.ButterKnife;
import itworks.eddy.soccermemorygame.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    @Bind(R.id.switchEffects)
    Switch switch2;
    @Bind(R.id.switchMusic)
    Switch switch1;
    @Bind(R.id.seekBarVolume)
    SeekBar seekBarVolume;
    @Bind(R.id.btRestartScore)
    Button btRestartScore;
    @Bind(R.id.btDeleteUser)
    Button btDeleteUser;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
