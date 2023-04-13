package me.zsnow.redestone.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BTPblacklist implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("btpblacklist")) {
			if (!(sender instanceof Player)) {
				return true;
			}
			Player p = (Player) sender;
			if (!(p.hasPermission("zs.gerente"))) {
				p.sendMessage("§cVocê precisa do grupo Gerente ou superior para executar este comando.");
				return true;
			}
			if (args.length <= 1) {
				p.sendMessage("§cUtilize /btpblacklist <add/remove> {NICK} §7- §cUtilize nos membros da equipe que abusarem do comando.");
				return true;
			}
			if (args.length >= 2) {
				Player target = Bukkit.getPlayer(args[1]);
					if (target == null) {
						p.sendMessage("§cEste jogador não está online.");
						return true;
					}
				if (args[0].equalsIgnoreCase("add")) {
			//		if (Btp.BlackList.contains(target.getName())) {
						p.sendMessage("§cEste jogador já está na lista negra.");
						return true;
					}
			//		Btp.BlackList.add(target.getName());
					p.sendMessage("§4§lBLACKLIST: §eVocê adicionou §c" +target.getName()+ " §ena lista negra do BTP.");
					target.playSound(target.getLocation(), Sound.NOTE_BASS, 1.0F, 0.5F);
					target.sendMessage("§4§lBLACKLIST: §6Você foi bloqueado de usar o BTP.");
					target.sendMessage("§f§l● §fDevido o mal uso do BTP, você perdeu acesso á esse comando.");
		//			Main.getPlugin().getConfig().set("btp-blacklist", Btp.BlackList);
					Main.getPlugin().saveConfig();
					return true;
				}
				if (args[0].equalsIgnoreCase("remove")) {
			//		if (!(Btp.BlackList.contains(target.getName()))) {
						p.sendMessage("§cEste jogador não está na lista negra.");
						return true;
					}
		//			Btp.BlackList.remove(target.getName());
		//			p.sendMessage("§4§lBLACKLIST: §aVocê removeu §e" +target.getName()+ " §ada lista negra do BTP.");
		//			target.playSound(target.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 0.5F);
		//			target.sendMessage("§4§lBLACKLIST: §6Você foi liberado para usar o BTP novamente.");
		//			target.sendMessage("§f§l● §fEsperamos que tenha se arrependido de seus atos, da próxima, não seremos benevolentes.");
		//			Main.getPlugin().getConfig().set("btp-blacklist", Btp.BlackList);
					Main.getPlugin().saveConfig();
					return true;
				} else {
		//			p.sendMessage("§cUtilize /btpblacklist <add/remove> {NICK}");
					return true;
				}
		//	}
	//	}
	//	return false;
	}
	
}
