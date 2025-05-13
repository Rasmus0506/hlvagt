package com.gmail.markushygedombrowski.adminGUIs;

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

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class VagtStats implements Listener {
    private final int VAGT_LEVEL_INDEX = 19;
    private final int STATS_INDEX = 21;
    private final int DELIVERED_INDEX = 23;
    private final int PV_INDEX = 28;
    private final int TIME_INDEX = 30;
    private final int VAGTPOSTER_INDEX = 32;
    private HLvagt plugin;

    public VagtStats(HLvagt plugin) {
        this.plugin = plugin;
    }

    public void create(Player player, PlayerProfile profile) {

        Player target = Bukkit.getPlayer(profile.getUuid());
        if(target == null) {
            target = Bukkit.getOfflinePlayer(profile.getUuid()).getPlayer();

        }
        Inventory inv = getServer().createInventory(null, 54, "Vagt Stats for " + target.getName());
        ItemStack vagtLevel = new ItemStack(Material.EXP_BOTTLE, 1);
        ItemStack stats = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemStack delivered = new ItemStack(Material.GOLD_HELMET, 1);
        ItemStack pv = new ItemStack(Material.CHEST, 1);
        ItemStack time = new ItemStack(Material.WATCH, 1);
        ItemStack vagtposter = new ItemStack(Material.BEACON, 1);

        ItemMeta vagtLevelMeta = vagtLevel.getItemMeta();
        ItemMeta statsMeta = stats.getItemMeta();
        ItemMeta deliveredMeta = delivered.getItemMeta();
        ItemMeta pvMeta = pv.getItemMeta();
        ItemMeta timeMeta = time.getItemMeta();
        ItemMeta vagtposterMeta = vagtposter.getItemMeta();

        vagtLevelMeta.setDisplayName("§6§lVagt Level");
        statsMeta.setDisplayName("§6§lStats");
        deliveredMeta.setDisplayName("§6§lDelivered");
        pvMeta.setDisplayName("§c§lPV");
        timeMeta.setDisplayName("§a§lSpilletid");
        vagtposterMeta.setDisplayName("§a§lVagt Poster");

        List<String> lore = new ArrayList<>();
        lore.add(0, "§aVagt Level: §6" + profile.getProperty("level"));
        vagtLevelMeta.setLore(lore);
        vagtLevel.setItemMeta(vagtLevelMeta);

        lore.set(0,"§6§lStats§7");
        lore.add(1,"§c§lDød: §f" + profile.getProperty("deaths"));
        lore.add(2,"§a§lDræbt: §f" + profile.getProperty("kills"));
        lore.add(3,"§a§lPenge: §f" + plugin.econ.getBalance(target));
        lore.add(4,"§a§lLøn: §f" + profile.getProperty("salary"));
        statsMeta.setLore(lore);
        stats.setItemMeta(statsMeta);
        lore.clear();

        lore.add(0,"§6§lDelivered§7");
        deliveredMeta.setLore(lore);
        delivered.setItemMeta(deliveredMeta);
        lore.clear();

        lore.add(0,"§c§lPV§7");
        lore.add("§a§lPV: §f" + profile.getProperty("pv"));
        pvMeta.setLore(lore);
        pv.setItemMeta(pvMeta);
        lore.clear();

        int seconds = target.getStatistic(Statistic.PLAY_ONE_TICK) / 20;
        int minutes = (seconds / 60);
        int hours = (minutes / 60);
        int actualMinutes = minutes - (hours * 60);
        lore.add(0,"§a§lSpilletid§7");
        lore.add("§9" + hours + " §6Hours");
        lore.add("§9"+actualMinutes + " §6Minutes");
        timeMeta.setLore(lore);
        time.setItemMeta(timeMeta);
        lore.clear();

        lore.add(0,"§a§lVagt Poster§7");
        lore.add("§a§lVagt Poster: §f" + profile.getProperty("vagtposter"));
        vagtposterMeta.setLore(lore);
        vagtposter.setItemMeta(vagtposterMeta);
        lore.clear();

        inv.setItem(VAGT_LEVEL_INDEX, vagtLevel);
        inv.setItem(STATS_INDEX, stats);
        inv.setItem(DELIVERED_INDEX, delivered);
        inv.setItem(PV_INDEX, pv);
        inv.setItem(TIME_INDEX, time);
        inv.setItem(VAGTPOSTER_INDEX, vagtposter);

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().getTitle().contains("Vagt Stats")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
        }
    }

}
