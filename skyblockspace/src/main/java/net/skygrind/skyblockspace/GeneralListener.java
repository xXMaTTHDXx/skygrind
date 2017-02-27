package net.skygrind.skyblockspace;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Matt on 2017-02-26.
 */
public class GeneralListener implements Listener {

    private final double gravity = 0.25;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().getY() > event.getFrom().getY()) {
            event.getPlayer().setVelocity(event.getPlayer().getVelocity().multiply(gravity));
        }
    }
}
