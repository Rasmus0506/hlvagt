package com.gmail.markushygedombrowski.listners;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class DropItemListener implements Listener {
    private boolean drop;
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if(!p.hasPermission("vagt")) return;
        if(p.isOp()) return;

        if(!isDrop()) {
            p.sendMessage("§cDu kan ikke drop items!");
            p.sendMessage("§cSkriv: §7/drop §cfor og drop");
            event.setCancelled(true);
            event.isCancelled();
        } else {
            p.sendMessage("§cDu har droppet: §7" + item.getType());
            setDrop(false);
        }
    }

    public boolean isDrop() {
        return drop;
    }

    public void setDrop(boolean drop) {
        this.drop = drop;
    }
}
