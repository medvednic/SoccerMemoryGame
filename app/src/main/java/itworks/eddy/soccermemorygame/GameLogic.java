package itworks.eddy.soccermemorygame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import itworks.eddy.soccermemorygame.Models.Card;

/**
 * Created by medve on 12/05/2016.
 */
public class GameLogic {

    private static Context context;
    private static int cardsNum;
    private static int unmatchedCards;
    private static List<Card> cards;
    private static List<Card> selectedCards;
    private static List<Integer> randomized;

    public static void initCards(List<ImageView> imageViews, int [] resources, Context gameContext){
        context = gameContext;
        cards = new ArrayList<>();
        selectedCards = new ArrayList<>();
        randomized = new ArrayList<>();
        cardsNum = imageViews.size();
        unmatchedCards = cardsNum;
        int [] randIndex = new int[2];
        Random randomGen = new Random();
        for (int resourceId: resources) {
            do{
                randIndex[0] = randomGen.nextInt(cardsNum);
                randIndex[1] = randomGen.nextInt(cardsNum);
            }while (randIndex[0] == randIndex[1] || randomized.contains(randIndex[0]) || randomized.contains(randIndex[1]));
            randomized.add(randIndex[0]);
            randomized.add(randIndex[1]);
            cards.add(new Card(imageViews.get(randIndex[0]), resourceId));
            cards.add(new Card(imageViews.get(randIndex[1]), resourceId));
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
        if (selectedCards.get(0).getResource() == selectedCards.get(1).getResource()){
            clearSelection();
            return true;
        }
        else{
            hideCard(selectedCards.get(0).getView());
            hideCard(selectedCards.get(1).getView());
        }
        //}
        return false;
    }

    public static void checkWin(){
        unmatchedCards -=2;
        if (unmatchedCards == 0){
            Toast.makeText(context, "You have won!", Toast.LENGTH_SHORT).show();
        }
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

    public static void clear(){
        cards = new ArrayList<>();
        selectedCards = new ArrayList<>();
        randomized = new ArrayList<>();
        context = null;
    }
}