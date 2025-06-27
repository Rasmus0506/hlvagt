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

private List<VagtAchievement> initializeKillAchievements() {
    List<VagtAchievement> achievements = new ArrayList<>();
    
    // De første 10 achievements (1-10 kills)
    for (int i = 1; i <= 10; i++) {
        double bonus = 0.05 + ((i - 1) * 0.02);
        achievements.add(new VagtAchievement(
            "achievement_kill_" + i,
            "Dræb " + i + " spiller" + (i == 1 ? "" : "e"),
            i,
            bonus
        ));
    }
    
    // Fra 15 til 100 med spring på 5
    for (int i = 15; i <= 100; i += 5) {
        double bonus = 0.25 + ((i - 15) / 5.0) * 0.05;
        achievements.add(new VagtAchievement(
            "achievement_kill_" + i,
            "Dræb " + i + " spillere",
            i,
            bonus
        ));
    }
    
    return achievements;
}

private void checkSingleKillAchievement(Player p, PlayerProfile profile, int kills, int required, double bonusPercent) {
    String achievementKey = "achievement_kill_" + required;
    if (kills >= required && !profile.hasProperty(achievementKey)) {
        profile.setProperty(achievementKey, true);
        profile.setProperty(achievementKey + "_bonus", String.valueOf(bonusPercent));
        // Fjern wait() og notify() kald
        p.sendMessage("§aNyt Achievement: §6Dræbte " + required + " spillere!");
        p.sendMessage("§7Du har fået §a+" + bonusPercent + "% §7bonus til din løn!");
    }
}

public double calculateTotalSalaryBonus(PlayerProfile profile) {
    if (profile == null) return 0.0;
    
    double totalBonus = 0.0;
    
    // Check kill achievements
    for (VagtAchievement achievement : initializeKillAchievements()) {
        String property = achievement.property;
        String bonusProperty = property + "_bonus";
        
        if (profile.hasProperty(property) && profile.hasProperty(bonusProperty)) {
            try {
                String bonusValue = profile.getProperty(bonusProperty).toString();
                if (bonusValue != null) {
                    totalBonus += Double.parseDouble(bonusValue);
                }
            } catch (NumberFormatException e) {
                logger.warning("Ugyldig bonus værdi for achievement: " + property);
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
    List<VagtAchievement> achievements = new ArrayList<>();
    achievements.add(new VagtAchievement("damage_achievement_1000", "Deal 1000 damage", 1000, 5.0));
    achievements.add(new VagtAchievement("damage_achievement_5000", "Deal 5000 damage", 5000, 10.0));
    achievements.add(new VagtAchievement("damage_achievement_10000", "Deal 10000 damage", 10000, 15.0));
    return achievements;
}
private void checkSingleDamageAchievement(Player p, PlayerProfile profile, int damage, int required, double bonusPercent) throws InterruptedException {
    String achievementProperty = "damage_achievement_" + required;
    
    if (damage >= required && !profile.hasProperty(achievementProperty)) {
        profile.setProperty(achievementProperty, "true");
        profile.setProperty(achievementProperty + "_bonus", String.valueOf(bonusPercent));
        p.sendMessage("§aNew Achievement: §6Dealt " + required + " damage!");
        p.sendMessage("§7Du har fået §a+" + bonusPercent + "% §7bonus til din løn!");
    }
}
}