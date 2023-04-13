package me.zsnow.redestone.api;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.zsnow.redestone.config.Configs;

import java.util.Map;

public class LocationAPI {

    public static LocationAPI location = new LocationAPI();

    public static LocationAPI getLocation() {
        return location;
    }

    // POS 2 RETORNA ERRO

    public enum location {
        ENTRADA,
        SAIDA,
        MODERAR,
        CAMAROTE,
        POS1,
        POS2
    }

    private Map<String, Location> locationMap = Maps.newConcurrentMap();

    public String teleportTo(final Player p, location location) {
        String local = location.toString();

        Location loc = locationMap.getOrDefault(local, null);

        if (loc == null) {
            double X, Y, Z;
            float Yaw, Pitch;
            X = Configs.locations.getConfig().getDouble(local + ".X");
            Y = Configs.locations.getConfig().getDouble(local + ".Y");
            Z = Configs.locations.getConfig().getDouble(local + ".Z");
            Yaw = (float) Configs.locations.getConfig().getLong(local + ".Yaw");
            Pitch = (float) Configs.locations.getConfig().getLong(local + ".Pitch");
            World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(local + ".Mundo"));

            loc = new Location(Mundo, X, Y, Z, Yaw, Pitch).add(0.5, 0.0, 0.5);
            locationMap.put(local, loc);
        }

        switch (location) {
            case ENTRADA:
                local = "ENTRADA";
                p.teleport(loc);
                break;
            case SAIDA:
                local = "SAIDA";
                p.teleport(loc);
                break;
            case MODERAR:
                local = "MODERAR";
                p.teleport(loc);
                break;
            case CAMAROTE:
                local = "CAMAROTE";
                p.teleport(loc);
                break;
            case POS1:
                local = "POS1";
                p.teleport(loc);
                break;
            case POS2:
                local = "POS2";
                p.teleport(loc);
                break;
            default:
                throw new IllegalArgumentException("A localidade informada nao pode ser encontrada: " + local);
        }
        return local;
    }

    public String setLocation(final Player p, location location) {
        String local = null;
        double x = p.getLocation().getBlockX();
        double y = p.getLocation().getBlockY();
        double z = p.getLocation().getBlockZ();
        float yaw = p.getLocation().getYaw();
        float pitch = p.getLocation().getPitch();
        String world = p.getLocation().getWorld().getName().toString();

        switch (location) {
            case ENTRADA:
                local = "ENTRADA";
                Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
                Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
                Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
                Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
                Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
                Configs.locations.getConfig().set(local + ".Mundo", world);
                Configs.locations.saveConfig();
                break;
            case SAIDA:
                local = "SAIDA";
                Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
                Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
                Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
                Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
                Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
                Configs.locations.getConfig().set(local + ".Mundo", world);
                Configs.locations.saveConfig();
                break;
            case MODERAR:
                local = "MODERAR";
                Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
                Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
                Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
                Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
                Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
                Configs.locations.getConfig().set(local + ".Mundo", world);
                Configs.locations.saveConfig();
                break;
            case CAMAROTE:
                local = "CAMAROTE";
                Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
                Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
                Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
                Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
                Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
                Configs.locations.getConfig().set(local + ".Mundo", world);
                Configs.locations.saveConfig();
                break;
            case POS1:
                local = "POS1";
                Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
                Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
                Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
                Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
                Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
                Configs.locations.getConfig().set(local + ".Mundo", world);
                Configs.locations.saveConfig();
                break;
            case POS2:
                local = "POS2";
                Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
                Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
                Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
                Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
                Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
                Configs.locations.getConfig().set(local + ".Mundo", world);
                Configs.locations.saveConfig();
                break;
            default:
                throw new IllegalArgumentException("A localidade informada nao pode ser encontrada: " + local);
        }

        locationMap.remove(local);

        return local;
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
