package com.gmail.markushygedombrowski.sellchest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class VagtChestManager implements Listener {
    private final SellPriceManager priceManager;
    private final Map<Player, Boolean> usingSellChest = new HashMap<>();
    private static final String CHEST_TITLE = "§cVagt Sælge Kiste-c";

    public VagtChestManager(SellPriceManager priceManager) {
        this.priceManager = priceManager;
    }

    public void openSellChest(Player player) {
        if (!player.hasPermission("chest.use")) {
            player.sendMessage("§cDu har ikke adgang til at bruge denne kiste.");
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 36, CHEST_TITLE);
        player.openInventory(inv);
        usingSellChest.put(player, true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();

        if (!usingSellChest.containsKey(player)) return;
        usingSellChest.remove(player);

        if (!event.getInventory().getTitle().equals(CHEST_TITLE)) return;

        double totalMoney = 0;
        Map<String, Integer> soldItems = new HashMap<>();

        for (ItemStack item : event.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;

            int id = item.getTypeId();
            double price = priceManager.getPrice(id);

            if (price > 0) {
                int amount = item.getAmount();
                totalMoney += price * amount;

                String itemName = item.getType().name();
                soldItems.put(itemName, soldItems.getOrDefault(itemName, 0) + amount);
            } else {
                player.sendMessage("§3" + item.getType().name() + " §cEr Ikke Noget Værd!!");
            }
        }


        player.sendMessage("§6            Items§6:");
        player.sendMessage("");
        for (Map.Entry<String, Integer> entry : soldItems.entrySet()) {
            player.sendMessage("§7- §c" + entry.getValue() + " §6" + entry.getKey());
        }
        player.sendMessage("");
        player.sendMessage("§cDu Har Tjent§6 $" + totalMoney);


    }
}