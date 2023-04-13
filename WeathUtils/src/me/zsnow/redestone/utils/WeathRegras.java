package me.zsnow.redestone.utils;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WeathRegras implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("regras")) {
			if (!(sender instanceof Player)) {
				return true;
			} 
			Player p = (Player) sender;
			p.sendMessage("§e§lREGRAS: §fAcesse nosso site para conferir as regras: https://bit.ly/regrasredestone");
			p.playSound(p.getLocation(), Sound.ANVIL_USE, 1.0F, 0.5F);
		}
		return false;
	}
	
	
}
