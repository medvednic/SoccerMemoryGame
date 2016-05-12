package itworks.eddy.soccermemorygame.Views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itworks.eddy.soccermemorygame.BackgroundMusic;
import itworks.eddy.soccermemorygame.CardOnclickListener;
import itworks.eddy.soccermemorygame.GameLogic;
import itworks.eddy.soccermemorygame.R;

public class LevelOneGame extends AppCompatActivity {

    @Bind(R.id.ivCard0)
    ImageView ivCard0;
    @Bind(R.id.ivCard1)
    ImageView ivCard1;
    @Bind(R.id.ivCard2)
    ImageView ivCard2;
    @Bind(R.id.ivCard3)
    ImageView ivCard3;
    private int boardSize = 4;
    List<ImageView> ivCards = new ArrayList<>();
    private int [] resources = {R.drawable.animal, R.drawable.fish};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one_game);
        ButterKnife.bind(this);
        CardOnclickListener.setContext(getApplicationContext());
        if (BackgroundMusic.isAllowed()) {
            BackgroundMusic.start(); //resume music
        }
        initIvCardList();
        GameLogic.initCards(ivCards, resources);
    }

    private void initIvCardList() {
        ivCards.add(ivCard0);
        ivCards.add(ivCard1);
        ivCards.add(ivCard2);
        ivCards.add(ivCard3);
    }

    @Override
    protected void onPause() {
        if (BackgroundMusic.isAllowed()) {
            BackgroundMusic.pause(); //pause music
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        GameLogic.clear();
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onPostResume() {
        if (BackgroundMusic.isAllowed()) {
            BackgroundMusic.start(); //resume music
        }
        super.onPostResume();
    }

    @OnClick({R.id.ivCard0, R.id.ivCard1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCard0:
                break;
            case R.id.ivCard1:
                break;
        }
    }
}
