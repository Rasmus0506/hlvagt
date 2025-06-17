package com.gmail.markushygedombrowski.vagtMenu.subMenu;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AchievementsGUI implements Listener {
    private static final String MENU_TITLE = "§8Vagt Achievements";
    private static final int INVENTORY_SIZE = 54;
    private static final int DEATH_ACHIEVEMENT_SLOT = 0;

    private final HLvagt plugin;
    private final List<VagtAchievement> achievements;

    public AchievementsGUI(HLvagt plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin kan ikke være null");
        }
        this.plugin = plugin;
        this.achievements = initializeAchievements();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private List<VagtAchievement> initializeAchievements() {
        List<VagtAchievement> achievementList = new ArrayList<>();
        achievementList.add(new VagtAchievement("achievement_death_1", "Dø 1 gang", "$500", 1));
        achievementList.add(new VagtAchievement("achievement_death_5", "Dø 5 gange", "$2500", 5));
        achievementList.add(new VagtAchievement("achievement_death_10", "Dø 10 gange", "15% af din løn", 10));
        return achievementList;
    }

    private static final String ERROR_MESSAGE_PLAYER = "§cDer skete en fejl ved åbning af achievement menu";
    private static final String ERROR_MESSAGE_LOG = "Fejl ved åbning af achievement menu for %s: %s";

    public void openMenu(Player player) {
        if (player == null) {
            return;
        }

        try {
            Inventory achievementMenu = createAchievementMenu(player);
            player.openInventory(achievementMenu);
        } catch (Exception e) {
            handleMenuOpenError(player, e);
        }
    }

    private Inventory createAchievementMenu(Player player) {
        Inventory achievementMenu = Bukkit.createInventory(null, INVENTORY_SIZE, MENU_TITLE);

        PlayerProfile playerProfile = plugin.getPlayerProfile(player.getUniqueId());
        if (playerProfile != null) {
            achievementMenu.setItem(DEATH_ACHIEVEMENT_SLOT, createDeathAchievementItem(player, playerProfile));
        }

        return achievementMenu;
    }

    private void handleMenuOpenError(Player player, Exception e) {
        player.sendMessage(ERROR_MESSAGE_PLAYER);
        plugin.getLogger().warning(String.format(ERROR_MESSAGE_LOG, player.getName(), e.getMessage()));
    }


    private ItemStack createDeathAchievementItem(Player player, PlayerProfile profile) {
        ItemStack deathItem = new ItemStack(Material.SKULL_ITEM, 1, (byte) 1);
        ItemMeta meta = deathItem.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("§cDøds Achievements");
            meta.setLore(createDeathAchievementLore(player, profile));
            deathItem.setItemMeta(meta);
        }

        return deathItem;
    }

    private List<String> createDeathAchievementLore(Player player, PlayerProfile profile) {
        List<String> lore = new ArrayList<>();
        lore.add("§7Statistik for dine dødsfald:");
        lore.add("§7Total dødsfald: §c" + player.getStatistic(Statistic.DEATHS));
        lore.add("");

        for (VagtAchievement achievement : achievements) {
            achievement.addToLore(lore, profile);
        }

        return lore;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(MENU_TITLE)) {
            event.setCancelled(true);
        }
    }

    private static class VagtAchievement {
        private final String property;
        private final String description;
        private final String penalty;
        private final int requiredDeaths;

        public VagtAchievement(String property, String description, String penalty, int requiredDeaths) {
            this.property = property;
            this.description = description;
            this.penalty = penalty;
            this.requiredDeaths = requiredDeaths;
        }

        public void addToLore(List<String> lore, PlayerProfile profile) {
            String status = profile.hasProperty(property) ? "§a✓" : "§c✗";
            lore.add(status + " §7" + description);
            lore.add("   §7Straf: §c" + penalty);
        }
    }
}