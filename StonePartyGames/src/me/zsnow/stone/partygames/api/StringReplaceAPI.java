package me.zsnow.stone.partygames.api;

import org.bukkit.entity.Player;

import me.zsnow.stone.partygames.Main;
import me.zsnow.stone.partygames.configs.Configs;
import me.zsnow.stone.partygames.games.MainEvent;

public class StringReplaceAPI {

	// private static String prefix = ChatColor.translateAlternateColorCodes('&', Configs.config.getString("prefix"));
	private final static String prefix = Main.getInstance().getPrefix;
		final private static int maxPlayer = Configs.config.getConfig().getInt("maximo-de-jogadores");
		final private static String moeda = "⛃";
		final private static String seta = "➜";
		
	
	public static String replaceMsg(String msg) {
		MainEvent evento = MainEvent.getInstance();		
	//	NumberFormatAPI formatter = new NumberFormatAPI();
		//String custo = formatter.formatNumber(evento.getRecompensa());
		return msg.replace("{prefix}", prefix)
		.replace("{tempo}", String.valueOf(evento.getTempo()))
		.replace("{participantes}", String.valueOf(evento.getParticipantes().size()))
		//.replace("{premio}", custo)
		//.replace("{recompensa}", String.valueOf(evento.getRecompensa()))
		.replace("{moeda_icon}", moeda)
		.replace("{seta}", seta)
		.replace("{max_player}", String.valueOf(maxPlayer));
	}
	
	public static String replaceMsg2(String msg, Player p, String time) {
		MainEvent evento = MainEvent.getInstance();	
		//NumberFormatAPI formatter = new NumberFormatAPI();
		//String custo = formatter.formatNumber(evento.getRecompensa());
		String vencedorNome = p.getName();
		return msg.replace("{prefix}", prefix)
		.replace("{tempo}", String.valueOf(evento.getTempo()))
		.replace("{participantes}", String.valueOf(evento.getParticipantes().size()))
	//	.replace("{premio}", custo)
	//	.replace("{recompensa}", String.valueOf(evento.getRecompensa()))
		.replace("{moeda_icon}", moeda)
		.replace("{seta}", seta)
		.replace("{time}", time)
		.replace("{prefix}", prefix)
		.replace("{vencedor}", vencedorNome)
	//	.replace("{tempo_lobby}", String.valueOf(evento.getTempoLobby()))
		.replace("{max_player}", String.valueOf(maxPlayer));
	}
	
}
