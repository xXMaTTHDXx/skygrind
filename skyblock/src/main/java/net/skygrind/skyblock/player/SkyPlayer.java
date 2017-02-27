package net.skygrind.skyblock.player;

import net.skygrind.skyblock.island.Island;
import org.bukkit.entity.Player;

/**
 * Created by Matt on 2017-02-11.
 */
public class SkyPlayer {

    private Player bukkitPlayer;

    public Island island;

    public int level;

    public SkyPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public void setBukkitPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }

    public Island getIsland() {
        return island;
    }

    public void setIsland(Island island) {
        this.island = island;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return bukkitPlayer.getName();
    }
}
