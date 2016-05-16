package itworks.eddy.soccermemorygame.Models;

import android.widget.ImageView;

import itworks.eddy.soccermemorygame.CardOnclickListener;

/** Card - represents a card from the game, each card is assigned a view, resource and bonus points.
 *  the view has a custom onclick listener that "binds" the card with the view, and calls the checkmatch
 *  method upon selection.
 *
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
