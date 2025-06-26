package com.gmail.markushygedombrowski.sellchest;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.HashSet;
import java.util.Set;

public class SellChestCommand implements CommandExecutor {
    
    private final HLvagt plugin;
    private final VagtChestManager chestManager;

    public SellChestCommand(HLvagt plugin, VagtChestManager chestManager) {
        this.plugin = plugin;
        this.chestManager = chestManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(VagtUtils.isSenderNotPlayer(sender)) return true;

        Player p = (Player) sender;
        if(VagtUtils.notHasPermission(p, "admin")) return true;

        // Opret et tomt Set af transparente blokke
        Set<Material> transparent = new HashSet<>();
        transparent.add(Material.AIR);
        transparent.add(Material.WATER);
        transparent.add(Material.STATIONARY_WATER);
        
        Block targetBlock = p.getTargetBlock(transparent, 5);
        
        if (targetBlock == null || targetBlock.getType() != Material.CHEST) {
            p.sendMessage("§cDu skal kigge på en kiste!");
            return true;
        }

        chestManager.registerSellChest(targetBlock.getLocation());
        p.sendMessage("§aDenne kiste er nu registreret som en sælge-kiste!");
        
        return true;
    }
}