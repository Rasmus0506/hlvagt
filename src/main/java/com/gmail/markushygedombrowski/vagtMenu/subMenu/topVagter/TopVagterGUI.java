package com.gmail.markushygedombrowski.vagtMenu.subMenu.topVagter;

import com.gmail.markushygedombrowski.utils.VagtUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class TopVagterGUI implements Listener {
    private final int TOP10KILLS_index = 1;
    private final int TOP10DEATH_INDEX = 3;
    private final int TOP10WALKED_INDEX = 5;
    private final int TOP10MONEY_INDEX = 7;
    private final int TOP10LEVEL_INDEX = 11;
    private final int TOP10VAGTPOSTER_INDEX = 13;

    private ItemStack kills = new ItemStack(Material.DIAMOND_SWORD);
    private ItemStack deaths = new ItemStack(Material.SKULL_ITEM);
    private ItemStack walked = new ItemStack(Material.DIAMOND_BOOTS);
    private ItemStack money = new ItemStack(Material.GOLD_INGOT);
    private ItemStack level = new ItemStack(Material.EXP_BOTTLE);
    private ItemStack vagtposter = new ItemStack(Material.BEACON);


    public void create(Player p) {
        Inventory inventory = Bukkit.createInventory(null, 27, "top10");
        meta();
        inventory.setItem(TOP10KILLS_index, kills);
        inventory.setItem(TOP10DEATH_INDEX, deaths);
        inventory.setItem(TOP10WALKED_INDEX, walked);
        inventory.setItem(TOP10MONEY_INDEX, money);
        inventory.setItem(TOP10LEVEL_INDEX, level);
        inventory.setItem(TOP10VAGTPOSTER_INDEX, vagtposter);
        ItemStack glass = VagtUtils.getGlass();
        for(int i = 0; i < 27; i++) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i, glass);
            }
        }
        p.openInventory(inventory);

    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        if (clickeditem == null) {
            return;
        }
        if (inventory.getTitle().equalsIgnoreCase("top10")) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }


    public void meta() {
        ItemMeta metakills = kills.getItemMeta();
        metakills.setDisplayName("§7Top 10 §cKills");
        metakills.setLore(VagtUtils.top10Kills());
        kills.setItemMeta(metakills);

        ItemMeta metaDeaths = deaths.getItemMeta();
        metaDeaths.setDisplayName("§7Top 10 §4Deaths");
        metaDeaths.setLore(VagtUtils.top10Deaths());
        deaths.setItemMeta(metaDeaths);

        ItemMeta metaWalked = walked.getItemMeta();
        metaWalked.setDisplayName("§7Top 10 §eGået");
        metaWalked.setLore(VagtUtils.top10Walk("vagt"));
        walked.setItemMeta(metaWalked);

        ItemMeta metaMoney = money.getItemMeta();
        metaMoney.setDisplayName("§7Top 10 §aPenge");
        metaMoney.setLore(VagtUtils.top10Money("vagt"));
        money.setItemMeta(metaMoney);

        ItemMeta metaLevel = level.getItemMeta();
        metaLevel.setDisplayName("§7Top 10 §bLevel");
        metaLevel.setLore(VagtUtils.top10Level());
        level.setItemMeta(metaLevel);

        ItemMeta metaVagtposter = vagtposter.getItemMeta();
        metaVagtposter.setDisplayName("§7Top 10 §dVagtposter");
        metaVagtposter.setLore(VagtUtils.top10VagtPoster());
        vagtposter.setItemMeta(metaVagtposter);

    }


}
