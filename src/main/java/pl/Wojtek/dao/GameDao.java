package pl.Wojtek.dao;

import pl.Wojtek.data.GameContainer;
import pl.Wojtek.data.RoomContainer;
import pl.Wojtek.model.Game;
import pl.Wojtek.model.Player;
import pl.Wojtek.model.Room;
import pl.Wojtek.model.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class GameDao {

    private GameContainer dataContainer = GameContainer.getInstance();


    public Game getGame(Player player){
        List<Game> list = dataContainer.getGames();
        for(Game g : list){
            if(g.getPlayers().contains(player)){
                return g;
            }
        }
        return null;
    }

    public Game getGame(Room room){
        List<Game> list = dataContainer.getGames();
        for(Game g : list){
            if(Objects.equals(g.getRoom().getName(), room.getName())){
                return g;
            }
        }
        return null;
    }


    public void addGame(Game g){
        dataContainer.addGame(g);
    }

}
