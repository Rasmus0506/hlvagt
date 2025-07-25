package com.gmail.markushygedombrowski.achievements;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
       // rangeCheck(p,profile,deaths,150,300,50,"deaths");
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

        for (int i = 300; i <= 600; i += 50) {
            double bonus = 1.0 + ((i - 150) / 50.0) * 0.25;
            checkSingleKillAchievement(p, profile, kills, i, bonus);
        }
        for (int i = 1000; i <= 800; i += 60) {
            double bonus = 1.0 + ((i - 1000) / 60.0) * 0.30;
            checkSingleKillAchievement(p, profile, kills, i, bonus);
        }
    }

    private void checkSingleDeathAchievement(Player p, PlayerProfile profile, int deaths, int required, double penaltyPercent) throws InterruptedException {
        String achievementKey = "achievement_death_" + required;
        if (deaths >= required && !profile.hasProperty(achievementKey)) {
            profile.setProperty(achievementKey, true);
            profile.setProperty(achievementKey + "_penalty", String.valueOf(penaltyPercent));
        }
    }
    private void rangeCheck(Player p, PlayerProfile profile, int amount, int start, int total, int upAmount, String type) throws InterruptedException{
        for (int i = start; i <= total; i += upAmount) {
            if(type.equalsIgnoreCase("deaths")) {
                double penalty = 0.25 + ((i - 15) / 5.0) * 0.05;
                checkSingleDeathAchievement(p, profile, amount, i, penalty);
            } else if (type.equalsIgnoreCase("kills")) {
                double bonus = 0.05 + ((i - 1) * 0.02);
                checkSingleKillAchievement(p, profile, amount, i, bonus);
            }

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



    private List<VagtAchievement> initializeKillAchievements() {
        List<VagtAchievement> achievements = new ArrayList<>();

        // De første 10 achievements (1-10 kills)
        for (int i = 1; i <= 10; i++) {
            double bonus = 0.05 + ((i - 1) * 0.02);
            achievements.add(new VagtAchievement(
                    "achievement_kill_" + i,
                    "Dræb " + i + " spiller" + (i == 1 ? "" : "e"),
                    i,
                    bonus,
                    "kill"
            ));
        }

        // Fra 15 til 100 med spring på 5
        for (int i = 15; i <= 100; i += 5) {
            double bonus = 0.25 + ((i - 15) / 5.0) * 0.05;
            achievements.add(new VagtAchievement(
                    "achievement_kill_" + i,
                    "Dræb " + i + " spillere",
                    i,
                    bonus,
                    "kill"
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
        double totalBonus = 0.0;
        // Kill achievements
        for (int i = 1; i <= 1000; i++) {
            String bonusKey = "achievement_kill_" + i + "_bonus";
            if (profile.hasProperty(bonusKey)) {
                totalBonus += Double.parseDouble((String) profile.getProperty(bonusKey));
            }
        }
        return totalBonus;
    }

    private static class VagtAchievement {
        private final String property;
        private final String description;
        private final int requiredDeaths;
        private final double penaltyPercent;
        private final int requiredAmount;
        private final double percentModifier;
        private final String type;

        public VagtAchievement(String property, String description, int requiredAmount, double modifier, String type) {
            this.property = property;
            this.description = description;
            this.requiredDeaths = requiredAmount;
            this.penaltyPercent = modifier;
            this.requiredAmount = requiredAmount;
            this.percentModifier = modifier;
            this.type = type;
        }
    }


}