package br.com.zsnow.eventocustom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigApi
{
  private Plugin plugin;
  private String name;
  private File file;
  private FileConfiguration config;
  
  public String getName()
  {
    return this.name;
  }
  
  public void setPlugin(Plugin plugin)
  {
    this.plugin = plugin;
  }
  
  public File getFile()
  {
    return this.file;
  }
  
  public FileConfiguration getConfig()
  {
    return this.config;
  }
  
  public ConfigApi(String name, Plugin plugin)
  {
    this.plugin = plugin;
    if (plugin == null) {
      this.plugin = JavaPlugin.getProvidingPlugin(getClass());
    }
    this.name = name;
    reloadConfig();
  }
  
  public ConfigApi(String name)
  {
    this(name, null);
  }
  
  @SuppressWarnings("deprecation")
public ConfigApi reloadConfig()
  {
    this.file = new File(this.plugin.getDataFolder(), this.name);
    this.config = YamlConfiguration.loadConfiguration(this.file);
    InputStream defaults = this.plugin.getResource(this.file.getName());
    if (defaults != null)
    {
      YamlConfiguration loadConfig = YamlConfiguration.loadConfiguration(defaults);
      this.config.setDefaults(loadConfig);
    }
    return this;
  }
  
  public ConfigApi saveConfig()
  {
    try
    {
      this.config.save(this.file);
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
    return this;
  }
  
  public String message(String path)
  {
    return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path));
  }
  
  public List<String> getMessages(String path)
  {
    List<String> messages = new ArrayList<String>();
    for (String line : getStringList(path)) {
      messages.add(toChatMessage(line));
    }
    return messages;
  }
  
  public void saveDefaultConfig()
  {
    if (this.plugin.getResource(this.name) != null) {
      this.plugin.saveResource(this.name, false);
    }
    reloadConfig();
  }
  
  public void saveResource()
  {
    this.plugin.saveResource(this.name, true);
  }
  
  public void remove(String path)
  {
    this.config.set(path, null);
  }
  
  public ConfigApi saveDefault()
  {
    this.config.options().copyDefaults(true);
    saveConfig();
    return this;
  }
  
  public ItemStack getItem(String path)
  {
    return (ItemStack)get(path);
  }
  
  public Location getLocation(String path)
  {
    return (Location)get(path);
  }
  
  public static String toChatMessage(String text)
  {
    return ChatColor.translateAlternateColorCodes('&', text);
  }
  
  public static String toConfigMessage(String text)
  {
    return text.replace("§", "&");
  }
  
  public boolean delete()
  {
    return this.file.delete();
  }
  
  public boolean exists()
  {
    return this.file.exists();
  }
  
  public void add(String path, Object value)
  {
    this.config.addDefault(path, value);
  }
  
  public boolean contains(String path)
  {
    return this.config.contains(path);
  }
  
  public ConfigurationSection create(String path)
  {
    return this.config.createSection(path);
  }
  
  public Object get(String path)
  {
    return this.config.get(path);
  }
  
  public boolean getBoolean(String path)
  {
    return this.config.getBoolean(path);
  }
  
  public ConfigurationSection getSection(String path)
  {
    if (!this.config.isConfigurationSection(path)) {
      this.config.createSection(path);
    }
    return this.config.getConfigurationSection(path);
  }
  
  public double getDouble(String path)
  {
    return this.config.getDouble(path);
  }
  
  public int getInt(String path)
  {
    return this.config.getInt(path);
  }
  
  public List<Integer> getIntegerList(String path)
  {
    return this.config.getIntegerList(path);
  }
  
  public ItemStack getItemStack(String path)
  {
    return this.config.getItemStack(path);
  }
  
  public Set<String> getKeys(boolean deep)
  {
    return this.config.getKeys(deep);
  }
  
  public List<?> getList(String path)
  {
    return this.config.getList(path);
  }
  
  public long getLong(String path)
  {
    return this.config.getLong(path);
  }
  
  public List<Long> getLongList(String path)
  {
    return this.config.getLongList(path);
  }
  
  public List<Map<?, ?>> getMapList(String path)
  {
    return this.config.getMapList(path);
  }
  
  public String getString(String path)
  {
    return this.config.getString(path);
  }
  
  public List<String> getStringList(String path)
  {
    return this.config.getStringList(path);
  }
  
  public Map<String, Object> getValues(boolean deep)
  {
    return this.config.getValues(deep);
  }
  
  public void set(String path, Object value)
  {
    this.config.set(path, value);
  }
}
