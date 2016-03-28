package pl.Wojtek.data;

import pl.Wojtek.model.Game;
import pl.Wojtek.model.Word;

import java.util.*;

public class GameContainer {

    private static List<Game> games = Collections.synchronizedList(new ArrayList<Game>());
    private final static Object dataLock = new Object();

    /*
     * Thread safe singleton class
     * http://stackoverflow.com/a/16106598/2170846
     */
    private static class DataContainerHolder{
        static final GameContainer GAME_CONTAINER_INSTANCE = new GameContainer();
    }

    public static GameContainer getInstance(){
        return DataContainerHolder.GAME_CONTAINER_INSTANCE;
    }

    private GameContainer(){
        initial();
    }

    public List<Game> getGames(){
        synchronized (dataLock) {
            return games;
        }
    }

    public void addGame(Game g){
        synchronized (dataLock) {
            games.add(g);
        }
    }

    public void initial(){
    }
}
