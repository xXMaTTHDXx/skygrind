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
public class InviteDecline extends SGCommand {

    IslandRegistry registry = SkyBlock.getPlugin().getIslandRegistry();

    public InviteDecline() {
        super("decline");
    }

    @Override
    protected void handleCommand(Player player, String[] args) throws CommandException {
        if (!registry.hasInvite(player)) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You do not have any pending invites.");
            return;
        }

        Island invite = registry.getInviteFor(player);

        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "Declined invite from " + ChatColor.GOLD + invite.getName());

        Player owner = Bukkit.getPlayer(invite.getOwner());

        if (owner == null) {
            return;
        }

        owner.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] " + ChatColor.GOLD + player.getName() + ChatColor.GRAY + " has declined your island invite.");
        registry.getIslandInvites().remove(player.getUniqueId());
        return;
    }
}
