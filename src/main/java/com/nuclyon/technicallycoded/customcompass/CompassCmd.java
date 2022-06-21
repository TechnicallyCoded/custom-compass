package com.nuclyon.technicallycoded.customcompass;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CompassCmd implements CommandExecutor {
    public CustomCompass plug;

    public CompassCmd(CustomCompass plug) {
        this.plug = plug;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String msg, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (cmd.getName().equalsIgnoreCase("customcompass") || cmd.getName().equalsIgnoreCase("cc")) {
                if (args.length == 0)
                    player.sendMessage(ChatColor.YELLOW + "To use the the Custom Compass plugin " + ChatColor.GOLD + "shift-click" + ChatColor.YELLOW + " a block to set as your target.");
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("reset"))
                        if (player.hasPermission("customcompass.cmd.reset")) {
                            this.plug.getConfig().set("players." + player.getUniqueId() + "." + player.getWorld().getUID(), null);

                            String actionBarMsg = BukkitListeners.configMessage("trackspawn");
                            ActionBarUtil.sendAction(player, actionBarMsg == null ? "" : actionBarMsg);

                            Location bedLoc = player.getBedSpawnLocation();
                            player.setCompassTarget(bedLoc != null ? bedLoc : player.getWorld().getSpawnLocation());
                            this.plug.saveConfig();

                            String reply = BukkitListeners.configMessage("custom_reset");
                            player.sendMessage(reply == null ? "" : reply);
                        } else {
                            String reply = BukkitListeners.configMessage("no_perm");
                            player.sendMessage(reply == null ? "" : reply);
                        }
                    if (args[0].equalsIgnoreCase("set"))
                        if (player.hasPermission("customcompass.cmd.setloc")) {
                            if (args.length == 4) {
                                int x, y, z;
                                try {
                                    x = Integer.parseInt(args[1]);
                                    y = Integer.parseInt(args[2]);
                                    z = Integer.parseInt(args[3]);
                                } catch (NumberFormatException e) {
                                    String reply = BukkitListeners.configMessage("invalid_coords");
                                    player.sendMessage(reply == null ? "" : reply);
                                    return true;
                                }
                                Location loc = new Location(player.getWorld(), x, y, z);
                                String strl = "players." + player.getUniqueId() + "." + player.getWorld().getUID();
                                this.plug.getConfig().set(strl + ".X", x);
                                this.plug.getConfig().set(strl + ".Y", y);
                                this.plug.getConfig().set(strl + ".Z", z);
                                this.plug.saveConfig();
                                player.setCompassTarget(loc);
                            } else {
                                String reply = BukkitListeners.configMessage("set_loc_help");
                                player.sendMessage(reply == null ? "" : reply);
                            }
                        } else {
                            String reply = BukkitListeners.configMessage("no_perm");
                            player.sendMessage(reply == null ? "" : reply);
                        }
                }
            }
        }
        return false;
    }
}
