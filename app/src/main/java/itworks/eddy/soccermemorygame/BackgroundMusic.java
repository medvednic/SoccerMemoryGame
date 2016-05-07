package itworks.eddy.soccermemorygame;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/** BackgroundMusic - handles MediaPlayer for background music
 *  static MediaPlayer variable is used so that it can be accessed from different activities/fragments,
 *  methods to safely start, pause, release the MediaPlayer were implemented,
 *  methods to handle settings change were implemented
 */
public class BackgroundMusic {

    private static Boolean allowMusic = false;
    private static float currentVolume = 0.75f;
    private static MediaPlayer backgroundPlayer;

    public static void initPlayer(Context context, Boolean musicOpt, float initialVolume){
        allowMusic = musicOpt;
        backgroundPlayer = MediaPlayer.create(context, R.raw.wayne_kinos_01_progressions_intro);
        backgroundPlayer.setLooping(true);
        setVolume(initialVolume);
        Log.d("Background Player-->", "instantiated");
    }

    public static Boolean isAllowed(){
        return allowMusic;
    }

    public static void setVolume(float volume){
        Log.d("volume set to:", String.valueOf(volume));
        currentVolume = volume;
        backgroundPlayer.setVolume(volume, volume);
    }

    public static float getVolume() {
        return currentVolume;
    }

    public static int getVolumeInt() {
        int volumeInt = (int)(currentVolume*100);
        return volumeInt;
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
