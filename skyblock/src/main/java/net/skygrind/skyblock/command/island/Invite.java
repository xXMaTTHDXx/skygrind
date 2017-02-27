package net.skygrind.skyblock.command.island;

import net.skygrind.core.command.SGCommand;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tech.rayline.core.command.CommandException;
import tech.rayline.core.command.RDCommand;

/**
 * Created by Matt on 2017-02-25.
 */
public class Invite extends SGCommand {

    protected Invite() {
        super("invite");
    }

    @Override
    protected void handleCommand(Player player, String[] args) throws CommandException {
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "/island invite [Player]");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        IslandRegistry registry = SkyBlock.getPlugin().getIslandRegistry();

        if (!registry.hasIsland(player)) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You do not have an island!");
            return;
        }

        Island island = registry.getIslandForPlayer(player);

        if (target == null) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "That player is not currently online!");
            return;
        }

        if (island.getMembers().size() >= island.getMaxPlayers()) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You're currently restricted to 4 island members... To increase this visit out store at <link>");
            return;
        }

        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "Invited " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + " to your island.");

        MessageUtil.sendServerTheme(player, ChatColor.GRAY + "Invited " + target.getName() + " to your island.");
        target.sendMessage(ChatColor.GRAY + "Type " + ChatColor.GREEN + ChatColor.BOLD.toString() + "/ACCEPT " +
        ChatColor.GRAY + "or " + ChatColor.RED + ChatColor.BOLD.toString() + "/DECLINE");

        target.sendMessage(ChatColor.GRAY + "This invite expires in 3 minutes!");

        registry.getIslandInvites().put(target.getUniqueId(), island);

        new BukkitRunnable() {
            @Override
            public void run() {
                registry.getIslandInvites().remove(target.getUniqueId());
            }
        }.runTaskLater(SkyBlock.getPlugin(), 20*60*3L);
    }
}
