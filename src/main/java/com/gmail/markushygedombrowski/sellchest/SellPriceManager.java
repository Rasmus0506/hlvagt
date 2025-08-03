package com.gmail.markushygedombrowski.sellchest;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SellPriceManager {
    private final Map<Integer, Double> sellPrices = new HashMap<>();
    private final File configFile;
    private final FileConfiguration config;

    public SellPriceManager(File dataFolder) {
        this.configFile = new File(dataFolder, "sellprices.yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);
        loadPrices();
    }

    private void loadPrices() {
        setupDefaultPrices(); // Altid indlæs standard priser først

        if (config.contains("prices")) {
            for (String key : config.getConfigurationSection("prices").getKeys(false)) {
                try {
                    int id = Integer.parseInt(key);
                    double price = config.getDouble("prices." + key);
                    sellPrices.put(id, price);
                } catch (NumberFormatException e) {
                }
            }
        }

    }

    private void setupDefaultPrices() {
        // Blokke og basis materialer
        sellPrices.put(1, 5.0);       // STONE
        sellPrices.put(2, 5.0);       // GRASS
        sellPrices.put(3, 5.0);       // DIRT
        sellPrices.put(4, 5.0);       // COBBLESTONE
        sellPrices.put(5, 0.3);       // WOOD_PLANKS
        sellPrices.put(6, 5.0);       // SAPLING
        sellPrices.put(12, 5.0);      // SAND
        sellPrices.put(15, 10.0);     // IRON_ORE
        sellPrices.put(16, 10.0);     // COAL_ORE
        sellPrices.put(17, 0.3);      // LOG
        sellPrices.put(18, 1.0);      // LEAVES
        sellPrices.put(20, 1.0);      // GLASS
        sellPrices.put(21, 1.0);      // LAPIS_ORE
        sellPrices.put(22, 210.0);    // LAPIS_BLOCK
        sellPrices.put(24, 6.0);      // SANDSTONE
        sellPrices.put(26, 110.0);    // BED
        sellPrices.put(35, 11.0);     // WOOL
        sellPrices.put(37, 8.0);      // YELLOW_FLOWER
        sellPrices.put(38, 8.0);      // RED_FLOWER
        sellPrices.put(42, 210.0);    // IRON_BLOCK
        sellPrices.put(50, 4.0);      // TORCH
        sellPrices.put(53, 0.3);      // OAK_STAIRS
        sellPrices.put(54, 11.0);     // CHEST
        sellPrices.put(56, 100.0);    // DIAMOND_ORE
        sellPrices.put(57, 1000.0);   // DIAMOND_BLOCK
        sellPrices.put(263, 40.0);   // COAL
        sellPrices.put(173, 360.0);   // COAL_BLOCK
        sellPrices.put(170, 2550.0);   // HAY_BLOCK

        // Redstone og mekanismer
        sellPrices.put(61, 8.0);      // FURNACE
        sellPrices.put(63, 6.0);      // SIGN
        sellPrices.put(64, 6.0);      // WOODEN_DOOR
        sellPrices.put(65, 4.0);      // LADDER
        sellPrices.put(81, 36.0);     // CACTUS
        sellPrices.put(86, 8.0);      // PUMPKIN
        sellPrices.put(88, 11.0);     // SOUL_SAND
        sellPrices.put(91, 9.0);      // JACK_O_LANTERN
        sellPrices.put(95, 4.0);      // STAINED_GLASS
        sellPrices.put(98, 6.0);      // STONE_BRICKS
        sellPrices.put(126, 0.3);     // WOODEN_SLAB
        sellPrices.put(330, 0.3);     // IRON_DOOR

        // Værktøj og våben
        sellPrices.put(256, 5.0);     // IRON_SHOVEL
        sellPrices.put(257, 5.0);     // IRON_PICKAXE
        sellPrices.put(258, 5.0);     // IRON_AXE
        sellPrices.put(267, 200.0);   // IRON_SWORD
        sellPrices.put(276, 260.0);   // DIAMOND_SWORD
        sellPrices.put(277, 8.0);     // DIAMOND_SHOVEL
        sellPrices.put(278, 8.0);     // DIAMOND_PICKAXE
        sellPrices.put(279, 8.0);     // DIAMOND_AXE
        sellPrices.put(283, 100.0);   // GOLD_SWORD

        // Rustning
        sellPrices.put(302, 200.0);   // CHAINMAIL_CHESTPLATE
        sellPrices.put(305, 200.0);   // CHAINMAIL_BOOTS
        sellPrices.put(306, 210.0);   // IRON_HELMET
        sellPrices.put(307, 140.0);   // IRON_CHESTPLATE
        sellPrices.put(308, 140.0);   // IRON_LEGGINGS
        sellPrices.put(309, 120.0);   // IRON_BOOTS
        sellPrices.put(310, 250.0);   // DIAMOND_HELMET
        sellPrices.put(311, 300.0);   // DIAMOND_CHESTPLATE
        sellPrices.put(312, 250.0);   // DIAMOND_LEGGINGS
        sellPrices.put(313, 200.0);   // DIAMOND_BOOTS

        // Mad og forbrugsvarer
        sellPrices.put(293, 100.0);   // DIAMOND_HOE
        sellPrices.put(295, 1.0);     // SEEDS
        sellPrices.put(296, 283.0);   // WHEAT
        sellPrices.put(297, 850.0);   // BREAD
        sellPrices.put(360, 6.0);     // MELON
        sellPrices.put(361, 7.0);     // PUMPKIN_SEEDS
        sellPrices.put(365, 3.0);     // RAW_CHICKEN
        sellPrices.put(366, 6.0);     // COOKED_CHICKEN

        // Andre værdifulde items
        sellPrices.put(264, 81.0);    // DIAMOND
        sellPrices.put(265, 23.0);    // IRON_INGOT
        sellPrices.put(266, 200.0);   // GOLD_INGOT
        sellPrices.put(371, 850.0);   // GOLD_NUGGET
        sellPrices.put(388, 360.0);   // EMERALD
        sellPrices.put(334, 3.0);   // LEATHER

        savePrices();
    }

    private void savePrices() {
        for (Map.Entry<Integer, Double> entry : sellPrices.entrySet()) {
            config.set("prices." + entry.getKey(), entry.getValue());
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getPrice(int id) {
        Double price = sellPrices.get(id);
        return price != null ? price : 0.0;
    }

    public void setPrice(int id, double price) {
        sellPrices.put(id, price);
        savePrices();
    }

    public void printAllPrices(Player player) {
        player.sendMessage("§6=== Sælge Priser ===");
        for (Map.Entry<Integer, Double> entry : sellPrices.entrySet()) {
            Material material = Material.getMaterial(entry.getKey());
            String itemName = material != null ? material.name() : "ID:" + entry.getKey();
            player.sendMessage(String.format("§7%s: §6$%.2f", itemName, entry.getValue()));
        }
    }
}