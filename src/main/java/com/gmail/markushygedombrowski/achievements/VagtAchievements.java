package com.gmail.markushygedombrowski.achievements;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.utils.Logger;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public class VagtAchievements {
    private final HLvagt plugin;
    private final PlayerProfiles playerProfiles;
    private final Logger logger;

    public VagtAchievements(HLvagt plugin, PlayerProfiles playerProfiles, Logger logger) {
        this.plugin = plugin;
        this.playerProfiles = playerProfiles;
        this.logger = logger;
    }

    public void checkDeathAchievement(Player p) {
        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
        int deaths = p.getStatistic(Statistic.DEATHS);

        // Tjek for forskellige death achievements
        if (deaths >= 1 && !profile.hasProperty("achievement_death_1")) {
            applyDeathPenalty(p, profile, 500, false);
            profile.setProperty("achievement_death_1", true);
            logger.notify("achievements", p.getName() + " opnåede death achievement 1");
        }
        
        if (deaths >= 5 && !profile.hasProperty("achievement_death_5")) {
            applyDeathPenalty(p, profile, 2500, false);
            profile.setProperty("achievement_death_5", true);
            logger.notify("achievements", p.getName() + " opnåede death achievement 5");
        }

        if (deaths >= 10 && !profile.hasProperty("achievement_death_10")) {
            applyDeathPenalty(p, profile, 15, true);
            profile.setProperty("achievement_death_10", true);
            logger.notify("achievements", p.getName() + " opnåede death achievement 10");
        }
    }


    private void applyDeathPenalty(Player player, PlayerProfile profile, double amount, boolean isPercentage) {
        double penalty;
        double currentBalance = plugin.econ.getBalance(player);

        if (isPercentage) {
            penalty = currentBalance * (amount / 100.0);
        } else {
            penalty = amount;
        }

        plugin.econ.withdrawPlayer(player, penalty);
        sendPenaltyMessage(player, amount, isPercentage);
    }

    private void sendPenaltyMessage(Player player, double amount, boolean isPercentage) {
        String penaltyText = isPercentage ? amount + "%" : "$" + amount;
        player.sendMessage("§c☠ Du har mistet " + penaltyText + " for at dø!");
    }
}