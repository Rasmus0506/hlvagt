package com.gmail.markushygedombrowski.npc;

import com.gmail.markushygedombrowski.npc.vagthavende.VagthavendeOfficer;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VagtNPCer implements Listener {

    private VagthavendeOfficer vagthavendeOfficer;

    public VagtNPCer(VagthavendeOfficer vagthavendeOfficer) {
        this.vagthavendeOfficer = vagthavendeOfficer;
    }

    @EventHandler
    public void onNPCRightClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        NPC npc = event.getNPC();
        if (npc.getFullName().equalsIgnoreCase("§3§lVagtHavende Officer")) {
            if (!isVagt(event, player)) {
                return;
            }
            vagthavendeOfficer.create(player);
        }
    }


    private boolean isVagt(NPCRightClickEvent event, Player player) {
        if (!player.hasPermission("vagt.npcs")) {
            player.sendMessage("§4Du har ikke tilladelse til at rankup som vagt SMUT");
            event.setCancelled(true);
            return false;

        }
        return true;
    }
}
