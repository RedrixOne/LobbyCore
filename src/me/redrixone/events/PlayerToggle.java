package me.redrixone.events;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerToggle implements Listener {
    private Map<Player, Long> interactTimes = new HashMap<>();
    private Connection connection;

    public PlayerToggle(JavaPlugin plugin) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("corpselog");
        dataSource.setPassword("1234");
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("visibility");
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
        createPlayerVisibilityTable();
    }

    private void createPlayerVisibilityTable() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS player_visibility (uuid VARCHAR(36) PRIMARY KEY, visible BOOLEAN)");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        short durability = 12;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT visible FROM player_visibility WHERE uuid = ?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                boolean isVisible = resultSet.getBoolean("visible");
                if(!isVisible) durability = 8; // grigio
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ItemStack item = new ItemStack(Material.INK_SACK, 1, durability);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Player visibility" + ChatColor.GRAY + " (Right Click)");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "Right Click to toggle player visibility!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        player.getInventory().setItem(7, item);

        if(durability == 8) {
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (other != player) {
                    player.hidePlayer(other);
                }
            }
        }
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.INK_SACK && event.getAction().name().contains("RIGHT")) {
            event.setCancelled(true);
            long currentTime = System.currentTimeMillis();
            long previousTime = interactTimes.getOrDefault(player, 0L);
            long elapsedTime = currentTime - previousTime;
            long remainingTime = (3500 - elapsedTime) / 1000;
            if (currentTime - previousTime < 3500) { // 3 secondi
                player.sendMessage(ChatColor.DARK_AQUA + "You must wait " + ChatColor.AQUA + remainingTime + "s " + ChatColor.DARK_AQUA + "between uses!");
                return;
            }
            interactTimes.put(player, currentTime);
            boolean isVisible = item.getDurability() == 12;
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (other != player) {
                    if (isVisible) {
                        player.hidePlayer(other);
                    } else {
                        player.showPlayer(other);
                    }
                }
            }
            item.setDurability(isVisible ? (short) 8 : (short) 12);
            player.sendMessage(isVisible ? ChatColor.RED + "Player visibility disabled!" : ChatColor.GREEN + "Player visibility enabled!");
            player.updateInventory();
            // Salva lo stato della visibilità del giocatore nel database
            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO player_visibility (uuid, visible) VALUES (?, ?) ON DUPLICATE KEY UPDATE visible = ?");
                statement.setString(1, player.getUniqueId().toString());
                statement.setBoolean(2, !isVisible);
                statement.setBoolean(3, !isVisible);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem != null && currentItem.getType() == Material.INK_SACK) {
            event.setCancelled(true);
        }
    }
}