package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class VagtLevelAdminCommands implements CommandExecutor {
    private PlayerProfiles playerProfiles;


    public VagtLevelAdminCommands(PlayerProfiles playerProfiles) {
        this.playerProfiles = playerProfiles;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        if(!sender.hasPermission("Vagt.Admin")) {
            sender.sendMessage("§4Du har ikke tilladelse til at bruge denne kommando");
            return true;
        }
        if(alias.equalsIgnoreCase("vaddexp")) {
            addExp(args, (Player) sender);
            return true;
        } else if(alias.equalsIgnoreCase("vaddlevel")) {
            addlevel(args, (Player) sender);
            return true;
        }
        if(!(sender instanceof Player)) {
            System.out.println("kun spillere kan bruge denne command");
            return true;
        }
        Player p = (Player) sender;
        boolean reseta = false;
        if(alias.equalsIgnoreCase("vlevel")) {
            setLevel(args, p);
            return true;
        } else if (alias.equalsIgnoreCase("vexp")) {
            setExp(args, p);
            return true;
        } else if (alias.equalsIgnoreCase("vreset")) {
            reset(args, p);
            return true;
        } else if(alias.equalsIgnoreCase("showstats")) {
            if(args.length == 0) {
                p.sendMessage("§4Brug /showstats <spiller>");
                return true;
            }
            Player target = getPlayer(args, p);
            if (target == null) return true;
            if(!target.hasPermission("vagt")) {
                p.sendMessage("§4Spilleren er ikke vagt");
                return true;
            }
            PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());


            return true;
        } else if(alias.equalsIgnoreCase("vbufflevel")) {
            if(args.length == 0 || args.length == 1) {
                p.sendMessage("§4Brug /vbufflevel <spiller> <level>");
                return true;
            }
            Player target = getPlayer(args, p);
            if (target == null) return true;
            int level = Integer.parseInt(args[1]);
            PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
            profile.setProperty("buff", level);
            p.sendMessage("§aDu har sat " + target.getName() + " til buff level " + level);
            try {
                playerProfiles.save(profile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return true;
        }

        if(!p.hasPermission("Vagt.HAdmin")) {
            p.sendMessage("§4Du har ikke tilladelse til at bruge denne kommando");
            return true;
        }
        if (alias.equalsIgnoreCase("vconfirmresetall")) {
            reseta = true;

        }
        if(!reseta) {
            p.sendMessage("§4-------RESET ALL-------");
            p.sendMessage("§4§lER DU SIKKER PÅ AT DU VIL NULSTILLE ALLE SPILLERES LEVEL OG EXP?");
            p.sendMessage("§4§lSKRIV /vconfirmresetall FOR AT BEKRÆFTE");
            p.sendMessage("§4-----------------------");
        }
        if(reseta) {
            confirmResetAll(p);
        }



        return true;
    }

    public void addlevel(String[] args, Player p) {
        if(args.length == 0 || args.length == 1) {
            p.sendMessage("§4Brug /vlevel <spiller> <level>");
            return;
        }
        Player target = getPlayer(args, p);
        if (target == null) return;
        double level = Double.parseDouble(args[1]);
        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        int currentLevel = profile.castPropertyToInt(profile.getProperty("level"));
        profile.setProperty("level", currentLevel + level);
        p.sendMessage("§aDu har givet " + target.getName() + " extra level " + level);
        try {
            playerProfiles.save(profile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return;
    }
    private void confirmResetAll(Player p) {
        playerProfiles.getProfileMap().values().forEach(profile -> {
            profile.setProperty("level",1);
            profile.setXp(0);

        });
        playerProfiles.saveAll();
        p.sendMessage("§aYou have reset all players' level and exp.");
    }
    private void reset(String[] args, Player p) {
        if(args.length == 0) {
            p.sendMessage("§4Brug /vreset <spiller>");
            return;
        }
        Player target = getPlayer(args, p);
        if (target == null) return;
        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        profile.setProperty("level",1);
        profile.setXp(0);
        p.sendMessage("§aDu har nulstillet " + target.getName() + "'s level og exp");
        try {
            playerProfiles.save(profile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return;
    }
    private void setExp(String[] args, Player p) {
        if(args.length == 0 || args.length == 1) {
            p.sendMessage("§4Brug /vexp <spiller> <exp>");
            return;
        }
        Player target = getPlayer(args, p);
        if (target == null) return;
        int exp = Integer.parseInt(args[1]);
        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        profile.setXp(exp);
        p.sendMessage("§aDu har sat " + target.getName() + " til " + exp + " exp");
        try {
            playerProfiles.save(profile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return;
    }
    public void addExp(String[] args, Player p) {
        if(args.length == 0 || args.length == 1) {
            p.sendMessage("§4Brug /vexp <spiller> <exp>");
            return;
        }
        Player target = getPlayer(args, p);
        if (target == null) return;
        int exp = Integer.parseInt(args[1]);
        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        int currentExp = profile.castPropertyToInt(profile.getProperty("exp"));
        profile.setXp(currentExp + exp);
        p.sendMessage("§aDu har tilføjet " + target.getName() + " " + exp + "extra exp");
        try {
            playerProfiles.save(profile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return;
    }

    private void setLevel(String[] args, Player p) {
        if(args.length == 0 || args.length == 1) {
            p.sendMessage("§4Brug /vlevel <spiller> <level>");
            return;
        }
        Player target = getPlayer(args, p);
        if (target == null) return;
        int level = Integer.parseInt(args[1]);
        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        profile.setProperty("level", level);
        p.sendMessage("§aDu har sat " + target.getName() + " til level " + level);
        try {
            playerProfiles.save(profile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return;
    }

    @Nullable
    private Player getPlayer(String[] args, Player p) {
        Player target = p.getServer().getPlayer(args[0]);
        if(target == null) {
            target = p.getServer().getOfflinePlayer(args[0]).getPlayer();
            if (target == null) {
                p.sendMessage("§4Spilleren findes ikke");
                return null;
            }
        }
        return target;
    }
}
