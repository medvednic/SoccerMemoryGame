package itworks.eddy.soccermemorygame.Models;

/** User - represents a single player: has username and records for each level
 *
 */
public class User {
    private String username;
    private Integer lvl1;
    private Integer lvl3;
    private Integer lvl2;

    public User(String username, Integer lvl1, Integer lvl3, Integer lvl2) {
        this.username = username;
        this.lvl1 = lvl1;
        this.lvl3 = lvl3;
        this.lvl2 = lvl2;
    }

    public User(String username) {
        this.username = username;
        this.lvl1 = 0;
        this.lvl3 = 0;
        this.lvl2 = 0;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLvl1(Integer lvl1) {
        this.lvl1 = lvl1;
    }

    public void setLvl3(Integer lvl3) {
        this.lvl3 = lvl3;
    }

    public void setLvl2(Integer lvl2) {
        this.lvl2 = lvl2;
    }

    public String getUsername() {
        return username;
    }

    public Integer getLvl1() {
        return lvl1;
    }

    public Integer getLvl3() {
        return lvl3;
    }

    public Integer getLvl2() {
        return lvl2;
    }

    public  int getLevelScore(int level){
        if (level == 1){
            return lvl1;
        }
        else if (level == 2){
            return lvl2;
        }
        else {
            return lvl3;
        }
    }
}
