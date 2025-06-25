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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.entity.PlayerDeathEvent;
import java.util.EventObject;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;

public class AchievementsGUI implements Listener {
    // Opdater menu titler til at matche tema med §8 (sort)
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

        // De første 10 achievements (1-10 kills)
        for (int i = 1; i <= 10; i++) {
            double bonus = 0.05 + ((i - 1) * 0.02);
            achievementList.add(new VagtAchievement("achievement_kill_" + i,
                    "Dræb " + i + " spiller" + (i == 1 ? "" : "e"), i, bonus, "damage"));
        }

        // Fra 15 til 100 med spring på 5
        for (int i = 15; i <= 100; i += 5) {
            double bonus = 0.25 + ((i - 15) / 5.0) * 0.05;
            achievementList.add(new VagtAchievement("achievement_kill_" + i,
                    "Dræb " + i + " spillere", i, bonus, "damage"));
        }

        // Fra 125 til 250 med spring på 25
        for (int i = 125; i <= 250; i += 25) {
            double bonus = 1.0 + ((i - 125) / 25.0) * 0.15;
            achievementList.add(new VagtAchievement("achievement_kill_" + i,
                    "Dræb " + i + " spillere", i, bonus, "damage"));
        }

        // Fra 300 til 500 med spring på 50
        for (int i = 300; i <= 500; i += 50) {
            double bonus = 1.80 + ((i - 300) / 50.0) * 0.25;
            achievementList.add(new VagtAchievement("achievement_kill_" + i,
                    "Dræb " + i + " spillere", i, bonus, "damage"));
        }

        return achievementList;
    }

    private List<VagtAchievement> initializeDeathAchievements() {
        List<VagtAchievement> achievementList = new ArrayList<>();

        // De første 10 achievements (1-10 dødsfald)
        for (int i = 1; i <= 10; i++) {
            double penalty = 0.05 + ((i - 1) * 0.02);
            achievementList.add(new VagtAchievement("achievement_death_" + i,
                    "Dø " + i + " gang" + (i == 1 ? "" : "e"), i, penalty, "damage"));
        }

        // Fra 15 til 100 med spring på 5
        for (int i = 15; i <= 100; i += 5) {
            double penalty = 0.25 + ((i - 15) / 5.0) * 0.05;
            achievementList.add(new VagtAchievement("achievement_death_" + i,
                    "Dø " + i + " gange", i, penalty, "damage"));
        }

        // Fra 125 til 250 med spring på 25
        for (int i = 125; i <= 250; i += 25) {
            double penalty = 1.0 + ((i - 125) / 25.0) * 0.15;
            achievementList.add(new VagtAchievement("achievement_death_" + i,
                    "Dø " + i + " gange", i, penalty, "damage"));
        }
        // Fra 300 til 500 med spring på 50
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
    
    // Start med 5 damage og forøg med 15
    for (int damage = 5; damage <= 500; damage += 15) {
        achievementList.add(new VagtAchievement(
            "achievement_damage_" + damage,
            "Giv " + damage + " damage",
            damage,
            currentBonus,
            "damage"
        ));
        currentBonus *= 2; // Fordobl bonus for hvert niveau
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
    ItemStack sword = hdb.getItemHead("21774"); // Brug et passende head ID
    ItemMeta meta = sword.getItemMeta();
    meta.setDisplayName("§6⚔ Damage Achievements ⚔");

    List<String> lore = new ArrayList<>();
    int totalDamage = player.getStatistic(Statistic.DAMAGE_DEALT);
    lore.add("§7Total damage dealt: §6" + totalDamage);
    lore.add("");
    lore.add("§7Achievements opnået:");

    PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());
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
    Inventory deathMenu = Bukkit.createInventory(null, INVENTORY_SIZE, DEATH_MENU_TITLE + " - Side " + (page + 1));
    PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());

    if (profile != null) {
        int currentDeaths = player.getStatistic(Statistic.DEATHS);
        int startIndex = page * ITEMS_PER_PAGE;
        
        for (int i = 0; i < ITEMS_PER_PAGE && (startIndex + i) < deathAchievements.size(); i++) {
            VagtAchievement achievement = deathAchievements.get(startIndex + i);
            ItemStack item = createDeathAchievementItem(currentDeaths, profile, achievement);
            deathMenu.setItem(i, item);
        }

        // Tilføj navigationsknapper
        deathMenu.setItem(BACK_BUTTON_SLOT, createNavigationButton(true));
        if (hasNextPage(page, deathAchievements.size())) {
            deathMenu.setItem(NEXT_PAGE_SLOT, createNavigationButton(false));
        }
    }

    fillEmptySlots(deathMenu);
    player.openInventory(deathMenu);
}

