package net.skygrind.core.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tech.rayline.core.command.CommandException;
import tech.rayline.core.command.RDCommand;

/**
 * Created by Matt on 2017-02-26.
 */
public class InvSee extends SGCommand {

    public InvSee() {
        super("invsee");
    }

    @Override
    protected void handleCommand(Player player, String[] args) throws CommandException {
        if (!player.hasPermission("skygrind.invsee")) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You do not have permission to do this!");
            return;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "/invsee [Player]");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "That player is not online!");
            return;
        }
        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "Opening " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + " 's inventory");
        player.openInventory(target.getInventory());
    }
}
