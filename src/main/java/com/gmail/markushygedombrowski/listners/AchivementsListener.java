package com.gmail.markushygedombrowski.listners;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class AchivementsListener implements Listener {
    private final HLvagt plugin;

    public AchivementsListener(HLvagt plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            Player player = event.getPlayer();
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
        Player killer = victim.getKiller();
        
        try {
            handleDeathAchievements(victim);
            
            if (killer != null) {
                handleKillAchievements(killer);
            }
        } catch (InterruptedException e) {
            plugin.getLogger().warning("Fejl ved checking af achievement: " + e.getMessage());
            e.printStackTrace();
        }
    }

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

        for (int i = 300; i <= 500; i += 50) {
            double bonus = 1.80 + ((i - 300) / 50.0) * 0.25;
            checkSingleKillAchievement(player, profile, kills, i, bonus);
        }
    }

    private void checkSingleDeathAchievement(Player p, PlayerProfile profile, int deaths, int required, double penaltyPercent) throws InterruptedException {
        String achievementKey = "achievement_death_" + required;
        if (deaths >= required && !profile.hasProperty(achievementKey)) {
            profile.setProperty(achievementKey, true);
            profile.setProperty(achievementKey + "_penalty", String.valueOf(penaltyPercent));
            plugin.getLogger().info("Death achievement unlocked for " + p.getName() + ": " + achievementKey);
        }
    }

    private void checkSingleKillAchievement(Player p, PlayerProfile profile, int kills, int required, double bonusPercent) throws InterruptedException {
        String achievementKey = "achievement_kill_" + required;
        if (kills >= required && !profile.hasProperty(achievementKey)) {
            profile.setProperty(achievementKey, true);
            profile.setProperty(achievementKey + "_bonus", String.valueOf(bonusPercent));
            plugin.getLogger().info("Kill achievement unlocked for " + p.getName() + ": " + achievementKey);
        }
    }
}