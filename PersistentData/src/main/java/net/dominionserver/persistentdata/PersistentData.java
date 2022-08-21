package net.dominionserver.persistentdata;

import net.dominionserver.persistentdata.filemanagement.YAMLFile;
import net.dominionserver.persistentdata.testcommand.Frontfacingcommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class PersistentData extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        // Plugin startup logic
        this.getCommand("persist").setExecutor(new Frontfacingcommand());
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }
}
