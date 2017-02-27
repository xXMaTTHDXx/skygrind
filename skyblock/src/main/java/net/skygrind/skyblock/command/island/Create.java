package net.skygrind.skyblock.command.island;

import com.sk89q.worldedit.MaxChangedBlocksException;
import net.skygrind.core.command.SGCommand;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.IslandType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.rayline.core.command.CommandException;
import tech.rayline.core.command.RDCommand;

import java.util.Arrays;

/**
 * Created by Matt on 2017-02-25.
 */
public class Create extends SGCommand {

    protected Create() {
        super("create");
    }

    @Override
    protected void handleCommand(Player player, String[] args) throws CommandException {
        if (args.length > 0) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "/island create");
            return;
        }

        if (SkyBlock.getPlugin().getIslandRegistry().hasIsland(player)) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You already have an island!");
            return;
        }

        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "Opening island selection.");
        openIslandGUI(player);
    }

    public void openIslandGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "Island Selection");

        for (IslandType type : IslandType.values()) {
            ItemStack item = new ItemStack(Material.PAPER, 1);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(type.getDisplay());
            meta.setLore(Arrays.asList(type.getLore()));
            item.setItemMeta(meta);
            inv.addItem(item);
        }
        player.openInventory(inv);
    }
}
