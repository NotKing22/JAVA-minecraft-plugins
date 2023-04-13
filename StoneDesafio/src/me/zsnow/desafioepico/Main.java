package me.zsnow.desafioepico;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import me.zsnow.desafioepico.configAPI.BossBar;
import me.zsnow.desafioepico.configAPI.Configs;
import me.zsnow.desafioepico.controller.EventController;
import me.zsnow.desafioepico.controller.MenuController;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	public static Main getInstance() {
		return getPlugin(Main.class);
	}
	
	public WorldGuardPlugin wGuard;
	
	 public Economy economy = null;
		
	  private boolean setupEconomy() {
	    RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
	    if (economyProvider != null) {
	      this.economy = ((Economy)economyProvider.getProvider());
	    }
	    return this.economy != null;
	  }
	
	public void onEnable() {
		if(!new File(getDataFolder(), "locations.yml").exists()){
			Configs.locations.saveDefaultConfig();
			Bukkit.getServer().getConsoleSender().sendMessage("§a[StoneDesafio] LocationFile gerada com sucesso!");
		}
		if(!new File(getDataFolder(), "config.yml").exists()){
			Configs.config.saveDefaultConfig();
			Configs.config.reloadConfig();
			Bukkit.getServer().getConsoleSender().sendMessage("§a[StoneDesafio] ConfigurationFile gerada com sucesso!");
		}
		getCommand("desafio").setExecutor(new Commands());
		Bukkit.getServer().getPluginManager().registerEvents(new Listeners(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new MenuController(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new CustomRecipes(), this);
		startCheck();
		wGuard = getWorldGuard();
		setupEconomy();
		CustomRecipes custom = new CustomRecipes();
		custom.makeRecipe();
		
		for (World mundo : Bukkit.getWorlds()) {
			for (Entity entidades : mundo.getEntities()) {
				if (!(entidades instanceof Player) && (entidades.hasMetadata("boss-nether") || 
					(entidades.hasMetadata("cavaleironegro-servo") || (entidades.hasMetadata("netherservos"))))) {
					 entidades.remove();
				}
			}
		}
		Bukkit.getServer().getConsoleSender().sendMessage("§a[StoneDesafio] Plugin inicializado.");
		
	//	CustomRecipes crafts = new CustomRecipes();
	//	crafts.loadCustomCarminitaRecipes();
		
	}
	
	public void onDisable() {
		for (Player p1 : EventController.admin.getNetherParticipantes()) {
			BossBar.removeStatusBar(p1);
			sendTo(p1, "SAIDA-NETHER");
		}
	/*	for (Player p2 : EventController.admin.getEdenParticipantes()) {
			BossBar.removeStatusBar(p2);
			sendTo(p2, "SAIDA-NETHER");
		}*/
		StopNether();
	}
	
	public static ConsoleCommandSender sc = Bukkit.getConsoleSender();
	
	public void startCheck(){
		EventController evento = new EventController();
	    Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, () -> {
	        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
	        String time = format.format(new Date(System.currentTimeMillis()));
	        if(time.equals(Configs.config.getConfig().getString("Horario.Nether")) && evento.getNetherOcorrendo() == false){
	        	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "desafio iniciar nether");
	        }
	    }, 20, 20);
	    sc.sendMessage("§3[StoneDesafio] Comando executado! Desafio Nether iniciando automaticamente.");
	}
	
	
    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
    
	public static void useConsole(String m) {
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), m);
	}

	public static void StopNether() {
		for (Player participantes : EventController.admin.getNetherParticipantes()) {
			EventController.admin.playerCoinsNether.clear();
			sendTo(participantes, "Saida-nether");
		}
		EventController.admin.setNetherOcorrendo(false);
		EventController.admin.setNetherEntradaLiberada(false);
		EventController.admin.getNetherParticipantes().clear();
		for (World mundo : Bukkit.getWorlds()) {
			for (Entity entidades : mundo.getEntities()) {
				if (!(entidades instanceof Player) && (entidades.hasMetadata("boss-nether") || 
					(entidades.hasMetadata("cavaleironegro-servo") || (entidades.hasMetadata("netherservos"))))) {
					 entidades.remove();
				}
			}
		}
	}
	
	public static void sendTo(Player p, String location) {
		location = location.toUpperCase(); //adicionei
		double X = Configs.locations.getConfig().getDouble(location + ".X");
        double Y = Configs.locations.getConfig().getDouble(location + ".Y");
        double Z = Configs.locations.getConfig().getDouble(location + ".Z");
        float Yaw = (float)Configs.locations.getConfig().getLong(location + ".Yaw");
        float Pitch = (float)Configs.locations.getConfig().getLong(location + ".Pitch");
	    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
		p.teleport(loc);
	}
}
