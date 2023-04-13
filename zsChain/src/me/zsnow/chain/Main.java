package me.zsnow.chain;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
//	public static ConfigApi locations;
	
	//public NPCManager npcManager;
	
	public static Main getPlugin() {
		return getPlugin(Main.class);
	}
	
	public void onEnable() {
		getCommand("chain").setExecutor(new Commands());
		Bukkit.getServer().getPluginManager().registerEvents(new Listeners(), this);
		Bukkit.getServer().getConsoleSender().sendMessage(" ");
		Bukkit.getServer().getConsoleSender().sendMessage(" §5§l[zsCHAIN] §aInicializado com sucesso.");
		Bukkit.getServer().getConsoleSender().sendMessage(" §5§lCreditos: §azSnowReach.");
		Bukkit.getServer().getConsoleSender().sendMessage(" ");
		saveDefaultConfig();
	//	locations = new ConfigApi("locations.yml", this);
	  }
	/*
	this.npcManager = new NPCManager();
    (new BukkitRunnable() {
        public void run() {
          NPCManager.CreateNPC2();
        }
      }).runTaskLater((Plugin)this, 40L);
      */
	
	public void onDisable() {
		Bukkit.getServer().getConsoleSender().sendMessage(" ");
		Bukkit.getServer().getConsoleSender().sendMessage(" §5§l[zsCHAIN] §cDesativado com sucesso.");
		Bukkit.getServer().getConsoleSender().sendMessage(" ");
	}
	
	  /*  try {
    Commands.dungeonNPC.destroy();
  } catch (Exception exception) {}
  try {
    Commands.dungeonNPC.getEntity().remove();
  } catch (Exception exception) {}
  */
	
}

    
