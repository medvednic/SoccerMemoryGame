package itworks.eddy.soccermemorygame;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by medve on 06/05/2016.
 */
public class BackgroundMusic {

    private static Boolean allowMusic = false;
    private static MediaPlayer backgroundPlayer;

    public static void initPlayer(Context context, Boolean musicOpt){
        allowMusic = musicOpt;
        backgroundPlayer = MediaPlayer.create(context, R.raw.wayne_kinos_01_progressions_intro);
        backgroundPlayer.setLooping(true);
        Log.d("Background Player-->", "instantiated");
    }

    public static Boolean isAllowed(){
        return allowMusic;
    }

    public static void changeState (Boolean musicOpt){
        allowMusic = musicOpt;
        Log.d("allow music-->", String.valueOf(musicOpt));
    }

    public static void pause(){
        Log.d("player is playing", String.valueOf(backgroundPlayer.isPlaying()));
        if (backgroundPlayer.isPlaying()){
            backgroundPlayer.pause();
        }
    }

    public static void start(){
        Log.d("player is playing", String.valueOf(backgroundPlayer.isPlaying()));
        if (!backgroundPlayer.isPlaying()){
            backgroundPlayer.start();
        }
    }

    public static void releasePlayer(){
        Log.d("player is playing", String.valueOf(backgroundPlayer.isPlaying()));
        if (backgroundPlayer.isPlaying()){
            Log.d("Releasing", "!");
            allowMusic = false;
            backgroundPlayer.stop();
            backgroundPlayer.reset();
            backgroundPlayer.release();
            backgroundPlayer = null;
            Log.d("Released", "!");
        }
    }
}
