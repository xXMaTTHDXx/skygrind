package net.skygrind.skyblock.island;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.SchematicFormat;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.misc.LocationUtil;
import net.skygrind.skyblock.misc.Logger;
import net.skygrind.skyblock.player.SkyPlayer;
import net.skygrind.skyblock.region.Region;
import net.skygrind.skyblock.region.RegionHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.hamcrest.core.Is;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Matt on 2017-02-11.
 */
public class IslandRegistry {

    public List<Island> playerIslands = new ArrayList<>();

    private File islandDir = new File(SkyBlock.getPlugin().getDataFolder(), "islands");
    private Map<UUID, Island> islandInvites = new HashMap<>();

    private final int islandDistance = 1000;
    private final int baseIslandSize = 80;

    public void init() {
        loadIslands();
    }

    public void registerIsland(UUID owner, Island island) {
        this.playerIslands.add(island);
    }

    public void disable() {
        for (Island island : playerIslands) {

            File islandFile = getFileForIsland(island);
            YamlConfiguration config = YamlConfiguration.loadConfiguration(islandFile);

            config.set("owner", island.getOwner());
            config.set("min", LocationUtil.serialize(island.getContainer().getMin()));
            config.set("max", LocationUtil.serialize(island.getContainer().getMax()));
            List<String> mems = config.getStringList("members");
            for (UUID uuid : island.getMembers()) {
                if (!mems.contains(uuid.toString())) {
                    continue;
                }
                mems.add(uuid.toString());
            }
            config.set("members", mems);
            config.set("spawn", island.getSpawn());
            config.set("maxPlayers", island.getMaxPlayers());
        }
    }

