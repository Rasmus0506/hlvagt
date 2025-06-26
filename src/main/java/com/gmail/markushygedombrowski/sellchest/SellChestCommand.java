package com.gmail.markushygedombrowski.sellchest;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SellChestCommand implements CommandExecutor {
    
    private final HLvagt plugin;

    public SellChestCommand(HLvagt plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(VagtUtils.isSenderNotPlayer(sender)) return true;

        Player p = (Player) sender;
        if(VagtUtils.notHasPermission(p, "admin")) return true;

        ItemStack chest = new ItemStack(Material.CHEST, 1);
        ItemMeta meta = chest.getItemMeta();
        meta.setDisplayName("§cVagt Sælge Kiste-c");
        chest.setItemMeta(meta);
        
        p.getInventory().addItem(chest);
        p.sendMessage("§aDu har modtaget en Vagt Sælge Kiste!");
        
        return true;
    }
}