package com.gmail.markushygedombrowski.vagtMenu.subMenu.achievementGUIS;

import com.gmail.markushygedombrowski.achievements.SimpleAchievement;
import com.gmail.markushygedombrowski.achievements.SimpleAchievementManager;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.settings.DataProperty;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SubAchievementGUI implements Listener {
    private final VagtUtils utils;
    private final SimpleAchievementManager achievementManager;
    private final HeadDatabaseAPI api = new HeadDatabaseAPI();



    public SubAchievementGUI(VagtUtils utils, SimpleAchievementManager achievementManager) {
        this.utils = utils;
        this.achievementManager = achievementManager;
    }

    public void create(Player p,PlayerProfile profile, String groupName) {
        Inventory inv = Bukkit.createInventory(null, 54, "§a§lAchievement: "+ groupName);
        List<SimpleAchievement> achievements = achievementManager.getAchievementsByGroup(groupName);
        int[] index = {9};
        inv.setItem(45, utils.getBackButton());
        inv.setItem(53, utils.getNextButton());
        ItemStack completedItem;
        ItemMeta meta;
        String completedLore;
        String color;

        achievements.sort(Comparator.comparing(SimpleAchievement::getRequirement));

        for (SimpleAchievement achievement : achievements) {

            List<String> lore = new ArrayList<>();
            if(profile.hasCompletedAchievement(achievement.getId())) {
                completedItem = api.getItemHead("21774");
                meta = completedItem.getItemMeta();
                color = "§a";
                completedLore = "§a✔ Achievement completed";

            } else {
                completedItem = api.getItemHead("9382");
                meta = completedItem.getItemMeta();
                color = "§c";
                completedLore = "§c✖ Achievement ikke completed mangler " + achievement.getDataProperty().getData(profile.getDeliveredItems(),profile) + "§7/§c" + achievement.getRequirement();
            }
            meta.setDisplayName(color + achievement.getDescription());
            lore.add("§7Krav: " + achievement.getRequirement());
            lore.add("§7" + achievement.getType() + ": §f" + achievement.getModifier());
            lore.add(completedLore);
            meta.setLore(lore);
            completedItem.setItemMeta(meta);
            inv.setItem(index[0], completedItem);
            index[0]++;
        }
        utils.fillEmptySlots(inv);
        p.openInventory(inv);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (event.getClickedInventory().getTitle().contains("§a§lAchievement: ")) {

            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);


        }
    }
}
