package net.skygrind.skygrindjs;

import net.skygrind.core.SkyPlugin;
import net.skygrind.skygrindjs.script.SkyScript;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

        for (File file : getDataFolder().listFiles()) {
            if (file.getName().endsWith(".js")) {
                SkyScript skyScript = new SkyScript(file);
                skyScript.run();
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

    public static SkyGrindJS getPlugin() {
        return plugin;
    }
}
