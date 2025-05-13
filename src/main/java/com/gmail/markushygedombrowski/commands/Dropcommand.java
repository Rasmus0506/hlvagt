package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.listners.DropItemListener;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Dropcommand implements CommandExecutor {
    private DropItemListener listener;


    public Dropcommand(DropItemListener listener) {
        this.listener = listener;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("kun spillere kan bruge denne command");
            return true;
        }

        Player p = (Player) sender;
        if (VagtUtils.notHasPermission(p,"vagt")) return true;

        listener.setDrop(!listener.isDrop());
        return true;
    }
}
