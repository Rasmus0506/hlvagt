package com.gmail.markushygedombrowski.vagtMenu.subMenu;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.cooldown.VagtCooldown;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Lon {
    private final HLvagt plugin;
    private final Settings settings;
    private int taskId;

    public Lon(HLvagt plugin, PlayerProfiles playerProfiles, Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
        startCooldownTimer();
    }

    private void startCooldownTimer() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("vagt") && VagtCooldown.isCooling(player.getName(), "lon")) {
                    double remaining = VagtCooldown.getRemaining(player.getName(), "lon");

                    if (remaining <= 0) {
                        VagtCooldown.removeCooldown(player.getName(), "lon");
                        player.sendMessage(ChatColor.GREEN + "Du kan nu få din vagtløn igen!");
                    }
                }
            }
        }, 20L, 20L); // Kør hvert sekund (20 ticks)
    }

    public void stopTimer() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }

    public void giveLon(Player p) {

        if (p == null) {
            return;
        }


        if (!p.hasPermission("vagt")) {
            return;
        }


        if (VagtCooldown.isCooling(p.getName(), "lon")) {
            double remaining = VagtCooldown.getRemaining(p.getName(), "lon");
            int minutes = (int) (remaining / 60);
            int seconds = (int) (remaining % 60);
            p.sendMessage(ChatColor.RED + "Du skal vente " + ChatColor.YELLOW + minutes + " minutter og " + seconds + " sekunder" + ChatColor.RED + " før du kan få løn igen!");
            return;
        }


        PlayerProfile profile = plugin.getPlayerProfile(p.getUniqueId());
        if (profile == null) {
            p.sendMessage(ChatColor.RED + "Der opstod en fejl ved indlæsning af din profil!");
            return;
        }


        if (p.getGameMode() == GameMode.CREATIVE) {
            p.sendMessage(ChatColor.RED + "Du kan ikke få løn i Creative!");
            VagtCooldown.add(p.getName(), "lon", settings.getLonTime(), System.currentTimeMillis());
            return;
        }


        double basisLon = profile.castPropertyToInt(profile.getProperty("salary"));
        if (basisLon <= 0) {
            p.sendMessage(ChatColor.RED + "Der er en fejl med din basis-løn!");
            return;
        }

        double penaltyPercent = plugin.getVagtAchievements().calculateTotalSalaryPenalty(profile);
        double bonusPercent = plugin.getVagtAchievements().calculateTotalSalaryBonus(profile);

        double finalLon = basisLon * (1 - (penaltyPercent / 100.0)) * (1 + (bonusPercent / 100.0));


        if (finalLon <= 0) {
            p.sendMessage(ChatColor.RED + "Der opstod en fejl ved beregning af din løn!");
            return;
        }


        plugin.econ.depositPlayer(p, finalLon);
        

        StringBuilder message = new StringBuilder();
        message.append(ChatColor.GREEN).append("Du har fået din vagtløn på ").append(ChatColor.GOLD).append("$").append(String.format("%.2f", finalLon)).append(ChatColor.GREEN).append("!");
        
        if (penaltyPercent > 0) {
            message.append("\n").append(ChatColor.RED).append("(-").append(String.format("%.2f", penaltyPercent))
                    .append("% pga. døds-achievements)");
        }
        if (bonusPercent > 0) {
            message.append("\n").append(ChatColor.GREEN).append("(+").append(String.format("%.2f", bonusPercent))
                    .append("% bonus for kills/damage)");
        }
        
        p.sendMessage(message.toString());


        VagtCooldown.add(p.getName(), "lon", settings.getLonTime(), System.currentTimeMillis());
    }
}