package com.gmail.markushygedombrowski.warp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class VagtSpawnManager {
    private final Map<String, VagtSpawnInfo> warpMap = new HashMap<>();
    private final JavaPlugin plugin;

    public VagtSpawnManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        FileConfiguration config = plugin.getConfig();
        warpMap.clear();
        for (String key : config.getConfigurationSection("warps").getKeys(false)) {
            String name = config.getString("warps." + key + ".name");
            double x = config.getDouble("warps." + key + ".location.x");
            double y = config.getDouble("warps." + key + ".location.y");
            double z = config.getDouble("warps." + key + ".location.z");
            String stringWorld = config.getString("warps." + key + ".location.world");
            World world = Bukkit.getWorld(stringWorld);
            Location location = new Location(world, x, y, z);
            VagtSpawnInfo vagtSpawnInfo = new VagtSpawnInfo(name, location);
            warpMap.put(name, vagtSpawnInfo);
        }
    }

    public void save(VagtSpawnInfo vagtSpawnInfo) {
        FileConfiguration config = plugin.getConfig();
        String key = "" + (warpMap.size() + 1);
        config.set("warps." + key + ".name", vagtSpawnInfo.getName());
        config.set("warps." + key + ".location.x", vagtSpawnInfo.getLocation().getX());
        config.set("warps." + key + ".location.y", vagtSpawnInfo.getLocation().getY());
        config.set("warps." + key + ".location.z", vagtSpawnInfo.getLocation().getZ());
        config.set("warps." + key + ".location.world", vagtSpawnInfo.getLocation().getWorld().getName());
        plugin.saveConfig();
        warpMap.put(vagtSpawnInfo.getName(), vagtSpawnInfo);

    }
    public VagtSpawnInfo getWarpInfo(String warpName) {
        return warpMap.get(warpName);
    }

}
