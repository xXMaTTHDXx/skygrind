package net.skygrind.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.rayline.core.command.RDCommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 2017-02-11.
 */
public class SGCommand extends RDCommand {

    protected SGCommand(String name, RDCommand... subCommands) {
        super(name, subCommands);
    }
}
