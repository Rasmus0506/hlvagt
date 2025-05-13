package com.gmail.markushygedombrowski.vagtMenu.subMenu;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;


import java.util.ArrayList;

import java.util.List;


public class PVGUI implements Listener {

    private final int PV_INDEX = 4;
    private Wool woolRed = new Wool(DyeColor.RED);
    private Wool woolGreen = new Wool(DyeColor.GREEN);
    private ItemStack pv = woolRed.toItemStack(1);
    private int kost;
    private int pvNr;
    private HLvagt plugin;
    private Inventory cinventory = Bukkit.createInventory(null, 9, "PV");
    private ItemMeta metapv2 = pv.getItemMeta();
    private PlayerProfiles profiles;


    public PVGUI(HLvagt plugin, PlayerProfiles profiles) {
        this.plugin = plugin;
        this.profiles = profiles;
    }


    public void create(Player p) {
        PlayerProfile profile = profiles.getPlayerProfile(p.getUniqueId());
        kost = (100000 * profile.castPropertyToInt(profile.getProperty("pv"))) * 2;
        pvNr = profile.castPropertyToInt(profile.getProperty("pv")) + 1;


        pv = woolGreen.toItemStack(1);
        List<String> pvlore = new ArrayList<>();

        metapv2.setDisplayName("§2Køb PV §7" + pvNr);
        pvlore.add("§6Koster: §7" + kost);

        metapv2.setLore(pvlore);
        pv.setItemMeta(metapv2);

        cinventory.setItem(PV_INDEX, pv);

        p.openInventory(cinventory);


    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();

        int clickedSlot = event.getRawSlot();
        if (clickeditem == null) return;
        if (inventory.getTitle().equalsIgnoreCase("PV")) {
            PlayerProfile profile = profiles.getPlayerProfile(p.getUniqueId());
            if (clickedSlot == PV_INDEX) {

                if (!plugin.econ.has(p, kost)) {

                    p.sendMessage("§cDu har ikke penge nok!");

                } else {

                    plugin.econ.withdrawPlayer(p, kost);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission unset playervaults.amount." + (pvNr - 1) + " prison");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission set playervaults.amount." + pvNr + " prison");


                    p.sendMessage("Du har nu adgang til " + pvNr + " Pv");
                    profile.setProperty("pv", pvNr);
                    pvNr = profile.castPropertyToInt(profile.getProperty("pv")) + 1;
                    kost = (100000 * profile.castPropertyToInt(profile.getProperty("pv")));
                    kost = kost + kost;
                    List<String> pvlore = new ArrayList<>();
                    metapv2.setDisplayName("§2Køb PV §7" + pvNr);
                    pvlore.add("§6Koster: §7" + kost);

                    metapv2.setLore(pvlore);
                    pv.setItemMeta(metapv2);

                    cinventory.setItem(PV_INDEX, pv);
                    p.updateInventory();

                }

            }


            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

        }
    }


}



