package pl.Wojtek.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class User {

    @NotNull
    @Size(min=1, max=20, message="Login length must be 1-15 characters")
    private String username;

    @NotNull
    @Size(min=5, max=20, message="Password length must be 5-20 characters")
    private String password;

    public User(){}

    public User(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User(" + username + "/" + password + ")";
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

}
