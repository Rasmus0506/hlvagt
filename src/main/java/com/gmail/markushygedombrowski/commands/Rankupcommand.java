package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.settings.Settings;

import com.gmail.markushygedombrowski.utils.VagtUtils;
import com.gmail.markushygedombrowski.warp.VagtSpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
public class Rankupcommand implements CommandExecutor {
    private Settings settings;
    private PlayerProfiles profiles;
    private VagtSpawnManager vagtSpawnManager;
    private HLvagt plugin;

    public Rankupcommand(Settings settings, PlayerProfiles profiles, VagtSpawnManager vagtSpawnManager, HLvagt plugin) {
        this.settings = settings;
        this.profiles = profiles;
        this.vagtSpawnManager = vagtSpawnManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (VagtUtils.isSenderNotPlayer(sender) || VagtUtils.notHasPermission((Player) sender, "direktør")) return true;

        Player p = (Player) sender;
        if (args.length <= 1) {
            p.sendMessage("/ansat <Player> <Rank>");
            p.sendMessage("Ranks: p-vagt, c-vagt, b-vagt, a-vagt, officer");
            return true;
        }

        Player ansat = Bukkit.getPlayer(args[0]);
        if (ansat == null) {
            p.sendMessage("er ikke en spiller");
            return true;
        }

        PlayerProfile profile = profiles.getPlayerProfile(ansat.getUniqueId());
        try {
            profiles.createVagt(ansat, profile);
            updateRankAndPermissions(args[1], ansat, profile,p);
            profiles.save(profile);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        plugin.econ.depositPlayer(ansat, 25000);
        ansat.teleport(vagtSpawnManager.getWarpInfo("vagtc").getLocation());



        return true;
    }

    private void updateRankAndPermissions(String rankArg, Player ansat, PlayerProfile profile, Player p) {
        String rank = "bob";
        String perm = "1";
        String prePerm = "0";
        if (rankArg.equalsIgnoreCase("p-vagt")) {
            ansat.setStatistic(Statistic.PLAY_ONE_TICK, 1);
            rank = "§cp-vagt";
            perm = "p-vagt";
            prePerm = ansat.hasPermission("a-fange") ? "a-fange" : ansat.hasPermission("b-fange") ? "b-fange" : "c-fange";
        } else if (rankArg.equalsIgnoreCase("c-vagt")) {
            rank = "§cc-vagt";
            perm = "c-vagt";
            prePerm = "p-vagt";

        } else if (rankArg.equalsIgnoreCase("b-vagt")) {
            rank = "§bb-vagt";
            perm = "b-vagt";
            prePerm = "c-vagt";
        } else if (rankArg.equalsIgnoreCase("a-vagt")) {
            rank = "§aa-vagt";
            perm = "a-vagt";
            prePerm = "b-vagt";
        } else if (rankArg.equalsIgnoreCase("officer")) {
            rank = "§6Officer";
            perm = "officer";
            prePerm = "a-vagt";
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + ansat.getName() + " parent remove " + prePerm + " prison");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + ansat.getName() + " parent add " + perm + " prison");
        boradcastMessageVagtRankup(p, ansat, rank);
    }

    private void boradcastMessageVagtRankup(Player p, Player ansat, String rank) {
        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
        Bukkit.broadcastMessage("§c§lVagten §6" + ansat.getName());
        Bukkit.broadcastMessage("§7Har lige Ranket up til " + rank);
        Bukkit.broadcastMessage("§7Ved hjælp af §4" + p.getName());
        Bukkit.broadcastMessage("             §a§lTILLYKKE!!!");
        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
    }
}
