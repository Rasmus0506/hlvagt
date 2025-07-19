package com.gmail.markushygedombrowski.npc.vagthavende;

import com.gmail.markushygedombrowski.achievements.AchievementUpdater;
import com.gmail.markushygedombrowski.deliveredItems.ItemProfileLoader;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VagthavendeOfficer implements Listener {

    private final int SHARDS_INDEX = 11;
    private final int GEAR_INDEX = 13;
    private final int BREAD_INDEX = 6;
    private final int BLAZERODS_INDEX = 24;
    private DeliverGearGUI deliverGearGUI;
    private ItemProfileLoader itemProfileLoader;
    private PlayerProfiles playerProfiles;


    public VagthavendeOfficer(DeliverGearGUI deliverGearGUI, ItemProfileLoader itemProfileLoader, PlayerProfiles playerProfiles) {
        this.deliverGearGUI = deliverGearGUI;
        this.itemProfileLoader = itemProfileLoader;
        this.playerProfiles = playerProfiles;

    }

    public void create(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Vagthavende Officer");
        ItemStack shards = new ItemStack(Material.PRISMARINE_CRYSTALS, 1);
        ItemStack gear = new ItemStack(Material.IRON_HELMET, 1);
        ItemStack bread = new ItemStack(Material.BREAD, 1);
        ItemStack blazeRods = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta shardsMeta = shards.getItemMeta();
        ItemMeta gearMeta = gear.getItemMeta();
        ItemMeta breadMeta = bread.getItemMeta();
        ItemMeta blazeRodsMeta = blazeRods.getItemMeta();
        shardsMeta.setDisplayName("§9§lShards");
        gearMeta.setDisplayName("§6§lGear");
        breadMeta.setDisplayName("§e§lBread");
        blazeRodsMeta.setDisplayName("§c§lBlazeRods");

        List<String> lore = new ArrayList<>();
        lore.add(0, "§7Aflever §9§lShards §7her!");
        lore.add(1, "§7Du for §b" + itemProfileLoader.getItemProfile(shardsMeta.getDisplayName()).getExp() + " §3exp");
        shardsMeta.setLore(lore);
        lore.set(0, "§7Aflever §6§lGear §7her!");
        lore.remove(1);
        gearMeta.setLore(lore);
        lore.set(0, "§7Aflever §e§lBread §7her!");
        lore.add(1, "§7Du for §b" + itemProfileLoader.getItemProfile(breadMeta.getDisplayName()).getExp() + " §3exp");
        breadMeta.setLore(lore);
        lore.clear();
        lore.add(0, "§7Aflever §c§lBlazeRods §7her!");
        lore.add(1, "§7Du for §b" + itemProfileLoader.getItemProfile(blazeRodsMeta.getDisplayName()).getExp() + " §3exp");
        blazeRodsMeta.setLore(lore);
        shards.setItemMeta(shardsMeta);
        gear.setItemMeta(gearMeta);
        bread.setItemMeta(breadMeta);
        blazeRods.setItemMeta(blazeRodsMeta);
        inventory.setItem(SHARDS_INDEX, shards);
        inventory.setItem(GEAR_INDEX, gear);
        inventory.setItem(BREAD_INDEX, bread);
        inventory.setItem(BLAZERODS_INDEX, blazeRods);
        player.openInventory(inventory);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        if (clickeditem == null) {
            return;
        }
        if (inventory.getTitle().equalsIgnoreCase("Vagthavende Officer")) {
            PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
            if(profile == null) {
                p.sendMessage("§ckunne ikke finde din profil");
                return;
            }

            switch (clickedSlot) {
                case SHARDS_INDEX:
                    int amount = deliverItem(p, clickeditem);
                   if(amount == 0){
                       p.sendMessage("§7Du har ikke §9§lShards §7i dit inventory");
                       break;
                   }

                    break;
                case GEAR_INDEX:
                    deliverGearGUI.create(p);
                    break;
                case BREAD_INDEX:
                    amount = deliverItem(p, clickeditem);
                    if (amount == 0) {
                        p.sendMessage("§7Du har ikke §e§lBread §7i dit inventory");
                        break;
                    }
                    break;
                case BLAZERODS_INDEX:
                    amount = deliverItem(p, clickeditem);
                    if (amount == 0) {
                        p.sendMessage("§7Du har ikke §c§lBlazeRods §7i dit inventory");
                        break;
                    }
                    break;
            }
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

        }


    }

    public int deliverItem(Player p, ItemStack item)  {
        if (p.getInventory().contains(item.getType())) {
            PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
            int amount = 0;
            int exp = 0;
            for (ItemStack itemStack : p.getInventory().getContents()) {
                if(itemStack == null) continue;
                if (itemStack.getType() == item.getType()) {
                    amount += itemStack.getAmount();
                    exp += itemProfileLoader.getItemProfile(item.getItemMeta().getDisplayName()).getExp() * itemStack.getAmount();
                    p.getInventory().remove(itemStack);
                }

            }
            if (amount == 0) {
                p.sendMessage("§7Du har ikke §c§lBlazeRods §7i dit inventory");
                return 0;
            }
            p.sendMessage("§7Du har afleveret §a" + amount + " " + item.getItemMeta().getDisplayName());
            p.sendMessage("§7Du har fået §b" + exp + "§3 exp");
            profile.addExp(exp);
            profile.getDeliveredItems().incrementItem(item.getType(), amount);
            try {
                playerProfiles.save(profile);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return amount;
        }

        return 0;
    }


}
