package me.zsnow.round6.api;

import org.bukkit.ChatColor;

import me.zsnow.round6.configs.Configs;
import me.zsnow.round6.manager.PedestreClass;
import me.zsnow.round6.manager.SemaforoClass;

public class StringReplaceAPI {

	 private static String prefix = ChatColor.translateAlternateColorCodes('&', Configs.config.getString("prefix"));
		final private static int maxPlayer = Configs.config.getConfig().getInt("maximo-de-jogadores");
		final private static String moeda = "⛃";
		final private static String seta = "➜";
	
	public static String replaceMsg(String msg) {
		SemaforoClass evento = SemaforoClass.getSemaforo();
		PedestreClass members = PedestreClass.getInstance();
		NumberFormatAPI formatter = new NumberFormatAPI();
		String custo = formatter.formatNumber(evento.getRecompensa());
		String vencedorNome = members.getPedestres().size() != 0 ? members.getPedestres().get(0).getName() : "";
		return msg.replace("{prefix}", prefix)
		.replace("{tempo}", String.valueOf(evento.getTempoEntrada()))
		.replace("{participantes}", String.valueOf(members.getPedestres().size()))
		.replace("{premio}", custo)
		.replace("{recompensa}", String.valueOf(evento.getRecompensa()))
		.replace("{moeda_icon}", moeda)
		.replace("{seta}", seta)
		.replace("{vencedor}", vencedorNome)
		.replace("{tempo_lobby}", String.valueOf(evento.getTempoLobby()))
		.replace("{max_player}", String.valueOf(maxPlayer));
	}
	
}
