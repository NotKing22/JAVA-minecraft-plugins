package me.zsnow.stone.altar;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("altar")) {
			if (sender.hasPermission("altar.gerente")) {
				if (args.length == 0) {
					sender.sendMessage("§6§lSTONE ALTAR §e- §7(Comandos)");
					sender.sendMessage(" ");
					sender.sendMessage(" §f/altar <create>"); //
					sender.sendMessage(" §f/altar <ver> <ID>"); // 
					sender.sendMessage(" §f/altar <spawn> <ID> [-s]");
					sender.sendMessage(" §c/altar <remove> <ID>"); 
					sender.sendMessage(" ");
					return true;
				}
				Player p = (Player) sender;
				Altar_Model altar = Altar_Model.getInstance();
					if (args[0].equalsIgnoreCase("ver")) {
						if (args.length == 1) {
							sender.sendMessage("§cSintaxe: /altar ver <ID>");
							return true;
						}
						if (args.length == 2) {
							try {
								int ID = Integer.parseInt(args[1]);
								if (Configs.altar.getConfigurationSection("ALTAR.") == null || Configs.config.getStringList("ALTAR." + ID) == null) {
									p.sendMessage("§c[StoneAltar] O arquivo de configuração está invalido ou não existe um atlar com esse ID #" + ID); 
									return true;
								}
								altar.openPanel(p, ID);
								return true;
							} catch (NumberFormatException e) {
								p.sendMessage("§cInsira um ID válido.");
								return true;
							}
						}
					}
				if (args[0].equalsIgnoreCase("spawn")) {
					if (args.length == 1) {
						sender.sendMessage("§cSintaxe: /altar spawn <ID>.");
						return true;
					}
					if (args.length >= 2) {
						try {
							int ID = Integer.parseInt(args[1]);
							if (!Altar_Model.altar_hp.containsKey(ID)) {
								altar.spawnAltar(ID);
								if (args.length == 3 && args[2].equalsIgnoreCase("-s")) {
									p.sendMessage("§eO altar #" + ID + " §efoi revivido silenciosamente.");
									return true;
								}
								for (String msg : Configs.config.getStringList("altar-spawn")) {
									Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
								}
							} else {
								p.sendMessage("§cEsse altar já está vivo.");
								return true;
							}
						} catch (NumberFormatException e) {
							p.sendMessage("§cInsira um ID válido.");
							return true;
						}
					}
				}
				if (args[0].equalsIgnoreCase("remover")) {
					if (args.length == 1) {
						sender.sendMessage("§cSintade: /altar remover <ID>.");
						return true;
					}
					if (args.length == 2) {
						try {
							int ID = Integer.parseInt(args[1]);
								if (Altar_Model.altar_hp.containsKey(ID)) {
									altar.deleteAltarCurrentMap(ID);
									altar.removeHologramByID(ID);
									for (World world : Bukkit.getWorlds()) {
										for (Entity entities : world.getEntities()) {
											if (entities.hasMetadata("altar_id") && entities.getType() == EntityType.ENDER_CRYSTAL) {
											
												int metadata_ID = entities.getMetadata("altar_id").get(0).asInt();
												
												if (metadata_ID == ID) {
													entities.remove();
													p.sendMessage("§aAltar #" + ID + " §aremovido.");
													return true;
												}
											}
										}
									}
							    	p.sendMessage("§cNenhum altar localizado com o ID informado.");
									return true;
								} else {
									p.sendMessage("§cEsse altar não está vivo.");
									return true;
								}
						} catch (NumberFormatException e) {
							p.sendMessage("§cInsira um ID válido.");
							return true;
						}
					}
				}

					if (args[0].equalsIgnoreCase("create")) {
						Location loc = p.getLocation();
						int showID = 0;
						if (Configs.altar.getConfigurationSection("ALTAR.") == null) {
							int ID = 1;
							Configs.altar.setHeader(" "
									+ "ENTRE NO SERVER E DIGITE '/altar create' nos locais do altar. Sempre de reload apos alguma alteracao tambem \n "
									+ " \n "
									+ " **COMO MATAR:** Clique com um machado de madeira no altar para matar instantaneamente. \n"
									+ " \n"
									+ " clique com botao direito em um altar para abrir o painel. \n"
									+ " ");
							Configs.altar.set("ALTAR." + ID + ".name", Arrays.asList("&d&lALTAR MISTICO", "  &6&linsert name ", "&c&lCONTRA-ATAQUE ATIVO"));
							Configs.altar.set("ALTAR." + ID + ".hp", 100.0D);
							Configs.altar.set("ALTAR." + ID + ".schedule-time", true);
							Configs.altar.set("ALTAR." + ID + ".spawn-at", Arrays.asList(new String[] {"12:00:00"}));
							Configs.altar.set("ALTAR." + ID + ".coord", "" + loc.getX() + "@" + loc.getY() + "@" + loc.getZ() + "@" + loc.getWorld().getName() + "");
							Configs.altar.set("ALTAR." + ID + ".enable-contra-ataque", true);
							Configs.altar.set("ALTAR." + ID + ".drops", "");

							Configs.altar.saveConfig();
							Configs.altar.reloadConfig();
							sender.sendMessage("§a[StoneAltar] Altar com ID §f#0" + ID + " §afoi gerado. Configure-o na altar.yml!");
							return true;
						}
						for (String ALTAR_ID : Configs.altar.getConfigurationSection("ALTAR.").getKeys(false)) {
							try {
								int ID = Integer.parseInt(ALTAR_ID);
								ID++;
								Configs.altar.set("ALTAR." + ID + ".name", Arrays.asList("&d&lALTAR MISTICO", "  &6&linsert name ", "&c&lCONTRA-ATAQUE ATIVO"));
								Configs.altar.set("ALTAR." + ID + ".hp", 100.0D);
								Configs.altar.set("ALTAR." + ID + ".schedule-time", true);
								Configs.altar.set("ALTAR." + ID + ".spawn-at", Arrays.asList(new String[] {"12:00:00"}));
								Configs.altar.set("ALTAR." + ID + ".coord", "" + loc.getX() + "@" + loc.getY() + "@" + loc.getZ() + "@" + loc.getWorld().getName() + "");
								Configs.altar.set("ALTAR." + ID + ".enable-contra-ataque", true);
								Configs.altar.set("ALTAR." + ID + ".drops", "");
							
								Configs.altar.saveConfig();
								Configs.altar.reloadConfig();
								showID = ID;
							} catch (NumberFormatException e) {
								sender.sendMessage("§cHouve um erro ao gerar o altar. Consulte a altar.yml");
								return true;
							}
						}
						sender.sendMessage("§a[StoneAltar] Altar com ID §f#0" + showID + " §afoi gerado. Configure-o na altar.yml!");
						return true;
					}
				}
			
			}
	return false;
}
	
    
}
