package com.gmail.markushygedombrowski.rankup;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class RankupLoader {

    private HashMap<String, RankupKrav> rankupKravHashMap = new HashMap<>();

    public void load(FileConfiguration config) {
        rankupKravHashMap.clear();
        for (String rankup : config.getConfigurationSection("rankup").getKeys(false)) {
            int bread = config.getInt("rankup." + rankup + ".bread");
            int shards = config.getInt("rankup." + rankup + ".shards");
            int goldnugget = config.getInt("rankup." + rankup + ".goldnugget");
            int vagtposter = config.getInt("rankup." + rankup + ".vagtposter");
            int onTime = config.getInt("rankup." + rankup + ".onTime");
            int vagtlevel = config.getInt("rankup." + rankup + ".vagtlevel");
            int money = config.getInt("rankup." + rankup + ".money");
            RankupKrav rankupKrav = new RankupKrav(rankup, bread, shards, goldnugget, vagtposter, onTime, vagtlevel, money);
            rankupKravHashMap.put(rankup, rankupKrav);
        }

    }

    public RankupKrav getRankupKrav(String rankup) {
        return rankupKravHashMap.get(rankup);
    }
}
