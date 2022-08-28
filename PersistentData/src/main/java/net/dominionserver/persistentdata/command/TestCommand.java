package net.dominionserver.persistentdata.command;

import org.bukkit.plugin.java.JavaPlugin;

public final class TestCommand extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        // Plugin startup logic
        this.getCommand("persist").setExecutor(new PersistentDataCommand());
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }
}
