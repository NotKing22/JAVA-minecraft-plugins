package me.zsnow.stone.endergun.api;

import org.bukkit.*;
import java.lang.reflect.*;
import org.bukkit.inventory.*;

public class NBTTag
{
    private static Class<?> getCraftItemstack() {
        final String Version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        try {
            final Class<?> c = Class.forName("org.bukkit.craftbukkit." + Version + ".inventory.CraftItemStack");
            return c;
        }
        catch (Exception ex) {
            System.out.println("Error on create Packet of ItemStack");
            ex.printStackTrace();
            return null;
        }
    }
    
	private static Object getnewNBTTag() {
        final String Version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        try {
            final Class<?> c = Class.forName("net.minecraft.server." + Version + ".NBTTagCompound");
            return c.newInstance();
        }
        catch (Exception ex) {
            System.out.println("Error on create new NBTTag");
            ex.printStackTrace();
            return null;
        }
    }
    
    private static Object setNBTTag(final Object NBTTag, final Object NMSItem) {
        try {
            final Method method = NMSItem.getClass().getMethod("setTag", NBTTag.getClass());
            method.invoke(NMSItem, NBTTag);
            return NMSItem;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private static Object getNMSItemStack(final ItemStack item) {
        final Class<?> cis = getCraftItemstack();
        try {
            final Method method = cis.getMethod("asNMSCopy", ItemStack.class);
            final Object answer = method.invoke(cis, item);
            return answer;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static ItemStack getBukkitItemStack(final Object item) {
        final Class<?> cis = getCraftItemstack();
        try {
            final Method method = cis.getMethod("asBukkitCopy", item.getClass());
            final Object answer = method.invoke(cis, item);
            return (ItemStack)answer;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static Object getNBTTagCompound(final Object nmsitem) {
        final Class<?> c = nmsitem.getClass();
        try {
            final Method method = c.getMethod("getTag", (Class<?>[])new Class[0]);
            final Object answer = method.invoke(nmsitem, new Object[0]);
            return answer;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ItemStack setString(final ItemStack item, final String key, final String Text) {
        Object nmsitem = getNMSItemStack(item);
        if (nmsitem == null) {
            System.out.println("NullValue");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
        if (nbttag == null) {
            nbttag = getnewNBTTag();
        }
        try {
            final Method method = nbttag.getClass().getMethod("setString", String.class, String.class);
            method.invoke(nbttag, key, Text);
            nmsitem = setNBTTag(nbttag, nmsitem);
            return getBukkitItemStack(nmsitem);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return item;
        }
    }
    
    public static String getString(final ItemStack item, final String key) {
        final Object nmsitem = getNMSItemStack(item);
        if (nmsitem == null) {
            System.out.println("NullValue");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
        if (nbttag == null) {
            nbttag = getnewNBTTag();
        }
        try {
            final Method method = nbttag.getClass().getMethod("getString", String.class);
            return (String)method.invoke(nbttag, key);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static ItemStack setInt(final ItemStack item, final String key, final Integer i) {
        Object nmsitem = getNMSItemStack(item);
        if (nmsitem == null) {
            System.out.println("NullValue");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
        if (nbttag == null) {
            nbttag = getnewNBTTag();
        }
        try {
            final Method method = nbttag.getClass().getMethod("setInt", String.class, Integer.TYPE);
            method.invoke(nbttag, key, i);
            nmsitem = setNBTTag(nbttag, nmsitem);
            return getBukkitItemStack(nmsitem);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return item;
        }
    }
    
    public static Integer getInt(final ItemStack item, final String key) {
        final Object nmsitem = getNMSItemStack(item);
        if (nmsitem == null) {
            System.out.println("NullValue");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
        if (nbttag == null) {
            nbttag = getnewNBTTag();
        }
        try {
            final Method method = nbttag.getClass().getMethod("getInt", String.class);
            return (Integer)method.invoke(nbttag, key);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static ItemStack setDouble(final ItemStack item, final String key, final Double d) {
        Object nmsitem = getNMSItemStack(item);
        if (nmsitem == null) {
            System.out.println("NullValue");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
        if (nbttag == null) {
            nbttag = getnewNBTTag();
        }
        try {
            final Method method = nbttag.getClass().getMethod("setDouble", String.class, Double.TYPE);
            method.invoke(nbttag, key, d);
            nmsitem = setNBTTag(nbttag, nmsitem);
            return getBukkitItemStack(nmsitem);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return item;
        }
    }
    
    public static Double getDouble(final ItemStack item, final String key) {
        final Object nmsitem = getNMSItemStack(item);
        if (nmsitem == null) {
            System.out.println("NullValue");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
        if (nbttag == null) {
            nbttag = getnewNBTTag();
        }
        try {
            final Method method = nbttag.getClass().getMethod("getDouble", String.class);
            return (Double)method.invoke(nbttag, key);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static ItemStack setBoolean(final ItemStack item, final String key, final Boolean d) {
        Object nmsitem = getNMSItemStack(item);
        if (nmsitem == null) {
            System.out.println("NullValue");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
        if (nbttag == null) {
            nbttag = getnewNBTTag();
        }
        try {
            final Method method = nbttag.getClass().getMethod("setBoolean", String.class, Boolean.TYPE);
            method.invoke(nbttag, key, d);
            nmsitem = setNBTTag(nbttag, nmsitem);
            return getBukkitItemStack(nmsitem);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return item;
        }
    }
    
    public static Boolean getBoolean(final ItemStack item, final String key) {
        final Object nmsitem = getNMSItemStack(item);
        if (nmsitem == null) {
            System.out.println("NullValue");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
        if (nbttag == null) {
            nbttag = getnewNBTTag();
        }
        try {
            final Method method = nbttag.getClass().getMethod("getBoolean", String.class);
            return (Boolean)method.invoke(nbttag, key);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static Boolean hasKey(final ItemStack item, final String key) {
        final Object nmsitem = getNMSItemStack(item);
        if (nmsitem == null) {
            System.out.println("NullValue");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
        if (nbttag == null) {
            nbttag = getnewNBTTag();
        }
        try {
            final Method method = nbttag.getClass().getMethod("hasKey", String.class);
            return (Boolean)method.invoke(nbttag, key);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
