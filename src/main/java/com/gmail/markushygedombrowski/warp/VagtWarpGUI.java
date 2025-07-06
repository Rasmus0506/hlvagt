package com.gmail.markushygedombrowski.warp;


import com.gmail.markushygedombrowski.inventory.ChangeInvOnWarp;
import com.gmail.markushygedombrowski.inventory.InvHolder;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

public class VagtWarpGUI implements Listener {
    private final VagtSpawnManager vagtSpawnManager;
    private final int WARP_C_INDEX = 11;
    private final int WARP_B_INDEX = 13;
    private final int WARP_A_INDEX = 15;
    private final int WARP_CVCELLESTUE_INDEX = 29;
    private final int WARP_BVCELLESTUE_INDEX = 31;
    private final int WARP_AVAGTCELLER_INDEX = 33;
    private final int WARP_APLUS_INDEX = 25;
    private final ChangeInvOnWarp changeInvOnWarp;
    private final PlayerProfiles playerProfiles;

    public VagtWarpGUI(VagtSpawnManager vagtSpawnManager, ChangeInvOnWarp changeInvOnWarp, PlayerProfiles playerProfiles) {
        this.vagtSpawnManager = vagtSpawnManager;
        this.changeInvOnWarp = changeInvOnWarp;
        this.playerProfiles = playerProfiles;
    }

    public void create(Player p, String block) {
        Inventory inventory = Bukkit.createInventory(null, 45, "Warp: " + block);
        ItemStack warpC = createWarpItem(DyeColor.RED, "§c§lWarp C");
        ItemStack warpB = createWarpItem(DyeColor.BLUE, "§b§lWarp B");
        ItemStack warpA = createWarpItem(DyeColor.GREEN, "§a§lWarp A");
        ItemStack warpAvagtceller = createWarpItem(DyeColor.GREEN, "§a§lWarp A Vagt Celler");
        ItemStack warpBvcellestue = createWarpItem(DyeColor.BLUE, "§b§lWarp B Vagt Celler");
        ItemStack warpCvcellestue = createWarpItem(DyeColor.RED, "§c§lWarp C Vagt Celler");

        inventory.setItem(WARP_C_INDEX, warpC);
        inventory.setItem(WARP_B_INDEX, warpB);
        inventory.setItem(WARP_A_INDEX, warpA);
        inventory.setItem(WARP_AVAGTCELLER_INDEX, warpAvagtceller);
        inventory.setItem(WARP_BVCELLESTUE_INDEX, warpBvcellestue);
        inventory.setItem(WARP_CVCELLESTUE_INDEX, warpCvcellestue);


        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 2);
        inventory.forEach(item -> {
            if (item == null) {
                inventory.setItem(inventory.firstEmpty(), glass);
            }
        });
        p.openInventory(inventory);
    }

    private ItemStack createWarpItem(DyeColor color, String displayName) {
        Wool wool = new Wool(color);
        ItemStack item = wool.toItemStack(1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        if (clickeditem == null || !inventory.getTitle().contains("Warp: ")) {
            return;
        }
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
        String block = inventory.getTitle().replace("Warp: ", "");
        switch (clickedSlot) {
            case WARP_C_INDEX:
                warpPlayer(p, "vagtc", "§7Du er warpet til §cC");
                break;
            case WARP_CVCELLESTUE_INDEX:
                warpPlayer(p, "C2", "§7Du er warpet til §cC Vagt Celler");
                break;
            case WARP_BVCELLESTUE_INDEX:
                warpPlayer(p, "bcellestue", "§7Du er warpet til §bB Celle Stue");
                break;
            case WARP_B_INDEX:
                if (!p.hasPermission("vagtwarpb")) {
                    p.sendMessage("§cDu har ikke højt nok rank");
                    return;
                }
                warpPlayer(p, "vagtb", "§7Du er warpet til §bB");
                break;
            case WARP_A_INDEX:
                if (!p.hasPermission("vagtwarpa")) {
                    p.sendMessage("§cDu har ikke højt nok rank");
                    return;
                }
                warpPlayer(p, "a", "§7Du er warpet til §aA");
                break;
            case WARP_AVAGTCELLER_INDEX:
                if (!p.hasPermission("vagtwarpa")) {
                    p.sendMessage("§cDu har ikke højt nok rank");
                    return;
                }
                warpPlayer(p, "avagtceller", "§7Du er warpet til §aA Vagt Celler");
                break;
            default:
                return;
        }
        changeInvOnWarp.changeInventory(p, block);
        InvHolder invHolder = changeInvOnWarp.getInventory(p.getUniqueId(), VagtUtils.getRegion(p.getLocation()));
        if (invHolder != null) {
            p.getInventory().setContents(invHolder.getInventory());
            p.getInventory().setArmorContents(invHolder.getGear());
            p.sendMessage("Du har skiftet gear");
            return;
        }
        p.getInventory().clear();
        p.getInventory().setHelmet(new ItemStack(Material.AIR));
        p.getInventory().setChestplate(new ItemStack(Material.AIR));
        p.getInventory().setLeggings(new ItemStack(Material.AIR));
        p.getInventory().setBoots(new ItemStack(Material.AIR));
        p.sendMessage("Dit inventory for denne region blev ikke fundet.");
        p.sendMessage("Du har ikke mistet dine ting");
        p.sendMessage("Hvis du warper er dine ting der stadig");
    }

    private void warpPlayer(Player p, String warpName, String message) {
        p.sendMessage(message);
        VagtSpawnInfo spawnInfo = vagtSpawnManager.getWarpInfo(warpName);
        if (spawnInfo != null) {
            p.teleport(spawnInfo.getLocation());
            InvHolder invHolder = changeInvOnWarp.getInventory(p.getUniqueId(), warpName);
            if (invHolder != null) {
                p.getInventory().setContents(invHolder.getInventory());
                p.getInventory().setArmorContents(invHolder.getGear());
                p.sendMessage("Du har skiftet gear");
                return;
            }
        }
    }
}