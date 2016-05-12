package itworks.eddy.soccermemorygame.Models;

import android.widget.ImageView;

import itworks.eddy.soccermemorygame.CardOnclickListener;

/**
 * Created by medve on 12/05/2016.
 */
public class Card {
    private ImageView view;
    private int resource;

    public Card(ImageView view, int resource) {
        this.view = view;
        this.resource = resource;
        view.setOnClickListener(new CardOnclickListener(this));
    }

    public ImageView getView() {
        return view;
    }

    public int getResource() {
        return resource;
    }
}
