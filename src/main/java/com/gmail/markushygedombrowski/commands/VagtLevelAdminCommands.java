package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;

import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class VagtLevelAdminCommands implements CommandExecutor {
    private final PlayerProfiles playerProfiles;
    private final HLvagt plugin;
    public VagtLevelAdminCommands(PlayerProfiles playerProfiles, HLvagt plugin) {
        this.playerProfiles = playerProfiles;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("Vagt.Admin")) {
            sender.sendMessage("§4Du har ikke tilladelse til at bruge denne kommando");
            return true;
        }

        if (!(sender instanceof Player)) {
            System.out.println("Kun spillere kan bruge denne command");
            return true;
        }

        Player player = (Player) sender;

        switch (alias.toLowerCase()) {
            case "vhelp":
                player.sendMessage("§aVagt Admin Commands:");
                player.sendMessage("§a/vaddexp <spiller> <exp> - Tilføj exp til en spiller");
                player.sendMessage("§a/vaddlevel <spiller> <level> - Tilføj level til en spiller");
                player.sendMessage("§a/vlevel <spiller> <level> - Sæt level for en spiller");
                player.sendMessage("§a/vexp <spiller> <exp> - Sæt exp for en spiller");
                player.sendMessage("§a/vreset <spiller> - Nulstil en spillers level og exp");
                player.sendMessage("§a/showstats <spiller> - Vis stats for en spiller");
                player.sendMessage("§a/vbufflevel <spiller> <level> - Sæt buff level for en spiller");
                player.sendMessage("§a/vconfirmresetall - Nulstil alle spilleres level og exp");
                player.sendMessage("§a/vsetvagtpost <spiller> <antal> - Sæt antal vagtposter for en spiller");
                player.sendMessage("§a/vsetplaytime <spiller> <tid> - Sæt spilletid for en spiller");
                player.sendMessage("§a/vgivelon <spiller> - Giv lon til en spiller");
                player.sendMessage("§a/vreload - Genindlæs pluginet");
                break;
            case "vaddexp":
                handleAddExp(args, player);
                break;
            case "vaddlevel":
                handleAddLevel(args, player);
                break;
            case "vlevel":
                handleSetLevel(args, player);
                break;
            case "vexp":
                handleSetExp(args, player);
                break;
            case "vreset":
                handleReset(args, player);
                break;
            case "showstats":
                handleShowStats(args, player);
                break;
            case "vbufflevel":
                handleBuffLevel(args, player);
                break;
            case "vconfirmresetall":
                handleConfirmResetAll(player);
                break;
            case "vsetvagtpost":
                handleVagtPost(args, player);
                break;
            case "vsetplaytime":
                handleSetPlayTime(args, player);
                break;
            case "vgivelon":
                handleGiveLon(args, player);
                break;
            case "vreload":
                plugin.reload();
                player.sendMessage("§aPluginet er genindlæst");
                break;
            default:
                player.sendMessage("§4Ugyldig kommando");


        }

        return true;
    }


    private void handleGiveLon(String[] args, Player player) {
        if (args.length < 1) {
            player.sendMessage("§4Brug /vgivelon <spiller>");
            return;
        }

        Player target = getPlayer(args[0], player);
        if (target == null) return;

        if (!target.hasPermission("vagt")) {
            player.sendMessage("§4Spilleren er ikke vagt");
            return;
        }

        // Assuming there's a method to give lon
        HLvagt.getInstance().getLon().giveLon(target);
    }
    private void handleSetPlayTime(String[] args, Player player) {
        if (args.length < 2) {
            player.sendMessage("§4Brug /vsetplaytime <spiller> <tid>");
            return;
        }

        Player target = getPlayer(args[0], player);
        if (target == null) return;

        int time = parseInt(args[1], player, "§4Ugyldig tid");
        if (time == -1) return;
        time = time * 20 * 60; // Convert to ticks
        player.setStatistic(Statistic.PLAY_ONE_TICK, time);

    }
    private void handleVagtPost(String[] args, Player player) {
        if (args.length < 1) {
            player.sendMessage("§4Brug /vsetvagtpost <spiller>");
            return;
        }

        Player target = getPlayer(args[0], player);
        if (target == null) return;

        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        int amount = Integer.parseInt(args[1]);
        profile.setProperty("vagtposter", amount);
        saveProfile(profile, player, "§aDu har sat " + target.getName() + " til at have taget " + args[1] + " vagtposter");
    }

    private void handleAddExp(String[] args, Player player) {
        if (args.length < 2) {
            player.sendMessage("§4Brug /vexp <spiller> <exp>");
            return;
        }

        Player target = getPlayer(args[0], player);
        if (target == null) return;

        int exp = parseInt(args[1], player, "§4Ugyldigt exp-tal");
        if (exp == -1) return;

        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        profile.setXp(profile.castPropertyToInt(profile.getProperty("exp")) + exp);
        saveProfile(profile, player, "§aDu har tilføjet " + target.getName() + " " + exp + " ekstra exp");
    }

    private void handleAddLevel(String[] args, Player player) {
        if (args.length < 2) {
            player.sendMessage("§4Brug /vlevel <spiller> <level>");
            return;
        }

        Player target = getPlayer(args[0], player);
        if (target == null) return;

        int level = parseInt(args[1], player, "§4Ugyldigt level-tal");
        if (level == -1) return;

        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        profile.setProperty("level", profile.castPropertyToInt(profile.getProperty("level")) + level);
        saveProfile(profile, player, "§aDu har givet " + target.getName() + " ekstra level " + level);
    }

    private void handleSetLevel(String[] args, Player player) {
        if (args.length < 2) {
            player.sendMessage("§4Brug /vlevel <spiller> <level>");
            return;
        }

        Player target = getPlayer(args[0], player);
        if (target == null) return;

        int level = parseInt(args[1], player, "§4Ugyldigt level-tal");
        if (level == -1) return;

        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        profile.setProperty("level", level);
        saveProfile(profile, player, "§aDu har sat " + target.getName() + " til level " + level);
    }

    private void handleSetExp(String[] args, Player player) {
        if (args.length < 2) {
            player.sendMessage("§4Brug /vexp <spiller> <exp>");
            return;
        }

        Player target = getPlayer(args[0], player);
        if (target == null) return;

        int exp = parseInt(args[1], player, "§4Ugyldigt exp-tal");
        if (exp == -1) return;

        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        profile.setXp(exp);
        saveProfile(profile, player, "§aDu har sat " + target.getName() + " til " + exp + " exp");
    }

    private void handleReset(String[] args, Player player) {
        if (args.length < 1) {
            player.sendMessage("§4Brug /vreset <spiller>");
            return;
        }

        Player target = getPlayer(args[0], player);
        if (target == null) return;

        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        profile.setProperty("level", 1);
        profile.setXp(0);
        saveProfile(profile, player, "§aDu har nulstillet " + target.getName() + "'s level og exp");
    }

    private void handleShowStats(String[] args, Player player) {
        if (args.length < 1) {
            player.sendMessage("§4Brug /showstats <spiller>");
            return;
        }

        Player target = getPlayer(args[0], player);
        if (target == null) return;

        if (!target.hasPermission("vagt")) {
            player.sendMessage("§4Spilleren er ikke vagt");
            return;
        }

        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        // Add logic to display stats here
    }

    private void handleBuffLevel(String[] args, Player player) {
        if (args.length < 2) {
            player.sendMessage("§4Brug /vbufflevel <spiller> <level>");
            return;
        }

        Player target = getPlayer(args[0], player);
        if (target == null) return;

        int level = parseInt(args[1], player, "§4Ugyldigt buff level");
        if (level == -1) return;

        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        profile.setProperty("buff", level);
        saveProfile(profile, player, "§aDu har sat " + target.getName() + " til buff level " + level);
    }

    private void handleConfirmResetAll(Player player) {
        playerProfiles.getProfileMap().values().forEach(profile -> {
            profile.setProperty("level", 1);
            profile.setXp(0);
        });
        playerProfiles.saveAll();
        player.sendMessage("§aDu har nulstillet alle spilleres level og exp.");
    }

    @Nullable
    private Player getPlayer(String name, Player player) {
        Player target = player.getServer().getPlayer(name);
        if (target == null) {
            player.sendMessage("§4Spilleren findes ikke");
        }
        return target;
    }

    private int parseInt(String value, Player player, String errorMessage) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            player.sendMessage(errorMessage);
            return -1;
        }
    }

    private void saveProfile(PlayerProfile profile, Player player, String successMessage) {
        try {
            playerProfiles.save(profile);
            player.sendMessage(successMessage);
        } catch (Exception e) {
            player.sendMessage("§4Kunne ikke gemme spillerens profil");
            e.printStackTrace();
        }
    }
}