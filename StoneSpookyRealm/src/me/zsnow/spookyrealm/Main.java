package me.zsnow.spookyrealm;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import me.zsnow.spookyrealm.configs.Configs;

public class Main extends JavaPlugin {
	
	public static Main getInstance() {
		return getPlugin(Main.class);
	}
	
    public WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (p instanceof WorldEditPlugin) return (WorldEditPlugin) p;
        else return null;
    }
	
	public void onEnable() {
		if (!new File(getDataFolder(), "config.yml").exists()) {
			Configs.config.saveDefaultConfig();
			Configs.config.reloadConfig();
		}
		if (!new File(getDataFolder(), "locations.yml").exists()) {
			Configs.locations.saveDefaultConfig();
			Configs.locations.reloadConfig();
		}
		for (World mundo : Bukkit.getWorlds()) {
			for (Entity entidades : mundo.getEntities()) {
				if (!(entidades instanceof Player) && (entidades.isCustomNameVisible() &&
						entidades.hasMetadata("vilarejo"))) {
					 entidades.remove();
				}
			}
		}
		int stack = 0;
		for (World mundo : Bukkit.getWorlds()) { // MUDAR NOME DO MUNDO
			for (Entity entidades : mundo.getEntities()) {
				if (!(entidades instanceof Player) && (entidades.isCustomNameVisible() &&
						entidades.hasMetadata("vilarejo"))) {
					 stack++;
				}
			}
		}
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(" §5§lSPOOKY REALM §e➟ §6§lO VILAREJO ESTA SENDO ASSOMBRADO!");
		Bukkit.broadcastMessage(" §d⚒  §ePegue sua armadura e ajude a elimina-las para ser recompensado. §6Acesso rápido com: §d/vilarejo");
		Bukkit.broadcastMessage(" ");
		EntityManagement entidade = EntityManagement.getInstance();
		try {
			for (int quantidade = 20; stack <= quantidade; stack++) {
				Random randomMob = new Random();
				if (randomMob.nextInt(4) == 3) {
					Random randomSpawn = new Random();
					entidade.spawnGhostFace((randomSpawn.nextInt(20)));
				}
				if (randomMob.nextInt(4) == 2) {
					Random randomSpawn = new Random();
					entidade.spawnCriaturaMaligna((randomSpawn.nextInt(20) + 1));
					}
				if (randomMob.nextInt(4) == 1) {
					Random randomSpawn = new Random();
					entidade.spawnVampire((randomSpawn.nextInt(20) + 1));
				}
			}
		} catch (IllegalArgumentException e) {
			Bukkit.getConsoleSender().sendMessage("§c[Vilarejo] A localidade dos monstros nao foi definida.");
		}
		Commands.entrada = true;
		getCommand("vilarejo").setExecutor(new Commands());
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
	}
	
}
