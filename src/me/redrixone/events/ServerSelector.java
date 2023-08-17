package me.redrixone.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.redrixone.api.Stack.compass;
import static me.redrixone.utils.ServerSelectorItems.createItem;
import static org.bukkit.Bukkit.getServer;

public class ServerSelector implements Listener {

    private Plugin plugin = getServer().getPluginManager().getPlugin("LobbyCore");
    List<String> testi = new ArrayList<String>();
    List<String> testiM = new ArrayList<String>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.getInventory().setItem(0, compass());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack itemComp = event.getItem();

        if (itemComp != null && itemComp.getType() == Material.COMPASS) {
            player.openInventory(serverSelector(player));
        }
    }

    private Inventory serverSelector(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 45, "Game Menu");

        //Flashing click to connect
        testi.add(ChatColor.AQUA + "▶ Click to Connect");
        testi.add(ChatColor.AQUA + "  Click to Connect");

        testiM.add(ChatColor.RED + "▶ Under development");
        testiM.add(ChatColor.RED + "  Under development");

        //Vanilla object
        List<String> loreV = Arrays.asList(ChatColor.DARK_GRAY + "Persistent Game", "", ChatColor.GRAY + "Play our Vanilla game and", ChatColor.GRAY + "have fun with your friends!", "", "");
        ItemStack vanilla = createItem(Material.GRASS, ChatColor.AQUA + "Vanilla", loreV);
        inventory.setItem(11, vanilla);
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            int index = 0;
            @Override
            public void run() {
                ItemStack itemRl = inventory.getItem(11);
                ItemMeta meta = itemRl.getItemMeta();
                loreV.set(5, testiM.get(index));
                meta.setLore(loreV);
                itemRl.setItemMeta(meta);
                inventory.setItem(11, itemRl);
                player.updateInventory();
                index = (index + 1) % testiM.size();
            }
        }, 0L, 20L);

        //Duels object
        List<String> loreD = Arrays.asList(ChatColor.DARK_GRAY + "Competitive", "", ChatColor.GRAY + "Quick paced 1v1, 2v2, 3v3, 4v4.", "", "");
        ItemStack duels = createItem(Material.IRON_SWORD, ChatColor.AQUA + "Duels", loreD);
        inventory.setItem(15, duels);
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            int index = 0;
            @Override
            public void run() {
                ItemStack itemRl = inventory.getItem(15);
                ItemMeta meta = itemRl.getItemMeta();
                loreD.set(4, testiM.get(index));
                meta.setLore(loreD);
                itemRl.setItemMeta(meta);
                inventory.setItem(15, itemRl);
                player.updateInventory();
                index = (index + 1) % testiM.size();
            }
        }, 0L, 20L);

        //Factions object
        List<String> loreF = Arrays.asList(ChatColor.DARK_GRAY + "Persistent Game", "", ChatColor.GRAY + "Enter an epic battle for supremacy in", ChatColor.GRAY + "a vast world, where alliances are forged", ChatColor.GRAY + "and broken in Minecraft's factions mode.", ChatColor.GRAY + "Build, defend and conquer territory to assert", ChatColor.GRAY + "your dominance.", ChatColor.GRAY + "Strategy, tactics, and limitless action await you", ChatColor.GRAY + "in this immersive gaming universe. §7(§cI§fT§aA§7)", "", "");
        ItemStack factions = createItem(Material.DIAMOND_AXE, ChatColor.AQUA + "OPFactions", loreF);
        inventory.setItem(31, factions);
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            int index = 0;
            @Override
            public void run() {
                ItemStack itemRl = inventory.getItem(31);
                ItemMeta meta = itemRl.getItemMeta();
                loreF.set(10, testi.get(index));
                meta.setLore(loreF);
                itemRl.setItemMeta(meta);
                inventory.setItem(31, itemRl);
                player.updateInventory();
                index = (index + 1) % testi.size();
            }
        }, 0L, 20L);

        return inventory;
    }

    @EventHandler
    public void onDropEvent(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem != null && currentItem.getType() == Material.COMPASS) {
            event.setCancelled(true);
        }
    }

}
