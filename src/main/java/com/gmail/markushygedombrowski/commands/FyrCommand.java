package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.HLvagt;

import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FyrCommand implements CommandExecutor {


    private final HLvagt plugin;
    private PlayerProfiles playerProfiles;

    public FyrCommand(HLvagt plugin, PlayerProfiles playerProfiles) {
        this.plugin = plugin;
        this.playerProfiles = playerProfiles;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(VagtUtils.isSenderNotPlayer(sender)) return true;

        Player p = (Player) sender;
        if (VagtUtils.notHasPermission(p,"direktør")) return true;

        if(args.length == 0) {
            p.sendMessage("/fyr <Player>");

            return true;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        if(!offlinePlayer.hasPlayedBefore()) {
            return true;
        }
        Player fyr = offlinePlayer.getPlayer();
        removeVagtPermsForPlayer(offlinePlayer);
        if(fyr == null) {
            return true;
        }
        PlayerProfile playerProfile = playerProfiles.getPlayerProfile(fyr.getUniqueId());
        playerProfiles.removeVagt(playerProfile);
        clearInventory(fyr);
        resetPlayerBalance(fyr);
        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
        Bukkit.broadcastMessage("§c§lVagten §6" + fyr.getName());
        Bukkit.broadcastMessage("§7Er lige blevet fyret");
        Bukkit.broadcastMessage("§7Af §4" + p.getName());
        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");




        return true;
    }

    private void resetPlayerBalance(OfflinePlayer p) {
        double money = plugin.econ.getBalance(p);

        plugin.econ.withdrawPlayer(p,money);
        plugin.econ.depositPlayer(p, 400);
    }

    private void clearInventory(OfflinePlayer p) {
        p.getPlayer().getInventory().clear();
        p.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR, 1));
        p.getPlayer().getInventory().setChestplate(new ItemStack(Material.AIR, 1));
        p.getPlayer().getInventory().setLeggings(new ItemStack(Material.AIR, 1));
        p.getPlayer().getInventory().setBoots(new ItemStack(Material.AIR, 1));
        p.getPlayer().setHealth(0);
    }

    private void removeVagtPermsForPlayer(OfflinePlayer p) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent remove c-vagt prison");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent remove b-vagt prison");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent remove a-vagt prison");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent remove officer prison");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent add c-fange prison");
    }


}
