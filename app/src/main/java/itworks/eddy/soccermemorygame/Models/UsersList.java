package itworks.eddy.soccermemorygame.Models;

import java.util.ArrayList;
import java.util.List;

/** UsersList - holds list of users, used in JSON response deserialization
 *
 */
public class UsersList {

    private List<User> users = new ArrayList<User>();

    public UsersList(List<User> users) {
        this.users = users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
