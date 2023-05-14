package br.com.zsnow.eventocustom;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static ConfigApi locations;
	public static String prefix = "§6§lC§eustom§6§lE§event§7§l";
	public static ArrayList<Player> participantes = new ArrayList<>();
	public static boolean EventoOcorrendo;
	public static boolean EntradaLiberada;
	public static boolean frozen;
	public static boolean pvp;
	public static boolean Quebrarblocos;
	public static boolean Colocarblocos;
	public static boolean Sopa;
	public static boolean DropOnDeath;
	public static boolean DeathBroadcast;
	public static boolean Respawn;
	public static int players_slot = 100;
	
	public static Main getPlugin() {
		return getPlugin(Main.class);
	}
	
	public static String getMensagem(String[] args) {
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < args.length; i++) {
			sb.append(args[i] + " ");
		}
		return sb.toString();
	}
	
	public void onEnable() {
		locations = new ConfigApi("locations.yml", this);
		getCommand("evento").setExecutor(new Commands());
		getCommand("editaritem").setExecutor(new ItemEdit());
		Bukkit.getServer().getPluginManager().registerEvents(new Listeners(), this);
		Bukkit.getServer().getConsoleSender().sendMessage(" ");
		Bukkit.getServer().getConsoleSender().sendMessage(Main.prefix + "§7§l: §ePlugin inicializado com sucesso.");
		Bukkit.getServer().getConsoleSender().sendMessage(" ");
		saveDefaultConfig();
	}
	
}
