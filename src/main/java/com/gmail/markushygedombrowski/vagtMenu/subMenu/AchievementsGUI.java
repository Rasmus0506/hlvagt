package com.gmail.markushygedombrowski.vagtMenu.subMenu;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
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

import java.util.ArrayList;
import java.util.List;

public class AchievementsGUI implements Listener {
    // Opdater menu titler til at matche tema med §8 (sort)
    private static final String MENU_TITLE = "§8Vagt Achievements";
    private static final String DEATH_MENU_TITLE = "§8Døds Achievements";
    private static final String KILL_MENU_TITLE = "§8Kills Achievements";
    private static final int INVENTORY_SIZE = 54;
    private static final int DEATH_ACHIEVEMENT_SLOT = 9;
    private static final int KILL_ACHIEVEMENT_SLOT_ROW = 10;

    private final HLvagt plugin;
    private final List<VagtAchievement> deathAchievements;
    private final List<VagtAchievement> killsAchievements;

    public AchievementsGUI(HLvagt plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin kan ikke være null");
        }
        this.plugin = plugin;
        this.deathAchievements = initializeDeathAchievements();
        this.killsAchievements = initializeKillsAchievements();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private List<VagtAchievement> initializeKillsAchievements() {
        List<VagtAchievement> achievementList = new ArrayList<>();

        // De første 10 achievements (1-10 kills)
        for (int i = 1; i <= 10; i++) {
            double bonus = 0.05 + ((i - 1) * 0.02);
            achievementList.add(new VagtAchievement("achievement_kill_" + i,
                    "Dræb " + i + " spiller" + (i == 1 ? "" : "e"), i, bonus));
        }

        // Fra 15 til 100 med spring på 5
        for (int i = 15; i <= 100; i += 5) {
            double bonus = 0.25 + ((i - 15) / 5.0) * 0.05;
            achievementList.add(new VagtAchievement("achievement_kill_" + i,
                    "Dræb " + i + " spillere", i, bonus));
        }

        // Fra 125 til 250 med spring på 25
        for (int i = 125; i <= 250; i += 25) {
            double bonus = 1.0 + ((i - 125) / 25.0) * 0.15;
            achievementList.add(new VagtAchievement("achievement_kill_" + i,
                    "Dræb " + i + " spillere", i, bonus));
        }

        // Fra 300 til 500 med spring på 50
        for (int i = 300; i <= 500; i += 50) {
            double bonus = 1.80 + ((i - 300) / 50.0) * 0.25;
            achievementList.add(new VagtAchievement("achievement_kill_" + i,
                    "Dræb " + i + " spillere", i, bonus));
        }

        return achievementList;
    }

    private List<VagtAchievement> initializeDeathAchievements() {
        List<VagtAchievement> achievementList = new ArrayList<>();

        // De første 10 achievements (1-10 dødsfald)
        for (int i = 1; i <= 10; i++) {
            double penalty = 0.05 + ((i - 1) * 0.02);
            achievementList.add(new VagtAchievement("achievement_death_" + i,
                    "Dø " + i + " gang" + (i == 1 ? "" : "e"), i, penalty));
        }

        // Fra 15 til 100 med spring på 5
        for (int i = 15; i <= 100; i += 5) {
            double penalty = 0.25 + ((i - 15) / 5.0) * 0.05;
            achievementList.add(new VagtAchievement("achievement_death_" + i,
                    "Dø " + i + " gange", i, penalty));
        }

        // Fra 125 til 250 med spring på 25
        for (int i = 125; i <= 250; i += 25) {
            double penalty = 1.0 + ((i - 125) / 25.0) * 0.15;
            achievementList.add(new VagtAchievement("achievement_death_" + i,
                    "Dø " + i + " gange", i, penalty));
        }
        // Fra 300 til 500 med spring på 50
        for (int i = 300; i <= 500; i += 50) {
            double penalty = 1.80 + ((i - 300) / 50.0) * 0.25;
            achievementList.add(new VagtAchievement("achievement_death_" + i,
                    "Dø " + i + " gange", i, penalty));
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
    menu.setItem(DEATH_ACHIEVEMENT_SLOT, deathButton);
    menu.setItem(KILL_ACHIEVEMENT_SLOT_ROW, killButton);
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
    public void openDeathMenu(Player player) {
        Inventory deathMenu = Bukkit.createInventory(null, INVENTORY_SIZE, DEATH_MENU_TITLE);
        PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());

        if (profile != null) {
            int currentDeaths = player.getStatistic(Statistic.DEATHS);

            for (int i = 0; i < deathAchievements.size(); i++) {
                VagtAchievement achievement = deathAchievements.get(i);
                ItemStack item = createDeathAchievementItem(currentDeaths, profile, achievement);
                deathMenu.setItem(i, item);
            }
        }

        player.openInventory(deathMenu);
    }

    // Opdater farvetemaet i achievement items
    private ItemStack createDeathAchievementItem(int currentDeaths, PlayerProfile profile, VagtAchievement achievement) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 1);
        ItemMeta meta = item.getItemMeta();

        boolean completed = currentDeaths >= achievement.requiredDeaths;
        String color = completed ? "§a" : "§c"; // Grøn for opnået, rød for ikke opnået

        meta.setDisplayName(color + achievement.description);

        List<String> lore = new ArrayList<>();
        lore.add("§7Krav: §f" + achievement.requiredDeaths + " dødsfald"); // Grå tekst med hvid værdi
        lore.add("§7Straf: §c-" + String.format("%.2f", achievement.penaltyPercent) + "% §7af din løn");
        lore.add("");

        if (completed) {
            lore.add("§a✓ Achievement opnået"); // Grøn checkmark
            if (!profile.hasProperty(achievement.property)) {
                lore.add("§e⚠ Mangler at blive registreret"); // Gul advarsel
            }
        } else {
            lore.add("§c✗ Mangler " + (achievement.requiredDeaths - currentDeaths) + " dødsfald"); // Rødt kryds
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    // Tilføj sort glas som baggrund
    private void fillEmptySlots(Inventory inventory) {
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15); // Sort glas (15)
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName("§r"); // Tom navn
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, glass);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        String title = event.getView().getTitle();
        if (title.equals(MENU_TITLE)) {
            event.setCancelled(true);
            if (event.getRawSlot() == DEATH_ACHIEVEMENT_SLOT) {
                openDeathMenu(player);
            } else if (event.getRawSlot() == KILL_ACHIEVEMENT_SLOT_ROW) {
                openKillsMenu(player);
            }
        } else if (title.equals(DEATH_MENU_TITLE) || title.equals(KILL_MENU_TITLE)) {
            event.setCancelled(true);
        }
    }

public void openKillsMenu(Player player) {
    Inventory killsMenu = Bukkit.createInventory(null, INVENTORY_SIZE, KILL_MENU_TITLE);
    PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());

    if (profile != null) {
        int currentKills = player.getStatistic(Statistic.PLAYER_KILLS);

        for (int i = 0; i < killsAchievements.size(); i++) {
            VagtAchievement achievement = killsAchievements.get(i);
            ItemStack item = createKillAchievementItem(currentKills, profile, achievement);
            killsMenu.setItem(i, item);
        }
    }

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
    private static class VagtAchievement {
        private final String property;
        private final String description;
        private final int requiredDeaths;
        private final double penaltyPercent;

        public VagtAchievement(String property, String description, int requiredDeaths, double penaltyPercent) {
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

}