package net.skygrind.skygrindjs;

import net.skygrind.core.SkyPlugin;
import net.skygrind.skygrindjs.script.SkyScript;
import org.bukkit.Bukkit;

import java.io.File;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Created by Matt on 2017-02-26.
 */
public class SkyGrindJS extends SkyPlugin {

    private Map<File, SkyScript> loaded = new HashMap<>();
    private static SkyGrindJS plugin;

    @Override
    protected void onModuleEnable() throws Exception {
        plugin = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get(getDataFolder().getAbsolutePath().replace("\\", "/"));
        dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        Bukkit.getServer().getScheduler().runTaskAsynchronously(this, () -> {

            while (true) {
                WatchKey key;
                try {
                    // wait for a key to be available
                    key = watcher.take();
                } catch (InterruptedException ex) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    // get event type
                    WatchEvent.Kind<?> kind = event.kind();

                    // get file name
                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();

                    if (!fileName.toFile().getName().endsWith(".js")) {
                        return;
                    }

                    System.out.println(kind.name() + ": " + fileName);

                    if (kind == OVERFLOW) {
                        continue;
                    } else {
                        File file = fileName.toFile();

                        if (getScript(file) ==null) {
                            SkyScript skyScript = new SkyScript(file);
                            this.loaded.put(file, skyScript);
                            skyScript.run();
                        }

                        SkyScript skyScript = getScript(file);
                        skyScript.clean();
                        skyScript.run();
                    }
                }

                // IMPORTANT: The key must be reset after processed
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        });

        for (File file : getDataFolder().listFiles()) {
            if (file.getName().endsWith(".js")) {
                SkyScript skyScript = new SkyScript(file);
                skyScript.run();
                loaded.put(file, skyScript);
            }
        }
    }

    @Override
    protected void onModuleDisable() throws Exception {
        plugin = null;
    }

    public Map<File, SkyScript> getLoaded() {
        return loaded;
    }

    public SkyScript getScript(File file) {
        for (File f : this.loaded.keySet()) {
            if (f.getName().equalsIgnoreCase(file.getName())) {
                return loaded.get(f);
            }
        }
        return null;
    }

    public static SkyGrindJS getPlugin() {
        return plugin;
    }
}
