package pl.Wojtek.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import pl.Wojtek.model.User;

public class UserContainer {

    private static List<User> registeredUsers = Collections.synchronizedList(new ArrayList<User>());
    private final static Object dataLock = new Object();

    /*
     * Thread safe singleton class
     * http://stackoverflow.com/a/16106598/2170846
     */
    private static class DataContainerHolder{
        static final UserContainer USER_CONTAINER_INSTANCE = new UserContainer();
    }

    public static UserContainer getInstance(){
        return DataContainerHolder.USER_CONTAINER_INSTANCE;
    }

    private UserContainer(){
        initial();
    }

    public List<User> getUsers(){
        synchronized (dataLock) {
            return registeredUsers;
        }
    }

    public void addUser(User u){
        synchronized (dataLock) {
            registeredUsers.add(u);
        }
    }

    public void initial(){
        User u1 = new User();
        User u2 = new User();
        User u3 = new User();


        u1.setUsername("1");
        u1.setPassword("1");

        u2.setUsername("2");
        u2.setPassword("2");

        u3.setUsername("3");
        u3.setPassword("3");

        this.addUser(u1);
        this.addUser(u2);
        this.addUser(u3);
    }
}
