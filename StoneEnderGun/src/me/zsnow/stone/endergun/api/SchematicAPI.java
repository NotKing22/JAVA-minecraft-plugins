package me.zsnow.stone.endergun.api;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;

import me.zsnow.stone.endergun.configs.Configs;

@SuppressWarnings("deprecation")
public class SchematicAPI {
	
	private static String schematicName = Configs.config.getConfig().getString("Schematic-file-name");
	
	public static void loadSchematic() {
		if (Configs.locations.getConfig().contains("SCHEMATIC-LOC")) {
			 double x = Configs.locations.getConfig().getDouble("SCHEMATIC-LOC.X");
		     double y = Configs.locations.getConfig().getDouble("SCHEMATIC-LOC.Y");
		   	 double z = Configs.locations.getConfig().getDouble("SCHEMATIC-LOC.Z");
		   	 World world = Bukkit.getWorld(Configs.locations.getConfig().getString("SCHEMATIC-LOC.Mundo"));
			 WorldEditPlugin worldEditPlugin = (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
			File schematic = new File(worldEditPlugin.getDataFolder() + File.separator + "schematics/" + schematicName + ".schematic");
			 EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(world), 1000000);
			 try {
				CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
				clipboard.rotate2D(0);
				clipboard.paste(session, new Vector(x, y, z), false); // true = no air
			} catch (MaxChangedBlocksException | DataException | IOException e) {
				e.printStackTrace();
				Bukkit.getConsoleSender().sendMessage("§c[StoneEnderGun] Houve um erro ao colar a Schematic.");
			}
		} else {
			Bukkit.getConsoleSender().sendMessage("§cA schematic nao teve sua localizacao definida.");
		}
	}

}
