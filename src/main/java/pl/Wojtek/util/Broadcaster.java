package pl.Wojtek.util;


import pl.Wojtek.model.Message;
import pl.Wojtek.model.Room;
import pl.Wojtek.model.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * implementation of https://vaadin.com/book/-/page/advanced.push.html
 */
public class Broadcaster implements Serializable {

    static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public interface BroadcastListener {
        void receiveBroadcast(Message message);
    }

    private static LinkedList<BroadcastListener> listeners = new LinkedList<BroadcastListener>();
    private static Map<Room, LinkedList<BroadcastListener>> rooms = new HashMap<Room, LinkedList<BroadcastListener>>();

    public static synchronized void broadcastMessage(final Message message) {
        User user = message.getUser();
        if(user!=null && user.hasRoom()) {
            Room room = user.getRoom();
            LinkedList<BroadcastListener> listeners = rooms.get(room);
            for (final BroadcastListener listener : listeners)
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        listener.receiveBroadcast(message);
                    }
                });

        }
        else{
            for (final BroadcastListener listener : listeners)
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        listener.receiveBroadcast(message);
                    }
                });
        }
    }

}
