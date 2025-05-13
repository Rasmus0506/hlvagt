package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FineCommand implements CommandExecutor {
    private HLvagt plugin;

    public FineCommand(HLvagt plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(VagtUtils.isSenderNotPlayer(sender)) return true;

        Player p = (Player) sender;
        if (VagtUtils.notHasPermission(p,"bøde")) return true;

        if(args.length <= 1) {
            p.sendMessage("§7§l----------§c§lVAGT§7§l----------");
            p.sendMessage("§l§c/bøde <navn> <rank>");
            p.sendMessage("§bB-§7fange -> §bb");
            p.sendMessage("§bB-§6donator -> §b§6bd");
            p.sendMessage("§aA-§7fange -> §aa");
            p.sendMessage("§aA-§6donator -> §aa§6d");
            p.sendMessage("§7§l----------§c§lVAGT§7§l----------");
            return true;
        }
        Player finedP = Bukkit.getPlayer(args[0]);
        if(finedP == null) {
            p.sendMessage("er ikke en spiller");
            return true;
        }
        p.sendMessage("pew");





        return true;
    }


}
