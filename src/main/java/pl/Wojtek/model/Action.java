package pl.Wojtek.model;

/**
 *
 */
public class Action {
    User user;

    public Action() {}

    public Action(User user) {
        this.user=user;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
