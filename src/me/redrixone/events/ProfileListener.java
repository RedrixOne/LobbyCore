package me.redrixone.events;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileListener implements Listener {

    private ItemMeta protectionDyeMeta;

    Connection connection;

    public ProfileListener(JavaPlugin plugin) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/lobby_secure";
            String username = "corpselog";
            String password = "1234";
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        createTable();
    }

    public void createTable() {
        try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS lobby_secure (uuid VARCHAR(36), lobbysecured BOOLEAN DEFAULT FALSE, PRIMARY KEY (uuid))")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setOwner(player.getName());
        headMeta.setDisplayName(ChatColor.AQUA + "My Profile " + ChatColor.GRAY + "(Right Click)");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "Right Click to browse your own profile!");
        headMeta.setLore(lore);
        head.setItemMeta(headMeta);
        addPlayerInTabase(player);
        player.getInventory().setItem(1, head);
        player.updateInventory();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemHead = event.getItem();

        if (itemHead != null && itemHead.getType() == Material.SKULL_ITEM) {
            mainMenu(player);
        }

    }

    public void mainMenu(Player player) {
        HeadDatabaseAPI api = new HeadDatabaseAPI();
        String replace = PlaceholderAPI.setPlaceholders(player, "%luckperms_prefix%");
        String suffix = PlaceholderAPI.setPlaceholders(player, "%luckperms_suffix%");
        String playerName = player.getDisplayName();
        Inventory inventory = Bukkit.createInventory(null, 54, "My Profile");

        //Testa giocatore
        ItemStack headPlayer = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta headMeta = (SkullMeta) headPlayer.getItemMeta();
        headMeta.setOwner(player.getName());
        headMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', replace + playerName));
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&bMoon&fPixel &7Level: &cSoon"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Guild: &cSoon"));
        headMeta.setLore(lore);
        headPlayer.setItemMeta(headMeta);

        //Testa amici
        ItemStack friendsHead = api.getItemHead("9508");
        SkullMeta friendsMeta = (SkullMeta) friendsHead.getItemMeta();
        friendsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bFriends"));
        List<String> loreFriends = new ArrayList<String>();
        loreFriends.add(ChatColor.translateAlternateColorCodes('&', "&7View your &bMoon&fPixel &7friends"));
        loreFriends.add(ChatColor.translateAlternateColorCodes('&', "&7profiles and interact with your"));
        loreFriends.add(ChatColor.translateAlternateColorCodes('&', "&7online friends!"));
        friendsMeta.setLore(loreFriends);
        friendsHead.setItemMeta(friendsMeta);

        //Party
        ItemStack partyHead = api.getItemHead("10146");
        SkullMeta partyMeta = (SkullMeta) partyHead.getItemMeta();
        partyMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bParty"));
        List<String> loreParty = new ArrayList<String>();
        loreParty.add(ChatColor.translateAlternateColorCodes('&', "&7Create a party and join up with"));
        loreParty.add(ChatColor.translateAlternateColorCodes('&', "&7other players to play games"));
        loreParty.add(ChatColor.translateAlternateColorCodes('&', "&7together!"));
        partyMeta.setLore(loreParty);
        partyHead.setItemMeta(partyMeta);

        //Separatore
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b"));
        glass.setItemMeta(glassMeta);

        //Settings
        ItemStack settings = new ItemStack(Material.REDSTONE_COMPARATOR, 1);
        ItemMeta settingsMeta = settings.getItemMeta();
        settingsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bSettings"));
        List<String> settingsLore = new ArrayList<String>();
        settingsLore.add(ChatColor.translateAlternateColorCodes('&', "&7Allows you to edit and control"));
        settingsLore.add(ChatColor.translateAlternateColorCodes('&', "&7various personal settings."));
        settingsLore.add(ChatColor.translateAlternateColorCodes('&', "&7"));
        settingsLore.add(ChatColor.translateAlternateColorCodes('&', "&bClick to edit your settings!"));
        settingsMeta.setLore(settingsLore);
        settings.setItemMeta(settingsMeta);

        //Info giocatore
        ItemStack infoHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta infoMeta = (SkullMeta) infoHead.getItemMeta();
        infoMeta.setOwner(player.getName());
        infoMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bCharacter Information"));
        List<String> infoLore = new ArrayList<String>();
        infoLore.add(ChatColor.translateAlternateColorCodes('&', "&7Rank: " + suffix));
        infoLore.add(ChatColor.translateAlternateColorCodes('&', "&7Level: &cSoon"));
        infoLore.add(ChatColor.translateAlternateColorCodes('&', "&bMoon&fPixel &7Diamonds: &cSoon"));
        infoLore.add(ChatColor.translateAlternateColorCodes('&', "&7"));
        infoLore.add(ChatColor.translateAlternateColorCodes('&', "&bClick to see the &bMoon&fPixel &bStore link."));
        infoMeta.setLore(infoLore);
        infoHead.setItemMeta(infoMeta);

        //Stats
        ItemStack stats = new ItemStack(Material.PAPER, 1);
        ItemMeta statsMeta = stats.getItemMeta();
        statsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bStats Viewer"));
        List<String> statsLore = new ArrayList<String>();
        statsLore.add(ChatColor.translateAlternateColorCodes('&', "&7Showcases your stats for each"));
        statsLore.add(ChatColor.translateAlternateColorCodes('&', "&7game and overview of all."));
        statsLore.add(ChatColor.translateAlternateColorCodes('&', "&7"));
        statsLore.add(ChatColor.translateAlternateColorCodes('&', "&7Players ranked &bMVP &7or higher"));
        statsLore.add(ChatColor.translateAlternateColorCodes('&', "&7can use &b/stats <player> &7to view"));
        statsLore.add(ChatColor.translateAlternateColorCodes('&', "&7other players stats."));
        statsLore.add(ChatColor.translateAlternateColorCodes('&', "&7"));
        statsLore.add(ChatColor.translateAlternateColorCodes('&', "&bClick to view your stats!"));
        statsMeta.setLore(statsLore);
        stats.setItemMeta(statsMeta);

        inventory.setItem(3, headPlayer);
        inventory.setItem(4, friendsHead);
        inventory.setItem(5, partyHead);
        inventory.setItem(9, glass);
        inventory.setItem(10, glass);
        inventory.setItem(11, glass);
        inventory.setItem(12, glass);
        inventory.setItem(13, glass);
        inventory.setItem(14, glass);
        inventory.setItem(15, glass);
        inventory.setItem(16, glass);
        inventory.setItem(17, glass);
        inventory.setItem(21, settings);
        inventory.setItem(22, infoHead);
        inventory.setItem(23, stats);
        player.openInventory(inventory);
    }

    public void settingsMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Game Settings");

        //Game settings
        ItemStack settings = new ItemStack(Material.REDSTONE_COMPARATOR, 1);
        ItemMeta settingsMeta = settings.getItemMeta();
        settingsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bGame Settings"));
        settings.setItemMeta(settingsMeta);

        //Separatore
        ItemStack glassCyan = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);
        ItemMeta glassCyanMeta = glassCyan.getItemMeta();
        glassCyanMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8▲ &7Categories"));
        List<String> cyanLore = new ArrayList<String>();
        cyanLore.add(ChatColor.translateAlternateColorCodes('&', "&8▼ &7Settings"));
        glassCyanMeta.setLore(cyanLore);
        glassCyan.setItemMeta(glassCyanMeta);

        ItemStack glassGray = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta glassGrayMeta = glassGray.getItemMeta();
        glassGrayMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8▲ &7Categories"));
        List<String> grayLore = new ArrayList<String>();
        grayLore.add(ChatColor.translateAlternateColorCodes('&', "&8▼ &7Settings"));
        glassGrayMeta.setLore(grayLore);
        glassGray.setItemMeta(glassGrayMeta);

        //Lobby protection (CommandBlock)
        ItemStack lobbyProtection = new ItemStack(Material.COMMAND, 1);
        ItemMeta lobbyProtectionMeta = lobbyProtection.getItemMeta();

        try (PreparedStatement statement = connection.prepareStatement("SELECT lobbysecured FROM lobby_secure WHERE uuid = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                boolean lobbysecured = result.getBoolean("lobbysecured");
                if (lobbysecured) {
                    lobbyProtectionMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a/lobby Protection"));
                } else {
                    lobbyProtectionMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c/lobby Protection"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<String> lobbyProtLore = new ArrayList<String>();
        lobbyProtLore.add(ChatColor.translateAlternateColorCodes('&', "&7Requires you to type the /lobby"));
        lobbyProtLore.add(ChatColor.translateAlternateColorCodes('&', "&7command twice to avoid"));
        lobbyProtLore.add(ChatColor.translateAlternateColorCodes('&', "&7accidents."));
        lobbyProtectionMeta.setLore(lobbyProtLore);
        lobbyProtection.setItemMeta(lobbyProtectionMeta);

        //Lobby protection (Dye)
        short durability = 0;
        try (PreparedStatement statement = connection.prepareStatement("SELECT lobbysecured FROM lobby_secure WHERE uuid = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                boolean lobbysecured = result.getBoolean("lobbysecured");
                if (lobbysecured) {
                    durability = 12;
                } else {
                    durability = 8;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ItemStack protectionDye = new ItemStack(Material.INK_SACK, 1, durability);
        ItemMeta protectionDyeMeta = protectionDye.getItemMeta();
        List<String> protectionDyeLore = new ArrayList<String>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT lobbysecured FROM lobby_secure WHERE uuid = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                boolean lobbysecured = result.getBoolean("lobbysecured");
                if (lobbysecured) {
                    protectionDyeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a/lobby Protection"));
                    protectionDyeLore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to disable!"));
                } else {
                    protectionDyeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c/lobby Protection"));
                    protectionDyeLore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to enable!"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        protectionDyeMeta.setLore(protectionDyeLore);
        protectionDye.setItemMeta(protectionDyeMeta);


        inventory.setItem(0, settings);
        inventory.setItem(9, glassCyan);
        inventory.setItem(10, glassGray);
        inventory.setItem(11, glassGray);
        inventory.setItem(12, glassGray);
        inventory.setItem(13, glassGray);
        inventory.setItem(14, glassGray);
        inventory.setItem(15, glassGray);
        inventory.setItem(16, glassGray);
        inventory.setItem(17, glassGray);
        inventory.setItem(31, lobbyProtection);
        inventory.setItem(40, protectionDye);
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem != null && currentItem.getType() == Material.SKULL_ITEM || currentItem.getType() == Material.PAPER) {
            event.setCancelled(true);
        }

        if (currentItem != null && currentItem.getType() == Material.REDSTONE_COMPARATOR) {
            settingsMenu(player);
            event.setCancelled(true);
        }

        if (currentItem != null && currentItem.getType() == Material.COMMAND) {
            ItemMeta currentItemMeta = currentItem.getItemMeta();
            InventoryView view = player.getOpenInventory();
            ItemStack dye = view.getItem(40);
            ItemMeta dyeMeta = dye.getItemMeta();
            List<String> dyeLore = new ArrayList<String>();
            if (currentItemMeta.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&c/lobby Protection"))) {
                currentItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a/lobby Protection"));
                dyeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a/lobby Protection"));
                dyeLore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to disable!"));
                short durability = 12;
                dye.setDurability(durability);
                updatePlayerInDatabase(player, true); // aggiorna il valore nel database a 0 (false)
            } else {
                currentItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c/lobby Protection"));
                dyeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c/lobby Protection"));
                dyeLore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to enable!"));
                short durability = 8;
                dye.setDurability(durability);
                updatePlayerInDatabase(player, false); // aggiorna il valore nel database a 1 (true)
            }
            currentItem.setItemMeta(currentItemMeta);
            dyeMeta.setLore(dyeLore);
            dye.setItemMeta(dyeMeta);
            player.updateInventory();
            event.setCancelled(true);
        }

        if (currentItem != null && currentItem.getDurability() == 8 || currentItem.getDurability() == 12) {
            ItemMeta currentItemMeta = currentItem.getItemMeta();
            InventoryView view = player.getOpenInventory();
            ItemStack command = view.getItem(31);
            ItemMeta commandMeta = command.getItemMeta();
            List<String> dyeLore = new ArrayList<String>();
            if (currentItem.getDurability() == 8){
                currentItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a/lobby Protection"));
                dyeLore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to disable!"));
                short d = 12;
                currentItem.setDurability(d);
                commandMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a/lobby Protection"));
                updatePlayerInDatabase(player, true); // aggiorna il valore nel database a 0 (false)
            } else {
                currentItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c/lobby Protection"));
                dyeLore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to enable!"));
                short d = 8;
                currentItem.setDurability(d);
                commandMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c/lobby Protection"));
                updatePlayerInDatabase(player, false); // aggiorna il valore nel database a 0 (false)
            }
            currentItemMeta.setLore(dyeLore);
            currentItem.setItemMeta(currentItemMeta);
            command.setItemMeta(commandMeta);
            player.updateInventory();
            event.setCancelled(true);
        }



    }

    private void updatePlayerInDatabase(Player player, boolean lobbysecured) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE lobby_secure SET lobbysecured = ? WHERE uuid = ?")) {
            statement.setBoolean(1, lobbysecured);
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPlayerInTabase(Player player) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT IGNORE INTO lobby_secure (uuid) VALUES (?)")) {
            statement.setString(1, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
