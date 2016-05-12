package itworks.eddy.soccermemorygame;

import android.view.View;

import itworks.eddy.soccermemorygame.Models.Card;

/**
 * Created by medve on 11/05/2016.
 */
public class CardOnclickListener implements View.OnClickListener {

    Card card;

    public CardOnclickListener(Card card) {
        this.card = card;
    }

    @Override
    public void onClick(final View v) {
        GameLogic.selectCard(card);
        /*final ImageView imageView = (ImageView) v;
        if (GameLogic.selectCard(card)){
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
                            if (GameLogic.checkMatch()) {
                                Toast.makeText(context, "Match found!", Toast.LENGTH_SHORT).show();
                                GameLogic.clearSelection();
                                if (GameLogic.checkWin()){
                                    Toast.makeText(context, "You have won!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
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
                                                GameLogic.clearSelection();
                                                imageView.setEnabled(true);
                                            }
                                        });
                                        a.start();
                                    }
                                });
                                a.setTarget(imageView);
                                a.start();
                            }

                        }
                    });
                    a.setTarget(imageView);
                    a.start();
                }
            });
            a.setTarget(imageView);
            a.start();
        }*/
    }
}
//old animate base animations
        /*imageView.animate().rotationY(90).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Picasso.with(context)
                        .load(R.drawable.animal)
                        .into(imageView);
                imageView.animate().rotationY(180).setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        imageView.animate().setStartDelay(2000).rotationY(90).setDuration(500).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                Picasso.with(context)
                                        .load(R.drawable.gear_head)
                                        .into(imageView);
                                imageView.animate().rotationY(0).setDuration(500).start();
                                imageView.setEnabled(true);

                            }
                        }).start();
                    }
                }).start();
            }
        }).start();*/