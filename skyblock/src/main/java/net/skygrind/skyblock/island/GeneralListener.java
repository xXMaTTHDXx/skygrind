package net.skygrind.skyblock.island;

import net.skygrind.skyblock.SkyBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Matt on 2017-02-25.
 */
public class GeneralListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player pl = event.getPlayer();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.teleport(SkyBlock.getPlugin().getSpawn());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (player.hasPermission("skygrind.keepinv")) {
            event.setKeepInventory(true);
        }
        else {
            event.setKeepInventory(false);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(SkyBlock.getPlugin().getSpawn());
    }
}
