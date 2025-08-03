package com.gmail.markushygedombrowski.listners;

import com.gmail.markushygedombrowski.achievements.CompletedAchievementEvent;
import com.gmail.markushygedombrowski.achievements.SimpleAchievement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AchievementListener implements Listener {

    @EventHandler
    public void onAchievementComp(CompletedAchievementEvent event) {
        Player player = event.getPlayer();
        SimpleAchievement achievement = event.getAchievement();

        player.sendMessage("§a§l----- Achievement Unlocked -----§r");
        player.sendMessage("§b§lDu har gennemført en achievement!");
        player.sendMessage("§9§l" + achievement.getDescription());
        if (achievement.getType().equalsIgnoreCase("penalty")) {
            player.sendMessage("§4§lPenalty: §c§l" + achievement.getModifier() + "% §7 i løn");
        }
        else if (achievement.getType().equalsIgnoreCase("bonus")) {
            player.sendMessage("§2§lBonus: §a§l" + achievement.getModifier() + "% §7 i løn");
        }
        player.sendMessage("§7§lFor at se dine achievements, skriv §a/vagt");
        player.sendMessage("§a§l----- Achievement Unlocked -----§r");

    }
}
