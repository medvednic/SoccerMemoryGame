package itworks.eddy.soccermemorygame;

import itworks.eddy.soccermemorygame.Models.User;

/**
 * Created by medve on 16/05/2016.
 */
public class Session {
    public static User currentUser;

    public static int getCurrentLevelScore(int level){
        if (level == 1){
            return currentUser.getLvl1();
        }
        else if (level == 2){
            return currentUser.getLvl2();
        }
        else {
            return currentUser.getLvl3();
        }
    }
}
