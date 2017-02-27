package net.skygrind.skyblock.command.island;

import net.skygrind.core.command.SGCommand;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tech.rayline.core.command.CommandException;
import tech.rayline.core.command.RDCommand;

/**
 * Created by Matt on 2017-02-25.
 */
public class Home extends SGCommand {

    protected Home() {
        super("home");
    }

    @Override
    protected void handleCommand(Player player, String[] args) throws CommandException {
        if (args.length > 0) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "/island home");
            return;
        }

        if (!SkyBlock.getPlugin().getIslandRegistry().hasIsland(player)) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You do not have an island!");
            return;
        }

        Island playerIsland = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);

        System.out.println(playerIsland.getName());
        System.out.println(playerIsland.getSpawn().getBlockX() + ", " + playerIsland.getSpawn().getBlockZ());

        player.teleport(SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player).getSpawn());
        player.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "Teleported you to your island home.");
    }
}
