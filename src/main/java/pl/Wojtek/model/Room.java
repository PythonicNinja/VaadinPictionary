package pl.Wojtek.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 */
public class Room {

    @NotNull
    @Size(min=1, max=20, message="Room name must be 1-20 characters")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Room(" + this.name + ")";
    }
}
