package com.gmail.markushygedombrowski.buff;


import com.gmail.markushygedombrowski.settings.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AktivBuffCmd implements CommandExecutor {
    private Settings settings;
    private BuffManager buffManager;

    public AktivBuffCmd(Settings settings, BuffManager buffManager) {
        this.settings = settings;
        this.buffManager = buffManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("kun spillere kan bruge denne command");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("buff.admin")) {
            player.sendMessage("§cDu har ikke permission til at starte et event");
            return true;
        }

        if (alias.equalsIgnoreCase("buff")) {
            return handleBuffCommand(player, args);
        }
        return handleBuffActivation(player);
    }

    private boolean handleBuffCommand(Player player, String[] args) {
        if (args.length <= 1) {
            player.sendMessage("§c/buff <player> <buff level>");
            return true;
        }

        Player target = player.getServer().getPlayer(args[0]);
        int level = Integer.parseInt(args[1]);

        BuffLevels buffLevels = buffManager.getBuffLevel(level);
        if (target == null || buffLevels == null) {
            player.sendMessage(target == null ? "§cSpilleren er ikke online" : "§cBuff level findes ikke");
            return true;
        }
        buffLevels.giveBuff(target);
        player.sendMessage("§aDu har givet " + target.getName() + " buff level " + level);
        return true;
    }

    private boolean handleBuffActivation(Player player) {
        if (settings.isAktivbuff()) {
            player.sendMessage("§cDu har stoppet for at vagter kan tag buff");
        } else {
            player.sendMessage("§aDu har startet for at vagter kan tag buff");
        }
        settings.setAktivbuff(!settings.isAktivbuff());
        return true;
    }
}
