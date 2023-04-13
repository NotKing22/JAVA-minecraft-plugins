package me.zsnow.multieventos;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;


public class DisablePlugin implements CommandExecutor {

	ArrayList<Player> verificar = new ArrayList<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("multieventos")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cApenas jogadores");
			}
			Player p = (Player) sender;
			if (!(p.hasPermission("zs.genrete"))) {
				p.sendMessage("§cVocê precisa do cargo Gerente ou superior para executar este comando.");
				return true;
			}
			if (args.length == 0) {
				p.sendMessage("§cUtilize o comando: /multieventos parar");
				return true;
			}
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("parar")) {
					if (!verificar.contains(p)) {
							p.sendMessage("§aTem certeza que deseja desabilitar este plugin?");
							p.sendMessage("§aDigite novamente o comando para §a§lconfirmar.");
							verificar.add(p);
							(new BukkitRunnable() {
								
								int tempo = 10;
								
								@Override
								public void run() {
									tempo--;
									if (tempo == 0) {
										verificar.remove(p);
										cancel();
									}
								}
							}).runTaskTimer(Main.getInstance(), 0, 20L);
							return true;
						} else {
							p.sendMessage("§cPlugin desabilitado.");
							 Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("MultiEventos");
							 Bukkit.getPluginManager().disablePlugin(plugin);
						}
					}
				}
			}
		return false;
	}
	
}
