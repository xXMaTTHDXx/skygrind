package net.skygrind.skyblock.island;

import com.sk89q.worldedit.MaxChangedBlocksException;
import net.md_5.bungee.api.ChatColor;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.player.PlayerRegistry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandGUIHandler implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player pl = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        if (!event.getClickedInventory().getName().equalsIgnoreCase("Island Selection")) {
            return;
        }

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();

        IslandType type = IslandType.valueOf(ChatColor.stripColor(item.getItemMeta().getDisplayName()).toUpperCase());

        pl.closeInventory();

        try {
            SkyBlock.getPlugin().getIslandRegistry().createIsland(pl, type);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }
}
