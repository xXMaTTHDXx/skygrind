package net.skygrind.hub;

import net.skygrind.core.SkyPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Matt on 2017-02-11.
 */
public class Hub extends SkyPlugin implements Listener {

    @Override
    protected void onModuleEnable() throws Exception {

    }


    @Override
    protected void onModuleDisable() throws Exception {

    }

    @EventHandler
    public void loseHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
    }
}
