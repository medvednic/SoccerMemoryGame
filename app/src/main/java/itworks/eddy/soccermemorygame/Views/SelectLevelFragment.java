package itworks.eddy.soccermemorygame.Views;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itworks.eddy.soccermemorygame.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectLevelFragment extends Fragment {

    AnimatorSet clickAnimation;
    Intent intent;
    @Bind(R.id.IvLevel1)
    ImageView IvLevel1;
    @Bind(R.id.IvLevel2)
    ImageView IvLevel2;
    @Bind(R.id.IvLevel3)
    ImageView IvLevel3;

    public SelectLevelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_level, container, false);
        ButterKnife.bind(this, view);
        clickAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.click);
        clickAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.IvLevel1, R.id.IvLevel2, R.id.IvLevel3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.IvLevel1:
                clickAnimation.setTarget(IvLevel1);
                intent = new Intent(getActivity(), LevelOneGame.class);
                break;
            case R.id.IvLevel2:
                clickAnimation.setTarget(IvLevel2);
                intent = new Intent(getActivity(), LevelTwoGame.class);
                break;
            case R.id.IvLevel3:
                clickAnimation.setTarget(IvLevel3);
                intent = new Intent(getActivity(), LevelThreeGame.class);
                break;
        }
        clickAnimation.start();
    }
}
