package pl.Wojtek.model;

import com.vaadin.server.VaadinSession;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

public class User {

    private UUID id;

    @NotNull
    @Size(min=1, max=20, message="Login length must be 1-15 characters")
    private String username;

    @NotNull
    @Size(min=1, max=20, message="Password length must be 1-20 characters")
    private String password;

    private Room room;

    public User(){}

    public User(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "User(" + username + "/" + password + '@' + room.toString() + ")";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        Boolean insert = (this.room == null);
        Boolean update = (this.room != null);
        if(insert) {
            room.addUser(this);
        }else{
            this.room.removeUser(this); // delete
            if(room != null){ // update
                room.addUser(this);
            }
        }
        this.room = room;
    }

    public Boolean hasRoom(){
        return this.room != null;
    }

    public void logout(){
        VaadinSession.getCurrent().getSession().setAttribute("user", null);
    }
}
