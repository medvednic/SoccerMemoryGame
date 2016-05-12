package itworks.eddy.soccermemorygame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by medve on 11/05/2016.
 */
public class CardOnclickListener implements View.OnClickListener {

    //ImageView imageView;
    int resource;
    static Context context;

    public CardOnclickListener(int resource) {
        //this.imageView = view;
        this.resource = resource;
    }

    public static void setContext(Context context) {
        CardOnclickListener.context = context;
    }

    @Override
    public void onClick(final View v) {
        final ImageView imageView = (ImageView) v;
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
                        // TODO: 11/05/2016 happens only if no match was made
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
                                            imageView.setEnabled(true);
                                        }
                                    });
                                    a.start();
                                }
                            });
                            a.setTarget(imageView);
                            a.start();
                    }
                });
                a.setTarget(imageView);
                a.start();
            }
        });
        a.setTarget(imageView);
        a.start();

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