package me.zsnow.multieventos;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.zsnow.multieventos.bolao.Commands;
import me.zsnow.multieventos.configAPI.Configs;
import me.zsnow.multieventos.desembaralhe.DesembaralheCMD;
import me.zsnow.multieventos.fastclick.FastClickCMD;
import me.zsnow.multieventos.fastwrite.FastWriteCMD;
import me.zsnow.multieventos.loteria.LoteriaCommands;
import me.zsnow.multieventos.matematica.MathCMD;
import me.zsnow.multieventos.quiz.QuizCMD;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	 public Economy economy = null;
		
	  private boolean setupEconomy() {
	    RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
	    if (economyProvider != null) {
	      this.economy = ((Economy)economyProvider.getProvider());
	    }
	    return this.economy != null;
	  }
	  
	public static Main getInstance() {
		return getPlugin(Main.class);
	}
	
	public static void load() {
		try {
			me.zsnow.multieventos.API.TitleAPI.load();	
		} 
		catch (Exception e) {}
	}
	
	private void enablePlugin() {
		load();
	}
	
//	public static Version getVersion() {
//		return version;
//	}
	
	public void onEnable() {
		setupEconomy();
		checkServerVersion();
		enablePlugin();
		getCommand("multieventos").setExecutor(new DisablePlugin());
		getCommand("fastwrite").setExecutor(new FastWriteCMD());
		getCommand("matematica").setExecutor(new MathCMD());
		getCommand("loteria").setExecutor(new LoteriaCommands());
		getCommand("bolao").setExecutor(new Commands());
		getCommand("fastclick").setExecutor(new FastClickCMD());
		getCommand("quiz").setExecutor(new QuizCMD());
		getCommand("desembaralhe").setExecutor(new DesembaralheCMD());
		Bukkit.getPluginManager().registerEvents(new QuizCMD(), this);
		if(!new File(getDataFolder(), "config.yml").exists()){
			Configs.config.saveDefaultConfig();
			Bukkit.getServer().getConsoleSender().sendMessage("§a[MultiEventos] Plugin inicializado com sucesso!");
		}
		Bukkit.getServer().getPluginManager().registerEvents(new FastWriteCMD(), this);
	}
	
	private Version checkServerVersion() {
		String ver = Bukkit.getVersion();

		if (ver.contains("1.13"))
			return Version.v1_13;
		else if (ver.contains("1.12"))
			return Version.v1_12;
		else if (ver.contains("1.11"))
			return Version.v1_11;
		else if (ver.contains("1.10"))
			return Version.v1_10;
		else if (ver.contains("1.9"))
			return Version.v1_9;
		else if (ver.contains("1.8"))
			return Version.v1_8;
		else if (ver.contains("1.7"))
			return Version.v1_7;
		else if (ver.contains("1.6"))
			return Version.v1_6;
		else if (ver.contains("1.5"))
			return Version.v1_5;
		else
			return Version.DESCONHECIDA;
	}

	
}
