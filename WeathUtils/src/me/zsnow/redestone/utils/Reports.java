package me.zsnow.redestone.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Reports implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("report")) {
			if (!(sender instanceof Player)) {
				return true;
			}
			Player p = (Player) sender;
			if (args.length == 0) {
				p.sendMessage("§eUse o comando §b/reportar <nick> (motivo).");
				return true;
			}
			else if (args.length == 1) {
				p.sendMessage("§eUse o comando §b/reportar <nick> (motivo).");
				return true;
			}
			else if (args.length >= 2) {
			Player target = Bukkit.getPlayer(args[0]);
			if (!(target == null)) {
				if (!(target.getName() == p.getName())) {
					p.sendMessage("§eObrigado por informar sobre a atividade §csuspeita §edeste usuário.");
					p.sendMessage("§eCaso haja um membro da equipe §aonline§e, ele analisará o jogador e irá puni-lo caso necessário.");
					p.sendMessage("§6* §eVale lembrar que o uso §cabusivo §edeste comando gera punição.");
					p.playSound(p.getLocation(), Sound.VILLAGER_YES, 1.0F, 0.5F);
					for (Player staff : Bukkit.getOnlinePlayers()) {
						if (staff.hasPermission("zs.report")) {
							staff.sendMessage("  §c§lNOVO REPORT");
							staff.sendMessage("§c* Acusado: §f" + target.getName());
							staff.sendMessage("§c* Vítima: §f" + p.getName());
							staff.sendMessage("§c* Motivo: §f" + Main.getMensagem2(args));
							TextComponent msg = new TextComponent("§c* Clique aqui §7(Teletransportar)");
							msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] { new TextComponent("§eClique para ir até o jogador. ") }));
							msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + target.getName()));
							staff.playSound(staff.getLocation(), Sound.VILLAGER_YES, 1.0F, 0.5F);
							staff.spigot().sendMessage(msg);
							}
						}
					} else 
						{
						p.sendMessage("§cVocê não pode reportar a sí mesmo.");
						return true;
						}
				} else 
					{
					p.sendMessage("§cJogador não encontrado.");
					return true;
					}
				} 
			}
		return false;
	}

}

/*
  	private String getRandomValue(int lenght){
		StringBuilder builder = new StringBuilder();
		String randomValue = "0123456789";
		for(int i = 0; i<lenght; i++){
			double index = Math.random() * randomValue.length();
			builder.append(randomValue.charAt((int)index));
		}
		return builder.toString();
	}
	*/
