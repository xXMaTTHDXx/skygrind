package net.skygrind.skyblock;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.skygrind.core.SkyPlugin;
import net.skygrind.skyblock.command.SetSpawnCommand;
import net.skygrind.skyblock.command.SpawnCommand;
import net.skygrind.skyblock.command.island.InviteAccept;
import net.skygrind.skyblock.command.island.InviteDecline;
import net.skygrind.skyblock.command.island.IslandCommand;
import net.skygrind.skyblock.command.RegionCommand;
import net.skygrind.skyblock.island.GeneralListener;
import net.skygrind.skyblock.island.IslandGUIHandler;
import net.skygrind.skyblock.island.IslandRegistry;
import net.skygrind.skyblock.island.listeners.IslandListener;
import net.skygrind.skyblock.misc.LocationUtil;
import net.skygrind.skyblock.player.PlayerRegistry;
import net.skygrind.skyblock.region.RegionHandler;
import net.skygrind.skyblock.schematic.SchematicLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.HandlerList;

/**
 * Created by Matt on 2017-02-10.
 */
public class SkyBlock extends SkyPlugin {

    private static SkyBlock plugin;
    private PlayerRegistry playerRegistry;
    private RegionHandler regionHandler;
    private IslandRegistry islandRegistry;
    private Location spawn;

    public SchematicLoader schematicLoader;

    private WorldEditPlugin worldEditPlugin;

    private World islandWorld;

    /**
     * TODO List for tomorrow
     *
     * Finish Region Command (Create /region create [name] /region delete [name] dispose of active sessions.)
     * Test Schem loading
     * Allow Schems to take a region ?
     */

    @Override
    public void onModuleEnable() {
        plugin = this;
        this.regionHandler = new RegionHandler();
        this.playerRegistry = new PlayerRegistry();
        this.islandRegistry = new IslandRegistry();
        this.registerListener(playerRegistry);
        this.registerListener(new IslandListener());
        this.registerListener(new IslandGUIHandler());
        this.registerCommand(new IslandCommand());
        this.registerCommand(new RegionCommand());
        this.registerCommand(new SetSpawnCommand());
        this.registerCommand(new SpawnCommand());
        this.registerCommand(new InviteAccept());
        this.registerCommand(new InviteDecline());
        this.registerListener(new GeneralListener());
        getConfig().options().copyDefaults(true);
        saveConfig();

        islandRegistry.init();

        this.spawn = LocationUtil.deserialize(getConfig().getString("spawn"));

        if (!Bukkit.getPluginManager().getPlugin("WorldEdit").isEnabled()) {
            Bukkit.getPluginManager().disablePlugin(this);
        }

        worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

        if (!Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
            Bukkit.getPluginManager().disablePlugin(this);
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv create Skyblock normal -g VoidWorld");

        islandWorld = Bukkit.getWorld("Skyblock");

        schematicLoader = new SchematicLoader(this);

        //TODO load schems
        //TODO load player data
    }

    public RegionHandler getRegionHandler() {
        return regionHandler;
    }

    public World getIslandWorld() {
        return islandWorld;
    }

    @Override
    public void onModuleDisable() {
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
        islandRegistry.disable();
        plugin = null;
    }

    public IslandRegistry getIslandRegistry() {
        return islandRegistry;
    }

    public PlayerRegistry getPlayerRegistry() {
        return playerRegistry;
    }

    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }

    public SchematicLoader getSchematicLoader() {
        return schematicLoader;
    }

    public static SkyBlock getPlugin() {
        return plugin;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        getConfig().set("spawn", LocationUtil.serialize(spawn));
        saveConfig();
        this.spawn = spawn;
    }
}
