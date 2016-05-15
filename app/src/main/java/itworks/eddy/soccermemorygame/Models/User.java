package itworks.eddy.soccermemorygame.Models;

/**
 * Created by medve on 27/04/2016.
 */
public class User {
    private String username;
    private String lvl1;
    private String lvl3;
    private String lvl2;

    public User(String username, String lvl1, String lvl3, String lvl2) {
        this.username = username;
        this.lvl1 = lvl1;
        this.lvl3 = lvl3;
        this.lvl2 = lvl2;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLvl1(String lvl1) {
        this.lvl1 = lvl1;
    }

    public void setLvl3(String lvl3) {
        this.lvl3 = lvl3;
    }

    public void setLvl2(String lvl2) {
        this.lvl2 = lvl2;
    }

    public String getUsername() {
        return username;
    }

    public String getLvl1() {
        return lvl1;
    }

    public String getLvl3() {
        return lvl3;
    }

    public String getLvl2() {
        return lvl2;
    }
}
