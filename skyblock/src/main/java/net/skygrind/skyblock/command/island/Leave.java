package net.skygrind.skyblock.command.island;

import net.skygrind.core.command.SGCommand;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tech.rayline.core.command.CommandException;
import tech.rayline.core.command.RDCommand;

/**
 * Created by Matt on 2017-02-25.
 */
public class Leave extends SGCommand {

    IslandRegistry registry = SkyBlock.getPlugin().getIslandRegistry();

    protected Leave() {
        super("leave");
    }

    @Override
    protected void handleCommand(Player player, String[] args) throws CommandException {
        if (args.length > 0) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "/island leave");
            return;
        }

        Island island = registry.getIslandForPlayer(player);

        if (island == null) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You do not have an island to leave!");
            return;
        }

        if (island.getOwner().equals(player.getUniqueId())) {
            if (island.getMembers().size() > 0) {
                island.setOwner(island.getMembers().get(0));
            }
            else {
                registry.deleteIsland(player, island);
                Bukkit.broadcastMessage(ChatColor.GRAY + ChatColor.BOLD.toString() + island.getName() + " Status: " + ChatColor.RED + ChatColor.BOLD.toString() + "[FALLEN]");
            }
        }

        if (island.getMembers().contains(player.getUniqueId())) {
            island.getMembers().remove(player.getUniqueId());
            player.teleport(SkyBlock.getPlugin().getSpawn());
        }

        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "Successfully left your island!");
        return;
    }
}
