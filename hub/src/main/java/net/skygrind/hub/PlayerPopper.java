package net.skygrind.hub;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Matt on 27/02/2017.
 */
public class PlayerPopper implements Listener {

    @EventHandler
    public void onPlayerPop(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) && !(event.getDamager() instanceof Arrow)) {
            return;
        }

        event.setDamage(0);
        event.setCancelled(true);

        Player player = (Player) event.getEntity();
        Arrow arrow = (Arrow) event.getDamager();


    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (item.getType() == Material.BOW) {

                {
                    ItemStack itemStack = new ItemStack(Material.FISHING_ROD, 1);
                    ItemMeta meta = itemStack.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Grappling Hook " + ChatColor.GRAY + "(Left Lick Toggle)");
                    itemStack.setItemMeta(meta);
                    player.getInventory().setItem(8,itemStack );
                }

            }
            else if (item.getType() == Material.FISHING_ROD) {

                {
                    ItemStack itemStack = new ItemStack(Material.BOW, 1);
                    ItemMeta meta = itemStack.getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Player Popper " + ChatColor.GRAY + "(Left Lick Toggle)");
                    itemStack.setItemMeta(meta);
                    player.getInventory().setItem(8,itemStack );
                }
            }
            else {
                return;
            }
        }
        else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item.getType() == Material.FISHING_ROD) {

            }
        }
    }
}
