package com.gmail.markushygedombrowski.achievements;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class VagtAchievements {
    private final HLvagt plugin;
    private final PlayerProfiles playerProfiles;
    private final Logger logger;

    public VagtAchievements(HLvagt plugin, PlayerProfiles playerProfiles, Logger logger) {
        this.plugin = plugin;
        this.playerProfiles = playerProfiles;
        this.logger = logger;
    }

    public void checkDeathAchievement(Player p) throws InterruptedException {
        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
        int deaths = p.getStatistic(Statistic.DEATHS);

        checkSingleDeathAchievement(p, profile, deaths, 1, 0.05);

        for (int i = 2; i <= 10; i++) {
            double penalty = 0.05 + ((i - 1) * 0.02);
            checkSingleDeathAchievement(p, profile, deaths, i, penalty);
        }

        for (int i = 15; i <= 100; i += 5) {
            double penalty = 0.25 + ((i - 15) / 5.0) * 0.05;
            checkSingleDeathAchievement(p, profile, deaths, i, penalty);
        }

        for (int i = 125; i <= 250; i += 25) {
            double penalty = 1.0 + ((i - 125) / 25.0) * 0.15;
            checkSingleDeathAchievement(p, profile, deaths, i, penalty);
        }

        for (int i = 150; i <= 300; i += 50) {
            double penalty = 1.0 + ((i - 150) / 50.0) * 0.25;
            checkSingleDeathAchievement(p, profile, deaths, i, penalty);
        }
    }

    private void checkSingleDeathAchievement(Player p, PlayerProfile profile, int deaths, int required, double penaltyPercent) throws InterruptedException {
        String achievementKey = "achievement_death_" + required;
        if (deaths >= required && !profile.hasProperty(achievementKey)) {
            profile.setProperty(achievementKey, true);
            profile.setProperty(achievementKey + "_penalty", String.valueOf(penaltyPercent));
            profile.wait(); // Gem ændringerne med det samme
            logger.notify();
        }
    }

    public double calculateTotalSalaryPenalty(PlayerProfile profile) {
        double totalPenalty = 0.0;

        for (int i = 1; i <= 10; i++) {
            String key = "achievement_death_" + i;
            if (profile.hasProperty(key) && profile.hasProperty(key + "_penalty")) {
                try {
                    double penalty = Double.parseDouble(String.valueOf(profile.getProperty(key + "_penalty")));
                    totalPenalty += penalty;
                } catch (NumberFormatException e) {
                    try {
                        logger.wait(Long.parseLong("Fejl ved parsing af penalty for " + key + ": " + e.getMessage()));
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }

        for (int i = 15; i <= 100; i += 5) {
            String key = "achievement_death_" + i;
            if (profile.hasProperty(key) && profile.hasProperty(key + "_penalty")) {
                try {
                    double penalty = Double.parseDouble(String.valueOf(profile.getProperty(key + "_penalty")));
                    totalPenalty += penalty;
                } catch (NumberFormatException e) {
                    try {
                        logger.wait(Long.parseLong("Fejl ved parsing af penalty for " + key + ": " + e.getMessage()));
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }

        for (int i = 125; i <= 250; i += 25) {
            String key = "achievement_death_" + i;
            if (profile.hasProperty(key) && profile.hasProperty(key + "_penalty")) {
                try {
                    double penalty = Double.parseDouble(String.valueOf(profile.getProperty(key + "_penalty")));
                    totalPenalty += penalty;
                } catch (NumberFormatException e) {
                    try {
                        logger.wait(Long.parseLong("Fejl ved parsing af penalty for " + key + ": " + e.getMessage()));
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        }

        for (int i = 150; i <= 300; i += 50) {
            String key = "achievement_death_" + i;
            if (profile.hasProperty(key) && profile.hasProperty(key + "_penalty")) {
                try {
                    double penalty = Double.parseDouble(String.valueOf(profile.getProperty(key + "_penalty")));
                    totalPenalty += penalty;
                } catch (NumberFormatException e) {
                    try {
                        logger.wait(Long.parseLong("Fejl ved parsing af penalty for " + key + ": " + e.getMessage()));
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        }

        return Math.min(totalPenalty, 100.0);
    }
    public void checkKillAchievement(Player p) throws InterruptedException {
        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
        int kills = p.getStatistic(Statistic.PLAYER_KILLS);

        checkSingleKillAchievement(p, profile, kills, 1, 0.05);

        for (int i = 2; i <= 10; i++) {
            double bonus = 0.05 + ((i - 1) * 0.02);
            checkSingleKillAchievement(p, profile, kills, i, bonus);
        }

        for (int i = 15; i <= 100; i += 5) {
            double bonus = 0.25 + ((i - 15) / 5.0) * 0.05;
            checkSingleKillAchievement(p, profile, kills, i, bonus);
        }

        for (int i = 125; i <= 250; i += 25) {
            double bonus = 1.0 + ((i - 125) / 25.0) * 0.15;
            checkSingleKillAchievement(p, profile, kills, i, bonus);
        }

        for (int i = 150; i <= 300; i += 50) {
            double bonus = 1.0 + ((i - 150) / 50.0) * 0.25;
            checkSingleKillAchievement(p, profile, kills, i, bonus);
        }
    }

    private void checkSingleKillAchievement(Player p, PlayerProfile profile, int kills, int required, double bonusPercent) throws InterruptedException {
        String achievementKey = "achievement_kill_" + required;
        if (kills >= required && !profile.hasProperty(achievementKey)) {
            profile.setProperty(achievementKey, true);
            profile.setProperty(achievementKey + "_bonus", String.valueOf(bonusPercent));
            profile.wait();
            logger.notify();
        }
    }

    public double calculateTotalSalaryBonus(PlayerProfile profile) {
        double totalBonus = 0.0;
        

        for (int i = 1; i <= 10; i++) {
            String property = "achievement_kill_" + i;
            if (profile.hasProperty(property)) {
                String bonusProperty = property + "_bonus";
                String bonusValue = profile.getProperty(bonusProperty).toString();
                if (bonusValue != null) {
                    try {
                        totalBonus += Double.parseDouble(bonusValue);
                    } catch (NumberFormatException e) {
                        plugin.getLogger().warning("Ugyldig bonus værdi for " + bonusProperty + ": " + bonusValue);
                    }
                }
            }
        }
        

        for (int i = 15; i <= 100; i += 5) {
            String property = "achievement_kill_" + i;
            if (profile.hasProperty(property)) {
                String bonusProperty = property + "_bonus";
                String bonusValue = profile.getProperty(bonusProperty).toString();
                if (bonusValue != null) {
                    try {
                        totalBonus += Double.parseDouble(bonusValue);
                    } catch (NumberFormatException e) {
                        plugin.getLogger().warning("Ugyldig bonus værdi for " + bonusProperty + ": " + bonusValue);
                    }
                }
            }
        }
        

        for (int i = 125; i <= 250; i += 25) {
            String property = "achievement_kill_" + i;
            if (profile.hasProperty(property)) {
                String bonusProperty = property + "_bonus";
                String bonusValue = profile.getProperty(bonusProperty).toString();
                if (bonusValue != null) {
                    try {
                        totalBonus += Double.parseDouble(bonusValue);
                    } catch (NumberFormatException e) {
                        plugin.getLogger().warning("Ugyldig bonus værdi for " + bonusProperty + ": " + bonusValue);
                    }
                }
            }
        }
        

        for (int i = 300; i <= 500; i += 50) {
            String property = "achievement_kill_" + i;
            if (profile.hasProperty(property)) {
                String bonusProperty = property + "_bonus";
                String bonusValue = profile.getProperty(bonusProperty).toString();
                if (bonusValue != null) {
                    try {
                        totalBonus += Double.parseDouble(bonusValue);
                    } catch (NumberFormatException e) {
                        plugin.getLogger().warning("Ugyldig bonus værdi for " + bonusProperty + ": " + bonusValue);
                    }
                }
            }
        }
        
        return totalBonus;
    }
private static class VagtAchievement {
    private final String property;
    private final String description;
    private final int requiredAmount;
    private final double percentModifier;

    public VagtAchievement(String property, String description, int requiredAmount, double percentModifier) {
        this.property = property;
        this.description = description;
        this.requiredAmount = requiredAmount;
        this.percentModifier = percentModifier;
    }
}
private List<VagtAchievement> initializeDamageAchievements() {
    List<VagtAchievement> achievementList = new ArrayList<>();
    double currentBonus = 0.001;
    
    // Start med 5 damage og forøg med 15
    for (int damage = 5; damage <= 500; damage += 15) {
        achievementList.add(new VagtAchievement(
            "achievement_damage_" + damage,
            "Giv " + damage + " damage",
            damage,
            currentBonus
        ));
        // Fordobl bonus for hvert niveau
        currentBonus *= 2;
    }
    
    return achievementList;
}
}