package com.example.timeplugin;


import com.example.timeplugin.commands.HideCommand;
import com.example.timeplugin.commands.ShowCommand;
import com.example.timeplugin.util.TimeBarManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.example.timeplugin.listeners.PlayerJoinListener;
import com.example.timeplugin.listeners.PlayerQuitListener;

public class TimePlugin extends JavaPlugin {

    private static TimePlugin instance;
    private TimeBarManager timeBarManager;

    @Override
    public void onEnable() {
        instance = this;

        // Save default config if it doesn't exist
        saveDefaultConfig();

        timeBarManager = new TimeBarManager(this);
        timeBarManager.start();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        // Register commands
        getCommand("show").setExecutor(new ShowCommand(this));
        getCommand("hide").setExecutor(new HideCommand(this));

        getLogger().info("✓ TimeBar plugin enabled!");
    }

    @Override
    public void onDisable() {

        if (timeBarManager != null) {
            timeBarManager.stop();
        }
        getLogger().info("✓ TimeBar plugin disabled!");
    }

    public static TimePlugin getInstance() {
        return instance;
    }

    public TimeBarManager getTimeBarManager() {
        return timeBarManager;
    }
}