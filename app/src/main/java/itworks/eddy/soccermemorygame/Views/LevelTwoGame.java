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
import itworks.eddy.soccermemorygame.Session;

public class LevelTwoGame extends AppCompatActivity {

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
    @Bind(R.id.ivCard6)
    ImageView ivCard6;
    @Bind(R.id.ivCard7)
    ImageView ivCard7;
    @Bind(R.id.ivCard8)
    ImageView ivCard8;
    @Bind(R.id.ivCard9)
    ImageView ivCard9;
    @Bind(R.id.ivCard10)
    ImageView ivCard10;
    @Bind(R.id.ivCard11)
    ImageView ivCard11;
    @Bind(R.id.tvScore)
    TextView tvScore;
    @Bind(R.id.tvRecord)
    TextView tvRecord;
    List<ImageView> ivCards = new ArrayList<>();
    private int[] resources = {R.drawable.sports_baseball, R.drawable.sports_basketball,
            R.drawable.sports_bowling, R.drawable.sports_football, R.drawable.sports_soccer, R.drawable.sports_tennis};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_two_game);
        this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        ButterKnife.bind(this);
        if (BackgroundMusic.isAllowed()) {
            BackgroundMusic.start(); //resume music
        }
        //if record != 0 show it...
        tvRecord.setText(Session.currentUser.getLvl2().toString());
        initIvCardList();
        GameLogic.initCards(getApplicationContext(), 2, tvScore, ivCards, resources);
    }

    private void initIvCardList() {
        ivCards.add(ivCard0);
        ivCards.add(ivCard1);
        ivCards.add(ivCard2);
        ivCards.add(ivCard3);
        ivCards.add(ivCard4);
        ivCards.add(ivCard5);
        ivCards.add(ivCard6);
        ivCards.add(ivCard7);
        ivCards.add(ivCard8);
        ivCards.add(ivCard9);
        ivCards.add(ivCard10);
        ivCards.add(ivCard11);
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
                        LevelTwoGame.super.onBackPressed();
                    }
                }).create().show();
    }
}
