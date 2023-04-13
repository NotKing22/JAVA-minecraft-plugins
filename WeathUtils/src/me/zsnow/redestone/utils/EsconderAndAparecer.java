package me.zsnow.redestone.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EsconderAndAparecer implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("esconder")) {
			if (!(sender instanceof Player)) {
				return true;
		}
			Player p = (Player) sender;
			p.sendMessage("§eAgora todos os jogadores ficaram §6invisíveis §epara você.");
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
			for (Player membros : Bukkit.getOnlinePlayers()) {
				if (!(membros.hasPermission("ea.bypass"))) {
					p.hidePlayer(membros);
				}
			}
		}	
		if (cmd.getName().equalsIgnoreCase("aparecer")) {
			if (!(sender instanceof Player)) {
				return true;
		}
			Player p = (Player) sender;
			p.sendMessage("§eAgora todos os jogadores ficaram §6visíveis §epara você.");
			p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 0.5F);
				for (Player membros : Bukkit.getOnlinePlayers()) {
					if (!(membros.hasPermission("ea.bypass"))) {
						p.showPlayer(membros);
				}
			}
		}
		return false;
	}
}