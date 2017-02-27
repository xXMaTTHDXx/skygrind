package net.skygrind.skyblock.island.listeners;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandListener implements Listener {

    private IslandRegistry registry = SkyBlock.getPlugin().getIslandRegistry();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player placer = event.getPlayer();

        Location location = event.getBlockPlaced().getLocation();

        if (registry.conflicts(location)) {

            Island conflict = registry.getIslandAt(location);

            if (!conflict.getMembers().contains(placer.getUniqueId()) && !conflict.getOwner().equals(placer.getUniqueId())) {
                placer.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You do not have permission to build here!");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player placer = event.getPlayer();

        Location location = event.getBlock().getLocation();

        if (registry.conflicts(location)) {

            Island conflict = registry.getIslandAt(location);

            if (!conflict.getMembers().contains(placer.getUniqueId()) && !conflict.getOwner().equals(placer.getUniqueId())) {
                placer.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You do not have permission to build here!");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();

            if (!(block.getType() == Material.CHEST)) {
                return;
            }

            if (registry.conflicts(block.getLocation())) {

                Island conflict = registry.getIslandAt(block.getLocation());

                if (!conflict.getMembers().contains(player.getUniqueId()) && !conflict.getOwner().equals(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You do not have permission to open containers here!");
                    event.setCancelled(true);
                }
            }
        }
    }
}