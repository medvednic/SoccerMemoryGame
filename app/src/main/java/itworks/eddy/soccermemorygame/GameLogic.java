package itworks.eddy.soccermemorygame;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import itworks.eddy.soccermemorygame.Models.Card;

/**
 * Created by medve on 12/05/2016.
 */
public class GameLogic {

    private static int cardsNum;
    private static List<Card> cards;
    private static List<Integer> randomized;

    public static void initCards(List<ImageView> imageViews, int [] resources){
        cards = new ArrayList<>();
        randomized = new ArrayList<>();
        cardsNum = imageViews.size();
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

    public static void clear(){
        cards = new ArrayList<>();
        randomized = new ArrayList<>();
        cardsNum = 0;
    }
}
