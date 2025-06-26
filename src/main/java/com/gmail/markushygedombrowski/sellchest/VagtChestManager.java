package com.gmail.markushygedombrowski.sellchest;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class VagtChestManager implements Listener {
    private final SellPriceManager priceManager;
    private final Economy economy;
    private final Set<Location> sellChests;
    private final File chestsFile;
    private final FileConfiguration chestsConfig;
    private static final String CHEST_TITLE = "§cVagt Sælge Kiste";

    public VagtChestManager(SellPriceManager priceManager, Economy economy, File dataFolder) {
        this.priceManager = priceManager;
        this.economy = economy;
        this.sellChests = new HashSet<>();
        this.chestsFile = new File(dataFolder, "sellchests.yml");
        this.chestsConfig = YamlConfiguration.loadConfiguration(chestsFile);
        loadSellChests();
    }

    private void loadSellChests() {
        if (chestsConfig.contains("chests")) {
            for (String locString : chestsConfig.getStringList("chests")) {
                String[] parts = locString.split(",");
                Location loc = new Location(
                        Bukkit.getWorld(parts[0]),
                        Double.parseDouble(parts[1]),
                        Double.parseDouble(parts[2]),
                        Double.parseDouble(parts[3])
                );
                sellChests.add(loc);
            }
        }
    }

    private void saveSellChests() {
        List<String> locations = new ArrayList<>();
        for (Location loc : sellChests) {
            locations.add(String.format("%s,%f,%f,%f",
                    loc.getWorld().getName(),
                    loc.getX(),
                    loc.getY(),
                    loc.getZ()
            ));
        }
        chestsConfig.set("chests", locations);
        try {
            chestsConfig.save(chestsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerSellChest(Location location) {
        sellChests.add(location);
        saveSellChests();
    }

    @EventHandler
    public void onChestBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.CHEST) {
            if (sellChests.contains(event.getBlock().getLocation())) {
                if (!event.getPlayer().hasPermission("admin")) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("§cDu kan ikke ødelægge en sælge-kiste!");
                } else {
                    sellChests.remove(event.getBlock().getLocation());
                    saveSellChests();
                    event.getPlayer().sendMessage("§aSælge-kisten er blevet fjernet!");
                }
            }
        }
    }

    @EventHandler
    public void onChestInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock().getType() != Material.CHEST) return;

        Location chestLoc = event.getClickedBlock().getLocation();
        if (!sellChests.contains(chestLoc)) return;

        event.setCancelled(true);
        openSellMenu(event.getPlayer());
    }

    private void openSellMenu(Player player) {
        if (!player.hasPermission("chest.use")) {
            player.sendMessage("§cDu har ikke adgang til at bruge denne kiste.");
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 36, CHEST_TITLE);
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        if (!event.getInventory().getTitle().equals(CHEST_TITLE)) return;

        Player player = (Player) event.getPlayer();
        processItems(player, event.getInventory());
    }

    private void processItems(Player player, Inventory inv) {
        double totalMoney = 0;
        Map<String, Integer> soldItems = new HashMap<>();

        player.sendMessage("§7Debug: Starter processering af items...");

        for (ItemStack item : inv.getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;

            int id = item.getTypeId();
            // Tilføj mere detaljeret debugging her
            player.sendMessage("§7Debug: Tjekker item: " + item.getType().name());
            player.sendMessage("§7Debug: Item data: ID=" + id + ", Durability=" + item.getDurability());
            double price = priceManager.getPrice(id);

            player.sendMessage("§7Debug: Fundet pris: " + price);

            int amount = item.getAmount();
            if (price > 0) {
                double itemTotal = price * amount;
                totalMoney += itemTotal;
                String itemName = item.getType().name().replace("_", " ").toLowerCase();
                soldItems.put(itemName, soldItems.getOrDefault(itemName, 0) + amount);
                player.sendMessage("§7Debug: Tilføjet til total: " + itemTotal + " (nu " + totalMoney + ")");
            } else {
                player.sendMessage("§7Debug: Item ignoreret - ingen pris sat for ID " + id);
            }
        }

        inv.clear();

        // Fjern denne betingelse midlertidigt for at se om beskeden bliver vist
        //if (totalMoney > 0) {
        player.sendMessage("§7Debug: Forsøger at vise besked med total: " + totalMoney);
        try {
            if (totalMoney > 0) {
                economy.depositPlayer(player, totalMoney);
            }
            player.sendMessage("§6            Items§6:");
            player.sendMessage("");
            for (Map.Entry<String, Integer> entry : soldItems.entrySet()) {
                player.sendMessage("§7- §c" + entry.getValue() + "x §6" + entry.getKey());
            }
            player.sendMessage("");
            if (totalMoney > 0) {
                player.sendMessage("§cDu Har Tjent§6 $" + String.format("%.2f", totalMoney));
            } else {
                player.sendMessage("§cIngen items blev solgt");
            }
        } catch (Exception e) {
            player.sendMessage("§cDer skete en fejl: " + e.getMessage());
            e.printStackTrace();
        }
        //}
    }
}