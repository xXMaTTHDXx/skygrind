package net.skygrind.skygrindjs.script;

import net.skygrind.skygrindjs.SkyGrindJS;
import net.skygrind.skygrindjs.script.event.SkyGrindEventCallback;
import net.skygrind.skygrindjs.script.event.SkyGrindEventExecutor;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import javax.script.ScriptException;
import java.util.function.Consumer;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getLogger;

/**
 * Created by Matt on 2017-02-26.
 */
public class ScriptBukkit {

    private SkyScript skyScript;

    public ScriptBukkit(SkyScript skyScript) {
        this.skyScript = skyScript;
    }

    public void registerEvent(Class<? extends Event> eventClass, String function) {
        registerEvent(eventClass, EventPriority.NORMAL, function);
    }

    public <T extends Event> void registerEvent(Class<T> eventClass, SkyGrindEventCallback<T> callback) {
        registerEvent(eventClass, EventPriority.NORMAL, callback);
    }

    public <T extends Event> void registerEvent(Class<T> eventClass, EventPriority priority, String function) {
        registerEvent(eventClass, priority, true, function);
    }


    public <T extends Event> void registerEvent(Class<T> eventClass, EventPriority priority, SkyGrindEventCallback<T> callback) {
        registerEvent(eventClass, priority, true, callback);
    }

    public <T extends Event> void registerEvent(Class<T> eventClass, EventPriority priority, boolean ignoreCancelled, final String function) {
        registerEvent(eventClass, priority, ignoreCancelled, new SkyGrindEventCallback<T>() {
            @Override
            public void callback(T t) {
                try {
                    skyScript.invokeLibraryFunction(function, t);
                } catch (ScriptException ex) {
                    getLogger().log(Level.WARNING, ex.getMessage());
                } catch (NoSuchMethodException ex) {
                    getLogger().log(Level.WARNING, ex.getMessage());
                } catch (RuntimeException ex) {
                    getLogger().log(Level.WARNING, ex.getMessage());
                }
            }
        });


    }
    public <T extends Event> void registerEvent(Class<? extends Event> eventClass, EventPriority priority, boolean ignoreCancelled, SkyGrindEventCallback<T> callback) {
        SkyGrindEventExecutor executor = new SkyGrindEventExecutor(eventClass, callback);
        Bukkit.getServer().getPluginManager().registerEvent(eventClass, executor, priority, executor, SkyGrindJS.getPlugin(), ignoreCancelled);
    }
}
