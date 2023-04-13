package me.zsnow.stone.guerreiro.manager;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class EventManager {

	public static EventManager manager = new EventManager();
	
	private int tempo;
	private boolean pvpStatus;
	private boolean entradaLiberada;
	private boolean eventoOcorrendo;
	private ArrayList<Player> participantes = new ArrayList<>();
	
	public int getTempo() {
		return this.tempo;
	}
	
	public boolean getEntradaLiberada() {
		return this.entradaLiberada;
	}
	
	public boolean getEventoOcorrendo() {
		return this.eventoOcorrendo;
	}
	
	public ArrayList<Player> getParticipantes() {
		return this.participantes;
	}
	
	public boolean getPvPStatus() {
		return this.pvpStatus;
	}
	
	//
	
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	
	public void setEntradaLiberada(boolean booleanValue) {
		this.entradaLiberada = booleanValue;
	}
	
	public void setEventoOcorrendo(boolean booleanValue) {
		this.eventoOcorrendo = booleanValue;
	}
	
	public void setPvPStatus(boolean booleanValue) {
		this.pvpStatus = booleanValue;
	}
	
	public void sendTo(Player p, String location) {
		location = location.toUpperCase(); //adicionei
		double X = Configs.locations.getConfig().getDouble(location + ".X");
        double Y = Configs.locations.getConfig().getDouble(location + ".Y");
        double Z = Configs.locations.getConfig().getDouble(location + ".Z");
        float Yaw = (float)Configs.locations.getConfig().getLong(location + ".Yaw");
        float Pitch = (float)Configs.locations.getConfig().getLong(location + ".Pitch");
	    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
		p.teleport(loc);
	}
	
	
	public void setLoc(Player p, String LocationName) {
		LocationName = LocationName.toUpperCase();
	    double x = p.getLocation().getBlockX();
    	double y = p.getLocation().getBlockY();
		double z = p.getLocation().getBlockZ();
		float yaw = p.getLocation().getYaw();
		float pitch = p.getLocation().getPitch();
		String world = p.getLocation().getWorld().getName().toString();
		Configs.locations.getConfig().set(LocationName + ".X", Double.valueOf(x));
		Configs.locations.getConfig().set(LocationName + ".Y", Double.valueOf(y));
		Configs.locations.getConfig().set(LocationName + ".Z", Double.valueOf(z));
		Configs.locations.getConfig().set(LocationName + ".Yaw", Float.valueOf(yaw));
		Configs.locations.getConfig().set(LocationName + ".Pitch", Float.valueOf(pitch));
		Configs.locations.getConfig().set(LocationName + ".Mundo", world);
		Configs.locations.saveConfig();
	}
	
	public static SimpleClans getSC() {
		return SimpleClans.getInstance();
	}
	  
	public void enableClanDamage(Player p) {
		if (getSC().getClanManager().getClanPlayer(p) != null)
			getSC().getClanManager().getClanPlayer(p).setFriendlyFire(true); 
	}
	  
	public void disableClanDamage(Player p) {
		if (getSC().getClanManager().getClanPlayer(p) != null)
			getSC().getClanManager().getClanPlayer(p).setFriendlyFire(false); 
	}
}
