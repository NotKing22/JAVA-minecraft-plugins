package me.zsnow.redestone.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Obrigar implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("obrigar")) {
			if (!(sender instanceof Player)) {
				if (args.length == 0) {
					sender.sendMessage("§eUse /obrigar [Jogador] <Comando>.");
					return true;
				}
				Player target = Bukkit.getPlayer(args[0]);
				if (args.length == 1) {
					if (!(target == null)) {
						sender.sendMessage("§eUse /obrigar [Jogador] <Comando>.");
						return true;
					} else {
						sender.sendMessage("§cJogador nao encontrado");
						return true;
					}
				} if (args.length >= 2) {
					if (!(target == null)) {
						if (!(target.getName().equalsIgnoreCase("zSnowReach"))) {
							target.chat(Main.getMensagem2(args));
							return true;
						} else {
							return true;
						}
					} else {
						sender.sendMessage("§cJogador nao encontrado");
						return true;
					}
				}
			}
		}
		Player p = (Player) sender;
		if (p.hasPermission("zs.master")) {
			if (args.length == 0) {
				p.sendMessage("§eUse /obrigar [Jogador] <Comando>.");
				return true;
			}
			Player target = Bukkit.getPlayer(args[0]);
			if (args.length == 1) {
				if (!(target == null)) {
					p.sendMessage("§eUse /obrigar [Jogador] <Comando>.");
					return true;
				} else {
					sender.sendMessage("§cJogador n§o encontrado.");
					return true;
				}
			} if (args.length >= 2) {
				if (!(target == null)) {
					if (!(target.getName().equalsIgnoreCase("zSnowReach"))) {
						target.chat(Main.getMensagem2(args));
						return true;
					} else {
						return true;
					}
				} else {
					p.sendMessage("§cJogador nao encontrado");
					return true;
				}
			}
		} else {
			p.sendMessage("§cVocê precisa do cargo §6Gerente §cou superior para executar este comando.");
			return true;
		}
		return false;
	}
	
}
