package me.zsnow.stone.partygames.configs;

import java.util.List;
import java.io.File;
import java.io.IOException;

import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {

	private Plugin plugin;
	private File file;
	private YamlConfiguration yamlConfiguration;

	public ConfigManager(Plugin plugin, String folderDir, String name) {
		this.plugin = plugin;
		if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
		file = new File(plugin.getDataFolder(), folderDir + name);
		reloadConfig();
		plugin.getLogger().info("Config " + folderDir + name + " criada/conectada.");
	}

	public YamlConfiguration getConfig() {
		return yamlConfiguration;
	}

	public void reloadConfig() {
		yamlConfiguration = YamlConfiguration.loadConfiguration(file);
	}

	public void saveConfig() {
		try {
			getConfig().save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveDefaults() {
		getConfig().options().copyDefaults(true);
	}

	public void saveDefaultConfig(String folderDir) {
		getPlugin().saveResource(folderDir + file.getName(), false);
	}

	public Plugin getPlugin() {
		return plugin;
	}
     
	public String getString(String path) {
		return getConfig().getString(path);
	}

	public List<String> getStringList(String path) {
		return getConfig().getStringList(path);
	}

	public int getInt(String path) {
		return getConfig().getInt(path);
	}

	public List<Integer> getIntegerList(String path) {
		return getConfig().getIntegerList(path);
	}

	public double getDouble(String path) {
		return getConfig().getDouble(path);
	}

	public List<Double> getDoubleList(String path) {
		return getConfig().getDoubleList(path);
	}

	public List<Float> getFloatList(String path) {
		return getConfig().getFloatList(path);
	}

	public List<Short> getShortList(String path) {
		return getConfig().getShortList(path);
	}

	public ItemStack getItemStack(String path) {
		return getConfig().getItemStack(path);
	}

	public World getWorld(String path) {
		return Bukkit.getWorld(getString(path));
	}

	public List<?> getList(String path) {
		return getConfig().getList(path);
	}

	public long getLong(String path) {
		return getConfig().getLong(path);
	}

	public List<Long> getLongList(String path) {
		return getConfig().getLongList(path);
	}

	public boolean getBoolean(String path) {
		return getConfig().getBoolean(path);
	}

	public void set(String path, Object value) {
		getConfig().set(path, value);
	}

	public boolean exists() {
		return getFile().exists();
	}

	public void delete() {
		getFile().delete();
	}

	public File getFile() {
		return file;
	}

	public Object get(String path) {
		return getConfig().get(path);
	}

	public boolean isNull(String path) {
		return get(path) == null;
	}

	public ConfigurationSection getConfigurationSection(String section) {
		return getConfig().getConfigurationSection(section);
	}

}
