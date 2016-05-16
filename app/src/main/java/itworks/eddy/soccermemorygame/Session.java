package itworks.eddy.soccermemorygame;

import itworks.eddy.soccermemorygame.Models.User;

/** Session - holds a static user object for easier access to session data from other
 *            activities and classes
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
