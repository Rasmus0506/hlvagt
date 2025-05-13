package com.gmail.markushygedombrowski.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VagtChat implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

        if(!sender.hasPermission("vagt")) {
            sender.sendMessage("§cDet har du ikke permission til!");
            return true;
        }
        Player p = (Player) sender;
        if(args.length == 0) {
            p.sendMessage("§7§l----------§c§lVAGT§7§l----------");
            p.sendMessage("§c /vc §7<text>");
            p.sendMessage("§7§l----------§c§lVAGT§7§l----------");
            return true;
        }
        StringBuilder message = new StringBuilder("§2§lVagt Chat: " + p.getDisplayName());
        for(String text : args) {
            message.append(" §7").append(text);
        }
        Bukkit.broadcast(message.toString(),"vagt");

        return true;
    }
}
