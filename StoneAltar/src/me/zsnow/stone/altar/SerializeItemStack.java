package me.zsnow.stone.altar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SerializeItemStack {

    public static ItemStack deserializeItemStack(String data){
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(data, 32).toByteArray());
        DataInputStream dataInputStream = new DataInputStream(inputStream);
       
        ItemStack itemStack = null;
        try {
            Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
            Class<?> nmsItemStackClass = getNMSClass("ItemStack");
            Object nbtTagCompound = getNMSClass("NBTCompressedStreamTools").getMethod("a", DataInputStream.class).invoke(null, dataInputStream);
            //Object nbtTagCompound = getNMSClass("NBTCompressedStreamTools").getMethod("a", DataInputStream.class).invoke(null, inputStream);
            Object craftItemStack = nmsItemStackClass.getMethod("createStack", nbtTagCompoundClass).invoke(null, nbtTagCompound);
            itemStack = (ItemStack) getOBClass("inventory.CraftItemStack").getMethod("asBukkitCopy", nmsItemStackClass).invoke(null, craftItemStack);
        } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
     
        return itemStack;
    }
   
    public static String serializeItemStack(ItemStack item) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);
       
        try {
            Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
            Constructor<?> nbtTagCompoundConstructor = nbtTagCompoundClass.getConstructor();
            Object nbtTagCompound = nbtTagCompoundConstructor.newInstance();
            Object nmsItemStack = getOBClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
            getNMSClass("ItemStack").getMethod("save", nbtTagCompoundClass).invoke(nmsItemStack, nbtTagCompound);
            getNMSClass("NBTCompressedStreamTools").getMethod("a", nbtTagCompoundClass, DataOutput.class).invoke(null, nbtTagCompound, (DataOutput)dataOutput);

        } catch (SecurityException | NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
     
        //return BaseEncoding.base64().encode(outputStream.toByteArray());
        return new BigInteger(1, outputStream.toByteArray()).toString(32);
    }
   
       private static Class<?> getNMSClass(String name) {
           String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
           try {
               return Class.forName("net.minecraft.server." + version + "." + name);
           } catch (ClassNotFoundException var3) {
               var3.printStackTrace();
               return null;
           }
       }
     
       private static Class<?> getOBClass(String name) {
           String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
           try {
               return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
           } catch (ClassNotFoundException var3) {
               var3.printStackTrace();
               return null;
           }
       }
       
       public static String serializePlayerInventory(Player player) {
           ItemStack[] inventory = player.getInventory().getContents();
           StringBuilder serializedInventory = new StringBuilder();
           
           for (ItemStack item : inventory) {
               if (item != null) {
                   String serializedItem = SerializeItemStack.serializeItemStack(item);
                   serializedInventory.append(serializedItem).append(",");
               } else {
                   serializedInventory.append("null,");
               }
           }
           
           return serializedInventory.toString();
       }
       
       public static ItemStack[] deserializePlayerInventory(String serializedInventory) {
           String[] items = serializedInventory.split(",");
           ItemStack[] inventory = new ItemStack[items.length];
           
           for (int i = 0; i < items.length; i++) {
               if (!items[i].equals("null")) {
                   ItemStack item = SerializeItemStack.deserializeItemStack(items[i]);
                   inventory[i] = item;
               }
           }
           
          // player.getInventory().setContents(inventory);
		return inventory;
       }
     
       public static String serializeInventory(Inventory inventory) {
    	    ItemStack[] contents = inventory.getContents();
    	    StringBuilder serializedInventory = new StringBuilder();

    	    for (ItemStack item : contents) {
    	        if (item != null) {
    	            String serializedItem = SerializeItemStack.serializeItemStack(item);
    	            serializedInventory.append(serializedItem).append(",");
    	        } else {
    	            serializedInventory.append("null,");
    	        }
    	    }

    	    return serializedInventory.toString();
    	}
}
