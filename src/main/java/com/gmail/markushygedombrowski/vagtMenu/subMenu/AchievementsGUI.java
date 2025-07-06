package com.gmail.markushygedombrowski.vagtMenu.subMenu;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
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
    private static final int DEATH_ACHIEVEMENT_SLOT = 9;
    private static final int KILL_ACHIEVEMENT_SLOT_ROW = 10;
    private static final int DAMAGE_ACHIEVEMENT_SLOT = 11;
    private static final int BACK_BUTTON_SLOT = 45;
    private static final int NEXT_PAGE_SLOT = 53;
    private static final int ITEMS_PER_PAGE = 36; // Antal achievements per side
    private final HLvagt plugin;
    private final List<VagtAchievement> deathAchievements;
    private final List<VagtAchievement> killsAchievements;
    private final List<VagtAchievement> damageAchievements;

    public AchievementsGUI(HLvagt plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin kan ikke være null");
        }
        this.plugin = plugin;
        this.deathAchievements = initializeDeathAchievements();
        this.killsAchievements = initializeKillsAchievements();
        this.damageAchievements = initializeDamageAchievements();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private List<VagtAchievement> initializeKillsAchievements() {
        List<VagtAchievement> achievementList = new ArrayList<>();


        for (int i = 1; i <= 10; i++) {
            double bonus = 0.05 + ((i - 1) * 0.02);
            achievementList.add(new VagtAchievement(
                    "achievement_kill_" + i,
                    "Dræb " + i + " spiller" + (i == 1 ? "" : "e"),
                    i,
                    bonus,
                    "kill"
            ));
        }


        for (int i = 15; i <= 100; i += 5) {
            double bonus = 0.25 + ((i - 15) / 5.0) * 0.05;
            achievementList.add(new VagtAchievement(
                    "achievement_kill_" + i,
                    "Dræb " + i + " spillere",
                    i,
                    bonus,
                    "kill"
            ));
        }


        for (int i = 125; i <= 250; i += 25) {
            double bonus = 1.0 + ((i - 125) / 25.0) * 0.15;
            achievementList.add(new VagtAchievement(
                    "achievement_kill_" + i,
                    "Dræb " + i + " spillere",
                    i,
                    bonus,
                    "kill"
            ));
        }


        for (int i = 300; i <= 600; i += 50) {
            double bonus = 1.0 + ((i - 150) / 50.0) * 0.25;
            achievementList.add(new VagtAchievement(
                    "achievement_kill_" + i,
                    "Dræb " + i + " spillere",
                    i,
                    bonus,
                    "kill"
            ));
        }


        for (int i = 600; i <= 1000; i += 60) {
            double bonus = 1.0 + ((i - 600) / 60.0) * 0.30;
            achievementList.add(new VagtAchievement(
                    "achievement_kill_" + i,
                    "Dræb " + i + " spillere",
                    i,
                    bonus,
                    "kill"
            ));
        }

        return achievementList;
    }

    private List<VagtAchievement> initializeDeathAchievements() {
        List<VagtAchievement> achievementList = new ArrayList<>();


        for (int i = 1; i <= 10; i++) {
            double penalty = 0.05 + ((i - 1) * 0.02);
            achievementList.add(new VagtAchievement("achievement_death_" + i,
                    "Dø " + i + " gang" + (i == 1 ? "" : "e"), i, penalty, "damage"));
        }


        for (int i = 15; i <= 100; i += 5) {
            double penalty = 0.25 + ((i - 15) / 5.0) * 0.05;
            achievementList.add(new VagtAchievement("achievement_death_" + i,
                    "Dø " + i + " gange", i, penalty, "damage"));
        }


        for (int i = 125; i <= 250; i += 25) {
            double penalty = 1.0 + ((i - 125) / 25.0) * 0.15;
            achievementList.add(new VagtAchievement("achievement_death_" + i,
                    "Dø " + i + " gange", i, penalty, "damage"));
        }

        for (int i = 300; i <= 500; i += 50) {
            double penalty = 1.80 + ((i - 300) / 50.0) * 0.25;
            achievementList.add(new VagtAchievement("achievement_death_" + i,
                    "Dø " + i + " gange", i, penalty, "damage"));
        }


        return achievementList;
    }

    private List<VagtAchievement> initializeDamageAchievements() {
        List<VagtAchievement> achievementList = new ArrayList<>();
        double currentBonus = 0.001;


        for (int damage = 5; damage <= 500; damage += 15) {
            achievementList.add(new VagtAchievement(
                    "achievement_damage_" + damage,
                    "Giv " + damage + " damage",
                    damage,
                    currentBonus,
                    "damage"
            ));
            currentBonus *= 2;
        }

        return achievementList;
    }

    public void openMenu(Player player) {
        if (player == null) return;

        try {
            Inventory mainMenu = createMainMenu(player);
            player.openInventory(mainMenu);
        } catch (Exception e) {
            handleMenuOpenError(player, e);
        }
    }

    private Inventory createMainMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, INVENTORY_SIZE, MENU_TITLE);
        ItemStack deathButton = createDeathMenuButton(player);
        ItemStack killButton = createKillMenuButton(player);
        ItemStack damageButton = createDamageMenuButton(player);
        menu.setItem(DEATH_ACHIEVEMENT_SLOT, deathButton);
        menu.setItem(KILL_ACHIEVEMENT_SLOT_ROW, killButton);
        menu.setItem(DAMAGE_ACHIEVEMENT_SLOT, damageButton);
        fillEmptySlots(menu);
        return menu;
    }

    private ItemStack createDeathMenuButton(Player player) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 1);
        ItemMeta meta = skull.getItemMeta();
        meta.setDisplayName("§c☠ Døds Achievements ☠");

        List<String> lore = new ArrayList<>();
        int deaths = player.getStatistic(Statistic.DEATHS);
        lore.add("§7Dine totale dødsfald: §c" + deaths);
        lore.add("");
        lore.add("§7Achievements opnået:");

        PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());
        int achieved = 0;
        if (profile != null) {
            for (VagtAchievement achievement : deathAchievements) {
                if (profile.hasProperty(achievement.property)) {
                    achieved++;
                }
            }
        }
        lore.add("§7" + achieved + "§8/§7" + deathAchievements.size());
        lore.add("");
        lore.add("§eKlik for at se alle achievements");

        meta.setLore(lore);
        skull.setItemMeta(meta);
        return skull;
    }

    private ItemStack createKillMenuButton(Player player) {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName("§a☠ Kills Achievements ☠");

        List<String> lore = new ArrayList<>();
        int kills = player.getStatistic(Statistic.PLAYER_KILLS);
        lore.add("§7Dine totale kills: §a" + kills);
        lore.add("");
        lore.add("§7Achievements opnået:");

        PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());
        int achieved = 0;
        if (profile != null) {
            for (VagtAchievement achievement : killsAchievements) {
                if (profile.hasProperty(achievement.property)) {
                    achieved++;
                }
            }
        }
        lore.add("§7" + achieved + "§8/§7" + killsAchievements.size());
        lore.add("");
        lore.add("§eKlik for at se alle achievements");

        meta.setLore(lore);
        sword.setItemMeta(meta);
        return sword;
    }

    private ItemStack createDamageMenuButton(Player player) {
        HeadDatabaseAPI hdb = new HeadDatabaseAPI();
        ItemStack sword = hdb.getItemHead("21774");
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName("§6⚔ Damage Achievements ⚔");

        List<String> lore = new ArrayList<>();
        PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());


        int totalDamage = 0;
        try {
            if (!profile.hasProperty("total_damage")) {
                profile.setProperty("total_damage", "0");
            }
            String damageStr = profile.getProperty("total_damage").toString();
            totalDamage = damageStr != null ? Integer.parseInt(damageStr) : 0;
        } catch (NumberFormatException e) {
            profile.setProperty("total_damage", "0");
        }

        lore.add("§7Total damage dealt: §6" + totalDamage);
        lore.add("");
        lore.add("§7Achievements opnået:");

        int achieved = 0;
        if (profile != null) {
            for (VagtAchievement achievement : damageAchievements) {
                if (profile.hasProperty(achievement.property)) {
                    achieved++;
                }
            }
        }
        lore.add("§7" + achieved + "§8/§7" + damageAchievements.size());
        lore.add("");
        lore.add("§eKlik for at se alle achievements");

        meta.setLore(lore);
        sword.setItemMeta(meta);
        return sword;
    }

    public void openDeathMenu(Player player, int page) {
        Inventory inventory = Bukkit.createInventory(null, INVENTORY_SIZE, DEATH_MENU_TITLE + " - Side " + page);
        PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());

        if (profile != null) {
            int currentDeaths = player.getStatistic(Statistic.DEATHS);
            int startIndex = page * ITEMS_PER_PAGE;

            for (int i = 0; i < ITEMS_PER_PAGE && (startIndex + i) < deathAchievements.size(); i++) {
                VagtAchievement achievement = deathAchievements.get(startIndex + i);
                ItemStack item = createDeathAchievementItem(currentDeaths, profile, achievement);
                inventory.setItem(i, item);
            }


            inventory.setItem(BACK_BUTTON_SLOT, createNavigationButton(true));
            if (hasNextPage(page, deathAchievements.size())) {
                inventory.setItem(NEXT_PAGE_SLOT, createNavigationButton(false));
            }
        }

        fillEmptySlots(inventory);
        player.openInventory(inventory);
    }

    private ItemStack createDeathAchievementItem(int currentDeaths, PlayerProfile profile, VagtAchievement achievement) {
        HeadDatabaseAPI hdb = new HeadDatabaseAPI();
        ItemStack item;
        boolean completed = currentDeaths >= achievement.requiredDeaths;

        if (completed && profile.hasProperty(achievement.property)) {
            item = hdb.getItemHead("21774");
        } else {
            item = hdb.getItemHead("9382");
        }

        ItemMeta meta = item.getItemMeta();
        String color = completed ? "§a" : "§c";
        meta.setDisplayName(color + achievement.description);

        List<String> lore = new ArrayList<>();
        lore.add("§7Krav: §f" + achievement.requiredDeaths + " dødsfald");
        lore.add("§7Straf: §c-" + String.format("%.2f", achievement.penaltyPercent) + "% §7af din løn");
        lore.add("");

        if (completed) {
            lore.add("§a✓ Achievement opnået");
            if (!profile.hasProperty(achievement.property)) {
                lore.add("§e⚠ Mangler at blive registreret");
            }
        } else {
            lore.add("§c✗ Mangler " + (achievement.requiredDeaths - currentDeaths) + " dødsfald");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private void fillEmptySlots(Inventory inventory) {
        short[] colors = {14};

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, colors[0]);
                ItemMeta glassMeta = glass.getItemMeta();
                glassMeta.setDisplayName("§r");
                glass.setItemMeta(glassMeta);
                inventory.setItem(i, glass);
            }
        }
    }

    private boolean hasNextPage(int currentPage, int totalItems) {

        return currentPage == 0 && (totalItems > ITEMS_PER_PAGE);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        String title = event.getView().getTitle();
        if (!title.startsWith("§8") && !title.contains("Vagt")) {
            return;
        }

        event.setCancelled(true);

        if (event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.CHEST) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        int currentPage = getCurrentPage(title);


        if (title.equals(MENU_TITLE)) {
            handleMainMenuClick(player, slot);
            return;
        }


        handleNavigation(player, slot, title, currentPage);
    }

    private void handleMainMenuClick(Player player, int slot) {
        if (slot == DEATH_ACHIEVEMENT_SLOT) {
            openDeathMenu(player, 0);
        } else if (slot == KILL_ACHIEVEMENT_SLOT_ROW) {
            openKillsMenu(player, 0);
        } else if (slot == DAMAGE_ACHIEVEMENT_SLOT) {
            openDamageMenu(player, 0);
        }
    }

    private void handleNavigation(Player player, int slot, String title, int currentPage) {
        if (slot == BACK_BUTTON_SLOT) {
            openMenu(player);
        } else if (slot == NEXT_PAGE_SLOT) {
            if (title.startsWith(DEATH_MENU_TITLE)) {
                openDeathMenu(player, currentPage + 1);
            } else if (title.startsWith(KILL_MENU_TITLE)) {
                openKillsMenu(player, currentPage + 1);
            } else if (title.startsWith(DAMAGE_MENU_TITLE)) {
                openDamageMenu(player, currentPage + 1);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        String title = event.getView().getTitle();

        if (title.startsWith("§8") || title.contains("Vagt")) {
            event.setCancelled(true);
        }
    }

    private int getCurrentPage(String title) {
        try {
            String[] parts = title.split(" - Side ");
            if (parts.length > 1) {
                return Integer.parseInt(parts[1].trim()) - 1;
            }
        } catch (NumberFormatException e) {

        }
        return 0;
    }


    public void openKillsMenu(Player player, int page) {
        if (page < 0 || player == null) return;

        Inventory inventory = Bukkit.createInventory(null, INVENTORY_SIZE, KILL_MENU_TITLE + " - Side " + (page + 1));
        PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());

        if (profile != null) {
            int currentKills = player.getStatistic(Statistic.PLAYER_KILLS);
            int startIndex = page * ITEMS_PER_PAGE;

            for (int i = 0; i < ITEMS_PER_PAGE && (startIndex + i) < killsAchievements.size(); i++) {
                VagtAchievement achievement = killsAchievements.get(startIndex + i);
                ItemStack item = createKillAchievementItem(currentKills, profile, achievement);
                inventory.setItem(i, item);
            }

            addNavigationButtons(inventory, page, killsAchievements.size());
        }

        fillEmptySlots(inventory);
        player.openInventory(inventory);
    }

    private ItemStack createKillAchievementItem(int currentKills, PlayerProfile profile, VagtAchievement achievement) {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();

        boolean completed = currentKills >= achievement.requiredDeaths;
        String color = completed ? "§a" : "§c";

        meta.setDisplayName(color + achievement.description);

        List<String> lore = new ArrayList<>();
        lore.add("§7Krav: §f" + achievement.requiredDeaths + " kills");
        lore.add("§7Bonus: §a+" + String.format("%.2f", achievement.penaltyPercent) + "% §7til din løn");
        lore.add("");

        if (completed) {
            lore.add("§a✓ Achievement opnået");
            if (!profile.hasProperty(achievement.property)) {
                lore.add("§e⚠ Mangler at blive registreret");
            }
        } else {
            lore.add("§c✗ Mangler " + (achievement.requiredDeaths - currentKills) + " kills");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }


    public void openDamageMenu(Player player, int page) {
        if (page < 0 || player == null) return;

        Inventory inventory = Bukkit.createInventory(null, INVENTORY_SIZE, DAMAGE_MENU_TITLE + " - Side " + (page + 1));
        PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());

        if (profile != null) {
            int currentDamage = 0;
            try {
                if (!profile.hasProperty("total_damage")) {
                    profile.setProperty("total_damage", "0");
                }
                String damageStr = profile.getProperty("total_damage").toString();
                currentDamage = damageStr != null ? Integer.parseInt(damageStr) : 0;
            } catch (NumberFormatException e) {
                profile.setProperty("total_damage", "0");
            }

            int startIndex = page * ITEMS_PER_PAGE;

            for (int i = 0; i < ITEMS_PER_PAGE && (startIndex + i) < damageAchievements.size(); i++) {
                VagtAchievement achievement = damageAchievements.get(startIndex + i);
                ItemStack item = createDamageAchievementItem(currentDamage, profile, achievement);
                inventory.setItem(i, item);
            }

            addNavigationButtons(inventory, page, damageAchievements.size());
        }

        fillEmptySlots(inventory);
        player.openInventory(inventory);
    }

    private ItemStack createDamageAchievementItem(int currentDamage, PlayerProfile profile, VagtAchievement achievement) {
        HeadDatabaseAPI hdb = new HeadDatabaseAPI();
        ItemStack item;
        boolean completed = currentDamage >= achievement.requiredAmount;

        if (completed && profile.hasProperty(achievement.property)) {
            item = hdb.getItemHead("21774");
        } else {
            item = hdb.getItemHead("9382");
        }

        ItemMeta meta = item.getItemMeta();
        String color = completed ? "§a" : "§c";
        meta.setDisplayName(color + achievement.description);

        List<String> lore = new ArrayList<>();
        lore.add("§7Krav: §f" + achievement.requiredAmount + " damage");
        lore.add("§7Bonus: §a+" + String.format("%.3f", achievement.percentModifier) + "% §7af din bonus løn");
        lore.add("");
        lore.add("§7Nuværende damage: §f" + currentDamage);

        if (completed) {
            lore.add("§a✓ Achievement opnået");
            if (!profile.hasProperty(achievement.property)) {
                lore.add("§e⚠ Mangler at blive registreret");
            }
        } else {
            lore.add("§c✗ Mangler " + (achievement.requiredAmount - currentDamage) + " damage");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static class VagtAchievement {
        private final String property;
        private final String description;
        private final int requiredDeaths;
        private final double penaltyPercent;
        public int requiredAmount;
        public Object percentModifier;

        public VagtAchievement(String property, String description, int requiredDeaths, double penaltyPercent, String damage) {
            this.property = property;
            this.description = description;
            this.requiredDeaths = requiredDeaths;
            this.penaltyPercent = penaltyPercent;
        }
    }

    private static final String ERROR_MESSAGE_PLAYER = "§cDer skete en fejl ved åbning af achievement menu";
    private static final String ERROR_MESSAGE_LOG = "Fejl ved åbning af achievement menu for %s: %s";

    private void handleMenuOpenError(Player player, Exception e) {
        player.sendMessage(ERROR_MESSAGE_PLAYER);
        plugin.getLogger().warning(String.format(ERROR_MESSAGE_LOG, player.getName(), e.getMessage()));
        e.printStackTrace();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        try {

            plugin.getVagtAchievements().checkDeathAchievement(victim);


            if (killer != null) {
                plugin.getVagtAchievements().checkKillAchievement(killer);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    private void addNavigationButtons(Inventory inventory, int currentPage, int totalItems) {

        inventory.setItem(BACK_BUTTON_SLOT, createNavigationButton(true));


        if (hasNextPage(currentPage, totalItems)) {
            inventory.setItem(NEXT_PAGE_SLOT, createNavigationButton(false));
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player damager = (Player) event.getDamager();
        double damage = event.getFinalDamage();

        PlayerProfile profile = plugin.getPlayerProfile(damager.getUniqueId());

        int currentDamage = profile.castPropertyToInt(profile.getProperty("total_damage"));
        profile.setProperty("total_damage", String.valueOf(currentDamage + (int) damage));


    }
}