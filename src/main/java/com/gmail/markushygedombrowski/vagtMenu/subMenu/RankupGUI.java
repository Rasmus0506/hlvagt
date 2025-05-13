package com.gmail.markushygedombrowski.vagtMenu.subMenu;

import com.gmail.markushygedombrowski.rankup.RankupKrav;
import com.gmail.markushygedombrowski.rankup.RankupLoader;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import net.milkbowl.vault.economy.Economy;
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

import java.util.ArrayList;
import java.util.List;

public class RankupGUI implements Listener {
    private PlayerProfiles playerProfiles;
    private Economy econ;
    private final int RANKUP_INDEX = 13;
    private RankupLoader rankupLoader;

    public RankupGUI(PlayerProfiles playerProfiles, Economy econ, RankupLoader rankupLoader) {
        this.playerProfiles = playerProfiles;
        this.econ = econ;
        this.rankupLoader = rankupLoader;
    }

    public void create(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "§a§lRankup");
        long timeplayed = (p.getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60 / 60);
        ItemStack rankup = new ItemStack(Material.DIAMOND, 1);
        ItemMeta rankupMeta = rankup.getItemMeta();
        List<String> lore = new ArrayList<>();
        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
        RankupKrav rankupKrav;
        rankupMeta.setDisplayName("§a§lRankup");
        if (p.hasPermission("a-vagt")) {
            lore.add(0, "§c§lDu kan §4§lIkke §c§lRankup");
        } else {
            rankupKrav = rankupLoader.getRankupKrav(p.hasPermission("b-vagt") ? "a-vagt" : "b-vagt");
            lore.add("§6§lKrav:");
            lore.add("§a§lSpilletid: §b" + timeplayed + "§7/§b" + rankupKrav.getOnTime() + "§7§lTimer");
            lore.add("§bVagt Level: §b" + profile.getProperty("level") + "§7/§b" + rankupKrav.getVagtlevel());
            lore.add("§a§lPenge: §b" + econ.getBalance(p) + "§7/§b" + rankupKrav.getMoney());
            lore.add("§6§lGoldnuggets: §b" + getGoldnugget(p) + "§7/§b" + rankupKrav.getGoldnugget());
            lore.add("§2VagtPoster: §b" + profile.getProperty("vagtposter") + "§7/§b" + rankupKrav.getVagtposter());
            lore.add("§9§lShards: §b" + profile.getDeliveredItems().getShards() + "§7/§b" + rankupKrav.getShards());
            lore.add("§e§lBread: §b" + profile.getDeliveredItems().getBread() + "§7/§b" + rankupKrav.getBread());
            lore.add("§f§lIron Helmets: §b" + profile.getDeliveredItems().getIronHelmet() + "§7/§b" + (p.hasPermission("b-vagt") ? "3" : "1"));
            lore.add("§f§lIron Chestplates: §b" + profile.getDeliveredItems().getIronChestplate() + "§7/§b" + (p.hasPermission("b-vagt") ? "3" : "1"));
            lore.add("§f§lIron Leggings: §b" + profile.getDeliveredItems().getIronLeggings() + "§7/§b" + (p.hasPermission("b-vagt") ? "3" : "1"));
            lore.add("§f§lIron Boots: §b" + profile.getDeliveredItems().getIronBoots() + "§7/§b" + (p.hasPermission("b-vagt") ? "3" : "1"));
            lore.add("§f§lIron Swords: §b" + profile.getDeliveredItems().getIronSword() + "§7/§b" + (p.hasPermission("b-vagt") ? "3" : "1"));
        }

        rankupMeta.setLore(lore);
        rankup.setItemMeta(rankupMeta);
        inv.setItem(RANKUP_INDEX, rankup);

        p.openInventory(inv);
    }

    public int getGoldnugget(Player p) {
        return p.getInventory().all(Material.GOLD_NUGGET).values().stream().mapToInt(ItemStack::getAmount).sum();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        if (clickeditem == null || !inventory.getTitle().equalsIgnoreCase("§a§lRankup") || clickedSlot != RANKUP_INDEX) {
            return;
        }
        RankupKrav rankupKrav = rankupLoader.getRankupKrav(p.hasPermission("b-vagt") ? "a-vagt" : "b-vagt");
        if (rankupKrav.canRankup(playerProfiles.getPlayerProfile(p.getUniqueId())) && econ.has(p, rankupKrav.getMoney())
                && p.getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60 / 60 >= rankupKrav.getOnTime()
                && p.getInventory().contains(Material.GOLD_NUGGET, rankupKrav.getGoldnugget())) {

            econ.withdrawPlayer(p, rankupKrav.getMoney());
            p.getInventory().removeItem(new ItemStack(Material.GOLD_NUGGET, rankupKrav.getGoldnugget()));
            vagtRankupMessage(p, p.hasPermission("b-vagt") ? "§a§la-vagt" : "§b§lb-vagt");
            setVagtPerms(p, rankupKrav.getRankup());
        } else {
            p.sendMessage("§cDu opfylder ikke kravene til at rankup!");
        }
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
    }

    private void setVagtPerms(Player p, String perm) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent remove c-vagt prison");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent remove b-vagt prison");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent add " + perm + " prison");
    }

    private void vagtRankupMessage(Player p, String rank) {
        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
        Bukkit.broadcastMessage("§c§lVagten §6" + p.getName());
        Bukkit.broadcastMessage("§7Har lige Ranket up til " + rank);
        Bukkit.broadcastMessage("             §a§lTILLYKKE!!!");
        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
    }
}
