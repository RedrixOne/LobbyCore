package me.redrixone;

import me.redrixone.commands.FlyManager;
import me.redrixone.events.PlayerToggle;
import me.redrixone.events.*;
import me.redrixone.listeners.ServerSelectorListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyCore extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {

        Bukkit.getServer().getPluginManager().registerEvents(new BroadcastJoin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new TpJoin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new VoidTp(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BuildBlocker(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ServerSelector(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new FoodLevelCancel(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new FastConnect(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new FlyManager(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerToggle(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ProfileListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ServerSelectorListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.getCommand("fly").setExecutor(new FlyManager());


        System.out.println("LobbyCore | Enabled!");
    }

}
