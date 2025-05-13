package com.gmail.markushygedombrowski.rankup;

import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.utils.VagtUtils;

public class RankupKrav {
    private String rankup;
    private int bread;
    private int shards;
    private int goldnugget;
    private int vagtposter;
    private int onTime;
    private int vagtlevel;



    private int money;


    public RankupKrav(String rankup, int bread, int seeds, int goldnugget, int vagtposter, int onTime, int vagtlevel, int money) {
        this.rankup = rankup;
        this.bread = bread;
        this.shards = seeds;
        this.goldnugget = goldnugget;
        this.vagtposter = vagtposter;
        this.onTime = onTime;
        this.vagtlevel = vagtlevel;
        this.money = money;
    }
    public String getRankup() {
        return rankup;
    }
    public int getMoney() {
        return money;
    }
    public int getBread() {
        return bread;
    }

    public int getShards() {
        return shards;
    }

    public int getGoldnugget() {
        return goldnugget;
    }

    public int getVagtposter() {
        return vagtposter;
    }

    public int getOnTime() {
        return onTime;
    }

    public int getVagtlevel() {
        return vagtlevel;
    }

    public boolean canRankup(PlayerProfile profile) {
        return profile.getDeliveredItems().getBread() >= bread && profile.getDeliveredItems().getShards() >= shards && VagtUtils.castPropertyToInt(profile.getProperty("vagtposter")) >= vagtposter && VagtUtils.castPropertyToInt(profile.getProperty("level")) >= vagtlevel;
    }

}
