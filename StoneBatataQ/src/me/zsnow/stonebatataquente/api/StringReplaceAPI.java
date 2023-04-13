package me.zsnow.stonebatataquente.api;

import org.bukkit.ChatColor;

import me.zsnow.stonebatataquente.configs.Configs;
import me.zsnow.stonebatataquente.manager.BatataController;
import me.zsnow.stonebatataquente.manager.EventController;

public class StringReplaceAPI {

	 private static String prefix = ChatColor.translateAlternateColorCodes('&', Configs.config.getString("prefix"));
		final private static int maxPlayer = Configs.config.getConfig().getInt("maximo-de-jogadores");
		final private static String moeda = "⛃";
		final private static String quente = "♨";
		final private static String seta = "➜";
	
	public static String replaceMsg(String msg) {
		EventController evento = EventController.getInstance();
		BatataController batata = BatataController.getInstance();
		NumberFormatAPI formatter = new NumberFormatAPI();
		String custo = formatter.formatNumber(evento.getRecompensaCoins());
		return msg.replace("{prefix}", prefix)
		.replace("{tempo}", String.valueOf(evento.getTempoEntrada()))
		.replace("{participantes}", String.valueOf(batata.getParticipantes().size()))
		.replace("{premio}", custo)
		.replace("{moeda_icon}", moeda)
		.replace("{quente_icon}", quente)
		.replace("{seta}", seta)
		.replace("{max_player}", String.valueOf(maxPlayer));
	}
	
}
