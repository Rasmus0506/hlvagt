package com.gmail.markushygedombrowski.buff;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.cooldown.VagtCooldown;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class BuffGui implements Listener {
    private int BUFF_INDEX = 4;
    private final int EXTRABUFF_INDEX = 6;
    private final Settings settings;
    private final HLvagt plugin;
    private PlayerProfiles playerProfiles;
    private BuffManager buffManager;

    public BuffGui(Settings settings, HLvagt plugin, PlayerProfiles playerProfiles, BuffManager buffManager) {
        this.settings = settings;
        this.plugin = plugin;
        this.playerProfiles = playerProfiles;
        this.buffManager = buffManager;
    }

    public void create(Player p) {
        Inventory inventory = Bukkit.createInventory(null, 9, "§c§lBuff");
        int pay = 0;
        ItemStack buff = new ItemStack(Material.POTION,1,(short) 1);
        List<String> lore = new ArrayList<>();
        lore.add(0, "§7Koster: §b" + pay);
        if(VagtCooldown.isCooling(p.getName(), "buff")) {
            lore.add(1, "§7Cooldown: §b" + VagtCooldown.getRemaining(p.getName(), "buff") + " minutter");
        }
        lore.add("§c§lBuff Level: §b" + playerProfiles.getPlayerProfile(p.getUniqueId()).getProperty("buff"));
        ItemMeta meta = buff.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName("§CBuff");
        buff.setItemMeta(meta);
        if (p.hasPermission("extraBuff")) {
            pay = settings.getExtraBuffPay();
            lore.set(0, "§7Koster: §b" + pay);
            if(VagtCooldown.isCooling(p.getName(), "extraBuff")) {
                if(lore.size() > 1)
                    lore.set(1, "§7Cooldown: §b" + VagtCooldown.getRemaining(p.getName(), "extraBuff") + " minutter");
                else
                    lore.add(1, "§7Cooldown: §b" + VagtCooldown.getRemaining(p.getName(), "extraBuff") + " minutter");
            }
            ItemStack extraBuff = new ItemStack(Material.POTION,1,(short) 3);
            ItemMeta extraBuffItemMeta = extraBuff.getItemMeta();
            extraBuffItemMeta.setLore(lore);
            extraBuffItemMeta.setDisplayName("§bExtra §cBuff");
            extraBuff.setItemMeta(extraBuffItemMeta);
            BUFF_INDEX = 2;
            inventory.setItem(EXTRABUFF_INDEX, extraBuff);
        } else {
            BUFF_INDEX = 4;
        }
        inventory.setItem(BUFF_INDEX, buff);
        p.openInventory(inventory);


    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inventory = e.getClickedInventory();
        ItemStack clickeditem = e.getCurrentItem();
        int clickedSlot = e.getRawSlot();

        if (clickeditem == null) {
            return;
        }

        if (inventory.getTitle().equalsIgnoreCase("§c§lBuff")) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            int pay = settings.getBuffPay();
            if(!settings.isAktivbuff()) {
                p.sendMessage("§cBuff er ikke aktiv!");
                p.sendMessage("§cHAHAAHA");
                return;
            }
            if(clickeditem.getType() != Material.POTION) {
                return;
            }
            if (clickedSlot == BUFF_INDEX) {
                if (!plugin.econ.has(p, pay)) {
                    p.sendMessage("§cDu har ikke penge nok!");
                    return;
                }
                if(VagtCooldown.isCooling(p.getName(), "buff")){
                    p.sendMessage("§cDu kan først bruge denne buff igen om §b" + VagtCooldown.getRemaining(p.getName(), "buff") + "§c minutter");
                    return;
                }
                giveBuff(p, pay);

            }else if (clickedSlot == EXTRABUFF_INDEX) {
                if (!p.hasPermission("extraBuff")) {
                    return;
                }
                pay = settings.getExtraBuffPay();
                if (!plugin.econ.has(p, pay)) {
                    p.sendMessage("§cDu har ikke penge nok!");
                    return;
                }
                if(VagtCooldown.isCooling(p.getName(), "extraBuff")){
                    p.sendMessage("§cDu kan først bruge denne buff igen om §b" + VagtCooldown.getRemaining(p.getName(), "extraBuff") + "§c minutter");
                    return;
                }

                giveExtraBuff(p, pay);

            }
            p.sendMessage("§aDu har betalt §b" + pay + "§a for §cBuff");
        }

    }

    private void giveExtraBuff(Player p, int pay) {
        p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0));
        plugin.econ.withdrawPlayer(p, pay);
        p.sendMessage("§aDu har taget Extra §cBuff");
        VagtCooldown.add(p.getName(), "extraBuff", settings.getExtraBuffLength(),System.currentTimeMillis());
    }

    private void giveBuff(Player p, int pay) {
        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
        int buff = profile.castPropertyToInt(profile.getProperty("buff"));


        BuffLevels buffLevel = buffManager.getBuffLevel(buff);
        buffLevel.giveBuff(p);
        VagtCooldown.add(p.getName(), "buff", 120,System.currentTimeMillis());
        plugin.econ.withdrawPlayer(p, pay);
        p.sendMessage("§aDu har taget §cBuff");
    }
}
