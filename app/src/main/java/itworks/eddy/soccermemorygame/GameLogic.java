package itworks.eddy.soccermemorygame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import itworks.eddy.soccermemorygame.Models.Card;
import itworks.eddy.soccermemorygame.Models.ServerResponse;
import itworks.eddy.soccermemorygame.RESTaccess.apiServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by medve on 12/05/2016.
 */
public class GameLogic {

    private static Context context;
    private static int level;
    private static List<Card> cards;
    private static List<Card> selectedCards;
    private static List<Integer> randomized;
    private static int cardsNum;
    private static int unmatchedCards;
    private static TextView scoreView;
    private static int steps;
    private static int score;
    private static int winPoints = 1000;
    private static int penalty = 10;

    public static void initCards(Context gameContext, int gameLevel ,TextView scoreTv, List<ImageView> imageViews, int [] resources){
        context = gameContext;
        level = gameLevel;
        cards = new ArrayList<>();
        selectedCards = new ArrayList<>();
        randomized = new ArrayList<>();
        cardsNum = imageViews.size();
        unmatchedCards = cardsNum;
        scoreView = scoreTv;
        steps = 0;
        score = 0;
        winPoints = 1000;
        penalty = 10*level;
        int [] randIndex = new int[2];
        Random randomGen = new Random();
        for (int resourceId: resources) {
            do{
                randIndex[0] = randomGen.nextInt(cardsNum);
                randIndex[1] = randomGen.nextInt(cardsNum);
            }while (randIndex[0] == randIndex[1] || randomized.contains(randIndex[0]) || randomized.contains(randIndex[1]));
            randomized.add(randIndex[0]);
            randomized.add(randIndex[1]);
            cards.add(new Card(imageViews.get(randIndex[0]), resourceId, penalty));
            cards.add(new Card(imageViews.get(randIndex[1]), resourceId, penalty));
        }
    }

    public static void selectCard(Card selectedCard){
        if (selectedCards.size() < 2 && !selectedCards.contains(selectedCard)){
            selectedCards.add(selectedCard);
            revealCard(selectedCard);
        }
    }

    private static void revealCard(final Card card) {
        final ImageView imageView = card.getView();
        final int resource = card.getResource();
        imageView.setEnabled(false);
        Animator a = AnimatorInflater.loadAnimator(context, R.animator.to_mid);
        a.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Picasso.with(context).load(resource).into(imageView);
                Animator a = AnimatorInflater.loadAnimator(context, R.animator.from_mid);
                a.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (selectedCards.size() == 2 && selectedCards.get(1) == card){ //run check only after second flip
                            if (checkMatch()) {
                                Toast.makeText(context, "Match found!", Toast.LENGTH_SHORT).show();
                                //clearSelection();
                                checkWin();
                            }
                        }
                        /*else {
                            hideCard(selectedCards.get(0).getView()); //how to make it work? si it possible?!
                        }*/
                    }
                });
                a.setTarget(imageView);
                a.start();
            }
        });
        a.setTarget(imageView);
        a.start();
    }

    private static void hideCard(final ImageView imageView) {
        Animator a = AnimatorInflater.loadAnimator(context, R.animator.back_to_mid);
        a.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Picasso.with(context).load(R.drawable.gear_head).into(imageView);
                Animator a = AnimatorInflater.loadAnimator(context, R.animator.back_from_mid);
                a.setTarget(imageView);
                a.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (selectedCards.size() == 2 && selectedCards.get(1).getView() == imageView){
                            clearSelection();
                        }
                        imageView.setEnabled(true);
                    }
                });
                a.start();
            }
        });
        a.setTarget(imageView);
        a.start();
    }

    public static boolean checkMatch() {
        Log.d("Check for size: ", String.valueOf(selectedCards.size()));
        //if (selectedCards.size() == 2){
        steps++;
        if (selectedCards.get(0).getResource() == selectedCards.get(1).getResource()){
            score += selectedCards.get(0).getBonus() + selectedCards.get(1).getBonus();
            scoreView.setText(String.valueOf(score));
            clearSelection();
            return true;
        }
        else{
            selectedCards.get(0).lowerBonus();
            selectedCards.get(1).lowerBonus();
            hideCard(selectedCards.get(0).getView());
            hideCard(selectedCards.get(1).getView());
        }
        //}
        return false;
    }

    public static void clearSelection() {
        Log.d("Size before clear",String.valueOf(selectedCards.size()));
        if (selectedCards.size() == 2){
            selectedCards.remove(1);
            selectedCards.remove(0);
        }
        else if (selectedCards.size() == 1){
            selectedCards.remove(0);
        }
        Log.d("Size after clear",String.valueOf(selectedCards.size()));
    }

    public static void checkWin(){
        unmatchedCards -=2;
        if (unmatchedCards == 0){
            score += winPoints;
            scoreView.setText(String.valueOf(score));
            Toast.makeText(context, "You have won!", Toast.LENGTH_SHORT).show();
            if (score > Session.getCurrentLevelScore(level)){
                scoreView.setTextColor(Color.YELLOW);
                updateRecord();
                postScore();
            }
        }
    }

     private static void updateRecord() {
        SharedPreferences appPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = appPreferences.edit();
        if (level == 1){
            Session.currentUser.setLvl1(score);
            editor.putInt("lvl1", Session.currentUser.getLvl1());
        } else if (level == 2) {
            Session.currentUser.setLvl2(score);
            editor.putInt("lvl2", Session.currentUser.getLvl2());
        } else {
            Session.currentUser.setLvl3(score);
            editor.putInt("lvl3", Session.currentUser.getLvl3());
        }
        editor.apply();
    }

    private static void postScore() {
        final String BASE_URL = context.getString(R.string.api_server_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServices api = retrofit.create(apiServices.class);
        Call<ServerResponse> updateScore = api.updateScore(Session.currentUser.getUsername(), score, level);
        updateScore.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) { //if score was updated
                    Toast.makeText(context, "High score was registered!", Toast.LENGTH_SHORT);
                } else {
                    if (response.code() == 400) { //failed to update score
                        Log.d("failed to update score", " 400");
                    } else { //server returned error
                        String errMsg = response.raw().message();
                        Log.d("Error", errMsg);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }

    public static boolean isSelectedFull(){
        return selectedCards.size() == 2;
    }

    public static void clear(){
        cards = new ArrayList<>();
        selectedCards = new ArrayList<>();
        randomized = new ArrayList<>();
        context = null;
    }
}