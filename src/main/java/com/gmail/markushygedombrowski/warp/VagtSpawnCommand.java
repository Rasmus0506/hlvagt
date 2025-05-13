package com.gmail.markushygedombrowski.warp;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class VagtSpawnCommand implements CommandExecutor {
    private final VagtSpawnManager vagtSpawnManager;
    private final JavaPlugin plugin;

    public VagtSpawnCommand(VagtSpawnManager vagtSpawnManager, JavaPlugin plugin) {
        this.vagtSpawnManager = vagtSpawnManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("warpAllow")){
            sender.sendMessage("Det har du ikke permission til!");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§e/vagtspawn <name>");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cthis command can only be used by players");
            return true;
        }
        Player player = (Player) sender;
        String warpName = args[0];
        VagtSpawnInfo info = vagtSpawnManager.getWarpInfo(warpName);

        if (alias.equalsIgnoreCase("setvagtspawn")) {
            setWarp(player, warpName, info);
            return true;
        }
        if(alias.equalsIgnoreCase("resetvagtspawn")) {
            resetVagtSpawn(player,info);
            return true;
        }


        if(!doesExist(player,info)) return true;


        player.teleport(info.getLocation());
        return true;
    }

    private void setWarp(Player player, String warpName, VagtSpawnInfo info) {
        if (!player.hasPermission("setwarp")) {
            player.sendMessage("Det har du ikke permission til!");
            return;
        }
        if (doesExist(player,info)) {
            player.sendMessage("§cThat warp already exist!");
            player.sendMessage("§cUse /resetVagtSpawn");
            return;
        }
        Location location = player.getLocation();
        info = new VagtSpawnInfo(warpName, location);
        vagtSpawnManager.save(info);
        player.sendMessage("§a§lWarp created at §e" + location.toString());
    }
    private void resetVagtSpawn(Player player, VagtSpawnInfo info) {
        if (!player.hasPermission("setwarp")) {
            player.sendMessage("§cYou do not the permission to do that!");
            return;
        }
        if(!doesExist(player,info)) {
            return;
        }
        Location location = player.getLocation();
        info.setLocation(location);
        vagtSpawnManager.save(info);
        player.sendMessage("Replaced Warp location");
    }

    private boolean doesExist(Player player,VagtSpawnInfo info) {
        if(info == null) {
            player.sendMessage("§cThat warp doesn't exist");
            return false;
        }
        return true;

    }
}
