package itworks.eddy.soccermemorygame.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by medve on 15/05/2016.
 */
public class UsersList {

    private List<User> user = new ArrayList<User>();

    public UsersList(List<User> user) {
        this.user = user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public List<User> getUser() {
        return user;
    }
}
