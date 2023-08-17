package me.redrixone.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.Bukkit.getServer;
import static me.redrixone.utils.ServerSelectorItems.createItem;

public class FastConnect implements Listener {

    Plugin plugin = getServer().getPluginManager().getPlugin("LobbyCore");


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        List<String> testi = new ArrayList<String>();
        testi.add(ChatColor.AQUA + "▶ Click to Connect");
        testi.add(ChatColor.AQUA + "  Click to Connect");

        List<String> testiM = new ArrayList<String>();
        testiM.add(ChatColor.RED + "▶ Under development");
        testiM.add(ChatColor.RED + "  Under development");

        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();

        //Vanilla object
        List<String> loreV = Arrays.asList(ChatColor.DARK_GRAY + "Persistent Game", "", ChatColor.GRAY + "Play our Vanilla game and", ChatColor.GRAY + "have fun with your friends!", "", "");
        ItemStack vanilla = createItem(Material.GRASS, ChatColor.AQUA + "Vanilla", loreV);
        inventory.setItem(9, vanilla);
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            int index = 0;
            @Override
            public void run() {
                ItemStack itemRl = inventory.getItem(9);
                ItemMeta meta = itemRl.getItemMeta();
                loreV.set(5, testiM.get(index));
                meta.setLore(loreV);
                itemRl.setItemMeta(meta);
                inventory.setItem(9, itemRl);
                player.updateInventory();
                index = (index + 1) % testiM.size();
            }
        }, 0L, 20L);

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        ItemMeta glassMeta = glass.getItemMeta();

        glassMeta.setDisplayName(ChatColor.AQUA + "Custom Slot");
        List<String> glassLore = new ArrayList<String>();
        glassLore.add("");
        glassLore.add(ChatColor.RED + "Waiting for new games ;)");
        glassMeta.setLore(glassLore);
        glass.setItemMeta(glassMeta);

        inventory.setItem(10, glass);
        inventory.setItem(11, glass);
        inventory.setItem(12, glass);
        inventory.setItem(13, glass);
        inventory.setItem(14, glass);
        inventory.setItem(15, glass);
        inventory.setItem(16, glass);
        inventory.setItem(17, glass);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack currentItem = event.getCurrentItem();

        if (currentItem != null && currentItem.getType() == Material.GRASS && currentItem.getItemMeta().getDisplayName().contains("Vanilla")) {
            event.setCancelled(true);
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("Connect");
                out.writeUTF("vanilla");
            } catch (IOException e) {
                e.printStackTrace();
            }

            player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
        } else if (currentItem != null && currentItem.getType() == Material.STAINED_GLASS_PANE) {
            event.setCancelled(true);
        }
    }

}
