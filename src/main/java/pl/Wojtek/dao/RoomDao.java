package pl.Wojtek.dao;

import pl.Wojtek.data.RoomContainer;
import pl.Wojtek.model.Room;

import java.util.List;

public class RoomDao {

    private RoomContainer dataContainer = RoomContainer.getInstance();


    public Room getRoom(String name){
        List<Room> list = dataContainer.getRooms();
        for(Room r : list){
            if(r.getName().equals(name)){
                return r;
            }
        }
        return null;
    }

    public void addRoom(Room r){
        dataContainer.addRoom(r);
    }

}
