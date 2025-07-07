package com.gmail.markushygedombrowski.cooldown;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.Lon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class VagtCooldown {
    private Lon lon;
    private static File cooldownFile;
    private static FileConfiguration cooldownConfig;
    public static HashMap<String, VagtAbilityCooldown> cooldownPlayers = new HashMap<>();

    public VagtCooldown(Lon lon) {
        this.lon = lon;
        setupFiles();
        loadCooldowns();
    }

    private void setupFiles() {
        cooldownFile = new File(HLvagt.getInstance().getDataFolder(), "cooldowns.yml");
        if (!cooldownFile.exists()) {
            try {
                cooldownFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cooldownConfig = YamlConfiguration.loadConfiguration(cooldownFile);
    }

    private void loadCooldowns() {
        if (cooldownConfig.contains("cooldowns")) {
            for (String player : cooldownConfig.getConfigurationSection("cooldowns").getKeys(false)) {
                if (cooldownConfig.contains("cooldowns." + player)) {
                    for (String ability : cooldownConfig.getConfigurationSection("cooldowns." + player).getKeys(false)) {
                        long endTime = cooldownConfig.getLong("cooldowns." + player + "." + ability + ".endTime");
                        long duration = cooldownConfig.getLong("cooldowns." + player + "." + ability + ".duration");

                        if (endTime > System.currentTimeMillis()) {
                            if (!cooldownPlayers.containsKey(player)) {
                                cooldownPlayers.put(player, new VagtAbilityCooldown(player));
                            }
                            cooldownPlayers.get(player).cooldownMap.put(ability,
                                    new VagtAbilityCooldown(player, duration, System.currentTimeMillis() - (endTime - duration)));
                        }
                    }
                }
            }
        }
    }

    public static void saveCooldowns() {
        cooldownConfig.set("cooldowns", null);
        for (String player : cooldownPlayers.keySet()) {
            for (String ability : cooldownPlayers.get(player).cooldownMap.keySet()) {
                VagtAbilityCooldown cooldown = cooldownPlayers.get(player).cooldownMap.get(ability);
                long endTime = cooldown.systime + cooldown.seconds;
                if (endTime > System.currentTimeMillis()) {
                    cooldownConfig.set("cooldowns." + player + "." + ability + ".endTime", endTime);
                    cooldownConfig.set("cooldowns." + player + "." + ability + ".duration", cooldown.seconds);
                }
            }
        }
        try {
            cooldownConfig.save(cooldownFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void add(String player, String ability, long seconds, long systime) {
        if (!cooldownPlayers.containsKey(player)) cooldownPlayers.put(player, new VagtAbilityCooldown(player));
        if (isCooling(player, ability)) return;
        cooldownPlayers.get(player).cooldownMap.put(ability, new VagtAbilityCooldown(player, seconds * 1000, System.currentTimeMillis()));
        saveCooldowns();
    }


    public void handleCooldowns() {
        if (cooldownPlayers == null || cooldownPlayers.isEmpty()) {
            return;
        }
        new HashMap<>(cooldownPlayers).forEach((player, abilityCooldown) -> {
            if (abilityCooldown == null || abilityCooldown.cooldownMap == null || abilityCooldown.cooldownMap.isEmpty()) {
                return;
            }
            abilityCooldown.cooldownMap.entrySet().stream()
                    .filter(vagt -> getRemaining(player, vagt.getKey()) <= 0.0)
                    .forEach((cooldownEntry) -> {
                        removeCooldownLon(player, cooldownEntry.getKey());
                        saveCooldowns();
                    });
        });
    }

    public static double getRemaining(String player, String ability) {
        if (!cooldownPlayers.containsKey(player)) return 0.0;
        if (!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) return 0.0;

        VagtAbilityCooldown cooldown = cooldownPlayers.get(player).cooldownMap.get(ability);
        long endTime = cooldown.systime + cooldown.seconds;
        long currentTime = System.currentTimeMillis();

        if (currentTime >= endTime) {
            cooldownPlayers.get(player).cooldownMap.remove(ability);
            return 0.0;
        }

        double remainingSeconds = (endTime - currentTime) / 1000.0; // Konverter til sekunder
        return remainingSeconds / 60.0; // Konverter til minutter
    }

    public static void coolDurMessage(Player player, String ability) {
        if (player == null) {
            return;
        }
        if (!isCooling(player.getName(), ability)) {
            return;
        }
        if (ability.equalsIgnoreCase("lon")) {
            double totalSeconds = getRemaining(player.getName(), ability) * 60; // Konverter minutter til sekunder
            int wholeMinutes = (int) (totalSeconds / 60);
            int seconds = (int) (totalSeconds % 60);
            player.sendMessage(ChatColor.GRAY + " du får løn om " + ChatColor.AQUA + getRemaining(player.getName(), ability) + " Minuter");
        }
    }

    public static boolean isCooling(String player, String ability) {
        if (!cooldownPlayers.containsKey(player)) return false;
        if (!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) return false;

        VagtAbilityCooldown cooldown = cooldownPlayers.get(player).cooldownMap.get(ability);
        long endTime = cooldown.systime + cooldown.seconds;
        return System.currentTimeMillis() < endTime;

    }

    public static boolean removeCooldown(String player, String ability) {
        if (!cooldownPlayers.containsKey(player)) {
            return false;
        }
        if (!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) {
            return false;
        }
        cooldownPlayers.get(player).cooldownMap.remove(ability);
        Player cPlayer = Bukkit.getPlayer(player);

        return false;
    }


    public void removeCooldownLon(String player, String ability) {
        if (!cooldownPlayers.containsKey(player)) {
            return;
        }
        if (!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) {
            return;
        }

        Player cPlayer = Bukkit.getPlayer(player);
        if (cPlayer != null) {
            if (ability.equalsIgnoreCase("lon")) {
                cooldownPlayers.get(player).cooldownMap.remove(ability);
                lon.giveLon(cPlayer);
            } else {
                cooldownPlayers.get(player).cooldownMap.remove(ability);
            }
        }


    }
}