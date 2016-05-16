package itworks.eddy.soccermemorygame.Models;

import java.util.ArrayList;
import java.util.List;

/** UsersList - holds list of users, used in JSON response deserialization
 *
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
