package itworks.eddy.soccermemorygame.Views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itworks.eddy.soccermemorygame.BackgroundMusic;
import itworks.eddy.soccermemorygame.CardOnclickListener;
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
    public static int [] selected;
    public static int [] selected_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one_game);
        ButterKnife.bind(this);
        if (BackgroundMusic.isAllowed()) {
            BackgroundMusic.start(); //resume music
        }
        selected = new int[2];
        selected[0] = 0;
        selected[1] = 0;
        selected_ids = new int[2];
        selected_ids[0] = 0;
        selected_ids[1] = 0;
        ivCard0.setOnClickListener(new CardOnclickListener(ivCard0, getApplicationContext(), R.drawable.fish));
        ivCard1.setOnClickListener(new CardOnclickListener(ivCard1, getApplicationContext(), R.drawable.animal));
        ivCard2.setOnClickListener(new CardOnclickListener(ivCard2, getApplicationContext(), R.drawable.fish));
        ivCard3.setOnClickListener(new CardOnclickListener(ivCard3, getApplicationContext(), R.drawable.animal));
    }

    @Override
    protected void onPause() {
        if (BackgroundMusic.isAllowed()) {
            BackgroundMusic.pause(); //pause music
        }
        super.onPause();
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
