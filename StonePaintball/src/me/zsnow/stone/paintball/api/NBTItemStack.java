package me.zsnow.stone.paintball.api;

import org.bukkit.inventory.*;

public class NBTItemStack
{
    private ItemStack bukkititem;
    
    public NBTItemStack(final ItemStack item) {
        this.bukkititem = item.clone();
    }
    
    public ItemStack getItem() {
        return this.bukkititem;
    }
    
    public void setString(final String Key, final String Text) {
        this.bukkititem = NBTTag.setString(this.bukkititem, Key, Text);
    }
    
    public String getString(final String Key) {
        return NBTTag.getString(this.bukkititem, Key);
    }
    
    public void setInteger(final String key, final Integer Int) {
        this.bukkititem = NBTTag.setInt(this.bukkititem, key, Int);
    }
    
    public Integer getInteger(final String key) {
        return NBTTag.getInt(this.bukkititem, key);
    }
    
    public void setDouble(final String key, final Double d) {
        this.bukkititem = NBTTag.setDouble(this.bukkititem, key, d);
    }
    
    public Double getDouble(final String key) {
        return NBTTag.getDouble(this.bukkititem, key);
    }
    
    public void setBoolean(final String key, final Boolean b) {
        this.bukkititem = NBTTag.setBoolean(this.bukkititem, key, b);
    }
    
    public Boolean getBoolean(final String key) {
        return NBTTag.getBoolean(this.bukkititem, key);
    }
    
    public Boolean hasKey(final String key) {
        return NBTTag.hasKey(this.bukkititem, key);
    }
}
