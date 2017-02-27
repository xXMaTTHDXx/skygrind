package net.skygrind.skyblock.island;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

/**
 * Created by Matt on 2017-02-11.
 */
@RequiredArgsConstructor
public enum IslandType {
    DEFAULT("Default", ChatColor.GREEN + ChatColor.BOLD.toString() + "Default", "skyblock.island.default", new String[] {ChatColor.GOLD + "A basic Skyblock island."}, 80);


    private final String raw, display, permRequired;
    private final String[] lore;
    private final int size;

    public String getRaw() {
        return raw;
    }

    public String getDisplay() {
        return display;
    }

    public String getPermRequired() {
        return permRequired;
    }

    public int getSize() {
        return size;
    }

    public String[] getLore() {
        return lore;
    }
}
