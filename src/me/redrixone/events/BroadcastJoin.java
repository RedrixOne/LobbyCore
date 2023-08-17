package me.redrixone.events;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BroadcastJoin implements Listener {

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("lc.broadcast")) {
            String playerName = player.getDisplayName();
            String replace = PlaceholderAPI.setPlaceholders(player, "%luckperms_prefix%");
            String welcome = ChatColor.translateAlternateColorCodes('&', replace + "" + playerName + " &bjoined the lobby!");
            Bukkit.broadcastMessage(welcome);
        }
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

}
