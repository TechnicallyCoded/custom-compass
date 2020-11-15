package com.nuclyon.technicallycoded.customcompass;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class ShiftDisplay implements Listener {
    public Main pl;

    ArrayList<String> ar;

    public ShiftDisplay(Main pl) {
        this.ar = new ArrayList<>();
        this.pl = pl;
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player playerS = event.getPlayer();
        if (playerS.hasPermission("customcompass.event.toggleloc") &&
                playerS.getInventory().getItemInMainHand().getType() == Material.COMPASS)
            if (playerS.isSneaking()) {
                if (this.pl.getConfig().getString("players." + playerS.getUniqueId().toString() + "." + playerS.getWorld().getUID()) != null) {
                    String strl = "players." + playerS.getUniqueId().toString() + "." + playerS.getWorld().getUID();
                    int x = Integer.parseInt(this.pl.getConfig().getString(String.valueOf(strl) + ".X"));
                    int y = Integer.parseInt(this.pl.getConfig().getString(String.valueOf(strl) + ".Y"));
                    int z = Integer.parseInt(this.pl.getConfig().getString(String.valueOf(strl) + ".Z"));
                    Location custloc = new Location(playerS.getWorld(), x, y, z);
                    playerS.setCompassTarget(custloc);
                    ActionBar.sendAction(playerS, configM("track_custom"), this.pl);
                } else {
                    playerS.sendMessage(configM("no_custom_target_error"));
                }
            } else {
                ActionBar.sendAction(playerS, configM("trackspawn"), this.pl);
                Location bloc = playerS.getBedSpawnLocation();
                if (bloc == null) bloc = event.getPlayer().getWorld().getSpawnLocation();
                playerS.setCompassTarget(bloc);
            }
    }

    public String configM(String type) {
        return ChatColor.translateAlternateColorCodes('&',
                this.pl.getConfig().getString("messages." + type)
        );
    }

    @EventHandler
    public void onCompassClick(PlayerInteractEvent event) {
        Player playerC = event.getPlayer();
        if (!this.ar.contains(playerC.getName())) {
            this.ar.add(playerC.getName());
            if (playerC.hasPermission("customcompass.event.setloc")) {
                if (playerC.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK && playerC.isSneaking()) {
                        event.setCancelled(true);
                        int X = event.getClickedBlock().getLocation().getBlockX();
                        int Y = event.getClickedBlock().getLocation().getBlockY();
                        int Z = event.getClickedBlock().getLocation().getBlockZ();
                        String readLoc = String.valueOf(X) + ", " + Y + ", " + Z;
                        String strl = "players." + playerC.getUniqueId().toString() + "." + playerC.getWorld().getUID();
                        this.pl.getConfig().set(String.valueOf(strl) + ".X", Integer.valueOf(X));
                        this.pl.getConfig().set(String.valueOf(strl) + ".Y", Integer.valueOf(Y));
                        this.pl.getConfig().set(String.valueOf(strl) + ".Z", Integer.valueOf(Z));
                        this.pl.saveConfig();
                        playerC.sendMessage(String.valueOf(configM("change_custom_block")) + " (" + readLoc + ")");
                    }
                    if (event.getAction() == Action.RIGHT_CLICK_AIR && playerC.isSneaking()) {
                        event.setCancelled(true);
                        playerC.sendMessage(configM("click_on_block_error"));
                    }
                }
            } else {
                playerC.sendMessage(configM("no_perm"));
            }
        } else {
            this.ar.remove(playerC.getName());
        }
    }
}
