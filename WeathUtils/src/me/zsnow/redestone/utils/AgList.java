package me.zsnow.redestone.utils;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class AgList implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String args, String[] label) {
		if (cmd.getName().equalsIgnoreCase("aglist")) {
			if (!(sender instanceof Player)) {
				return true;
			}
			Player p = (Player) sender;
			if (args.length() >= 0) {
				p.sendMessage("§f- §e/ag §e<Mob>");
				p.sendMessage("§f- §e/ag§f1000 §e<Mob>");
				p.sendMessage("§f- §e/ag§f500K §e<Mob>");
				p.sendMessage("§f- §e/ag§f1KK §e<Mob>");
				p.sendMessage("§f- §e/ag§f50KK §e<Mob>");
				actionAPI("§e§lAVISO: §eo /AG é um atalho para comprar Spawners em grandes quantidades.", "[EasterEgg] Dica legal do tio zSnowReach.", p);
				p.sendMessage("§eCaso não saiba o nome dos mobs, utilize o comando: §f/agMobList");
				p.playSound(p.getLocation(), Sound.VILLAGER_YES, 1.0F, 0.5F);
			}
		}
		if (cmd.getName().equalsIgnoreCase("agmoblist")) {
			if (!(sender instanceof Player)) {
				return true;
			}
			Player p = (Player) sender;
			if (args.length() >= 0) {
				p.sendMessage("§f- §eVaca");
				p.sendMessage("§f- §ePorcoZumbi");
				p.sendMessage("§f- §eBruxa");
				p.sendMessage("§f- §eZumbi");
				p.sendMessage("§f- §eOvelha");
				p.sendMessage("§f- §eAldeao");
				p.sendMessage("§f- §eAranha");
				p.sendMessage("§f- §eGolem");
				p.sendMessage("§f- §eMagmacube");
				p.sendMessage("§f- §eGuardiao");
				p.sendMessage("§f- §eCreeper");
				p.sendMessage("§f- §eSlime");
				p.sendMessage("§f- §eBlaze");
				p.sendMessage("§e§lAVISO: §eCaso queira comprar algum desses mobs em grande escala, utilize os comandos do §f/aglist§e.");
				p.playSound(p.getLocation(), Sound.VILLAGER_YES, 1.0F, 0.5F);
			}
		
		}
		return false;
	}

	public void actionAPI(String valores, String suggest, Player p) {
		TextComponent msg = new TextComponent(valores);
		msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggest));
		p.spigot().sendMessage(msg);
	}
	
	
}
