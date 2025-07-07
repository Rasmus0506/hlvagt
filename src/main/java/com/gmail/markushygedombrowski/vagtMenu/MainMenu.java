package com.gmail.markushygedombrowski.vagtMenu;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.cooldown.VagtCooldown;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.*;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.topVagter.TopVagterGUI;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MainMenu implements Listener {
    private HLvagt plugin;
    private PVGUI pvgui;
    private TopVagterGUI topVagterGUI;
    private final int SPILTID_INDEX = 11;
    private final int STATS_INDEX = 13;
    private final int TOPVAGT_INDEX = 31;
    private final int ACHIVEMENT_INDEX = 49;
    private final int PV_INDEX = 28;
    private final int RANKUP_INDEX = 34;
    private final int VAGTLVL_INDEX = 47;
    private final int L0N_INDEX = 15;
    private final int SETTINGS_INDEX = 51;
    private HeadDatabaseAPI api = new HeadDatabaseAPI();
    private PlayerProfiles playerProfiles;
    private StatsGUI statsGUI;
    private RankupGUI rankupGUI;
    private VagtLevelGUI vagtLevelGUI;
    private AchievementsGUI achievementsGUI;

    public MainMenu(HLvagt plugin, PVGUI pvgui, TopVagterGUI topVagterGUI, PlayerProfiles playerProfiles,
                    StatsGUI statsGUI, RankupGUI rankupGUI, VagtLevelGUI vagtLevelGUI, AchievementsGUI achievementsGUI) {
        this.plugin = plugin;
        this.pvgui = pvgui;
        this.topVagterGUI = topVagterGUI;
        this.playerProfiles = playerProfiles;
        this.statsGUI = statsGUI;
        this.rankupGUI = rankupGUI;
        this.vagtLevelGUI = vagtLevelGUI;
        this.achievementsGUI = achievementsGUI; // KORREKT - brug parameteren
    }

    public void create(Player p, PlayerProfile profile) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§cVagt Menu §8" + p.getName());
        meta(p, profile, inventory);


        p.openInventory(inventory);

    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        try {
            if (!(event.getWhoClicked() instanceof Player)) {
                return;
            }

            Player p = (Player) event.getWhoClicked();
            String title = event.getView().getTitle();

            if (title.contains("Vagt Menu") || title.contains("§cVagt")) {
                event.setCancelled(true);

                int clickedSlot = event.getSlot();

                switch (clickedSlot) {
                    case L0N_INDEX:
                        if (VagtCooldown.isCooling(p.getName(), "lon")) {
                            VagtCooldown.coolDurMessage(p, "lon");
                            return;
                        } else {
                            plugin.getLon().giveLon(p);
                        }
                        // Opdater GUI'en efter handling
                        meta(p, plugin.getPlayerProfile(p.getUniqueId()), event.getInventory());
                        break;
                    case PV_INDEX:
                        pvgui.create(p);
                        break;
                    case STATS_INDEX:
                        statsGUI.create(p);
                        break;
                    case TOPVAGT_INDEX:
                        topVagterGUI.create(p);
                        break;
                    case ACHIVEMENT_INDEX:
                        achievementsGUI.openMenu(p);
                        break;
                    case RANKUP_INDEX:
                        rankupGUI.create(p);
                        break;
                    case VAGTLVL_INDEX:
                        vagtLevelGUI.openVagtLevelGUI(p);
                        break;
                }
            }
        } catch (NullPointerException e) {
            plugin.getLogger().warning("NullPointerException i MainMenu.onClickEvent: " + e.getMessage());
        }
    }

    public void meta(Player p, PlayerProfile profile, Inventory inventory) {

        ItemStack topvagt = api.getItemHead("846");
        ItemStack spilletid = api.getItemHead("2122");
        ItemStack pv = api.getItemHead("1193");
        ItemStack achivement = api.getItemHead("3231");
        ItemStack rankup = api.getItemHead("36770");
        ItemStack vagtlvl = api.getItemHead("53095");
        ItemStack lon = api.getItemHead("66671");
        ItemStack settings = api.getItemHead("23458");
        ItemStack stats = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        String pattern = "###,###.##";
        DecimalFormat df = new DecimalFormat(pattern);
        int seconds = p.getStatistic(Statistic.PLAY_ONE_TICK) / 20;
        int minutes = (seconds / 60);
        int hours = (minutes / 60);
        int actualMinutes = minutes - (hours * 60);

        SkullMeta statsmeta = (SkullMeta) stats.getItemMeta();
        statsmeta.setOwner(p.getName());
        stats.setItemMeta(statsmeta);

        ItemMeta metastats = stats.getItemMeta();
        ItemMeta metatopv = topvagt.getItemMeta();

        ItemMeta metatid = spilletid.getItemMeta();
        ItemMeta metapv = pv.getItemMeta();
        ItemMeta metaachive = achivement.getItemMeta();
        ItemMeta metarankup = rankup.getItemMeta();
        ItemMeta metavagtlvl = vagtlvl.getItemMeta();
        ItemMeta metalon = lon.getItemMeta();
        ItemMeta metasettings = settings.getItemMeta();


        metastats.setDisplayName("§7[§a§lStats§7]");
        metatopv.setDisplayName("§7[§cTopVagter§7]");
        metatid.setDisplayName("§7[§aSpilleTid§7]");
        metapv.setDisplayName("§7[§cPV§7]");
        metaachive.setDisplayName("§7[§aAchivements§7]");
        metarankup.setDisplayName("§7[§2Rank up§7]");
        metavagtlvl.setDisplayName("§7[§cVagt-Levels§7]");
        metalon.setDisplayName("§7[§aLøn§7]");
        metasettings.setDisplayName("§7[§cSettings§7]");


        List<String> topVagterLore = new ArrayList<>();
        topVagterLore.add("§7Se §aTopVagterne");

        List<String> statsLore = new ArrayList<>();
        statsLore.add("§6§lSe dine Stats§7");
        statsLore.add("§a§lLevel: §f" + profile.getProperty("level"));
        statsLore.add("§b§lXP: §f" + profile.getProperty("exp") + "/" + profile.getXpToNextLvl());
        statsLore.add("§c§lDød: §f" + profile.getProperty("deaths"));
        statsLore.add("§a§lDræbt: §f" + profile.getProperty("kills"));
        statsLore.add("§a§lPenge: §f$" + df.format(plugin.econ.getBalance(p)));
        statsLore.add("§a§lVagt Poster: §f" + profile.getProperty("vagtposter"));
        statsLore.add("§a§lShard Rate: §f" + profile.getProperty("shardsrate"));


        List<String> spilletidlore = new ArrayList<>();
        spilletidlore.add("§9" + hours + " §6Hours");
        spilletidlore.add("§9" + actualMinutes + " §6Minutes");


        List<String> lonLore = new ArrayList<>();
        double basisLon = profile.castPropertyToInt(profile.getProperty("salary"));
        double penaltyPercent = plugin.getVagtAchievements().calculateTotalSalaryPenalty(profile);
        double bonusPercent = plugin.getVagtAchievements().calculateTotalSalaryBonus(profile);
        double netPercent = bonusPercent - penaltyPercent;
        double finalLon = basisLon * (1 + (netPercent / 100.0));

        lonLore.add("§7Din §2basis-løn: §a$" + basisLon);

        if (penaltyPercent > 0) {
            lonLore.add("§cStraf fra døds-achievements: -" + String.format("%.2f", penaltyPercent) + "%");
        }
        if (bonusPercent > 0) {
            lonLore.add("§aBonus fra kill-achievements: +" + String.format("%.2f", bonusPercent) + "%");
        }
        if (netPercent != 0) {
            String netPercentPrefix = netPercent > 0 ? "§a+" : "§c";
            lonLore.add("§7Samlet bonus/straf: " + netPercentPrefix + String.format("%.2f", netPercent) + "%");
            lonLore.add("§7Samlet §2løn: §a$" + String.format("%.2f", finalLon));
        }

        if (VagtCooldown.isCooling(p.getName(), "lon")) {

        }


        metastats.setLore(statsLore);
        metatid.setLore(spilletidlore);
        metatopv.setLore(topVagterLore);
        metalon.setLore(lonLore);


        stats.setItemMeta(metastats);
        topvagt.setItemMeta(metatopv);
        spilletid.setItemMeta(metatid);
        pv.setItemMeta(metapv);
        achivement.setItemMeta(metaachive);
        rankup.setItemMeta(metarankup);
        vagtlvl.setItemMeta(metavagtlvl);
        lon.setItemMeta(metalon);
        settings.setItemMeta(metasettings);

        inventory.setItem(STATS_INDEX, stats);
        inventory.setItem(TOPVAGT_INDEX, topvagt);
        inventory.setItem(SPILTID_INDEX, spilletid);
        inventory.setItem(PV_INDEX, pv);
        inventory.setItem(ACHIVEMENT_INDEX, achivement);
        inventory.setItem(RANKUP_INDEX, rankup);
        inventory.setItem(VAGTLVL_INDEX, vagtlvl);
        inventory.setItem(L0N_INDEX, lon);
        inventory.setItem(SETTINGS_INDEX, settings);
        inventory.forEach(item -> {
            if (item == null) {
                inventory.setItem(inventory.firstEmpty(), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5));
            }
        });

    }

    private ItemStack createDamageMenuButton(Player player) {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName("§6⚔ Damage Achievements ⚔");

        List<String> lore = new ArrayList<>();

        double totalDamage = Math.abs(player.getStatistic(Statistic.DAMAGE_DEALT)) / 10.0;
        lore.add("§7Total damage givet: §6" + String.format("%.1f", totalDamage) + " hjerter");
        lore.add("");
        lore.add("§7Achievements opnået:");

        PlayerProfile profile = plugin.getPlayerProfile(player.getUniqueId());
        int achieved = 0;
        int total = 10;


        for (int i = 5; i <= 140; i += 15) {
            String achievementKey = "achievement_damage_" + i;
            if (profile != null && profile.hasProperty(achievementKey)) {
                achieved++;
            }
        }

        lore.add("§7" + achieved + "§8/§7" + total);
        lore.add("");
        lore.add("§eKlik for at se alle achievements");

        meta.setLore(lore);
        sword.setItemMeta(meta);
        return sword;
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        String title = event.getView().getTitle();
        if (title.contains("Vagt Menu") || title.contains("§cVagt")) {
            event.setCancelled(true);
        }
    }
}