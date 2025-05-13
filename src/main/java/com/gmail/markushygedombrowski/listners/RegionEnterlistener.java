package com.gmail.markushygedombrowski.listners;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.panikrum.PanikRum;
import com.gmail.markushygedombrowski.panikrum.PanikRumManager;
import com.gmail.markushygedombrowski.utils.Logger;
import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.mewin.WGRegionEvents.events.RegionLeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class RegionEnterlistener implements Listener {

    private Logger logger;
    private PanikRumManager panikRumManager;
    private HashMap<Player, Integer> panikrum = new HashMap<>();
    private HashMap<Player, PanikRum> tploc = new HashMap<>();

    public RegionEnterlistener(Logger logger, PanikRumManager panikRumManager) {
        this.logger = logger;
        this.panikRumManager = panikRumManager;
    }

    @EventHandler
    public void onRegionEnter(RegionEnterEvent event) {
        Player p = event.getPlayer();

        if (p.hasPermission("vagt.fceller")) {
            if (event.getRegion().getId().contains("a-") || event.getRegion().getId().contains("b-") || event.getRegion().getId().contains("c-")) {
                p.sendMessage("§4Du må ikke gå ind i celler som vagt!");
                p.sendMessage("§4Dette er blevet logget!");
                logger.formatMessage(p.getName() + " har forsøgt og gå ind i en celle!", "VAGTCELLER", "vagtcellelog");
                logger.formatMessage("CELLE: " + event.getRegion().getId(), "VAGTCELLER", "vagtcellelog");
                logger.formatMessage("WORLD: " + p.getWorld().getName(), "VAGTCELLER", "vagtcellelog");
            }
        }
        if (p.hasPermission("vagt")) {
            PanikRum panikRum = panikRumManager.getPanikRum(event.getRegion().getId());
            if (panikRum != null) {
                p.sendMessage("§cDu er gået ind i et §c§lPanikrum!");
                p.sendMessage("§cOm 3 minutter vil du blive teleporteret ud!");
                tploc.put(p, panikRum);
                panikrum.put(p,180);
            }

        }
    }

    public void panikrumTimer() {
        if (panikrum.isEmpty()) return;

        panikrum.forEach((k, v) -> {
            if (v <= 0) {
                k.sendMessage("§cDu er blevet teleporteret ud af panikrummet!");
                k.teleport(tploc.get(k).getLocation());
                panikrum.remove(k);
                tploc.remove(k);
            } else {
                panikrum.put(k, v - 1);
            }
        });
    }
    @EventHandler
    public void regionLeave(RegionLeaveEvent event) {
        Player p = event.getPlayer();
        if(panikrum.containsKey(p)) {
            panikrum.remove(p);
            tploc.remove(p);
        }
    }


}
