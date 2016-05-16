package itworks.eddy.soccermemorygame;

import android.view.View;

import itworks.eddy.soccermemorygame.Models.Card;

/** CardOnclickListener - custom onclick listener, set for views that represent a card,
 *                        when clicked - the card object will be selected.
 *
 */
public class CardOnclickListener implements View.OnClickListener {

    Card card;

    public CardOnclickListener(Card card) {
        this.card = card;
    }

    @Override
    public void onClick(final View v) {
        GameLogic.selectCard(card);

    }
}
