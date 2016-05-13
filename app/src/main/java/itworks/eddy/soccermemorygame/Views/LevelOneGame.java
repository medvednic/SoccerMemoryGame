package itworks.eddy.soccermemorygame.Views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import itworks.eddy.soccermemorygame.BackgroundMusic;
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
    @Bind(R.id.ivCard4)
    ImageView ivCard4;
    @Bind(R.id.ivCard5)
    ImageView ivCard5;
    @Bind(R.id.tvScore)
    TextView tvScore;
    @Bind(R.id.tvSteps)
    TextView tvSteps;
    List<ImageView> ivCards = new ArrayList<>();
    private int [] resources = {R.drawable.animal_duck, R.drawable.animal_owl, R.drawable.animal_wolf};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one_game);
        this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        ButterKnife.bind(this);
        if (BackgroundMusic.isAllowed()) {
            BackgroundMusic.start(); //resume music
        }
        //if record != 0 show it...
        initIvCardList();
        GameLogic.initCards(getApplicationContext(), 1, tvScore, ivCards, resources);
    }

    private void initIvCardList() {
        ivCards.add(ivCard0);
        ivCards.add(ivCard1);
        ivCards.add(ivCard2);
        ivCards.add(ivCard3);
        ivCards.add(ivCard4);
        ivCards.add(ivCard5);
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
        if (!GameLogic.isSelectedFull()){
            this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            displayBackDialog();
        }
    }

    @Override
    protected void onPostResume() {
        if (BackgroundMusic.isAllowed()) {
            BackgroundMusic.start(); //resume music
        }
        super.onPostResume();
    }

    private void displayBackDialog() {
        //display back to main menu dialog
        new AlertDialog.Builder(this)
                .setTitle("Back to main menu")
                .setMessage("Are you sure?")
                .setNeutralButton("Stay in game", null)
                .setNegativeButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameLogic.clear();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent); //restart current activity
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameLogic.clear();
                        //finish(); //is necessary?
                        LevelOneGame.super.onBackPressed();
                    }
                }).create().show();
    }
}
