package me.zsnow.redestone.utils;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpToggle implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tptoggle")) {
	    	if (!(sender instanceof Player)) {
	    		sender.sendMessage("Comando apenas para jogadores");
	    		return true;
	    	}
	    	Player p = (Player) sender;
	    	if (!(p.hasPermission("zs.gerente"))) {
	    		p.sendMessage("§cVocê precisa do cargo §6Gerente §cou superior para executar este comando.");
	    		return true;
    	} else {
	    	if (args.length >= 0) {
		    	if (Tp.Player.contains(p.getName())) {
		    		Tp.Player.remove(p.getName());
		    		p.sendMessage("§eO seu recebimento de teletransporte foi §ahabilitado.");
		    		 p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
						Main.getPlugin().getConfig().set("tptoggle-on", Tp.Player);
						Main.getPlugin().saveConfig();
		    		return true;
		    	} else {
		    		Tp.Player.add(p.getName());
		    		p.sendMessage("§eO seu recebimento de teletransporte foi §cdesabilitado.");
		    		 p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
						Main.getPlugin().getConfig().set("tptoggle-on", Tp.Player);
						Main.getPlugin().saveConfig();
		    		return true;
	    			}
	    		}
	    	}
	    }
		return false;
	}
	
}
