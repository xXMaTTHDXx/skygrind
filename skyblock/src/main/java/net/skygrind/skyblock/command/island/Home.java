package net.skygrind.skyblock.command.island;

import net.skygrind.core.command.SGCommand;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.entity.Player;
import tech.rayline.core.command.CommandException;

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
            MessageUtil.sendUrgent(player, "/island home");
            return;
        }

        if (!SkyBlock.getPlugin().getIslandRegistry().hasIsland(player)) {
            MessageUtil.sendUrgent(player, "You do not have an island to teleport to!");
            return;
        }

        Island playerIsland = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);

        System.out.println(playerIsland.getName());
        System.out.println(playerIsland.getSpawn().getBlockX() + ", " + playerIsland.getSpawn().getBlockZ());

        player.teleport(SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player).getSpawn());
        MessageUtil.sendInfo(player, "Teleported you to your island home.");
    }
}
