package net.skygrind.skygrindjs.script;

import net.skygrind.skygrindjs.script.command.CommandRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import javax.script.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 2017-02-26.
 */
public class SkyScript implements Runnable {

    private ScriptEngineManager manager;
    private ScriptEngine engine;

    private Invocable invocable;

    private String header = "load(\"nashorn:mozilla_compat.js\");";
    private File file;

    private List<Listener> listeners = new ArrayList<>();
    private List<PluginCommand> commands = new ArrayList<>();
    private List<BukkitTask> tasks = new ArrayList<>();

    public SkyScript(File file) {
        this.file = file;
        manager = new ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript");
        System.out.println(engine == null);
        invocable = (Invocable) engine;

        String[] javaImports = new String[]{"java.lang", "java.util", "java.util.concurrent", "java.util.concurrent.atomic", "java.util.regex", "org.bukkit", "org.bukkit.block", "org.bukkit.command", "org.bukkit.entity", "org.bukkit.event", "org.bukkit.event.inventory", "org.bukkit.event.player", "org.bukkit.event.entity", "org.bukkit.event.block", "org.bukkit.event.hanging", "org.bukkit.event.weather", "org.bukkit.event.world", "org.bukkit.event.vehicle", "org.bukkit.scoreboard", "org.bukkit.inventory", "org.bukkit.permissions", "org.bukkit.potion", "org.bukkit.enchantments", "org.bukkit.util", "org.bukkit.configuration.file"};
        for (String imp : javaImports) {
            header = header + "importPackage(" + imp + ");";
        }
    }

    @Override
    public void run() {
        try {

            Bindings b = engine.createBindings();
            b.put("bukkit", new ScriptBukkit(this));
            SimpleScriptContext context = new SimpleScriptContext();
            context.setBindings(b, 100);
            engine.eval(header, (ScriptContext)context);
            engine.eval("load(\"" + this.file.getAbsolutePath().replace("\\", "/") + "\");", (ScriptContext)context);

        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    public Object invokeLibraryFunction(String function, Object... args) throws ScriptException, NoSuchMethodException {
        if (invocable != null) {
            return invocable.invokeFunction(function, args);
        } else {
            throw new NoSuchMethodException("ScriptEngine does not implement javax.script.Invocable, you need to call plugin.setInvocable(Invocable) " +
                    "in order to invoke named scripts in your library");
        }
    }

    public List<Listener> getListeners() {
        return listeners;
    }

    public List<PluginCommand> getCommands() {
        return commands;
    }

    public List<BukkitTask> getTasks() {
        return tasks;
    }

    public void clean() {

        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }

        for (PluginCommand cmd : commands) {
            CommandRegistry.unregisterCommand(Bukkit.getServer(), cmd);
        }

        for (BukkitTask task : tasks) {
            task.cancel();
        }

        tasks.clear();
        commands.clear();
        listeners.clear();
    }
}
