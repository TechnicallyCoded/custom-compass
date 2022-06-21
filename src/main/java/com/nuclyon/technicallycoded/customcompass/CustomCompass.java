package com.nuclyon.technicallycoded.customcompass;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomCompass extends JavaPlugin implements Listener {

    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(new BukkitListeners(this), this);
        PluginCommand cmd = getCommand("customcompass");
        assert cmd != null;
        cmd.setExecutor(new CompassCmd(this));

        getConfig().options().copyDefaults(true);

        if (getConfig().getString("messages.track_custom") == null)
            getConfig().addDefault("messages.track_custom", "&eTracking your custom location...");
        if (getConfig().getString("messages.trackspawn") == null)
            getConfig().addDefault("messages.trackspawn", "&6Tracking your spawnpoint...");
        if (getConfig().getString("messages.change_custom_block") == null)
            getConfig().addDefault("messages.change_custom_block", "&aYour custom location has been set!");
        if (getConfig().getString("messages.no_custom_target_error") == null)
            getConfig().addDefault("messages.no_custom_target_error", "&4No custom target has been set!");
        if (getConfig().getString("messages.click_on_block_error") == null)
            getConfig().addDefault("messages.click_on_block_error", "&eRight click on a &6block &eto set the target");
        if (getConfig().getString("messages.custom_reset") == null)
            getConfig().addDefault("messages.custom_reset", "&cYour custom location has been reset!");
        if (getConfig().getString("messages.no_perm") == null)
            getConfig().addDefault("messages.no_perm", "&4You do not have permission to do this!");
        if (getConfig().getString("messages.set_loc_help") == null)
            getConfig().addDefault("messages.set_loc_help", "&cTo set a location /cc set <x> <y> <z>");
        saveConfig();

        int pluginId = 11657;
        Metrics metrics = new Metrics(this, pluginId);

        ConfigurationSection sect = getConfig().getConfigurationSection("players");
        metrics.addCustomChart(new SimplePie("player_coords_count", () -> {
            if (sect == null) return "0";
            else return sect.getKeys(false).size() + "";
        }));

        System.out.println("\033[33m[CC]\033[31m >\033[0m Custom Compass is now\033[32m ENABLED\033[0m");
    }

    public void onDisable() {
        System.out.println("\033[33m[CC]\033[31m >\033[0m Custom Compass is now\033[31m DISABLED\033[0m");
    }
}
