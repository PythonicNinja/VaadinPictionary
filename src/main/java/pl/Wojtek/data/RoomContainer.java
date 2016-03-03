package pl.Wojtek.data;

import pl.Wojtek.model.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoomContainer {

    private static List<Room> registeredRooms = Collections.synchronizedList(new ArrayList<Room>());
    private final static Object dataLock = new Object();

    /*
     * Thread safe singleton class
     * http://stackoverflow.com/a/16106598/2170846
     */
    private static class DataContainerHolder{
        static final RoomContainer ROOM_CONTAINER_INSTANCE = new RoomContainer();
    }

    public static RoomContainer getInstance(){
        return DataContainerHolder.ROOM_CONTAINER_INSTANCE;
    }

    private RoomContainer(){
        initial();
    }

    public List<Room> getRooms(){
        synchronized (dataLock) {
            return registeredRooms;
        }
    }

    public void addRoom(Room r){
        synchronized (dataLock) {
            registeredRooms.add(r);
        }
    }

    public void initial(){
        Room r1 = new Room();
        Room r2 = new Room();
        Room r3 = new Room();

        r1.setName("room 1");
        r2.setName("room 2");
        r3.setName("room 3");

        this.addRoom(r1);
        this.addRoom(r2);
        this.addRoom(r3);
    }
}
