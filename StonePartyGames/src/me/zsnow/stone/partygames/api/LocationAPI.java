package me.zsnow.stone.partygames.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.zsnow.stone.partygames.configs.ConfigManager;
import me.zsnow.stone.partygames.configs.Configs;

public class LocationAPI {

	public static LocationAPI location = new LocationAPI();
	
	public static LocationAPI getLocation() {
		return location;
	}
	
	public enum location {
		CRAZYPOOL_ENTRADA, CRAZYPOOL_SAIDA, SCHEM_VIDRO, CRAZYPOOL_AGUA_1, CRAZYPOOL_AGUA_2, 
	}

	
	public String teleportTo(final Player p, location location, ConfigManager gameLocConfig) {
		String local = location.toString();
		double X,Y,Z;
		float Yaw,Pitch;
			X = gameLocConfig.getConfig().getDouble(local + ".X");
	        Y = gameLocConfig.getConfig().getDouble(local + ".Y");
	        Z = gameLocConfig.getConfig().getDouble(local + ".Z");
	        Yaw = (float)gameLocConfig.getConfig().getLong(local + ".Yaw");
	        Pitch = (float)gameLocConfig.getConfig().getLong(local + ".Pitch");
		    World Mundo = Bukkit.getWorld(gameLocConfig.getConfig().getString(local + ".Mundo")); // MUNDO NULL?
		  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch).add(0.5,0.0,0.5);
			
		switch (location) {
			case CRAZYPOOL_ENTRADA:
				local = "CRAZYPOOL_ENTRADA";
					p.teleport(loc);
					break;
			case CRAZYPOOL_SAIDA:
				local = "CRAZYPOOL_SAIDA";
					p.teleport(loc);
					break;
			default:
				throw new IllegalArgumentException("A localidade informada nao pode ser encontrada: " + local);
			}
		return local;
		}
	
	public enum getMinigameType {
	   CRAZYPOOL,
	   BABIDI
	}
	
	public void setLocation(final Player p, getMinigameType getMinigameType, location location) {
		String local = null;
		    double x = p.getLocation().getBlockX();
	    	double y = p.getLocation().getBlockY();
			double z = p.getLocation().getBlockZ();
			float yaw = p.getLocation().getYaw();
			float pitch = p.getLocation().getPitch();
			String world = p.getLocation().getWorld().getName().toString();
				switch (getMinigameType) { // ERRO AQUI DIZ QUE O MINIGAME N EXISTE
					case CRAZYPOOL:
						switch (location) {
							case CRAZYPOOL_ENTRADA: 
								local = "CRAZYPOOL_ENTRADA";
								setConfigLoc(local, Configs.crazypool_locations, x, y, z, yaw, pitch, world);
								break;
							
							case CRAZYPOOL_SAIDA: 
								local = "CRAZYPOOL_SAIDA";
								setConfigLoc(local, Configs.crazypool_locations, x, y, z, yaw, pitch, world);
								break;
							
							case SCHEM_VIDRO: 
								local = "SCHEM_VIDRO";
								setConfigLoc(local, Configs.crazypool_locations, x, y, z, yaw, pitch, world);
								break;
							
							case CRAZYPOOL_AGUA_1: 
								local = "CRAZYPOOL_AGUA_1";
								setConfigLoc(local, Configs.crazypool_locations, x, y, z, yaw, pitch, world);
								break;
							
							case CRAZYPOOL_AGUA_2: 
								local = "CRAZYPOOL_AGUA_2";
								setConfigLoc(local, Configs.crazypool_locations, x, y, z, yaw, pitch, world);
								break;
							default:
								throw new IllegalArgumentException("Unexpected value, setlocation api");
							}
					default:
						//throw new IllegalArgumentException("MinigameType nao foi localizado em LocationAPI:  " + getMinigameType);
					}
		}
	
	private void setConfigLoc(String local, ConfigManager gameLocConfig, double x, double y, double z, float yaw, float pitch, String world) {
		gameLocConfig.getConfig().set(local + ".X", Double.valueOf(x));
		gameLocConfig.getConfig().set(local + ".Y", Double.valueOf(y));
		gameLocConfig.getConfig().set(local + ".Z", Double.valueOf(z));
		gameLocConfig.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
		gameLocConfig.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
		gameLocConfig.getConfig().set(local + ".Mundo", world);
		gameLocConfig.saveConfig();
	}
	
	/*public void teleportTo(Player p, String location) {
		location = location.toUpperCase(); //adicionei
		double X = Configs.locations.getConfig().getDouble(location + ".X");
        double Y = Configs.locations.getConfig().getDouble(location + ".Y");
        double Z = Configs.locations.getConfig().getDouble(location + ".Z");
        float Yaw = (float)Configs.locations.getConfig().getLong(location + ".Yaw");
        float Pitch = (float)Configs.locations.getConfig().getLong(location + ".Pitch");
	    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch).add(0.5,0.0,0.5);
		p.teleport(loc);
	}
	
	
	public void setLocation(Player p, String LocationName) {
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
	}*/
	
}