    public boolean hasIsland(Player player) {
        for (Island island : playerIslands) {
            if (island.getOwner().equals(player.getUniqueId()) || island.getMembers().contains(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public void loadIslands() {
        if (!islandDir.exists()) {
            islandDir.mkdir();
        }
        for (File islandFile : islandDir.listFiles()) {
            if (islandFile.getName().endsWith(".yml")) {
                //ISLAND!!

                YamlConfiguration islandConfig = YamlConfiguration.loadConfiguration(islandFile);

                UUID ownerID = UUID.fromString(islandConfig.getString("ownerID"));
                Location spawn = LocationUtil.deserialize(islandConfig.getString("spawn"));

                Location min = LocationUtil.deserialize(islandConfig.getString("min"));
                Location max = LocationUtil.deserialize(islandConfig.getString("max"));

                IslandType type = IslandType.valueOf(islandConfig.getString("type"));

                int maxPlayers = islandConfig.getInt("maxPlayers");

                List<UUID> members = new ArrayList<>();

                for (String uuid : islandConfig.getStringList("members")) {
                    UUID id = UUID.fromString(uuid);
                    members.add(id);
                }

                Island island = new Island(ownerID, spawn, type);
                island.setContainer(SkyBlock.getPlugin().getRegionHandler().createRegion(island.getName(), min, max));
                island.setMembers(members);
                island.setMaxPlayers(maxPlayers);

                registerIsland(ownerID, island);
            }
        }
        System.out.println("Loaded: " + playerIslands.size());
    }


    public void createIsland(Player player, IslandType type) throws MaxChangedBlocksException {

        if (hasIsland(player)) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You already have an island!");
            return;
        }

        Location center = findEmptySpace();

        Island island = new Island(player.getUniqueId(), center, type);
        island.setSize(baseIslandSize);

        double minX = center.getBlockX() - island.getType().getSize() / 2;
        double minY = 0;
        double minZ = center.getBlockZ() - island.getType().getSize() / 2;

        int maxX = center.getBlockX() + island.getType().getSize() / 2;
        int maxY = 256;
        int maxZ = center.getBlockZ() + island.getType().getSize() / 2;


        Location min = new Location(SkyBlock.getPlugin().getIslandWorld(), minX, minY, minZ);
        Location max = new Location(SkyBlock.getPlugin().getIslandWorld(), maxX, maxY, maxZ);

        Region container = SkyBlock.getPlugin().getRegionHandler().createRegion(island.getName(), min, max);
        island.setContainer(container);
        island.setMembers(new ArrayList<>());

        island.setMaxPlayers(4);

        File islandFile = new File(islandDir, player.getUniqueId().toString() + ".yml");

        if (!islandFile.exists()) {
            try {
                islandFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(islandFile);

        config.set("ownerID", player.getUniqueId().toString());
        config.set("min", LocationUtil.serialize(min));
        config.set("max", LocationUtil.serialize(max));
        config.set("members", "");
        config.set("spawn", LocationUtil.serialize(island.getSpawn()));
        config.set("type", island.getType().toString());
        try {
            config.save(islandFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            SkyBlock.getPlugin().getSchematicLoader().pasteSchematic("test.schematic", SkyBlock.getPlugin().getIslandWorld(), center.getBlockX(), 100, center.getBlockZ());
        } catch (DataException | IOException e) {
            e.printStackTrace();
        }

        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] Your Island is ready!");
        player.sendMessage(ChatColor.GRAY + "Type (/is home) to visit.");
        registerIsland(player.getUniqueId(), island);
    }

    public Location findEmptySpace() {
        if (playerIslands.isEmpty()) {
            return new Location(SkyBlock.getPlugin().getIslandWorld(), 0, 100, 0);
        }

        Location base = null;

        for (Island island : playerIslands) {

            base = island.getSpawn().clone().add(islandDistance, 0, 0);
            int i = 0;
            while (conflicts(base) && i < 500000) {
                System.out.println("Conflicts");
                base = base.clone().add(islandDistance, 0, 0);
                i++;
            }
            System.out.println("Found base: " + base.getBlockX() + ", " + base.getBlockZ());
            break;
        }
        return base;
    }

    public boolean conflicts(Island island) {
        for (Island conflict : playerIslands) {
            if (conflict.getSpawn().toVector().isInAABB(island.getContainer().getMin().toVector(), island.getContainer().getMax().toVector()) ||
                    island.getSpawn().toVector().isInAABB(conflict.getContainer().getMin().toVector(), conflict.getContainer().getMax().toVector())) {
                return true;
            }
        }
        return false;
    }

    public boolean conflicts(Location loc) {
        for (Island conflict : playerIslands) {
            if (loc.toVector().isInAABB(conflict.getContainer().getMin().toVector(), conflict.getContainer().getMax().toVector())) {
                return true;
            }
        }
        return false;
    }

    public Island getIslandAt(Location location) {
        for (Island island : playerIslands) {
            if (location.toVector().isInAABB(island.getContainer().getMin().toVector(), island.getContainer().getMax().toVector())) {
                return island;
            }
        }
        return null;
    }

    public void deleteIsland(Player player, Island island) {
        //TODO find people on island

        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (isInIslandRegion(island, pl.getLocation())) {
                pl.teleport(SkyBlock.getPlugin().getSpawn());
                pl.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] This island is being deleted! Sent you to spawn");
            }
        }

        File toDelete = new File(islandDir, island.getOwner().toString());

        if (!toDelete.exists()) {
            return;
        }
        toDelete.delete();


        CuboidSelection sel = new CuboidSelection(SkyBlock.getPlugin().getIslandWorld(), island.getContainer().getMin(), island.getContainer().getMax());
        EditSession session = new EditSession(new BukkitWorld(SkyBlock.getPlugin().getIslandWorld()), 200000);

        try {
            session.setBlocks(new CuboidRegion(BukkitUtil.toVector(sel.getMinimumPoint().toVector()), BukkitUtil.toVector(sel.getMaximumPoint().toVector())), new BaseBlock(BlockID.AIR));
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
        SkyBlock.getPlugin().getRegionHandler().deleteRegion(island.getContainer());
        playerIslands.remove(island);
    }

    public boolean isInIslandRegion(Island island, Location loc) {
        Vector min = island.getContainer().getMin().toVector();
        Vector max = island.getContainer().getMax().toVector();

        return loc.toVector().isInAABB(min, max);
    }

    public Island getIslandForPlayer(Player player) {
        for (Island island : playerIslands) {
            if (island.getOwner().equals(player.getUniqueId()) || island.getMembers().contains(player.getUniqueId())) {
                return island;
            }
        }
        return null;
    }

    public boolean hasInvite(Player player) {
        return islandInvites.get(player.getUniqueId()) != null;
    }

    public Island getInviteFor(Player player) {
        if (hasInvite(player)) {
            return islandInvites.get(player.getUniqueId());
        }
        return null;
    }

    public Map<UUID, Island> getIslandInvites() {
        return islandInvites;
    }

    public File getFileForIsland(Island island) {
        return new File(islandDir, island.getOwner().toString() + ".yml");
    }
}
