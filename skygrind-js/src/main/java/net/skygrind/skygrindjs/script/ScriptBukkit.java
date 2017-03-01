package net.skygrind.skygrindjs.script;

import net.skygrind.skygrindjs.SkyGrindJS;
import net.skygrind.skygrindjs.script.command.CommandRegistry;
import net.skygrind.skygrindjs.script.command.SkyGrindCommandCallback;
import net.skygrind.skygrindjs.script.command.SkyGrindCommandExecutor;
import net.skygrind.skygrindjs.script.event.SkyGrindEventCallback;
import net.skygrind.skygrindjs.script.event.SkyGrindEventExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.scheduler.BukkitTask;

import javax.script.ScriptException;
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
                } catch (ScriptException | NoSuchMethodException | RuntimeException ex) {
                    getLogger().log(Level.WARNING, ex.getMessage());
                }
            }
        });
    }
    public <T extends Event> void registerEvent(Class<? extends Event> eventClass, EventPriority priority, boolean ignoreCancelled, SkyGrindEventCallback<T> callback) {
        SkyGrindEventExecutor executor = new SkyGrindEventExecutor(eventClass, callback);
        Bukkit.getServer().getPluginManager().registerEvent(eventClass, executor, priority, executor, SkyGrindJS.getPlugin(), ignoreCancelled);
        skyScript.getListeners().add(executor);
    }

    public PluginCommand registerCommand(String cmd, final String function) {
        return registerCommand(cmd, new SkyGrindCommandCallback() {
            @Override
            public boolean callback(CommandSender sender, String command, String[] args) {
                try {
                    return Boolean.TRUE == skyScript.invokeLibraryFunction(function, sender, command, args);
                } catch (ScriptException | NoSuchMethodException | RuntimeException ex) {
                    getLogger().log(Level.WARNING, ex.getMessage());
                }
                return false;
            }
        });
    }


    /**
     * Register a new command.
     * @param cmd The name of the command.
     * @param callback A handler for the command.
     */
    public PluginCommand registerCommand(String cmd, SkyGrindCommandCallback callback) {
        try {
            final PluginCommand command = CommandRegistry.registerCommand(SkyGrindJS.getPlugin(), cmd);
            if (command != null) {
                skyScript.getCommands().add(command);
                command.setExecutor(new SkyGrindCommandExecutor(callback));
                return command;
            }
        } catch (UnsupportedOperationException ex) {
            getLogger().log(Level.WARNING, null, ex);
        }
        return null;
    }

    public BukkitTask runSyncTimer(Runnable run, int delay, int ticks) {
        BukkitTask task = Bukkit.getServer().getScheduler().runTaskTimer(SkyGrindJS.getPlugin(), run, delay, ticks);
        skyScript.getTasks().add(task);
        return task;
    }

    public BukkitTask runAsyncTimer(Runnable run, int delay, int ticks) {
        BukkitTask task = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(SkyGrindJS.getPlugin(), run, delay, ticks);
        skyScript.getTasks().add(task);
        return task;
    }

    public BukkitTask runTaskLater(Runnable run, int later) {
        BukkitTask task = Bukkit.getServer().getScheduler().runTaskLater(SkyGrindJS.getPlugin(), run, later);
        skyScript.getTasks().add(task);
        return task;
    }

    public BukkitTask runAsyncTaskLater(Runnable run, int later) {
        BukkitTask task = Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(SkyGrindJS.getPlugin(), run, later);
        skyScript.getTasks().add(task);
        return task;
    }
}
