package me.zsnow.stone.altar;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {
	
	// ADD O CMD EXECUTOR 
	
	public static Main getInstance() {
		return getPlugin(Main.class);
	}
	
	Map<Integer, ArrayList<String>> horarios = new HashMap<>();
	
	public void onEnable() {
		if (!new File(getDataFolder(), "altar.yml").exists()) {
			Configs.altar.saveDefaultConfig();
			Bukkit.getServer().getConsoleSender().sendMessage("§a[StoneAltar] Altar.yml gerada com sucesso.");
		}
		if (!new File(getDataFolder(), "config.yml").exists()) {
			Configs.config.saveDefaultConfig();
			Bukkit.getServer().getConsoleSender().sendMessage("§a[StoneAltar] Config.yml gerada com sucesso.");
		}
		getCommand("altar").setExecutor(new Commands());
		Bukkit.getPluginManager().registerEvents(new Altar_Model(), this);
		
		saveHorariosCache();
		Altar_Model altar = Altar_Model.getInstance();
		
		(new BukkitRunnable() {
			
			@Override
			public void run() {
					LocalTime agora = LocalTime.now();
					DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss");
				    String horaFormatada = agora.format(formato);
				    
					for (Entry<Integer, ArrayList<String>> entry : horarios.entrySet()) {
						int ID = entry.getKey();
						for (String horas : entry.getValue()) {
							if (horas.equals(horaFormatada)) {
								if (!Altar_Model.altar_hp.containsKey(ID)) {
									altar.spawnAltar(ID);
									Bukkit.getConsoleSender().sendMessage("§a[StoneAltar] Altar com ID #0" + ID + " §afoi spawnado.");
									for (String msg : Configs.config.getStringList("altar-spawn")) {
										Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
									}
								} else {
									Bukkit.getConsoleSender().sendMessage("§c[StoneAltar] Altar com ID #0" + ID + " §cja estava spawnado.");
								}
							}
						}
					}
				}
		}).runTaskTimer(this, 0L, 20L);
	}
	
	public void onDisable() {
		final String metadata = "altar_id";
		int count = 0;
		for (World world : Bukkit.getWorlds()) {
			for (Entity entities : world.getEntities()) {
				if (entities.hasMetadata(metadata) && (entities.getType() == EntityType.ENDER_CRYSTAL || entities.getType() == EntityType.ARMOR_STAND)) {
					entities.remove();
					count++;
				}
			}
		} 
		Bukkit.getServer().getConsoleSender().sendMessage("§e[StoneAltar] Foram removidos um total de §f" + count + " §ealtares e hologramas devido o reload.");
	}
	
	public void saveHorariosCache() {
		if (Configs.altar.getConfigurationSection("ALTAR.") == null) {
			return;
		}
		for (String ALTAR_ID : Configs.altar.getConfigurationSection("ALTAR.").getKeys(false)) {
			if (Configs.altar.getBoolean("ALTAR." + ALTAR_ID + ".schedule-time") == true) {
				int ID = Integer.parseInt(ALTAR_ID);
				List<String> save = Configs.altar.getStringList("ALTAR." + ALTAR_ID + ".spawn-at");
				horarios.put(ID, (ArrayList<String>) save);
			}
		}
		Bukkit.getServer().getConsoleSender().sendMessage("§a[StoneAltar] Horários registrados com sucesso.");
	}
	
}
