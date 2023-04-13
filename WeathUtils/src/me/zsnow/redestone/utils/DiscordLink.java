package me.zsnow.redestone.utils;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class DiscordLink implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("discord")) {
			if (!(sender instanceof Player)) {
				return true;
			}
			TextComponent msg = new TextComponent(Main.getPlugin().getConfig().getString("msg-discord").replace("&", "§"));
			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] { new TextComponent(Main.getPlugin().getConfig().getString("submsg-discord").replace("&", "§")) }));
			msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Main.getPlugin().getConfig().getString("clickevent-discord").replace("&", "§")));
			p.sendMessage(" ");
			p.spigot().sendMessage(msg);
			p.sendMessage(" ");
			p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
			return true;
		}
		
		return false;
	}

}
