package me.zsnow.redestone;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.zsnow.redestone.config.Configs;

public class CommandMito implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String args, String[] label) {
		if (cmd.getName().equalsIgnoreCase("mito")) {
			if (!(sender instanceof Player)) return false;
		}
		Player p = (Player) sender;
		if (args.length() >= 0) {
			String mito = Configs.mito.getConfig().getString("mito-atual") == null ? "§7Ninguém" : Configs.mito.getConfig().getString("mito-atual");
			p.sendMessage("§6➜ §b§l[MVP] §f" + mito + " §bé o MVP atual.");
		}
		return false;
	}
	
}
