package com.gmail.markushygedombrowski.cooldown;

import java.util.HashMap;

public class VagtAbilityCooldown {
    public String ability = "";
    public String player = "";
    public long seconds;
    public long systime;

    public VagtAbilityCooldown(String player, long seconds, long systime) {
        this.player = player;
        this.seconds = seconds;
        this.systime = systime;
    }
    public VagtAbilityCooldown(String player) {
        this.player = player;
    }
    public HashMap<String, VagtAbilityCooldown> cooldownMap = new HashMap<>();


}