private ItemStack createDeathAchievementItem(int currentDeaths, PlayerProfile profile, VagtAchievement achievement) {
    HeadDatabaseAPI hdb = new HeadDatabaseAPI();
    ItemStack item;
    boolean completed = currentDeaths >= achievement.requiredDeaths;
    
    if (completed && profile.hasProperty(achievement.property)) {
        item = hdb.getItemHead("21774"); // Checkmark for gennemført
    } else {
        item = hdb.getItemHead("9382"); // Kryds for ikke gennemført
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

    // Tilføj sort glas som baggrund
private void fillEmptySlots(Inventory inventory) {
    // Array med alle mulige farve-værdier for farvet glas
    short[] colors = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 13, 14, 15};
    
    for (int i = 0; i < inventory.getSize(); i++) {
        if (inventory.getItem(i) == null) {
            // Vælg en tilfældig farve fra colors array
            short randomColor = colors[(int) (Math.random() * colors.length)];
            ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, randomColor);
            ItemMeta glassMeta = glass.getItemMeta();
            glassMeta.setDisplayName("§r");
            glass.setItemMeta(glassMeta);
            inventory.setItem(i, glass);
        }
    }
}

private boolean hasNextPage(int currentPage, int totalItems) {
    // Kun tillad én ekstra side (side 0 og side 1)
    return currentPage == 0 && (totalItems > ITEMS_PER_PAGE);
}

@EventHandler
public void onInventoryClick(InventoryClickEvent event) {
    String title = event.getView().getTitle();
    
    // Check om det er en af vores menuer
    if (title.equals(MENU_TITLE) || 
        title.startsWith(DEATH_MENU_TITLE) || 
        title.startsWith(KILL_MENU_TITLE) || 
        title.startsWith(DAMAGE_MENU_TITLE)) {
        
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
        
        // Hvis det ikke er en spiller, annuller eventet
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        
        // Håndter kun clicks i top inventory (menu)
        if (event.getClickedInventory() != null && 
            event.getClickedInventory().getType() == InventoryType.CHEST) {
            
            // Menu navigation
            if (title.equals(MENU_TITLE)) {
                if (event.getRawSlot() == DEATH_ACHIEVEMENT_SLOT) {
                    openDeathMenu(player, 0);
                } else if (event.getRawSlot() == KILL_ACHIEVEMENT_SLOT_ROW) {
                    openKillsMenu(player, 0);
                } else if (event.getRawSlot() == DAMAGE_ACHIEVEMENT_SLOT) {
                    openDamageMenu(player, 0);
                }
            } else {
                if (event.getRawSlot() == BACK_BUTTON_SLOT) {
                    openMenu(player);
                } else if (event.getRawSlot() == NEXT_PAGE_SLOT) {
                    int currentPage = getCurrentPage(title);
                    if (title.startsWith(DEATH_MENU_TITLE)) {
                        openDeathMenu(player, currentPage + 1);
                    } else if (title.startsWith(KILL_MENU_TITLE)) {
                        openKillsMenu(player, currentPage + 1);
                    } else if (title.startsWith(DAMAGE_MENU_TITLE)) {
                        openDamageMenu(player, currentPage + 1);
                    }
                }
            }
        }
    }
}

