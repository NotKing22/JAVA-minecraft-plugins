package me.zsnow.redestone.utils;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleClanChat implements CommandExecutor {

	 @Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		 if (cmd.getName().equalsIgnoreCase("lercc")) {
			 if (!(sender instanceof Player)) {
				 sender.sendMessage("§cComando executavel apenas por jogadores.");
				 return true;
			 }
			 Player p = (Player) sender;
			 if (!(p.hasPermission("zs.admin"))) {
				 p.sendMessage("§cVocê precisa do cargo §6Administrador §cou superior para executar este comando.");
				 return true;
			 }
			 if (args.length == 0) {
				 p.sendMessage("§c§lSPACE: §eUse /lercc [ON/OFF].");
				 return true;
			 }
			 if (args.length == 1) {
				 if (args[0].equalsIgnoreCase("on")) {
					 Main.useConsole("pex user "+p.getName()+" remove -simpleclans.admin.all-seeing-eye");
					 p.sendMessage("§c§lSPACE: §7Modo espião ativado.");
					 p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
					 return true;
				 }
				 if (args[0].equalsIgnoreCase("off")) {
					 Main.useConsole("pex user "+p.getName()+" add -simpleclans.admin.all-seeing-eye");
					 p.sendMessage("§c§lSPACE: §7Modo espião desativado.");
					 p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
					 return true;
				 } else {
					 p.sendMessage("§5§lSPACE: §eUse /lercc [ON/OFF].");
					 p.playSound(p.getLocation(), Sound.CAT_MEOW, 1.0F, 0.5F);
					 return true;
				 	}
				 }
				 return true;
			 }
		 
		return false;
	}
	
}
