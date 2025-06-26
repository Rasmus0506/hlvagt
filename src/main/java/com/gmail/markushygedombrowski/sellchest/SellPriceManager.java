package com.gmail.markushygedombrowski.sellchest;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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

        if (config.contains("prices")) {
            for (String key : config.getConfigurationSection("prices").getKeys(false)) {
                try {
                    int id = Integer.parseInt(key);
                    double price = config.getDouble("prices." + key);
                    sellPrices.put(id, price);
                } catch (NumberFormatException e) {

                }
            }
        } else {

            setupDefaultPrices();
        }
    }

    private void setupDefaultPrices() {

        sellPrices.put(1, 5.0);
        sellPrices.put(2, 5.0);

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
        return sellPrices.getOrDefault(id, 0.0);
    }

    public void setPrice(int id, double price) {
        sellPrices.put(id, price);
        savePrices();
    }
}