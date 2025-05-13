package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.panikrum.PanikRum;
import com.gmail.markushygedombrowski.panikrum.PanikRumManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PanikRumCommands implements CommandExecutor {
    private PanikRumManager panikRumManager;

    public PanikRumCommands(PanikRumManager panikRumManager) {
        this.panikRumManager = panikRumManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player))
            return true;
        Player player = (Player) commandSender;
        if(!player.hasPermission("panikrum")) {
            player.sendMessage("§cDu har ikke permission til at bruge denne kommando!");
            return true;
        }
        if(strings.length == 0) {
            player.sendMessage("§cBrug /panikrum add <navn> for at tilføje et panikrum");
            player.sendMessage("§cBrug /panikrum remove <navn> for at fjerne et panikrum");
            return true;
        }
        if(strings[0].equalsIgnoreCase("add")) {
            if(strings.length == 1) {
                player.sendMessage("§cBrug /panikrum add <navn> for at tilføje et panikrum");
                return true;
            }
            PanikRum panikRum = new PanikRum(player.getLocation(), strings[1]);
            panikRumManager.save(panikRum);
            player.sendMessage("§aPanikrummet " + strings[1] + " er blevet tilføjet!");
            return true;
        }
        if (strings[0].equalsIgnoreCase("remove")) {
            if(strings.length == 1) {
                player.sendMessage("§cBrug /panikrum remove <navn> for at fjerne et panikrum");
                return true;
            }
            PanikRum panikRum = panikRumManager.getPanikRum(strings[1]);
            if(panikRum == null) {
                player.sendMessage("§cPanikrummet " + strings[1] + " eksistere ikke!");
                return true;
            }
            panikRumManager.remove(panikRum);
            player.sendMessage("§aPanikrummet " + strings[1] + " er blevet fjernet!");
            return true;
        }

        return true;
    }
}
