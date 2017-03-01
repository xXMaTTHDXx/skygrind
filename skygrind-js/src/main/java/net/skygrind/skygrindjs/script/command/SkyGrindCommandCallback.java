package net.skygrind.skygrindjs.script.command;

import org.bukkit.command.CommandSender;

/**
 * Created by Matt on 27/02/2017.
 */
public interface SkyGrindCommandCallback {

    public boolean callback(CommandSender sender, String name, String[] args);
}
