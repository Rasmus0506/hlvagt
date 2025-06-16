package com.gmail.markushygedombrowski.abilities;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class GlowNearbyPlayers {

    private final PlayerProfiles playerProfiles; // Assume this is initialized elsewhere
    private final HLvagt plugin;

    public GlowNearbyPlayers(PlayerProfiles playerProfiles, HLvagt plugin) {
        this.playerProfiles = playerProfiles;
        this.plugin = plugin;
    }

    public void startGlowingTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                if (Bukkit.getOnlinePlayers().isEmpty()) return;
                for (Player player : players) {
                    if (!player.hasPermission("vagt") && !player.hasPermission("vagt.ability2")) return;
                    PlayerProfile profile = playerProfiles.getPlayerProfile(player.getUniqueId());


                    player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10).stream()
                            .filter(entity -> entity instanceof Player && entity != player)
                            .forEach(entity -> (player).playEffect(entity.getLocation(), Effect.GHAST_SHOOT, 0));
                }

            }

        }.runTaskTimer(plugin, 0L, 20L); // Runs every second (20 ticks)
    }
}