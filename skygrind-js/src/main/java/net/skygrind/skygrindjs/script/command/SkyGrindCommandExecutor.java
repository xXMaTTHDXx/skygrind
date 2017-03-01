package net.skygrind.skygrindjs.script.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Matt on 27/02/2017.
 */
public class SkyGrindCommandExecutor implements CommandExecutor {

    private SkyGrindCommandCallback callback;

    public SkyGrindCommandExecutor(SkyGrindCommandCallback callback) {
        this.callback = callback;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return callback.callback(commandSender, s, strings);
    }
}