@EventHandler
public void onInventoryDrag(InventoryDragEvent event) {
    String title = event.getView().getTitle();
    
    // Check om det er en af vores menuer
    if (title.equals(MENU_TITLE) || 
        title.startsWith(DEATH_MENU_TITLE) || 
        title.startsWith(KILL_MENU_TITLE) || 
        title.startsWith(DAMAGE_MENU_TITLE)) {
        
        event.setCancelled(true);
        for (Integer slot : event.getRawSlots()) {
            if (slot < event.getInventory().getSize()) {
                event.setCancelled(true);
                return;
            }
        }
    }
}

private int getCurrentPage(String title) {
    try {
        String[] parts = title.split(" - Side ");
        if (parts.length > 1) {
            return Integer.parseInt(parts[1].trim()) - 1;
        }
    } catch (NumberFormatException e) {
        // Ignorer parsing fejl
    }
    return 0;
}

public void openKillsMenu(Player player, int page) {
    Inventory killsMenu = Bukkit.createInventory(null, INVENTORY_SIZE, KILL_MENU_TITLE + " - Side " + (page + 1));
    PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());

    if (profile != null) {
        int currentKills = player.getStatistic(Statistic.PLAYER_KILLS);
        int startIndex = page * ITEMS_PER_PAGE;
        
        for (int i = 0; i < ITEMS_PER_PAGE && (startIndex + i) < killsAchievements.size(); i++) {
            VagtAchievement achievement = killsAchievements.get(startIndex + i);
            ItemStack item = createKillAchievementItem(currentKills, profile, achievement);
            killsMenu.setItem(i, item);
        }

        // Tilføj navigationsknapper
        killsMenu.setItem(BACK_BUTTON_SLOT, createNavigationButton(true));
        if (hasNextPage(page, killsAchievements.size())) {
            killsMenu.setItem(NEXT_PAGE_SLOT, createNavigationButton(false));
        }
    }

    fillEmptySlots(killsMenu);
    player.openInventory(killsMenu);
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
    Inventory damageMenu = Bukkit.createInventory(null, INVENTORY_SIZE, DAMAGE_MENU_TITLE + " - Side " + (page + 1));
    PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());

    if (profile != null) {
        int currentDamage = player.getStatistic(Statistic.DAMAGE_DEALT);
        int startIndex = page * ITEMS_PER_PAGE;
        
        for (int i = 0; i < ITEMS_PER_PAGE && (startIndex + i) < damageAchievements.size(); i++) {
            VagtAchievement achievement = damageAchievements.get(startIndex + i);
            ItemStack item = createDamageAchievementItem(currentDamage, profile, achievement);
            damageMenu.setItem(i, item);
        }

        damageMenu.setItem(BACK_BUTTON_SLOT, createNavigationButton(true));
        if (hasNextPage(page, damageAchievements.size())) {
            damageMenu.setItem(NEXT_PAGE_SLOT, createNavigationButton(false));
        }
    }

    fillEmptySlots(damageMenu);
    player.openInventory(damageMenu);
}

private ItemStack createDamageAchievementItem(int currentDamage, PlayerProfile profile, VagtAchievement achievement) {
    HeadDatabaseAPI hdb = new HeadDatabaseAPI();
    ItemStack item;
    boolean completed = currentDamage >= achievement.requiredAmount;
    
    if (completed && profile.hasProperty(achievement.property)) {
        item = hdb.getItemHead("21774"); // Checkmark
    } else {
        item = hdb.getItemHead("9382"); // Kryds
    }
    
    ItemMeta meta = item.getItemMeta();
    String color = completed ? "§a" : "§c";
    meta.setDisplayName(color + achievement.description);

    List<String> lore = new ArrayList<>();
    lore.add("§7Krav: §f" + achievement.requiredAmount + " damage");
    lore.add("§7Bonus: §a+" + String.format("%.3f", achievement.percentModifier) + "% §7af din bonus løn");
    lore.add("");

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

}