package net.skygrind.core;
import net.skygrind.core.command.InvSee;
import tech.rayline.core.plugin.RedemptivePlugin;

/**
 * Created by Matt on 2017-02-11.
 */
public class Core extends RedemptivePlugin {

    @Override
    protected void onModuleEnable() throws Exception {
        this.registerCommand(new InvSee());
    }

    @Override
    protected void onModuleDisable() throws Exception {

    }
}
