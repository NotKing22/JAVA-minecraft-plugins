package me.zsnow.stone.paintball.api;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.zsnow.stone.paintball.configs.Configs;
import me.zsnow.stone.paintball.manager.EventController;
import me.zsnow.stone.paintball.manager.MembersController;

public class StringReplaceAPI {

	 private static String prefix = ChatColor.translateAlternateColorCodes('&', Configs.config.getString("prefix"));
		final private static int maxPlayer = Configs.config.getConfig().getInt("maximo-de-jogadores");
		final private static String moeda = "⛃";
		final private static String seta = "➜";
	
	public static String replaceMsg(String msg) {
		EventController evento = EventController.getInstance();
		MembersController members = MembersController.getInstance();
		NumberFormatAPI formatter = new NumberFormatAPI();
		String custo = formatter.formatNumber(evento.getRecompensa());
		return msg.replace("{prefix}", prefix)
		.replace("{tempo}", String.valueOf(evento.getTempo()))
		.replace("{participantes}", String.valueOf(members.getParticipantes().size()))
		.replace("{premio}", custo)
		.replace("{recompensa}", String.valueOf(evento.getRecompensa()))
		.replace("{moeda_icon}", moeda)
		.replace("{seta}", seta)
		.replace("{tempo_lobby}", String.valueOf(evento.getTempoLobby()))
		.replace("{max_player}", String.valueOf(maxPlayer));
	}
	
	public static String replaceMsg2(String msg, Player p, String time) {
		EventController evento = EventController.getInstance();
		MembersController members = MembersController.getInstance();
		NumberFormatAPI formatter = new NumberFormatAPI();
		String custo = formatter.formatNumber(evento.getRecompensa());
		String vencedorNome = p.getName();
		return msg.replace("{prefix}", prefix)
		.replace("{tempo}", String.valueOf(evento.getTempo()))
		.replace("{participantes}", String.valueOf(members.getParticipantes().size()))
		.replace("{premio}", custo)
		.replace("{recompensa}", String.valueOf(evento.getRecompensa()))
		.replace("{moeda_icon}", moeda)
		.replace("{seta}", seta)
		.replace("{time}", time)
		.replace("{prefix}", prefix)
		.replace("{vencedor}", vencedorNome)
		.replace("{tempo_lobby}", String.valueOf(evento.getTempoLobby()))
		.replace("{max_player}", String.valueOf(maxPlayer));
	}
	
}
