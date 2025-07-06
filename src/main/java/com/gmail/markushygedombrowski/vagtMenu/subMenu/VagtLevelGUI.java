package com.gmail.markushygedombrowski.vagtMenu.subMenu;

import com.gmail.markushygedombrowski.levels.LevelManager;
import com.gmail.markushygedombrowski.levels.Levels;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.text.DecimalFormat;
import java.util.*;

public class VagtLevelGUI implements Listener {
    private PlayerProfiles playerProfiles;
    private HashMap<UUID, Integer> playerPageNumbers = new HashMap<>();
    private LevelManager levelManager;

    public VagtLevelGUI(PlayerProfiles playerProfiles, LevelManager levelManager) {
        this.playerProfiles = playerProfiles;
        this.levelManager = levelManager;
    }

    private final int NEXT_PAGE = 53;
    private final int PREV_PAGE = 45;

    public void openVagtLevelGUI(Player p) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§2Vagt Level side " + NEXT_PAGE);
        playerPageNumbers.put(p.getUniqueId(), NEXT_PAGE);
        inventory.setItem(NEXT_PAGE, createItem(p, Material.ARROW, "§aNæste side", null));

        for (int i = 9; i < 45; i++) {
            int lvl = i - 8;
            inventory.setItem(i, createItem(p, Material.WOOL, "§2§lVagt Level §6" + lvl, lvl));
        }
        fillEmptySlots(inventory);
        p.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        int clickedSlot = event.getRawSlot();
        if (event.getCurrentItem() == null || !event.getClickedInventory().getTitle().contains("§2Vagt Level")) {
            return;
        }
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem.getType() == Material.AIR || clickedItem.getType() == Material.STAINED_GLASS_PANE) {
            return;
        }
        if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§aNæste side") || clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§cForrige side")) {
            int currentPage = playerPageNumbers.getOrDefault(p.getUniqueId(), 1);
            int newPage = clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§aNæste side") ? currentPage + 1 : currentPage - 1;
            playerPageNumbers.put(p.getUniqueId(), newPage);
            nextSite(p, newPage);
        }
    }

    public void nextSite(Player p, int currentPage) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§2Vagt Level side " + currentPage);
        inventory.setItem(NEXT_PAGE, createItem(p, Material.ARROW, "§aNæste side", null));
        if (currentPage > 1) {
            inventory.setItem(PREV_PAGE, createItem(p, Material.ARROW, "§cForrige side", null));
        }
        for (int i = 9; i < 45; i++) {
            int lvl = i - 8 + (currentPage - 1) * 36;
            inventory.setItem(i, createItem(p, Material.WOOL, "§2§lVagt Level §6" + lvl, lvl));
        }
        fillEmptySlots(inventory);
        p.openInventory(inventory);
    }

    private ItemStack createItem(Player p, Material material, String displayName, Integer lvl) {
        ItemStack item;
        if (lvl != null) {
            PlayerProfile playerProfile = playerProfiles.getPlayerProfile(p.getUniqueId());
            double level = (double) playerProfile.getProperty("level");
            DyeColor color = level >= lvl ? (level == lvl ? DyeColor.YELLOW : DyeColor.GREEN) : DyeColor.RED;
            item = new ItemStack(new Wool(color).toItemStack(1));
            List<String> lore = new ArrayList<>();
            String pattern = "###,###.##";
            DecimalFormat df = new DecimalFormat(pattern);
            lore.add("§3Exp: §b" + playerProfile.getProperty("exp") + "§7/§b" + df.format(playerProfile.getExpSpecificLevel(lvl)));
            lore.add("§7§a+ §b1000 §7løn");
            Levels levels = levelManager.getLevel(lvl);
            if(levels != null) {
                List<String> rewards = levels.getLore();
                if (rewards != null) {
                    lore.addAll(rewards);
                }
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            item.setItemMeta(meta);
        } else {
            item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(displayName);
            item.setItemMeta(meta);
        }
        return item;
    }

    private void fillEmptySlots(Inventory inventory) {
        Random random = new Random();
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) random.nextInt(15));
        for (ItemStack item : inventory.getContents()) {
            if (item == null) {
                inventory.setItem(inventory.firstEmpty(), glass);
            }
        }
    }
}
