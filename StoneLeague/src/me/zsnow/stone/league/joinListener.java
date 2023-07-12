package me.zsnow.stone.league;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class joinListener implements Listener {

	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		LeagueCache cache = LeagueCache.getInstance();
		if (!cache.hasLeague(e.getPlayer())) {
			League league = League.UNRANKED;
			cache.setLeague(e.getPlayer().getName(), league);
			e.getPlayer().sendMessage(" ");
			e.getPlayer().sendMessage("§b⚒ §eVocê recebeu a liga §6§lUNRANKED§e. Elimine jogadores para desbloquear recompensas ao melhorar sua liga.");
			e.getPlayer().sendMessage(" ");
			return;
		}
	}
	
}
