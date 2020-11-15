package com.nuclyon.technicallycoded.customcompass;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CCcmd implements CommandExecutor {
    public Main plug;

    public CCcmd(Main plug) {
        this.plug = plug;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (cmd.getName().equalsIgnoreCase("customcompass") || cmd.getName().equalsIgnoreCase("cc")) {
                if (args.length == 0)
                    player.sendMessage(ChatColor.YELLOW + "To use the the Custom Compass plugin " + ChatColor.GOLD + "shift-click" + ChatColor.YELLOW + " a block to set as your target.");
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("reset"))
                        if (player.hasPermission("customcompass.cmd.reset")) {
                            this.plug.getConfig().set("players." + player.getUniqueId().toString() + "." + player.getWorld().getUID(), null);
                            ActionBar.sendAction(player, (new ShiftDisplay(this.plug)).configM("trackspawn"), this.plug);
                            player.setCompassTarget(player.getBedSpawnLocation());
                            this.plug.saveConfig();
                            player.sendMessage((new ShiftDisplay(this.plug)).configM("custom_reset"));
                        } else {
                            player.sendMessage((new ShiftDisplay(this.plug)).configM("no_perm"));
                        }
                    if (args[0].equalsIgnoreCase("set"))
                        if (player.hasPermission("customcompass.cmd.setloc")) {
                            if (args.length == 4) {
                                int x = Integer.parseInt(args[1]);
                                int y = Integer.parseInt(args[2]);
                                int z = Integer.parseInt(args[3]);
                                Location loc = new Location(player.getWorld(), x, y, z);
                                String strl = "players." + player.getUniqueId().toString() + "." + player.getWorld().getUID();
                                this.plug.getConfig().set(String.valueOf(strl) + ".X", Integer.valueOf(x));
                                this.plug.getConfig().set(String.valueOf(strl) + ".Y", Integer.valueOf(y));
                                this.plug.getConfig().set(String.valueOf(strl) + ".Z", Integer.valueOf(z));
                                this.plug.saveConfig();
                                player.setCompassTarget(loc);
                            } else {
                                player.sendMessage((new ShiftDisplay(this.plug)).configM("set_loc_help"));
                            }
                        } else {
                            player.sendMessage((new ShiftDisplay(this.plug)).configM("no_perm"));
                        }
                }
            }
        }
        return false;
    }
}
