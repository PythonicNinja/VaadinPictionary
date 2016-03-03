package pl.Wojtek.dao;

import java.util.List;

import pl.Wojtek.data.UserContainer;
import pl.Wojtek.model.User;

public class UserDao {

    private UserContainer userContainer = UserContainer.getInstance();


    public User getUser(String name){
        List<User> list = userContainer.getUsers();
        for(User u : list){
            if(u.getUsername().equals(name)){
                return u;
            }
        }
        return null;
    }

    public void addUser(User u){
        userContainer.addUser(u);
    }

}
