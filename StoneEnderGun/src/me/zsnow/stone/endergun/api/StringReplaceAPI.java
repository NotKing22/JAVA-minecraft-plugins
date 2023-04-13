package me.zsnow.stone.endergun.api;

import org.bukkit.ChatColor;

import me.zsnow.stone.endergun.configs.Configs;
import me.zsnow.stone.endergun.manager.EventController;
import me.zsnow.stone.endergun.manager.MembersController;

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
		String vencedorNome = members.getParticipantes().size() != 0 ? members.getParticipantes().get(0).getName() : "";
		return msg.replace("{prefix}", prefix)
		.replace("{tempo}", String.valueOf(evento.getTempo()))
		.replace("{participantes}", String.valueOf(members.getParticipantes().size()))
		.replace("{premio}", custo)
		.replace("{recompensa}", String.valueOf(evento.getRecompensa()))
		.replace("{moeda_icon}", moeda)
		.replace("{seta}", seta)
		.replace("{vencedor}", vencedorNome)
		.replace("{tempo_lobby}", String.valueOf(evento.getTempoLobby()))
		.replace("{max_player}", String.valueOf(maxPlayer));
	}
	
}
