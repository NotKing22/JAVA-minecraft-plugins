package me.zsnow.redestone.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


/*
 * Author: @zSnowReach - Sou novato, dá um desconto por qualquer estupidez
 */

public class Main extends JavaPlugin {
	
	public Main main = this;
	
	public static String getMensagem(String[] args) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < args.length; i++) {
			sb.append(args[i] + " ");
		}
		return sb.toString();
	}
	
	public static Main getPlugin() {
		return getPlugin(Main.class);
	}
	
	public static String getMensagem2(String[] args) {
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < args.length; i++) {
			sb.append(args[i] + " ");
		}
		return sb.toString();
	}
	
	public void onEnable() {
			getCommand("Lixeira").setExecutor(new Lixeira());
			Bukkit.getServer().getPluginManager().registerEvents(new Lixeira(), this);
			Bukkit.getServer().getPluginManager().registerEvents(new ColorSignADMIN(), this);
			getCommand("discord").setExecutor(new DiscordLink());
			getCommand("report").setExecutor(new Reports());
			getCommand("valores").setExecutor(new GuiaDeValores());
			getCommand("obrigar").setExecutor(new Obrigar());
			getCommand("tphere").setExecutor(new Tphere());
			getCommand("gm3").setExecutor(new ModGm3());
			getCommand("lercc").setExecutor(new ToggleClanChat());
			getCommand("regras").setExecutor(new WeathRegras());
			getCommand("editaritem").setExecutor(new ItemEdit());
			getCommand("tp").setExecutor(new Tp());
			getCommand("tptoggle").setExecutor(new TpToggle());
			getCommand("head").setExecutor(new Cabeca());
		//	getCommand("btp").setExecutor(new Btp());
		//	getCommand("btpblacklist").setExecutor(new BTPblacklist());
			getCommand("chapeu").setExecutor(new ChapeuEquip());
			getCommand("esconder").setExecutor(new EsconderAndAparecer());
			getCommand("aparecer").setExecutor(new EsconderAndAparecer());
			getCommand("compararip").setExecutor(new CompararIP());
			getCommand("aglist").setExecutor(new AgList());
			getCommand("agmoblist").setExecutor(new AgList());
			saveDefaultConfig();
			getServer().getConsoleSender().sendMessage(" ");
			getServer().getConsoleSender().sendMessage("§6[WeathUtils] §aInicializado com sucesso.");
			getServer().getConsoleSender().sendMessage(" ");
			
	}
	public void onDisable() {
		getServer().getConsoleSender().sendMessage(" ");
		getServer().getConsoleSender().sendMessage("§6[WeathUtils] §cDesativado com sucesso.");
		getServer().getConsoleSender().sendMessage(" ");
	}
	
	public static void useConsole(String m) {
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), m);
	}

}