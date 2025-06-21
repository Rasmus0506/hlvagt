package com.gmail.markushygedombrowski.vagtMenu;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.cooldown.VagtCooldown;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.*;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.topVagter.TopVagterGUI;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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
            Player p = (Player) event.getWhoClicked();
            Inventory inventory = event.getClickedInventory();
            ItemStack clickeditem = event.getCurrentItem();
            int clickedSlot = event.getRawSlot();
            if (clickeditem == null) {
                return;
            }
            if (inventory.getTitle().equalsIgnoreCase("§cVagt Menu §8" + p.getName())) {

                switch (clickedSlot) {
                    case PV_INDEX:
                        pvgui.create(p);
                        break;
                    case RANKUP_INDEX:
                        if (p.hasPermission("a-vagt") || p.hasPermission("officer") || p.hasPermission("viceinspektør") || p.hasPermission("inspektør") || p.hasPermission("direktør")) {
                            p.sendMessage("§aDu kan ikke ranke up mere!");
                            break;
                        } else if (p.hasPermission("p-vagt")) {
                            p.sendMessage("§cDu kan ikke rankup som P-vagt!!");
                            break;
                        }
                        rankupGUI.create(p);
                        break;
                    case L0N_INDEX:
                        if (VagtCooldown.isCooling(p.getName(), "lon")) {
                            VagtCooldown.coolDurMessage(p, "lon");
                            System.out.println("Cooldown");
                            break;
                        }
                        System.out.println("no cooldown");
                        break;
                    case TOPVAGT_INDEX:
                        topVagterGUI.create(p);
                        break;
                    case STATS_INDEX:
                        statsGUI.create(p);
                        break;
                    case VAGTLVL_INDEX:
                        vagtLevelGUI.openVagtLevelGUI(p, 1);
                        break;
                    case SETTINGS_INDEX:
                        break;
                    case ACHIVEMENT_INDEX:
                        achievementsGUI.openMenu(p);
                        if (event.getView().getTitle().equals(achievementsGUI)) {
                            event.setCancelled(true);
                            event.setResult(Event.Result.DENY);
                        }

                        break;
                }

                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
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
        // Getting Meta
        ItemMeta metastats = stats.getItemMeta();
        ItemMeta metatopv = topvagt.getItemMeta();

        ItemMeta metatid = spilletid.getItemMeta();
        ItemMeta metapv = pv.getItemMeta();
        ItemMeta metaachive = achivement.getItemMeta();
        ItemMeta metarankup = rankup.getItemMeta();
        ItemMeta metavagtlvl = vagtlvl.getItemMeta();
        ItemMeta metalon = lon.getItemMeta();
        ItemMeta metasettings = settings.getItemMeta();

        // DisplayName
        metastats.setDisplayName("§7[§a§lStats§7]");
        metatopv.setDisplayName("§7[§cTopVagter§7]");
        metatid.setDisplayName("§7[§aSpilleTid§7]");
        metapv.setDisplayName("§7[§cPV§7]");
        metaachive.setDisplayName("§7[§aAchivements§7]");
        metarankup.setDisplayName("§7[§2Rank up§7]");
        metavagtlvl.setDisplayName("§7[§cVagt-Levels§7]");
        metalon.setDisplayName("§7[§aLøn§7]");
        metasettings.setDisplayName("§7[§cSettings§7]");

        // Lores

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


        // I meta metoden, opdater lonLore listen:
        List<String> lonLore = new ArrayList<>();
        double basisLon = profile.castPropertyToInt(profile.getProperty("salary"));
        double penaltyPercent = plugin.getVagtAchievements().calculateTotalSalaryPenalty(profile);
        double finalLon = basisLon * (1 - (penaltyPercent / 100.0));

        lonLore.add("§7Din §2løn: §a$" + basisLon);
        lonLore.add("§7Din §2reelle løn: §a$" + basisLon + penaltyPercent);
        if (penaltyPercent > 0) {
            lonLore.add("§cStraf fra døds-achievements: -" + String.format("%.2f", penaltyPercent) + "%");
            lonLore.add("§7Faktisk §2løn: §a$" + String.format("%.2f", finalLon));
        }
        if (VagtCooldown.isCooling(p.getName(), "lon")) {
        }


        //Setting Lore on Item
        metastats.setLore(statsLore);
        metatid.setLore(spilletidlore);
        metatopv.setLore(topVagterLore);
        metalon.setLore(lonLore);

        // Setting Meta on Item

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
}