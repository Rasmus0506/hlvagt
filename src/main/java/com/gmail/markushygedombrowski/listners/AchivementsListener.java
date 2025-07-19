package com.gmail.markushygedombrowski.listners;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class AchivementsListener implements Listener {
    private final HLvagt plugin;
    private final PlayerProfiles playerProfiles;

    public AchivementsListener(HLvagt plugin, PlayerProfiles playerProfiles) {
        this.plugin = plugin;
        this.playerProfiles = playerProfiles;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("vagt")) return;
        
        try {
            handleDeathAchievements(player);
            handleKillAchievements(player);
        } catch (InterruptedException e) {
            plugin.getLogger().warning("Fejl ved checking af achievement ved join: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        if (!victim.hasPermission("vagt")) return;
        
        Player killer = victim.getKiller();
        
        try {
            handleDeathAchievements(victim);
            
            if (killer != null && killer.hasPermission("vagt")) {
                handleKillAchievements(killer);
            }
        } catch (InterruptedException e) {
            plugin.getLogger().warning("Fejl ved checking af achievement: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ... resten af metoderne forbliver uændrede ...
private void handleDeathAchievements(Player player) throws InterruptedException {
    PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());
        int deaths = player.getStatistic(Statistic.DEATHS);

        checkSingleDeathAchievement(player, profile, deaths, 1, 0.05);

        for (int i = 2; i <= 10; i++) {
            double penalty = 0.05 + ((i - 1) * 0.02);
            checkSingleDeathAchievement(player, profile, deaths, i, penalty);
        }

        for (int i = 15; i <= 100; i += 5) {
            double penalty = 0.25 + ((i - 15) / 5.0) * 0.05;
            checkSingleDeathAchievement(player, profile, deaths, i, penalty);
        }

        for (int i = 125; i <= 250; i += 25) {
            double penalty = 1.0 + ((i - 125) / 25.0) * 0.15;
            checkSingleDeathAchievement(player, profile, deaths, i, penalty);
        }

        for (int i = 300; i <= 500; i += 50) {
            double penalty = 1.80 + ((i - 300) / 50.0) * 0.25;
            checkSingleDeathAchievement(player, profile, deaths, i, penalty);
        }
    }


    private void handleKillAchievements(Player player) throws InterruptedException {
        PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());
        int kills = player.getStatistic(Statistic.PLAYER_KILLS);

        checkSingleKillAchievement(player, profile, kills, 1, 0.05);

        for (int i = 2; i <= 10; i++) {
            double bonus = 0.05 + ((i - 1) * 0.02);
            checkSingleKillAchievement(player, profile, kills, i, bonus);
        }

        for (int i = 15; i <= 100; i += 5) {
            double bonus = 0.25 + ((i - 15) / 5.0) * 0.05;
            checkSingleKillAchievement(player, profile, kills, i, bonus);
        }

        for (int i = 125; i <= 250; i += 25) {
            double bonus = 1.0 + ((i - 125) / 25.0) * 0.15;
            checkSingleKillAchievement(player, profile, kills, i, bonus);
        }

        for (int i = 300; i <= 600; i += 50) {
            double bonus = 1.0 + ((i - 150) / 50.0) * 0.25;
            checkSingleKillAchievement(player, profile, kills, i, bonus);
        }
        for (int i = 1000; i <= 800; i += 60) {
            double bonus = 1.0 + ((i - 1000) / 60.0) * 0.30;
            checkSingleKillAchievement(player, profile, kills, i, bonus);
        }
    }

private void checkSingleDeathAchievement(Player p, PlayerProfile profile, int deaths, int required, double penaltyPercent) throws InterruptedException {
    String achievementKey = "achievement_death_" + required;
    if (deaths >= required && !profile.hasProperty(achievementKey)) {
        profile.setProperty(achievementKey, true);
        profile.setProperty(achievementKey + "_penalty", String.valueOf(penaltyPercent));
        try {
            playerProfiles.save(profile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Send besked til spilleren
        p.sendMessage("§c☠ Achievement opnået: §7Dø " + required + " gang(e)");
        p.sendMessage("§7Du har fået en straf på §c-" + String.format("%.2f", penaltyPercent) + "% §7af din løn");
        
        // Log achievement
        plugin.getLogger().info(p.getName() + " har opnået death achievement: " + achievementKey);
    }
}

private void checkSingleKillAchievement(Player p, PlayerProfile profile, int kills, int required, double bonusPercent) throws InterruptedException {
    String achievementKey = "achievement_kill_" + required;
    if (kills >= required && !profile.hasProperty(achievementKey)) {
        profile.setProperty(achievementKey, true);
        profile.setProperty(achievementKey + "_bonus", String.valueOf(bonusPercent));
        try {
            playerProfiles.save(profile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Send besked til spilleren
        p.sendMessage("§a☠ Achievement opnået: §7Dræb " + required + " spiller(e)");
        p.sendMessage("§7Du har fået en bonus på §a+" + String.format("%.2f", bonusPercent) + "% §7til din løn");
        
        // Log achievement
        plugin.getLogger().info(p.getName() + " har opnået kill achievement: " + achievementKey);
    }
}




}