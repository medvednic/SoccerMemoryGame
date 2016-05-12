package itworks.eddy.soccermemorygame.Models;

import android.widget.ImageView;

import itworks.eddy.soccermemorygame.CardOnclickListener;

/**
 * Created by medve on 12/05/2016.
 */
public class Card {
    private ImageView view;
    private int resource;
    private int penalty;
    private int bonus;

    public Card(ImageView view, int resource, int penalty) {
        this.view = view;
        this.resource = resource;
        this.penalty = penalty;
        bonus = 100;
        view.setOnClickListener(new CardOnclickListener(this));
    }

    public ImageView getView() {
        return view;
    }

    public int getResource() {
        return resource;
    }

    public int getBonus() {
        return bonus;
    }

    public void lowerBonus() {
        if (bonus > 0) {
            bonus -= penalty;
        }
    }
}
