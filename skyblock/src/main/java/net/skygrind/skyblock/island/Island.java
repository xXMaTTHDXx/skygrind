package net.skygrind.skyblock.island;

import net.skygrind.skyblock.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

/**
 * Created by Matt on 2017-02-11.
 */
public class Island {

    private Location spawn;
    private Region container;
    private UUID owner;

    private int maxPlayers;

    private List<UUID> members;
    private int size;
    private IslandType type;
    private String islandName;

    public Island(UUID owner, Location spawn, IslandType type) {
        this.owner = owner;
        this.spawn = spawn;
        this.type = type;

        if (Bukkit.getPlayer(owner) == null) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(owner);
            islandName = player.getName();
        } else {
            this.islandName = Bukkit.getPlayer(owner).getName();
        }
    }

    public Location getSpawn() {
        return spawn;
    }

    public Region getContainer() {
        return container;
    }

    public void setContainer(Region container) {
        this.container = container;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void setMembers(List<UUID> members) {
        this.members = members;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public IslandType getType() {
        return type;
    }

    public void setType(IslandType type) {
        this.type = type;
    }

    public String getName() {
        if (Bukkit.getPlayer(owner) == null) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(owner);
            islandName = player.getName();
        } else {
            this.islandName = Bukkit.getPlayer(owner).getName();
        }

        return islandName + "'s Island";
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
}
