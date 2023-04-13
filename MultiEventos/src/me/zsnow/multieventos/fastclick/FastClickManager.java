package me.zsnow.multieventos.fastclick;

import java.text.DecimalFormat;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.zsnow.multieventos.API.RandomStringGenerator;
import me.zsnow.multieventos.API.RandomStringGenerator.Mode;
import me.zsnow.multieventos.configAPI.Configs;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class FastClickManager {
	
	public static FastClickManager evento = new FastClickManager();
	private String colorDefault = "§7§l";
	private String colorCerta = "§7§l";
	private String iconDefault = colorDefault + " [X] ";
	private String iconCerto = colorCerta + " [X] " + colorDefault;
	
	private String iconCertoTroll = " §a§l[X] " + colorDefault;
	private String iconCertoTroll2 = colorCerta + " [X] " + colorDefault;
	/*
	 * ERRO PREGUIÇA DE ARRUMAR
	 * private String colorDefault = Configs.config.getConfig().getString(ChatColor.translateAlternateColorCodes('&', "color-default"));
	private String colorCerta = Configs.config.getConfig().getString(ChatColor.translateAlternateColorCodes('&', "color-correta"));
	private String iconDefault = Configs.config.getConfig().getString(ChatColor.translateAlternateColorCodes('&', colorDefault + "icon-default"));
	private String iconCerto = Configs.config.getConfig().getString(ChatColor.translateAlternateColorCodes('&', colorCerta + " " + iconDefault + " " + colorDefault));
	*/
	private String key;
	
	private boolean ocorrendo;
	private double segundos;
	private int premio = Configs.config.getConfig().getInt("premio-fastclick");
	private boolean troll1;
	private boolean troll2;
	
	public void Stop() {
		troll1 = false;
		troll2 =  false;
		ocorrendo = false;
		key = null;
		setSegundos(0.0);
	}
	
	public boolean getTroll() {
		return this.troll1;
	}
	
	public void setTrollStatus(boolean booleanValue) {
		this.troll1 = booleanValue;
	}
	
	public boolean getTroll2() {
		return this.troll2;
	}
	
	public void setTroll2Status(boolean booleanValue) {
		this.troll2 = booleanValue;
	}
	
	public void setSegundos(double tempo) {
		this.segundos = tempo;
	}
	
	public Double getSegundos() {
		return segundos;
	}
	
	public String getKey() {
		return key;
	}
	
	public void embaralharChave() {
		this.key = RandomStringGenerator.getRandomString(20, Mode.APLHA);
	}
	
	public void deletKey() {
		this.key = null;
	}
	
	public Integer getPremio() {
		return premio;
	}
	
	public void setOcorrendo(boolean booleanValue) {
		this.ocorrendo = booleanValue;
	}
	
	public Boolean getOcorrendo() {
		return this.ocorrendo;
	}
	
	
	// gere um arg aleatorio para vencer se precisar de cmd
	
	public void sendFastClickMessage() {
		embaralharChave();
		DecimalFormat df = new DecimalFormat("###,###,###,###,###.##");
		String valorMoney = df.format(FastClickManager.evento.getPremio());
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			    player.sendMessage(" ");
		        player.sendMessage("§e§lFastClick §7➜  §fSeja o primeiro a clicar no "+ iconDefault +" §fcorreto e §eganhe " + valorMoney + " de coins.");
		        player.sendMessage(" ");

		        TextComponent json = new TextComponent(iconDefault);
		        TextComponent jsonCerto = new TextComponent(iconCerto);
		        jsonCerto.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fastclick ganhar " + getKey()));
		        
		        jsonCerto.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eClique no correto.").create()));
		        json.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClique no correto.").create()));
		        Random random = new Random();
		        int certo = random.nextInt((28 - 1) + 1);
		        for (int quantidade = 0; quantidade < 29; quantidade++) {
		        	if (quantidade == certo) {
			        	json.addExtra(jsonCerto);
		        	}
		        	json.addExtra(iconDefault);
		        	if (quantidade == 7 || quantidade == 14 || quantidade == 21 || quantidade == 28) {
			        	json.addExtra(iconDefault + "\n");
		        	} 
		        }
		        player.spigot().sendMessage(json);
		        player.sendMessage(" ");

		}
	}
	
	public void sendFastClickMessage2() {
		embaralharChave();
		DecimalFormat df = new DecimalFormat("###,###,###,###,###.##");
		String valorMoney = df.format(FastClickManager.evento.getPremio());
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			    player.sendMessage(" ");
		        player.sendMessage("§e§lFastClick §7➜  §fSeja o primeiro a clicar no §a§l"+ iconDefault +" §fcorreto e §eganhe " + valorMoney + " de coins.");
		        player.sendMessage(" ");

		        TextComponent json = new TextComponent(iconDefault);
		        TextComponent jsonCerto = new TextComponent(iconCertoTroll);
		        jsonCerto.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fastclick ganhar " + getKey()));
		        
		        jsonCerto.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eClique no correto.").create()));
		        json.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClique no correto.").create()));
		        Random random = new Random();
		        int certo = random.nextInt((28 - 1) + 1);
		        for (int quantidade = 0; quantidade < 29; quantidade++) {
		        	if (quantidade == certo) {
			        	json.addExtra(jsonCerto);
		        	}
		        	json.addExtra(iconDefault);
		        	if (quantidade == 7 || quantidade == 14 || quantidade == 21 || quantidade == 28) {
			        	json.addExtra(iconDefault + "\n");
		        	} 
		        }
		        player.spigot().sendMessage(json);
		        player.sendMessage(" ");

		}
	}
	
	public void sendFastClickMessageTroll() {
		embaralharChave();
		DecimalFormat df = new DecimalFormat("###,###,###,###,###.##");
		String valorMoney = df.format(FastClickManager.evento.getPremio());
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			    player.sendMessage(" ");
		        player.sendMessage("§e§lFastClick §7➜  §fSeja o primeiro a clicar no §a§l" + iconDefault +" §fcorreto e §eganhe §c-§e" + valorMoney + " de coins.");
		        player.sendMessage(" ");

		        TextComponent json = new TextComponent(iconDefault);
		        TextComponent jsonCerto = new TextComponent(iconCertoTroll);
		        jsonCerto.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fastclick ganhar " + getKey()));
		        
		        jsonCerto.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eClique e perca coins.").create()));
		        json.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClique no correto.").create()));
		        Random random = new Random();
		        int certo = random.nextInt((28 - 1) + 1);
		        for (int quantidade = 0; quantidade < 29; quantidade++) {
		        	if (quantidade == certo) {
			        	json.addExtra(jsonCerto);
		        	}
		        	json.addExtra(iconDefault);
		        	if (quantidade == 7 || quantidade == 14 || quantidade == 21 || quantidade == 28) {
			        	json.addExtra(iconDefault + "\n");
		        	} 
		        }
		        player.spigot().sendMessage(json);
		        player.sendMessage(" ");

		}
	}
	
	public void sendFastClickMessageTroll2() {
		embaralharChave();
		DecimalFormat df = new DecimalFormat("###,###,###,###,###.##");
		String valorMoney = df.format(FastClickManager.evento.getPremio());
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			    player.sendMessage(" ");
		        player.sendMessage("§e§lFastClick §7➜  §fSeja o primeiro a clicar no "+ iconDefault +" §fcorreto e §eperca §e" + valorMoney + " de coins.");
		        player.sendMessage(" ");

		        TextComponent json = new TextComponent(iconDefault);
		        TextComponent jsonCerto = new TextComponent(iconCertoTroll2);
		        jsonCerto.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fastclick ganhar " + getKey()));
		        
		        jsonCerto.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eClique e perca coins.").create()));
		        json.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClique no correto.").create()));
		        Random random = new Random();
		        int certo = random.nextInt((28 - 1) + 1);
		        for (int quantidade = 0; quantidade < 29; quantidade++) {
		        	if (quantidade == certo) {
			        	json.addExtra(jsonCerto);
		        	}
		        	json.addExtra(iconDefault);
		        	if (quantidade == 7 || quantidade == 14 || quantidade == 21 || quantidade == 28) {
			        	json.addExtra(iconDefault + "\n");
		        	} 
		        }
		        player.spigot().sendMessage(json);
		        player.sendMessage(" ");

		}
	}
}
