package me.redrixone.api;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Stack {

    //ServerSelector Item
    public static ItemStack compass() {

        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();
        compassMeta.setDisplayName(ChatColor.AQUA + "Game Menu " + ChatColor.GRAY + "(Right Click)");
        compassMeta.setLore(Arrays.asList(ChatColor.GRAY + "Right Click to visit the Game Menu!"));
        compass.setItemMeta(compassMeta);

        return compass;
    }


}
