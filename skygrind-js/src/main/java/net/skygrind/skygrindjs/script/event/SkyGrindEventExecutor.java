package net.skygrind.skygrindjs.script.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import java.util.List;

/**
 * Created by Matt on 2017-02-26.
 */
public class SkyGrindEventExecutor<T> implements EventExecutor, Listener {

    private SkyGrindEventCallback callback;
    private Class<T> eventType;

    public SkyGrindEventExecutor(Class<T> event, SkyGrindEventCallback callback) {
        this.eventType = event;
        this.callback = callback;
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        if (eventType.isInstance(event)) {
            T t = eventType.cast(event);

            try {
                callback.callback(t);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }
}
