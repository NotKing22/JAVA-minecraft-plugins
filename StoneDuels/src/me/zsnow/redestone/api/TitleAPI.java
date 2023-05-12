package me.zsnow.redestone.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.bukkit.entity.Player;

public class TitleAPI {
  private static Method a;
  
  private static Object enumTIMES;
  
  private static Object enumTITLE;
  
  private static Object enumSUBTITLE;
  
  private static Constructor<?> timeTitleConstructor;
  
  private static Constructor<?> textTitleConstructor;
  
  public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
    try {
      Object chatTitle = a.invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
      Object chatSubtitle = a.invoke(null, new Object[] { "{\"text\":\"" + subtitle + "\"}" });
      Object timeTitlePacket = timeTitleConstructor.newInstance(new Object[] { enumTIMES, null, fadeIn, stay, fadeOut });
      ReflectionUtils.sendPacket(player, timeTitlePacket);
      Object titlePacket = textTitleConstructor.newInstance(new Object[] { enumTITLE, chatTitle });
      ReflectionUtils.sendPacket(player, titlePacket);
      Object subtitlePacket = textTitleConstructor.newInstance(new Object[] { enumSUBTITLE, chatSubtitle });
      ReflectionUtils.sendPacket(player, subtitlePacket);
    } catch (IllegalArgumentException|IllegalAccessException|SecurityException|java.lang.reflect.InvocationTargetException|InstantiationException|NullPointerException e) {
      e.printStackTrace();
    } 
  }
  
  public static void load() {
    try {
      Class<?> enumClass, icbc = ReflectionUtils.getNMSClass("IChatBaseComponent");
      Class<?> ppot = ReflectionUtils.getNMSClass("PacketPlayOutTitle");
      if ((ppot.getDeclaredClasses()).length > 0) {
        enumClass = ppot.getDeclaredClasses()[0];
      } else {
        enumClass = ReflectionUtils.getNMSClass("EnumTitleAction");
      } 
      if ((icbc.getDeclaredClasses()).length > 0) {
        a = icbc.getDeclaredClasses()[0].getMethod("a", new Class[] { String.class });
      } else {
        a = ReflectionUtils.getNMSClass("ChatSerializer").getMethod("a", new Class[] { String.class });
      } 
      enumTIMES = enumClass.getField("TIMES").get(null);
      enumTITLE = enumClass.getField("TITLE").get(null);
      enumSUBTITLE = enumClass.getField("SUBTITLE").get(null);
      timeTitleConstructor = ppot.getConstructor(new Class[] { enumClass, icbc, int.class, int.class, int.class });
      textTitleConstructor = ppot.getConstructor(new Class[] { enumClass, icbc });
    } catch (SecurityException|IllegalArgumentException|NoSuchMethodException|IllegalAccessException|NoSuchFieldException|NullPointerException securityException) {}
  }
}

