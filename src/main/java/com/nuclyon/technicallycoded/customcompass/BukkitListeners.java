package com.nuclyon.technicallycoded.customcompass;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.jetbrains.annotations.Nullable;

public class BukkitListeners implements Listener {
    private static BukkitListeners instance;
    public CustomCompass pl;

    ArrayList<String> ar;

    public BukkitListeners(CustomCompass pl) {
        this.ar = new ArrayList<>();
        this.pl = pl;
        instance = this;
    }

    public static BukkitListeners getInstance() {
        return instance;
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player playerS = event.getPlayer();
        if (playerS.hasPermission("customcompass.event.toggleloc") &&
                playerS.getInventory().getItemInMainHand().getType() == Material.COMPASS)

            if (playerS.isSneaking()) {
                if (this.pl.getConfig().getString("players." + playerS.getUniqueId() + "." + playerS.getWorld().getUID()) != null) {
                    String strl = "players." + playerS.getUniqueId() + "." + playerS.getWorld().getUID();
                    FileConfiguration config = this.pl.getConfig();

                    String strX = config.getString(strl + ".X");
                    String strY = config.getString(strl + ".Y");
                    String strZ = config.getString(strl + ".Z");

                    if (strX == null || strY == null || strZ == null) return;

                    int x = Integer.parseInt(strX);
                    int y = Integer.parseInt(strY);
                    int z = Integer.parseInt(strZ);

                    Location custloc = new Location(playerS.getWorld(), x, y, z);
                    playerS.setCompassTarget(custloc);

                    ActionBarUtil.sendAction(playerS, configMessage("track_custom"));
                } else {
                    String reply = BukkitListeners.configMessage("no_custom_target_error");
                    playerS.sendMessage(reply == null ? "" : reply);
                }
            } else {
                ActionBarUtil.sendAction(playerS, configMessage("trackspawn"));
                Location bloc = playerS.getBedSpawnLocation();

                if (bloc == null) bloc = event.getPlayer().getWorld().getSpawnLocation();
                playerS.setCompassTarget(bloc);
            }
    }

    @Nullable
    public static String configMessage(String type) {
        String msg = instance.pl.getConfig().getString("messages." + type);
        if (msg == null) return null;
        return ChatColor.translateAlternateColorCodes('&', msg);
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

                        if (event.getClickedBlock() == null) return;

                        int X = event.getClickedBlock().getLocation().getBlockX();
                        int Y = event.getClickedBlock().getLocation().getBlockY();
                        int Z = event.getClickedBlock().getLocation().getBlockZ();

                        String readLoc = X + ", " + Y + ", " + Z;
                        String strl = "players." + playerC.getUniqueId() + "." + playerC.getWorld().getUID();

                        this.pl.getConfig().set(strl + ".X", X);
                        this.pl.getConfig().set(strl + ".Y", Y);
                        this.pl.getConfig().set(strl + ".Z", Z);
                        this.pl.saveConfig();

                        playerC.sendMessage(configMessage("change_custom_block") + " (" + readLoc + ")");
                    }
                    if (event.getAction() == Action.RIGHT_CLICK_AIR && playerC.isSneaking()) {
                        event.setCancelled(true);
                        String reply = BukkitListeners.configMessage("click_on_block_error");
                        playerC.sendMessage(reply == null ? "" : reply);
                    }
                }
            } else {
                String reply = BukkitListeners.configMessage("no_perm");
                playerC.sendMessage(reply == null ? "" : reply);
            }
        } else {
            this.ar.remove(playerC.getName());
        }
    }
}
