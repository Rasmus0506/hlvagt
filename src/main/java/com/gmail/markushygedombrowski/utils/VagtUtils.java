package com.gmail.markushygedombrowski.utils;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.settings.Settings;
import org.bukkit.Bukkit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class VagtUtils {
    private static HLvagt plugin;
    private static PlayerProfiles playerProfiles;
    private static Settings settings;

    public VagtUtils(HLvagt plugin, PlayerProfiles playerProfiles, Settings settings) {
        VagtUtils.plugin = plugin;
        VagtUtils.playerProfiles = playerProfiles;
        VagtUtils.settings = settings;
    }


    public static boolean removeItems(Player p, List<ItemStack> items) {
        for (ItemStack item : items) { //dette loop checker om han har alle items inden vi fjerner dem
            if (!p.getInventory().contains(item)) {

                return false; //returnere false hvis et af vores items i vores list ikke er der i hans inventory
            }
        }
        for (ItemStack item : items) { //dette looper fjerner alle items fordi vi ved der er nok nu
            p.getInventory().removeItem(item);
        }
        return true;
    }

    public static int repairItems(Player p) {
        ItemStack[] items = p.getInventory().getContents();
        int amount = 0;
        for (ItemStack item : items) {
            if (!(item == null)) {
                if (item.getType() != Material.SKULL) {
                    item.setDurability((short) (item.getType().getMaxDurability() - item.getType().getMaxDurability()));
                    amount = amount + 1;
                }

            }

        }
        return amount;


    }


    public static List<Player> getPlayers(String perm) {
        List<Player> playerList = new ArrayList<>();

        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all.hasPermission(perm)) {
                playerList.add(all.getPlayer());
            }
        }
        return playerList;
    }

    public static Player getAttacker(EntityDamageByEntityEvent event) {
        Player p;
        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            p = (Player) projectile.getShooter();
            return p;
        }
        p = (Player) event.getDamager();
        return p;
    }


    public static List<String> top10Deaths() {
        List<String> list = new ArrayList<>();
        List<Integer> sortmap = new LinkedList<>();
        playerProfiles.getProfileMap().forEach((uuid, profile) -> {
            sortmap.add(profile.castPropertyToInt(profile.getProperty("deaths")));
        });
        Collections.sort(sortmap);
        Collections.reverse(sortmap);
        sortmap.forEach(values -> playerProfiles.getProfileMap().forEach((uuid, profile) -> {
            if (profile.castPropertyToInt(profile.getProperty("deaths")) == values) {
                if (list.size() == 10) {
                    return;
                }
                if (!list.contains("§6" + profile.getName() + ": §7" + values)) {
                    list.add("§6" + profile.getName() + ": §7" + values);
                }
            }
        }));
        return list;
    }

    public static boolean isPlayerNotOnline(Player player) {
        return player == null;
    }

    public static List<String> top10Kills() {

        List<String> list = new ArrayList<>();
        List<Integer> sortmap = new LinkedList<>();
        playerProfiles.getProfileMap().forEach((uuid, profile) -> {
            sortmap.add(profile.castPropertyToInt(profile.getProperty("kills")));
        });
        Collections.sort(sortmap);
        Collections.reverse(sortmap);
        sortmap.forEach(values -> playerProfiles.getProfileMap().forEach((uuid, profile) -> {
            if (profile.castPropertyToInt(profile.getProperty("kills")) == values) {
                if (list.size() == 10) {
                    return;
                }
                if (!list.contains("§6" + profile.getName() + ": §7" + values)) {
                    list.add("§6" + profile.getName() + ": §7" + values);
                }
            }
        }));
        return list;
    }

    public static List<String> top10Walk(String perm) {
        HashMap<String, Integer> stats = new HashMap<>();
        List<Player> players = VagtUtils.getPlayers(perm);
        List<String> list = new ArrayList<>();

        players.forEach(player -> {

            stats.put(player.getName(), player.getStatistic(Statistic.WALK_ONE_CM) / 100);
        });
        List<Integer> sortmap = new LinkedList<>(stats.values());
        Collections.sort(sortmap);
        Collections.reverse(sortmap);

        sortmap.forEach(values -> stats.forEach((name, value) -> {
            if (value.equals(values)) {
                if (!list.contains(name + ": " + values)) {
                    list.add("§6" + name + ": §7" + values);
                }
            }
        }));
        return list;
    }


    public static boolean isSenderNotPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            System.out.println("denne command er kun for spillere");
            return true;
        }
        return false;
    }

    public static boolean notHasPermission(Player p, String perm) {
        if (!p.hasPermission(perm)) {
            p.sendMessage("§cDet har du ikke Permission til!");
            return true;
        }
        return false;
    }

    public static List<String> top10Money(String perm) {
        List<String> list = new ArrayList<>();
        List<Integer> sortmap = new LinkedList<>();


        return list;
    }

    public static List<String> top10Level() {
        List<String> list = new ArrayList<>();
        List<Integer> sortmap = new LinkedList<>();
        playerProfiles.getProfileMap().forEach((uuid, profile) -> {
            sortmap.add(profile.castPropertyToInt(profile.getProperty("level")));
        });
        Collections.sort(sortmap);
        Collections.reverse(sortmap);
        sortmap.forEach(values -> playerProfiles.getProfileMap().forEach((uuid, profile) -> {
            if (profile.castPropertyToInt(profile.getProperty("level")) == values) {
                if (list.size() == 10) {
                    return;
                }
                if (!list.contains("§6" + profile.getName() + ": §7" + values)) {
                    list.add("§6" + profile.getName() + ": §7" + values);
                }
            }
        }));
        return list;
    }

    public static List<String> top10VagtPoster() {
        List<String> list = new ArrayList<>();
        List<Integer> sortmap = new LinkedList<>();
        playerProfiles.getProfileMap().forEach((uuid, profile) -> {

            sortmap.add(profile.castPropertyToInt(profile.getProperty("vagtposter")));
        });
        Collections.sort(sortmap);
        Collections.reverse(sortmap);
        sortmap.forEach(values -> playerProfiles.getProfileMap().forEach((uuid, profile) -> {
            if (profile.castPropertyToInt(profile.getProperty("vagtposter")) == values) {
                if (list.size() == 10) {
                    return;
                }
                if (!list.contains("§6" + profile.getName() + ": §7" + values)) {
                    list.add("§6" + profile.getName() + ": §7" + values);
                }
            }
        }));
        return list;
    }


    public static boolean procent(double pro) {
        Random r = new Random();
        double num = r.nextInt(100);
        return num <= pro;

    }


    public static ItemStack getGlass() {
        Random r = new Random();
        return new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) r.nextInt(15));
    }
    public static String getRegion(Location location) {

        if (Utils.isLocInRegion(location, "a")) {
            return "a";
        } else if (Utils.isLocInRegion(location, "b")) {
            return "b";
        } else {
            return "c";
        }
    }

    public static int castPropertyToInt(Object key) {
        if (key instanceof Double) {
            return  (((Double) key).intValue());
        }
        return (int) key;
    }
    public void fillEmptySlots(Inventory inventory) {
        Random random = new Random();
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) random.nextInt(15));
        glass.getItemMeta().setDisplayName(" ");
        for (ItemStack item : inventory.getContents()) {
            if (item == null) {
                inventory.setItem(inventory.firstEmpty(), glass);
            }
        }
    }

    public ItemStack getCloseButton() {
        ItemStack closeButton = new ItemStack(Material.BARRIER);
        ItemMeta meta = closeButton.getItemMeta();
        meta.setDisplayName("§c§lClose");
        List<String> lore = new ArrayList<>();
        lore.add("§7Click to close the menu.");
        meta.setLore(lore);
        closeButton.setItemMeta(meta);
        return closeButton;
    }
    public ItemStack getBackButton() {
        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta meta = backButton.getItemMeta();
        meta.setDisplayName("§e§lBack");
        List<String> lore = new ArrayList<>();
        lore.add("§7Click to go back to the previous menu.");
        meta.setLore(lore);
        backButton.setItemMeta(meta);
        return backButton;
    }
    public ItemStack getNextButton() {
        ItemStack nextButton = new ItemStack(Material.ARROW);
        ItemMeta meta = nextButton.getItemMeta();
        meta.setDisplayName("§a§lNext Page");
        List<String> lore = new ArrayList<>();
        lore.add("§7Click to go to the next page.");
        meta.setLore(lore);
        nextButton.setItemMeta(meta);
        return nextButton;
    }
}
















