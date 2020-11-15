package com.nuclyon.technicallycoded.customcompass;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class ActionBar {
    public Main plugin;

    public ActionBar(Main plugin) {
        this.plugin = plugin;
    }

    public static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
    }

    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    public static void sendAction(Player p, String msg, Main plugin) {
        String ver = getServerVersion();
        try {
            if (ver.startsWith("v1_13_") || ver.startsWith("v1_14_") || ver.startsWith("v1_15_") || ver.startsWith("v1_16_")) {
                Object icbc = getNmsClass("ChatComponentText")
                        .getConstructor(new Class[] { String.class })
                        .newInstance(msg.replace("&", ""));
                Object ppoc = getNmsClass("PacketPlayOutChat")
                        .getConstructor(new Class[] { getNmsClass("IChatBaseComponent"), getNmsClass("ChatMessageType"), UUID.class })
                        .newInstance(icbc, getNmsClass("ChatMessageType").getEnumConstants()[2], p.getUniqueId());
                Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p);
                Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
                pcon.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(pcon, ppoc);
            } else if (ver.startsWith("v1_9_") || ver.startsWith("v1_10_") || ver.startsWith("v1_11_") || ver.startsWith("v1_12_")) {
                Object icbc = getNmsClass("ChatComponentText").getConstructor(new Class[] { String.class }).newInstance(msg.replace("&", ""));
                Object ppoc = getNmsClass("PacketPlayOutChat").getConstructor(new Class[] { getNmsClass("IChatBaseComponent"), byte.class }).newInstance(icbc, (byte) 2);
                Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p);
                Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
                pcon.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(pcon, ppoc);
            } else if (ver.startsWith("v1_8_")) {
                Object icbc = getNmsClass("IChatBaseComponent$ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, "{'text': '" + msg + "'}");
                Object ppoc = getNmsClass("PacketPlayOutChat").getConstructor(new Class[] { getNmsClass("IChatBaseComponent"), byte.class }).newInstance(icbc, (byte) 2);
                Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p);
                Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
                pcon.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(pcon, ppoc);
            } else if (ver.startsWith("v1_7_")) {
                Object icbc = getNmsClass("ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, "{'text': '" + msg + "'}");
                Object ppoc = getNmsClass("PacketPlayOutChat").getConstructor(new Class[] { getNmsClass("IChatBaseComponent"), byte.class }).newInstance(icbc, (byte) 2);
                Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p);
                Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
                pcon.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(pcon, ppoc);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | NoSuchFieldException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}