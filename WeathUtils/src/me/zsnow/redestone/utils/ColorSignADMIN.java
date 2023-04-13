package me.zsnow.redestone.utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class ColorSignADMIN implements Listener {

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void aoUsarPlaca(SignChangeEvent e) {
		if (e.getPlayer().hasPermission("zs.admin")) {
			for (int i = 0; i < e.getLines().length; i++) {
				e.setLine(i, e.getLine(i).replace('&', '§'));
			}
		}
	}
}