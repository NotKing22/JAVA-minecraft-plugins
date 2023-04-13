package me.zsnow.multieventos.API;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReflectionUtils {
  public static Class<?> getNMSClass(String name) {
    String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    try {
      return Class.forName("net.minecraft.server." + version + "." + name);
    } catch (ClassNotFoundException|ArrayIndexOutOfBoundsException e) {
      return null;
    } 
  }
  
  public static Class<?> getOBClass(String name) {
    String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    try {
      return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
    } catch (ClassNotFoundException|ArrayIndexOutOfBoundsException e) {
      return null;
    } 
  }
  
  public static void sendPacket(Player player, Object packet) {
    try {
      Object entityPlayer = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
      Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
      playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") }).invoke(playerConnection, new Object[] { packet });
    } catch (SecurityException|IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException|NoSuchMethodException|NoSuchFieldException e) {
      e.printStackTrace();
    } 
  }
}
