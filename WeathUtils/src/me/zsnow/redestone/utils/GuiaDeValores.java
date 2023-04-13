package me.zsnow.redestone.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class GuiaDeValores implements CommandExecutor {
 
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("valores")) {
			if (!(sender instanceof Player)) {
				/*sender.sendMessage("§6Cotação dos valores:");
				sender.sendMessage("§e1§ §2$§f1000000 §7(§e1§fM §7seis zeros§7)");
				sender.sendMessage("§e2§ §2$§f10000000 §7(§e10§fM §7sete zeros§7)");
				sender.sendMessage("§e3§ §2$§f100000000 §7(§e100§fM §7oito zeros§7)");
				sender.sendMessage("§e4§ §2$§f1000000000 §7(§e1§fB §7nove zeros§7)");
				sender.sendMessage("§e5§ §2$§f10000000000 §7(§e10§fB §7dez zeros§7)");
				sender.sendMessage("§e6§ §2$§f100000000000 §7(§e100§fB §7onze zeros§7)");
				sender.sendMessage("§e7§ §2$§f1000000000000 §7(§e1§fT §7doze zeros§7)");
				sender.sendMessage("§e8§ §2$§f10000000000000 §7(§e10§fT §7treze zeros§7)");
				sender.sendMessage("§e9§ §2$§f100000000000000 §7(§e100§fT §7quatorze zeros§7)");
				sender.sendMessage("§e10§ §2$§f1000000000000000 §7(§e1§fQ §7quinze zeros§7)");
				sender.sendMessage("§e11§ §2$§f10000000000000000 §7(§e10§fQ §7dezesseis zeros§7)");
				sender.sendMessage("§e12§ §2$§f100000000000000000 §7(§e100§fQ §7dezessete zeros§7)");
				sender.sendMessage("§e13§ §2$§f1000000000000000000 §7(§e1§fQQ §7dezoito zeros§7)");*/
				if (args.length >= 1) {
					int valor = Integer.parseInt(args[1]);
					int percentage = Integer.parseInt(args[0]);;
					int resultado = (valor/100)*10;
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§cValor: §e" + valor);
					Bukkit.broadcastMessage("§cpercentage: §e" + percentage + "§e%");
					Bukkit.broadcastMessage(percentage + "% de " + valor + " = " + resultado);
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§aResultado final " + (valor+resultado));
					
				}
				return true;
			}
			Player p = (Player) sender;
				p.sendMessage("§6Cotação dos valores:");
				actionAPI("§e1ª §2$§f1000000 §7(§e1§fM §c6 §7zeros§7)", "1M 1000000", p);
				actionAPI("§e2ª §2$§f10000000 §7(§e10§fM §c7 §7zeros§7)", "10M 10000000", p);
				actionAPI("§e3ª §2$§f100000000 §7(§e100§fM §c8 §7zeros§7)", "100M 100000000", p);
				actionAPI("§e4ª §2$§f1000000000 §7(§e1§fB §c9 §7zeros§7)", "1B 1000000000", p);
				actionAPI("§e5ª §2$§f10000000000 §7(§e10§fB §c10 §7zeros§7)", "10B 10000000000", p);
				actionAPI("§e6ª §2$§f100000000000 §7(§e100§fB §c11 §7zeros§7)", "100B 100000000000", p);
				actionAPI("§e7ª §2$§f1000000000000 §7(§e1§fT §c12 §7zeros§7)", "1T 1000000000000", p);
				actionAPI("§e8ª §2$§f10000000000000 §7(§e10§fT §c13 §7zeros§7)", "10T 10000000000000", p);
				actionAPI("§e9ª §2$§f100000000000000 §7(§e100§fT §c14 §7zeros§7)", "100T 100000000000000", p);
				actionAPI("§e10ª §2$§f1000000000000000 §7(§e1§fQ §c15 §7zeros§7)", "1Q 1000000000000000", p);
				actionAPI("§e11ª §2$§f10000000000000000 §7(§e10§fQ §c16 §7zeros§7)", "10Q 10000000000000000", p);
				actionAPI("§e12ª §2$§f100000000000000000 §7(§e100§fQ §c17 §7zeros§7)", "100Q 100000000000000000", p);
				actionAPI("§e13ª §2$§f1000000000000000000 §7(§e1§fQQ §c18 §7zeros§7)", "1QQ 1000000000000000000", p);
				actionAPI("§e14ª §2$§f10000000000000000000 §7(§e10§fQQ §c19 §7zeros§7)", "10QQ 10000000000000000000", p);
				actionAPI("§e15ª §2$§f100000000000000000000 §7(§e100§fQQ §c20 §7zeros§7)", "100QQ 100000000000000000000", p);
				actionAPI("§e16ª §2$§f1000000000000000000000 §7(§e1§fS §c21 §7zeros§7)", "1S 1000000000000000000000", p);
				actionAPI("§e17ª §2$§f10000000000000000000000 §7(§e10§fS §c22 §7zeros§7)", "10S 10000000000000000000000", p);
				actionAPI("§e18ª §2$§f100000000000000000000000 §7(§e100§fS §c23 §7zeros§7)", "100S 100000000000000000000000", p);
				actionAPI("§e19ª §2$§f1000000000000000000000000 §7(§e1§fSS §c24 §7zeros§7)", "1SS 1000000000000000000000000", p);
				actionAPI("§e20ª §2$§f10000000000000000000000000 §7(§e10§fSS §c25 §7zeros§7)", "10SS 10000000000000000000000000", p);
				actionAPI("§e21ª §2$§f100000000000000000000000000 §7(§e100§fSS §c26 §7zeros§7)", "100SS 100000000000000000000000000", p);
				actionAPI("§e22ª §2$§f1000000000000000000000000000 §7(§e1§fOC §c27 §7zeros§7)", "1OC 1000000000000000000000000000", p);
				actionAPI("§e23ª §2$§f10000000000000000000000000000 §7(§e10§fOC §c28 §7zeros§7)", "10/OC 10000000000000000000000000000", p);
				actionAPI("§e24ª §2$§f100000000000000000000000000000 §7(§e100§fOC §c29 §7zeros§7)", "100/OC 100000000000000000000000000000", p);
				actionAPI("§e§lDICA: §eClique sobre o valor desejado!", "[EasterEgg] Dica legal do tio zSnowReach.", p);
				p.playSound(p.getLocation(), Sound.VILLAGER_YES, 1.0F, 0.5F);

		}
		return false;
		
	}
	
	public void actionAPI(String valores, String suggest, Player p) {
		TextComponent msg = new TextComponent(valores);
		msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggest));
		p.spigot().sendMessage(msg);
	}
}

	
