package com.example.timeplugin.listeners;

import com.example.timeplugin.TimePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerJoinListener implements Listener {

    private final TimePlugin plugin;

    public PlayerJoinListener(TimePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.getTimeBarManager().startForPlayer(player);


    }
}