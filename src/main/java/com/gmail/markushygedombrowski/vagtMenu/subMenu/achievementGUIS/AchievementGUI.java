package com.gmail.markushygedombrowski.vagtMenu.subMenu.achievementGUIS;

import com.gmail.markushygedombrowski.achievements.SimpleAchievementManager;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AchievementGUI implements Listener {
    private final SimpleAchievementManager sAcheivementM;
    private final HeadDatabaseAPI api = new HeadDatabaseAPI();
    private final PlayerProfiles playerProfiles;
    private final int CLOSE_BUTTON_INDEX = 53;
    private final int BACK_BUTTON_INDEX = 45;
    private final VagtUtils utils;
    private final SubAchievementGUI subAchievementGUI;

    public AchievementGUI(SimpleAchievementManager sAcheivementM, PlayerProfiles playerProfiles, VagtUtils utils, SubAchievementGUI subAchievementGUI) {
        this.sAcheivementM = sAcheivementM;
        this.playerProfiles = playerProfiles;
        this.utils = utils;
        this.subAchievementGUI = subAchievementGUI;
    }

    public void create(Player player){
        PlayerProfile profile = playerProfiles.getPlayerProfile(player.getUniqueId());
        Inventory inv = Bukkit.createInventory(null, 54, "§a§lAchievements");
        int index = 9;
        inv.setItem(CLOSE_BUTTON_INDEX, utils.getCloseButton());
        inv.setItem(BACK_BUTTON_INDEX, utils.getBackButton());
        for (String groupName : sAcheivementM.getAchievementGroups().keySet()) {
            System.out.println("Achievement group: " + groupName);
            ItemStack item = achievementGroupItem(groupName, profile);
            inv.setItem(index, item);
            index++;
        }
        utils.fillEmptySlots(inv);
        player.openInventory(inv);

    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        Inventory inventory = event.getClickedInventory();
        if (inventory.getTitle().equals("§a§lAchievements")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
            if (event.getCurrentItem().getType() == Material.BARRIER) {
                player.closeInventory();
                return;
            }
            else if (event.getCurrentItem().getType() == Material.ARROW) {
                player.closeInventory();
                return;
            } else if (event.getCurrentItem().getType() == Material.SKULL_ITEM) {
                String groupName = event.getCurrentItem().getItemMeta().getDisplayName().replace("§a§l", "").replace("§c§l", "");
                if (sAcheivementM.getAchievementGroups().containsKey(groupName)) {
                    subAchievementGUI.create(player,playerProfiles.getPlayerProfile(player.getUniqueId()), groupName);
                }
            }


        }
    }

    public ItemStack achievementGroupItem(String groupName, PlayerProfile profile) {
        profile.getCompletedAchievementsGrouped().putIfAbsent(groupName, 0);
        int completed = profile.getCompletedAchievementsGrouped().get(groupName);
        int totalAchievements = sAcheivementM.getAchievementGroups().get(groupName);
        ItemStack item;
        ItemMeta meta;
        List<String> lore = new ArrayList<>();
        if(completed >= totalAchievements) {
            item = api.getItemHead("114453");
            meta = item.getItemMeta();
            meta.setDisplayName("§a§l" + groupName);
            lore.add("§7Du har gennemført alle");
            lore.add("§2"+ groupName + " §7achievements .");
            lore.add("§7Achievements completed: §a" + completed + "§7/§a" + totalAchievements);
            meta.setLore(lore);
        } else {
            item = api.getItemHead("97536");
            meta = item.getItemMeta();
            meta.setDisplayName("§c§l" + groupName);
            lore.add("§7Achievements completed: §c" + completed + "§7/§a" + totalAchievements);
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        item.setAmount(1);
        return item;

    }




}
