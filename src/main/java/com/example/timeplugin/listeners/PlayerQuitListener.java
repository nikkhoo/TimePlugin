package com.example.timeplugin.listeners;

import com.example.timeplugin.TimePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerQuitListener implements Listener {

    private final TimePlugin plugin;

    public PlayerQuitListener(TimePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        plugin.getTimeBarManager().stopForPlayer(player);


    }
}