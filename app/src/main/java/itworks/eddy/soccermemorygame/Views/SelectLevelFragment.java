package itworks.eddy.soccermemorygame.Views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import itworks.eddy.soccermemorygame.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectLevelFragment extends Fragment {


    public SelectLevelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_level, container, false);
    }

}
