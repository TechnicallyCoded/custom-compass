package com.nuclyon.technicallycoded.customcompass;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class ActionBarUtil {

    public CustomCompass plugin;

    private static final String ver = getServerVersion();

    public ActionBarUtil(CustomCompass plugin) {
        this.plugin = plugin;
    }

    public static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName()
                .replace(".", ",").split(",")[3] + "." + nmsClassName);
    }

    public static Class<?> getMinecraftClass(String nmsClassPath) throws ClassNotFoundException {
        return Class.forName("net.minecraft." + nmsClassPath);
    }

    public static String getServerVersion() {
        String[] parts = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        return parts[parts.length - 1];
    }

    public static void sendAction(Player p, String msg) {
        try {
            String coloredMsg = ChatColor.translateAlternateColorCodes('&', msg);

            if (ver.startsWith("v1_8_")) {
                Object icbc = getNmsClass("IChatBaseComponent$ChatSerializer")
                        .getMethod("a", String.class)
                        .invoke(null, "{'text': '" + coloredMsg + "'}");
                Object ppoc = getNmsClass("PacketPlayOutChat")
                        .getConstructor(new Class[] { getNmsClass("IChatBaseComponent"), byte.class })
                        .newInstance(icbc, (byte) 2);
                Object nmsp = p.getClass().getMethod("getHandle").invoke(p);
                Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
                pcon.getClass().getMethod("sendPacket", getNmsClass("Packet")).invoke(pcon, ppoc);
            }

            else if (ver.startsWith("v1_7_")) {
                Object icbc = getNmsClass("ChatSerializer")
                        .getMethod("a", String.class)
                        .invoke(null, "{'text': '" + coloredMsg + "'}");
                Object ppoc = getNmsClass("PacketPlayOutChat")
                        .getConstructor(new Class[] { getNmsClass("IChatBaseComponent"), byte.class })
                        .newInstance(icbc, (byte) 2);
                Object nmsp = p.getClass().getMethod("getHandle").invoke(p);
                Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
                pcon.getClass().getMethod("sendPacket", getNmsClass("Packet")).invoke(pcon, ppoc);
            }

            else {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(coloredMsg));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException |
                 SecurityException | ClassNotFoundException | InstantiationException | NoSuchFieldException |
                 NullPointerException e) {
            e.printStackTrace();
        }
    }
}