package br.com.zsnow.eventocustom;

import net.milkbowl.vault.chat.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;

public class VaultHook {
    protected static Chat chat;
    
    @SuppressWarnings("deprecation")
	public static String getPlayerPrefix(final String name) {
        final Player player = Bukkit.getPlayer(name);

        if (player != null) {
            return ChatColor.translateAlternateColorCodes('&', VaultHook.chat.getPlayerPrefix(player));
        }
        return ChatColor.translateAlternateColorCodes('&', VaultHook.chat.getPlayerPrefix("world", name));
    }

    public static String getGroup(final String name) {
        String resetColor = ChatColor.stripColor(getPlayerPrefix(name));

        return resetColor.replace(" ", "").replace("[", "").replace("]", "");
    }

    @SuppressWarnings("deprecation")
	public static String getPlayerSuffix(final String name) {
        final Player player = Bukkit.getPlayer(name);

        if (player != null) {
            return ChatColor.translateAlternateColorCodes('&', VaultHook.chat.getPlayerSuffix(player));
        }
        return ChatColor.translateAlternateColorCodes('&', VaultHook.chat.getPlayerSuffix("world", name));
    }
    
    public static void setupChat() {
        final RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            return;
        }
        VaultHook.chat = rsp.getProvider();
    }
    
    static {
        setupChat();
    }
}

