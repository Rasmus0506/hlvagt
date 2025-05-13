package com.gmail.markushygedombrowski.vagtMenu.subMenu;

import com.gmail.markushygedombrowski.HLvagt;

import com.gmail.markushygedombrowski.cooldown.VagtCooldown;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.settings.Settings;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Lon {
    private HLvagt plugin;
    private PlayerProfiles playerProfiles;
    private Settings settings;

    public Lon(HLvagt plugin, PlayerProfiles playerProfiles, Settings settings) {
        this.plugin = plugin;
        this.playerProfiles = playerProfiles;
        this.settings = settings;

    }


    public void giveLon(Player p) {
        if (p.hasPermission("vagt")) {

            PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());

            if(p.getGameMode() == org.bukkit.GameMode.CREATIVE){
                p.sendMessage(ChatColor.RED + "Du kan ikke få løn i Creative!");
                VagtCooldown.add(p.getName(), "lon", settings.getLonTime(), System.currentTimeMillis());
                return;
            }

            plugin.econ.depositPlayer(p, profile.castPropertyToInt(profile.getProperty("salary")));
            p.sendMessage(ChatColor.GRAY + "Du har fået" + ChatColor.AQUA + " Løn!");

            VagtCooldown.add(p.getName(), "lon", settings.getLonTime(), System.currentTimeMillis());


        }
    }
}



