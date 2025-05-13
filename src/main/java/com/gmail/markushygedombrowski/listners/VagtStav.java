package com.gmail.markushygedombrowski.listners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VagtStav implements Listener {

    @EventHandler
    public void vagtStav(EntityDamageByEntityEvent event) {

        Entity entity = event.getDamager();
        if (entity instanceof Player) {
            Player attacker = ((Player) entity).getPlayer();
            ItemStack item = attacker.getItemInHand();
            if(event.getEntity() instanceof Player){
                Player defender = (Player) event.getEntity();
                if (defender.hasPermission("vagt")) {
                    return;
                }
                if (item.getType() == Material.STICK && item.getItemMeta().getDisplayName().equalsIgnoreCase("§cvagt stav") && attacker.hasPermission("vagt.stav")) {
                    defender.sendTitle("§4GÅ VÆK FRA!", "§4" + attacker.getName());
                    defender.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1));
                }
            }


        }
    }

    @EventHandler
    public void vagtRightClick(PlayerInteractAtEntityEvent event) {

        Player p = event.getPlayer();
        if (!p.hasPermission("vagt")) {
            return;
        }
        ItemStack item = p.getItemInHand();
        if (!(item.getType() == Material.STICK)) {
            return;
        }

        if(item.getItemMeta() == null || !item.getItemMeta().getDisplayName().equalsIgnoreCase("§cvagt stav")) {
            return;
        }

        if (!event.getRightClicked().getType().equals(EntityType.PLAYER)) {
            return;
        }

        Player defender = (Player) event.getRightClicked();
        if (defender.hasPermission("vagt")) return;
        defender.sendTitle("§4§lDROP", "§4 Til " + p.getName());
        defender.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 2));
        p.sendMessage("§cDu har sagt drop til: " + defender.getDisplayName());
    }
}
