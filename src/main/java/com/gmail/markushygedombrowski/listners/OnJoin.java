package com.gmail.markushygedombrowski.listners;

import com.gmail.markushygedombrowski.levels.LevelRewards;
import com.gmail.markushygedombrowski.levels.LevelUpEvent;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.cooldown.VagtCooldown;
import com.gmail.markushygedombrowski.settings.Settings;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OnJoin implements Listener {
    private Map<UUID, Integer> playerCooldownTime = new HashMap<>();
    private PlayerProfiles playerProfiles;
    private Settings settings;
    private Integer time = 0;
    private LevelRewards levelRewards;

    public OnJoin(PlayerProfiles playerProfiles, Settings settings, LevelRewards levelRewards) {
        this.playerProfiles = playerProfiles;
        this.settings = settings;

        this.levelRewards = levelRewards;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        if (!p.hasPermission("Vagt")) {
            return;
        }
        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
        try {
            playerProfiles.createVagt(p, profile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (profile.getProperty("buff") == null) {
            int buff = 0;
            profile.setProperty("buff", buff);
        }
        levelRewards.updatePlayerLevel(p, profile);
        addPlayerToCooldown(p);
        int level = VagtUtils.castPropertyToInt(profile.getProperty("level"));
        int exp = VagtUtils.castPropertyToInt(profile.getProperty("exp"));

        LevelUpEvent levelUpEvent = new LevelUpEvent(false, p, level, profile, exp);
        Bukkit.getPluginManager().callEvent(levelUpEvent);

    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        if (!p.hasPermission("vagt")) return;
        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
        if (profile == null) return;
        event.setFormat("§7[§b" + profile.getProperty("level") + "§7] §7" + p.getDisplayName() + "§8: §f" + event.getMessage());

    }

    private void addPlayerToCooldown(Player p) {
        if (VagtCooldown.isCooling(p.getName(), "lon")) {
            return;
        }
        setCooldownTime(p);

        VagtCooldown.add(p.getName(), "lon", playerCooldownTime.get(p.getUniqueId()), System.currentTimeMillis());
    }

    private void setCooldownTime(Player p) {
        if (time == 0) {
            time = settings.getLonTime();
        }
        putPlayerInPlayerMap(p);
    }

    private void putPlayerInPlayerMap(Player p) {
        if (!playerCooldownTime.containsKey(p.getUniqueId())) {
            playerCooldownTime.put(p.getUniqueId(), time);
        }
    }


    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (!p.hasPermission("vagt")) return;
        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());

        if (profile == null) return;

        if (!VagtCooldown.isCooling(p.getName(), "lon")) {
            return;
        }

        time = (int) (VagtCooldown.getRemaining(p.getName(), "lon") * 60);
        VagtCooldown.removeCooldown(p.getName(), "lon");
        playerCooldownTime.replace(p.getUniqueId(), time);
        try {
            playerProfiles.save(profile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


}
