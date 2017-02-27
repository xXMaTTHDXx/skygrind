package net.skygrind.skyblock.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 2017-02-12.
 */
public class PlayerRegistry implements Listener {

    private List<SkyPlayer> players = new ArrayList<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player pl = e.getPlayer();

        SkyPlayer player = new SkyPlayer(pl);
        this.players.add(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player pl = e.getPlayer();
        SkyPlayer player = getPlayer(pl.getName());

        this.players.remove(player);
    }

    public SkyPlayer getPlayer(String name) {
        for (SkyPlayer player : players) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }
}
