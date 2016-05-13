package itworks.eddy.soccermemorygame.Views;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itworks.eddy.soccermemorygame.LevelTwoGAme;
import itworks.eddy.soccermemorygame.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectLevelFragment extends Fragment {


    @Bind(R.id.btnLvl1)
    Button btnLvl1;
    @Bind(R.id.btnLvl2)
    Button btnLvl2;
    Animator anim;

    public SelectLevelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_level, container, false);
        ButterKnife.bind(this, view);
        anim = AnimatorInflater.loadAnimator(getContext(), R.animator.fade_out);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Intent intent = new Intent(getActivity(), LevelOneGame.class);
                startActivity(intent);
            }
        });
        anim.setTarget(btnLvl1);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btnLvl1, R.id.btnLvl2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLvl1:
                anim.start();
                break;
            case R.id.btnLvl2:
                Intent intent = new Intent(getActivity(), LevelTwoGAme.class);
                startActivity(intent);
                break;
        }
    }
}
