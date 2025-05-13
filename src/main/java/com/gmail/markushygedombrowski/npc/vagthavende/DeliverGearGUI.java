package com.gmail.markushygedombrowski.npc.vagthavende;

import com.gmail.markushygedombrowski.deliveredItems.ItemProfileLoader;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.utils.Utils;
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

import java.util.Arrays;


public class DeliverGearGUI implements Listener {

    private final int[] ITEM_INDEXES = {10, 12, 14, 16, 22};
    private final Material[] DIAMOND_ITEMS = {Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.DIAMOND_SWORD};
    private final Material[] IRON_ITEMS = {Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.IRON_SWORD};
    private final String[] ITEM_NAMES = {"Helmet", "Chestplate", "Leggings", "Boots", "Sword"};
    private final ItemProfileLoader itemProfileLoader;
    private final PlayerProfiles playerProfiles;

    public DeliverGearGUI(ItemProfileLoader itemProfileLoader, PlayerProfiles playerProfiles) {
        this.itemProfileLoader = itemProfileLoader;
        this.playerProfiles = playerProfiles;
    }

    public void create(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "Aflever Gear");
        Material[] materials = Utils.isLocInRegion(p.getLocation(), "a") ? DIAMOND_ITEMS : IRON_ITEMS;
        String material = Utils.isLocInRegion(p.getLocation(), "a") ? "§b§lDiamond" : "§f§lIron";

        for (int i = 0; i < ITEM_INDEXES.length; i++) {
            ItemStack item = new ItemStack(materials[i]);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(material + " " + ITEM_NAMES[i]);
            meta.setLore(Arrays.asList("§7Aflever " + meta.getDisplayName() + " §7her!", "§7Du for §b" + itemProfileLoader.getItemProfile(meta.getDisplayName()).getExp() + " §3exp"));
            item.setItemMeta(meta);
            inv.setItem(ITEM_INDEXES[i], item);
        }
        p.openInventory(inv);
    }

    @EventHandler
    public void onClickInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        ItemStack clickeditem = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        if (clickeditem == null || !event.getClickedInventory().getTitle().equalsIgnoreCase("Aflever Gear") || !Arrays.stream(ITEM_INDEXES).anyMatch(i -> i == clickedSlot)) {
            return;
        }
        if (!gear(p, clickeditem)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            return;
        }
        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
        profile.getDeliveredItems().incrementItem(clickeditem.getType());
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
    }

    private boolean gear(Player p, ItemStack clickeditem) {
        if (deliverGear(p, clickeditem)) {
            PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
            profile.setXp(((Double) profile.getProperty("exp")).intValue() + itemProfileLoader.getItemProfile(clickeditem.getItemMeta().getDisplayName()).getExp());

            return true;
        }
        p.sendMessage("§7Du har ikke " + clickeditem.getItemMeta().getDisplayName());
        return false;
    }

    public boolean deliverGear(Player p, ItemStack gear) {
        if (p.getInventory().contains(gear.getType(),1)) {
            ItemStack item = p.getInventory().getItem(p.getInventory().first(gear.getType()));
            if(item.getItemMeta().getDisplayName() != null && (item.getItemMeta().getDisplayName().contains("§cC-") || item.getItemMeta().getDisplayName().contains("§bB-") || item.getItemMeta().getDisplayName().contains("§aA-"))) {
                return false;
            }
            p.sendMessage("§7Du har afleveret §a" + gear.getItemMeta().getDisplayName() + "§7!");
            ItemStack itemStack = new ItemStack(gear.getType(), 1);
            if(item.getItemMeta().getDisplayName() != null) {
                ItemMeta meta = item.getItemMeta();
                itemStack.setItemMeta(meta);
            }
            itemStack.setDurability(item.getDurability());
            itemStack.addEnchantments(item.getEnchantments());

            p.getInventory().removeItem(itemStack);
            return true;
        }
        return false;
    }
}
