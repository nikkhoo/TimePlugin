package com.example.timeplugin.util;

import com.example.timeplugin.TimePlugin;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TimeBarManager {

    private final TimePlugin plugin;
    private final Map<UUID, BossBar> playerBars = new ConcurrentHashMap<>();
    private final Map<UUID, Long> joinTimes = new ConcurrentHashMap<>();
    private final Set<UUID> hiddenPlayers = ConcurrentHashMap.newKeySet();
    private BukkitTask updateTask;

    public TimeBarManager(TimePlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (!isEnabled()) {
            return;
        }

        updateTask = plugin.getServer().getScheduler().runTaskTimer(plugin, this::updateBars, 0L, 20L);
    }

    public void stop() {
        if (updateTask != null) {
            updateTask.cancel();
            updateTask = null;
        }

        playerBars.values().forEach(BossBar::removeAll);
        playerBars.clear();
        joinTimes.clear();
        hiddenPlayers.clear();
    }

    public void startForPlayer(Player player) {
        if (!isEnabled()) {
            return;
        }

        UUID playerId = player.getUniqueId();
        hiddenPlayers.remove(playerId);
        joinTimes.put(playerId, System.currentTimeMillis());

        BossBar existingBar = playerBars.remove(playerId);
        if (existingBar != null) {
            existingBar.removeAll();
        }

        BossBar bossBar = Bukkit.createBossBar("", getBarColor(), getBarStyle());
        playerBars.put(playerId, bossBar);

        if (!hiddenPlayers.contains(playerId)) {
            bossBar.addPlayer(player);
        }

    }

    public void stopForPlayer(Player player) {
        UUID playerId = player.getUniqueId();
        joinTimes.remove(playerId);
        hiddenPlayers.remove(playerId);

        BossBar bossBar = playerBars.remove(playerId);
        if (bossBar != null) {
            bossBar.removePlayer(player);
            bossBar.removeAll();
        }
    }

    public boolean showForPlayer(Player player) {
        if (!isEnabled()) {
            return false;
        }

        UUID playerId = player.getUniqueId();
        hiddenPlayers.remove(playerId);

        if (!joinTimes.containsKey(playerId)) {
            joinTimes.put(playerId, System.currentTimeMillis());
        }

        BossBar bossBar = playerBars.computeIfAbsent(playerId,
                id -> Bukkit.createBossBar("", getBarColor(), getBarStyle()));

        if (!bossBar.getPlayers().contains(player)) {
            bossBar.addPlayer(player);
        }

        updateBar(player, bossBar);
        return true;
    }

    public void hideForPlayer(Player player) {
        UUID playerId = player.getUniqueId();
        hiddenPlayers.add(playerId);

        BossBar bossBar = playerBars.get(playerId);
        if (bossBar != null) {
            bossBar.removePlayer(player);
        }
    }





    private void updateBars() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            BossBar bossBar = playerBars.get(player.getUniqueId());
            if (bossBar != null) {
                updateBar(player, bossBar);
            }
        }
    }

    private void updateBar(Player player, BossBar bossBar) {
        Long joinTime = joinTimes.get(player.getUniqueId());
        if (joinTime == null) {
            return;
        }
        long elapsedSeconds = Math.max(0, (System.currentTimeMillis() - joinTime) / 1000);
        bossBar.setProgress(calculateProgress(elapsedSeconds));
        bossBar.setTitle(formatTitle(elapsedSeconds));

    }

    private double calculateProgress(long elapsedSeconds) {
        int cycleSeconds = getProgressCycleSeconds();
        if (cycleSeconds <= 0) {
            return 1.0;
        }

        long cyclePosition = elapsedSeconds % cycleSeconds;
        return Math.max(0.0, Math.min(1.0, (double) cyclePosition / cycleSeconds));
    }

    private String formatTitle(long elapsedSeconds) {
        String titleTemplate = plugin.getConfig().getString("plugin.time-bar.title", "§eSession time: §a%time%s");
        return titleTemplate.replace("%time%", String.valueOf(elapsedSeconds));
    }



    private boolean isEnabled() {
        return plugin.getConfig().getBoolean("plugin.time-bar.enabled", false);
    }

    private int getProgressCycleSeconds() {
        return plugin.getConfig().getInt("plugin.time-bar.progress-cycle-seconds", 60);
    }

    private BarColor getBarColor() {
        String colorValue = plugin.getConfig().getString("plugin.time-bar.color", "YELLOW");
        try {
            return BarColor.valueOf(colorValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BarColor.YELLOW;
        }
    }

    private BarStyle getBarStyle() {
        String styleValue = plugin.getConfig().getString("plugin.time-bar.style", "SOLID");
        try {
            return BarStyle.valueOf(styleValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BarStyle.SOLID;
        }
    }
}
