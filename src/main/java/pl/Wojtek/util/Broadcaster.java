package pl.Wojtek.util;


import pl.Wojtek.model.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

/**
 * implementation of https://vaadin.com/book/-/page/advanced.push.html
 */
public class Broadcaster implements Serializable {

    static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public interface BroadcastListener {
        void receiveBroadcast(Message message);
        void receiveBroadcast(Draw draw);
        void receiveBroadcast(Clear clear);
        void receiveBroadcast(Game game);
    }

    private static LinkedList<BroadcastListener> listeners = new LinkedList<BroadcastListener>();
    private static Map<String, LinkedList<BroadcastListener>> rooms = new HashMap<String, LinkedList<BroadcastListener>>();

    public static synchronized void register(Room room, BroadcastListener listener) {
        listeners.add(listener);
        LinkedList<BroadcastListener> listeners_per_room = new LinkedList<BroadcastListener>();

        String room_name = room.getName();

        if(rooms.get(room_name) != null) {
            listeners_per_room = rooms.get(room_name);
            listeners_per_room.add(listener);
            rooms.put(room_name, listeners_per_room);
        }else{
            listeners_per_room.add(listener);
            rooms.put(room_name, listeners_per_room);
        }
    }

    public static synchronized void unregister(Room room, BroadcastListener listener) {
        listeners.remove(listener);
        LinkedList<BroadcastListener> listeners_per_room = new LinkedList<BroadcastListener>();

        String room_name = room.getName();
        if(rooms.get(room_name) != null) {
            listeners_per_room = rooms.get(room_name);
            listeners_per_room.remove(listener);
            rooms.put(room_name, listeners_per_room);
        }
    }

    public static synchronized void broadcastMessage(final Message message) {
        User user = message.getUser();
        Room room = user.getRoom();
        System.out.println(
            room + " - " + rooms.get(room.getName())
        );
        for (final BroadcastListener listener : rooms.get(room.getName()))
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    listener.receiveBroadcast(message);
                }
        });
    }

    public static synchronized void broadcastMessage(final Draw draw) {
        User user = draw.getUser();
        Room room = user.getRoom();
        System.out.println(
            room + " - " + rooms.get(room.getName())
        );
        for (final BroadcastListener listener : rooms.get(room.getName()))
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    listener.receiveBroadcast(draw);
                }
            });
    }

    public static synchronized void broadcastMessage(final Clear clear) {
        User user = clear.getUser();
        Room room = user.getRoom();
        System.out.println(
                room + " - " + rooms.get(room.getName())
        );
        for (final BroadcastListener listener : rooms.get(room.getName()))
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    listener.receiveBroadcast(clear);
                }
            });
    }

    public static synchronized void broadcastMessage(final Game game) {

        Room room = game.getRoom();
        System.out.println(
                room + " - " + rooms.get(room.getName())
        );
        for (final BroadcastListener listener : rooms.get(room.getName()))
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    listener.receiveBroadcast(game);
                }
            });
    }

}
