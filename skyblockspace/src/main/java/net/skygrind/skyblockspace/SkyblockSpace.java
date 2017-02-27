package net.skygrind.skyblockspace;

import net.skygrind.core.SkyPlugin;

/**
 * Created by Matt on 2017-02-26.
 */
public class SkyblockSpace extends SkyPlugin {

    private static SkyblockSpace plugin;

    @Override
    protected void onModuleEnable() throws Exception {
        plugin = this;
    }

    @Override
    protected void onModuleDisable() throws Exception {
        plugin = null;
    }

    public static SkyblockSpace getPlugin() {
        return plugin;
    }
}
