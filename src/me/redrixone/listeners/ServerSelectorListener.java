package me.redrixone.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.bukkit.Bukkit.getServer;

public class ServerSelectorListener implements Listener {

    Plugin plugin = getServer().getPluginManager().getPlugin("LobbyCore");

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack currentItem = event.getCurrentItem();

        if (inventory.getName().contains("Menu")) {
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
            } else if (currentItem != null && currentItem.getType() == Material.IRON_SWORD && currentItem.getItemMeta().getDisplayName().contains("Duels")) {
                event.setCancelled(true);
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                try {
                    out.writeUTF("Connect");
                    out.writeUTF("duels-lobby");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            } else if (currentItem != null && currentItem.getType() == Material.DIAMOND_AXE && currentItem.getItemMeta().getDisplayName().contains("OPFactions")) {
                event.setCancelled(true);
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                try {
                    out.writeUTF("Connect");
                    out.writeUTF("fitaly-1");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            }
        }
    }
}
