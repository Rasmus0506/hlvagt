package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.npc.vagthavende.DeliverGearGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuiTestCommand implements CommandExecutor {
private DeliverGearGUI deliverGearGUI;

    public GuiTestCommand(DeliverGearGUI deliverGearGUI) {
        this.deliverGearGUI = deliverGearGUI;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        deliverGearGUI.create(player);
        return false;
    }
}
