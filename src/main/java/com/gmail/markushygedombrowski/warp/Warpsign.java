package com.gmail.markushygedombrowski.warp;



import com.gmail.markushygedombrowski.utils.VagtUtils;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class Warpsign implements Listener {
    private final VagtWarpGUI vagtWarpGUI;

    public Warpsign(VagtWarpGUI vagtWarpGUI) {
        this.vagtWarpGUI = vagtWarpGUI;
    }

    @EventHandler
    public void onPlayerClickSign(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN) {
                Sign s = (Sign) event.getClickedBlock().getState();
                if (s.getLine(0).equalsIgnoreCase("§9§lVagt Warp") && s.getLine(1).equalsIgnoreCase("Klik her for at") && s.getLine(2).equalsIgnoreCase("Warp til C, B, A") ) {
                    if (!p.hasPermission("VagtWarp")) {
                        p.sendMessage("§4Det har du ikke permission til!");
                        return;
                    }
                    String block = VagtUtils.getRegion(p.getLocation());
                    vagtWarpGUI.create(p,block);
                }

            }
        }
    }


}
