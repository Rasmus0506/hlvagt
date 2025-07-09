package com.gmail.markushygedombrowski.vagtMenu.subMenu;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AchievementsGUI implements Listener {

    private static final String MENU_TITLE = "§8Vagt Achievements";
    private static final String DEATH_MENU_TITLE = "§8Døds Achievements";
    private static final String KILL_MENU_TITLE = "§8Kills Achievements";
    private static final String DAMAGE_MENU_TITLE = "§8Damage Achievements";
    private static final int INVENTORY_SIZE = 54;
    private static final int ITEMS_PER_PAGE = 36;
    private static final int BACK_BUTTON_SLOT = 45;
    private static final int NEXT_PAGE_SLOT = 53;

    private final HLvagt plugin;
    private final List<VagtAchievement> deathAchievements;
    private final List<VagtAchievement> killsAchievements;
    private final List<VagtAchievement> damageAchievements;

    public AchievementsGUI(HLvagt plugin) {
        if (plugin == null) throw new IllegalArgumentException("Plugin cannot be null");
        this.plugin = plugin;
        this.deathAchievements = initializeAchievements("death");
        this.killsAchievements = initializeAchievements("kill");
        this.damageAchievements = initializeAchievements("damage");
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private List<VagtAchievement> initializeAchievements(String type) {
        List<VagtAchievement> achievements = new ArrayList<>();
        double modifier = type.equals("damage") ? 0.001 : 0.05;

        // Define ranges and increments
        int[][] ranges = {
                {1, 10, 1},    // Start: 1, End: 10, Increment: 1
                {15, 100, 5},  // Start: 15, End: 100, Increment: 5
                {125, 250, 25}, // Start: 125, End: 250, Increment: 25
                {300, 600, 50}, // Start: 300, End: 600, Increment: 50
                {600, 1000, 60} // Start: 600, End: 1000, Increment: 60
        };

        for (int[] range : ranges) {
            int start = range[0];
            int end = range[1];
            int increment = range[2];

            for (int i = start; i <= end; i += increment) {
                // Calculate modifier based on type and range
                if (type.equals("kill") || type.equals("death")) {
                    if (i <= 10) {
                        modifier = 0.05 + ((i - 1) * 0.02);
                    } else if (i <= 100) {
                        modifier = 0.25 + ((i - 15) / 5.0) * 0.05;
                    } else if (i <= 250) {
                        modifier = 1.0 + ((i - 125) / 25.0) * 0.15;
                    } else if (i <= 600) {
                        modifier = 1.0 + ((i - 150) / 50.0) * 0.25;
                    } else {
                        modifier = 1.0 + ((i - 600) / 60.0) * 0.30;
                    }
                }

                achievements.add(new VagtAchievement(
                        "achievement_" + type + "_" + i,
                        type.equals("kill") ? "Dræb " + i + " spillere" : "Dø " + i + " gange",
                        i,
                        modifier,
                        type
                ));
            }
        }

        return achievements;
    }

    public void openMenu(Player player) {
        if (player == null) return;
        try {
            Inventory menu = Bukkit.createInventory(null, INVENTORY_SIZE, MENU_TITLE);
            menu.setItem(9, createMenuButton(player, Statistic.DEATHS, deathAchievements, "§c☠ Døds Achievements ☠"));
            menu.setItem(10, createMenuButton(player, Statistic.PLAYER_KILLS, killsAchievements, "§a☠ Kills Achievements ☠"));
            menu.setItem(11, createMenuButton(player, "total_damage", damageAchievements, "§6⚔ Damage Achievements ⚔"));
            fillEmptySlots(menu);
            player.openInventory(menu);
        } catch (Exception e) {
            handleError(player, e);
        }
    }

    private ItemStack createMenuButton(Player player, Object stat, List<VagtAchievement> achievements, String title) {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);

        List<String> lore = new ArrayList<>();
        int statValue = stat instanceof Statistic ? player.getStatistic((Statistic) stat) : getPlayerProperty(player, (String) stat);
        lore.add("§7Total: §f" + statValue);
        lore.add("§7Achievements: §f" + countAchievements(player, achievements) + "§8/§f" + achievements.size());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private int getPlayerProperty(Player player, String property) {
        PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());
        if (profile == null || !profile.hasProperty(property)) return 0;
        try {
            return Integer.parseInt(profile.getProperty(property).toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int countAchievements(Player player, List<VagtAchievement> achievements) {
        PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());
        if (profile == null) return 0;
        return (int) achievements.stream().filter(a -> profile.hasProperty(a.property)).count();
    }

    private void fillEmptySlots(Inventory inventory) {
        ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta meta = filler.getItemMeta();
        meta.setDisplayName("§r");
        filler.setItemMeta(meta);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) inventory.setItem(i, filler);
        }
    }

    private void handleError(Player player, Exception e) {
        player.sendMessage("§cAn error occurred while opening the menu.");
        plugin.getLogger().warning("Error opening menu for " + player.getName() + ": " + e.getMessage());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();

        // Check if the inventory is the main menu
        if (title.equals(MENU_TITLE)) {
            event.setCancelled(true); // Prevent item movement

            int slot = event.getSlot();
            if (slot == 9) { // Slot for Death Achievements
                openDeathMenu(player, 0); // Open Death Menu at page 0
            } else if (slot == 10) { // Slot for Kill Achievements
                openKillsMenu(player, 0); // Open Kills Menu at page 0
            }
        }
        if (title.startsWith(DEATH_MENU_TITLE) || title.startsWith(KILL_MENU_TITLE)) {
            event.setCancelled(true); // Prevent item movement

            int slot = event.getSlot();
            int currentPage = getCurrentPage(title);

            if (slot == BACK_BUTTON_SLOT && currentPage > 0) {
                if (title.startsWith(DEATH_MENU_TITLE)) {
                    openDeathMenu(player, currentPage - 1);
                } else if (title.startsWith(KILL_MENU_TITLE)) {
                    openKillsMenu(player, currentPage - 1);
                }
            } else if (slot == NEXT_PAGE_SLOT) {
                if (title.startsWith(DEATH_MENU_TITLE)) {
                    openDeathMenu(player, currentPage + 1);
                } else if (title.startsWith(KILL_MENU_TITLE)) {
                    openKillsMenu(player, currentPage + 1);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        try {
            plugin.getVagtAchievements().checkDeathAchievement(victim);
            if (killer != null) plugin.getVagtAchievements().checkKillAchievement(killer);
        } catch (InterruptedException e) {
            plugin.getLogger().warning("Error processing achievements: " + e.getMessage());
        }
    }


    private static class VagtAchievement {
        private final String property;
        private final String description;
        private final int requiredAmount;
        private final double modifier;
        private final String type;

        public VagtAchievement(String property, String description, int requiredAmount, double modifier, String type) {
            this.property = property;
            this.description = description;
            this.requiredAmount = requiredAmount;
            this.modifier = modifier;
            this.type = type;
        }
    }
    public void openAchievementMenu(Player player, int page, String menuTitle, List<VagtAchievement> achievements, int statValue) {
        Inventory inventory = Bukkit.createInventory(null, INVENTORY_SIZE, menuTitle + " - Side " + (page + 1));
        PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());

        if (profile != null) {
            populateAchievements(inventory, achievements, statValue, profile, page);
            setNavigationButtons(inventory, page, achievements.size());
        }

        fillEmptySlots(inventory);
        player.openInventory(inventory);
    }

    private void populateAchievements(Inventory inventory, List<VagtAchievement> achievements, int statValue, PlayerProfile profile, int page) {
        int startIndex = page * ITEMS_PER_PAGE;

        for (int i = 0; i < ITEMS_PER_PAGE && (startIndex + i) < achievements.size(); i++) {
            VagtAchievement achievement = achievements.get(startIndex + i);
            ItemStack item = createAchievementItem(statValue, profile, achievement);
            inventory.setItem(i, item);
        }
    }

    private void setNavigationButtons(Inventory inventory, int page, int totalItems) {
        inventory.setItem(BACK_BUTTON_SLOT, createNavigationButton(true));
        if (hasNextPage(page, totalItems)) {
            inventory.setItem(NEXT_PAGE_SLOT, createNavigationButton(false));
        }
    }

    private ItemStack createAchievementItem(int statValue, PlayerProfile profile, VagtAchievement achievement) {
        HeadDatabaseAPI hdb = new HeadDatabaseAPI();
        boolean completed = statValue >= achievement.requiredAmount;
        ItemStack item = hdb.getItemHead(completed && profile.hasProperty(achievement.property) ? "21774" : "9382");

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName((completed ? "§a" : "§c") + achievement.description);

        List<String> lore = new ArrayList<>();
        lore.add("§7Krav: §f" + achievement.requiredAmount + " units");
        lore.add("§7Bonus: §a+" + String.format("%.2f", achievement.modifier) + "% §7modifier");
        lore.add("");

        if (completed) {
            lore.add("§a✓ Achievement opnået");

            if (!profile.hasProperty(achievement.property)) {
                lore.add("§e⚠ Mangler at blive registreret");
            }
        } else {
            lore.add("§c✗ Mangler " + (achievement.requiredAmount - statValue) + " units");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void openDeathMenu(Player player, int page) {
        int currentDeaths = player.getStatistic(Statistic.DEATHS);
        openAchievementMenu(player, page, DEATH_MENU_TITLE, deathAchievements, currentDeaths);
    }

    public void openKillsMenu(Player player, int page) {
        int currentKills = player.getStatistic(Statistic.PLAYER_KILLS);
        openAchievementMenu(player, page, KILL_MENU_TITLE, killsAchievements, currentKills);
    }
    private ItemStack createNavigationButton(boolean isBackButton) {
        ItemStack button = new ItemStack(Material.ARROW);
        ItemMeta meta = button.getItemMeta();
        if (isBackButton) {
            meta.setDisplayName("§c← Tilbage");
        } else {
            meta.setDisplayName("§aNæste Side →");
        }
        button.setItemMeta(meta);
        return button;
    }
    private boolean hasNextPage(int currentPage, int totalItems) {
        return (currentPage + 1) * ITEMS_PER_PAGE < totalItems;
    }
    private int getCurrentPage(String title) {
        try {
            String[] parts = title.split(" - Side ");
            return Integer.parseInt(parts[1]) - 1;
        } catch (Exception e) {
            return 0; // Default to page 0 if parsing fails
        }

    }

}
