package me.zsnow.redestone.listeners;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Lists;

import me.zsnow.redestone.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.zsnow.redestone.api.LocationAPI;
import me.zsnow.redestone.api.LocationAPI.loc_sumo;
import me.zsnow.redestone.api.LocationAPI.location;
import me.zsnow.redestone.api.NBTItemStack;
import me.zsnow.redestone.api.NumberFormatAPI;
import me.zsnow.redestone.cache.DuelCache;
import me.zsnow.redestone.config.Configs;
import me.zsnow.redestone.itemmethods.InventoryUtils;
import me.zsnow.redestone.itemmethods.ItemBuilder;
import me.zsnow.redestone.manager.DuelManager;
import me.zsnow.redestone.manager.InviteManager;
import me.zsnow.redestone.manager.SumoDuelManager;
import me.zsnow.redestone.manager.SumoInviteManager;
import me.zsnow.redestone.mysql.ConexaoSQL;
import me.zsnow.redestone.mysql.MysqlMethods;
import net.md_5.bungee.api.ChatColor;

public class MenuListeners extends ConexaoSQL implements Listener {


    final static String storage_name = "§eArmazém de itens";
    final static String leaderboard_name = "§bLeaderboard";
    final static String camarote_name = "§eCamarote";
    final static String duelItem_name = "§aDesafiar 1v1 PvP!";
    final static String sumoDuelItem_name = "§bDesafiar 1v1 Sumo! §e§l(NOVO)";
    final static String barrinha = "│";
    final static String kbSelection = "§6Customize o KnockBack";
    final static String potSelection = "§6Customize os efeitos";
    final static String arenaSelection = "§6Escolha a arena";
    //final static String getArrowUP = "§§§b";
    //final static String getArrowDown = "§§§a";

    final static String storagemenu_name = "§b§lDUELO §fSeu armazém";
    final static String inventory_name = "§b§lDUELO §fDesafie um jogador";
    final static String topmenu_name = "§b§lDUELO §fTop 9 jogadores";
    final static String menuSelection = "§b§lDUELO §fSumo Configurações";
    final static String duelos_mods_name = "Visualizando duelos | Moderação";

    public ArrayList<Player> chatEventPvP = new ArrayList<>();
    public ArrayList<Player> chatEventSumo = new ArrayList<>();

	@EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        
        // DEVIA ADAPTAR PARA SWITCH
        
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().equals(inventory_name) ||
                e.getInventory().getTitle().equals(topmenu_name) ||
            	e.getInventory().getTitle().equals(storagemenu_name) ||
        		e.getInventory().getTitle().equals(menuSelection) || 
    			e.getInventory().getTitle().equals(duelos_mods_name)) {
				e.setCancelled(true);
            //e.setResult(Result.DENY);
           SumoDuelManager sumoManager = SumoDuelManager.getInstance();
            ItemStack currentItem = e.getCurrentItem();
            if (currentItem != null && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName() && currentItem.getItemMeta().hasLore()) {
                if (currentItem.isSimilar(getTopItemStack())) {
                    openTopMenu(p);
                    p.playSound(p.getLocation(), Sound.HORSE_SADDLE, 1.0f, 0.5f);
                    return;
                }
                if (currentItem.isSimilar(getStorageItem())) {
                    openStorageMenu(p);
                    return;
                }
                if (currentItem.isSimilar(new ItemBuilder(Material.STORAGE_MINECART).displayname("§aColetar").lore("§7Clique para coletar seus itens.").build())) {
                    p.playSound(p.getLocation(), Sound.HORSE_SADDLE, 1.0f, 0.5f);
                    updateStorageMenu(p);
                    return;
                }
                if (currentItem.getItemMeta().getDisplayName().equals(camarote_name)) {
                    if (!DuelManager.getInstance().getCamarotePlayers().contains(p)) {
                        LocationAPI.getLocation().teleportTo(p, location.CAMAROTE);
                        DuelManager.getInstance().getCamarotePlayers().add(p);
                        p.sendMessage("§aVocê foi enviado para o camarote!");
                        return;
                    } else {
                        LocationAPI.getLocation().teleportTo(p, location.SAIDA);
                        DuelManager.getInstance().getCamarotePlayers().remove(p);
                        p.sendMessage("§aVocê foi enviado para a saída do camarote!");
                        return;
                    }
                }
                if (currentItem.getItemMeta().getDisplayName().equals(duelItem_name)) {
                    if (DuelManager.getInstance().getManutencaoStatus() == false) {
                        if (!chatEventPvP.contains(p)) {
                            if (InviteManager.getInstance().canInvite()) {
                                chatEventPvP.add(p);
                                p.closeInventory();
                                p.sendMessage(" ");
                                p.sendMessage(" §aDigite o nickname do jogador que deseja desafiar no chat.");
                                p.sendMessage(" §aPara cancelar esta ação digite §7'cancelar' §ano §nchat local§a.");
                                p.sendMessage(" ");
                                return;
                            } else {
                                p.sendMessage("§cJá há 2 jogadores na fila de duelo. Aguarde que o pedido expire ou seja aceito por um deles para poder enviar novamente.");
                                return;
                            }
                        } else {
                            p.closeInventory();
                            p.sendMessage("");
                            p.sendMessage(" §cVocê já está tentando desafiar um jogador.");
                            p.sendMessage(" §cCaso queira cancelar esta ação, digite §7'cancelar' §cno §nchat local§a.");
                            p.sendMessage("");
                            return;
                        }
                    } else {
                        p.sendMessage("§cO sistema de duelos está em manutenção. Volte novamente mais tarde!");
                        p.closeInventory();
                        return;
                    }
                }
                if (currentItem.getItemMeta().getDisplayName().equals(kbSelection)) {
                	p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
                    switch (getDataInfo(p).getKB()) {
						case 0:
							getDataInfo(p).setKB(getDataInfo(p).getKB()+1);
							break;
						case 1:
							getDataInfo(p).setKB(getDataInfo(p).getKB()+1);
							break;
						case 2:
							getDataInfo(p).setKB(getDataInfo(p).getKB()+1);
							break;
						default:
							getDataInfo(p).setKB(0);
							break;
						}
	                e.getInventory().setItem(e.getSlot(), updateKBselection(getDataInfo(p).getKB()));
	                p.updateInventory();
	                return;
                }
                if (currentItem.getItemMeta().getDisplayName().equals(potSelection)) {
                	p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
                    switch (getDataInfo(p).getPotLvl()) {
						case 0:
							getDataInfo(p).setPotLvl(getDataInfo(p).getPotLvl()+1);
							break;
						case 1:
							getDataInfo(p).setPotLvl(getDataInfo(p).getPotLvl()+1);
							break;
						case 2:
							getDataInfo(p).setPotLvl(getDataInfo(p).getPotLvl()+1);
							break;
						default:
							getDataInfo(p).setPotLvl(0);
							break;
						}
	                e.getInventory().setItem(e.getSlot(), updatePotselection(getDataInfo(p).getPotLvl()));
	                p.updateInventory();
	                return;
                }
                if (currentItem.getItemMeta().getDisplayName().equals(arenaSelection)) {
                	p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
                    switch (getDataInfo(p).getArena()) {
						case 0:
							getDataInfo(p).setArena(getDataInfo(p).getArena()+1);
							break;
						case 1:
							getDataInfo(p).setArena(getDataInfo(p).getArena()+1);
							break;
						default:
							getDataInfo(p).setArena(0);
							break;
						}
	                e.getInventory().setItem(e.getSlot(), updateArenaSelection(getDataInfo(p).getArena()));
	                p.updateInventory();
	                return;
                }

                if (currentItem.getItemMeta().getDisplayName().equals(sumoDuelItem_name)) {                    	
                    	openMenuSelection(p);
                    	return;
                    }
                NBTItemStack nbtItemStack = new NBTItemStack(currentItem);
                if (currentItem.getType() == Material.SKULL_ITEM && nbtItemStack.getString("playername") != null) {
                	  final Player currentPlayer = Bukkit.getPlayerExact(nbtItemStack.getString("playername"));
                	  if (currentPlayer == null) {
                		  p.sendMessage("§cO jogador não se encontra mais online.");
                		  p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 0.5f);
                		  openModerarMenu(p);
                		  return;
                	  }
                	  if (!(getDataInfo(currentPlayer).getDuelando().contains(currentPlayer) || DuelManager.getInstance().getDuelando().contains(currentPlayer))) {
                		  p.sendMessage(" ");
                		  p.sendMessage("§cEste duelo já terminou!");
                		  p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 0.5f);
                		  openModerarMenu(p);
                		  return;
                	  }
                	  if (DuelManager.getInstance().getDuelando().contains(currentPlayer)) {
                		  LocationAPI.getLocation().teleportTo(p, location.MODERAR);
                		  return;
                	  } 											// se n tiver no de cima entao pode rodar o de baixo
                	  int arena = getDataInfo(currentPlayer).getArena();
                	  staffHideSumoduel(p, currentPlayer, sumoManager.duelandoHash.get(currentPlayer));
                	  switch (arena) {
	                	  case 0:
								LocationAPI.getLocation().sumoTp(p, loc_sumo.SUMO_CLASSICA);
								break;
							case 1:
								LocationAPI.getLocation().sumoTp(p, loc_sumo.SUMO_PEQUENA);
								break;
							case 2:
								LocationAPI.getLocation().sumoTp(p, loc_sumo.SUMO_GRANDE);
								break;
							default:
								break;
						}
                }
                if (currentItem.getItemMeta().getDisplayName().equals("§aAvançar")) {
                	 if (SumoDuelManager.getInstance().getManutencaoStatus() == false) {
                    if (!chatEventSumo.contains(p)) {
                        if (InviteManager.getInstance().canInvite()) { 
                        	chatEventSumo.add(p);
			                    p.closeInventory();
			                    p.sendMessage(" ");
			                    p.sendMessage(" §aDigite o nickname do jogador que deseja desafiar no chat.");
			                    p.sendMessage(" §aPara cancelar esta ação digite §7'cancelar' §ano §nchat local§a.");
			                    p.sendMessage(" ");
			                    return;
			                } else {
			                    p.sendMessage("§cJá há 2 jogadores na fila de duelo Sumo. Aguarde que o pedido expire ou seja aceito por um deles para poder enviar novamente.");
			                    return;
			                }
			            } else {
			                p.closeInventory();
			                p.sendMessage("");
			                p.sendMessage(" §cVocê já está tentando desafiar um jogador.");
			                p.sendMessage(" §cCaso queira cancelar esta ação, digite §7'cancelar' §cno §nchat local§a.");
			                p.sendMessage("");
			                return;
			            }
			        } else {
			            p.sendMessage("§cO sistema de duelos está em manutenção. Volte novamente mais tarde!");
			            p.closeInventory();
			            return;
			        }
                }
            }
        }
    }
    
	public void staffHidePvPduel(Player staff, Player player1, Player player2) {
		for (Player duelandoPlayers : DuelManager.getInstance().getDuelando()) {
			staff.hidePlayer(duelandoPlayers);
		}
		staff.showPlayer(player1);
		staff.showPlayer(player2);
	}
	
	public void staffHideSumoduel(Player staff, Player player1, Player player2) {
		SumoDuelManager sumo = SumoDuelManager.getInstance();
		for (Player duelandoPlayers : sumo.getDuelando()) {
			staff.hidePlayer(duelandoPlayers);
		}
		staff.showPlayer(player1);
		staff.showPlayer(player2);
	}
    
    @EventHandler
    public void onSendNickname(AsyncPlayerChatEvent e) {
        InviteManager invite = InviteManager.getInstance();
        SumoInviteManager sumoInvite = SumoInviteManager.getInstance();
        final Player p = e.getPlayer();
        if (chatEventPvP.contains(p)) {
            e.setCancelled(true);
            final Player target = Bukkit.getPlayer(e.getMessage());
            if (e.getMessage().equals("cancelar")) {
                p.sendMessage("§c§lVocê optou por cancelar a ação convite de duelo.");
                chatEventPvP.remove(p);
                return;
            }
            if (DuelManager.getInstance().hasCoin(p)) {
                if (target != null) {
                    if (target != p) {
                        if (!invite.hasInvite(target)) {
                            if (invite.canInvite()) {
                                invite.sendInviteTo(p, target);

                                NumberFormatAPI formatter = new NumberFormatAPI();
                                String custo = formatter.formatNumber(DuelManager.getInstance().getCusto());
                                if (Configs.config.getBoolean("has-duelo-broadcast") == true) {
                                    for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("duelo-broadcast")) {
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
                                                .replace("$convidado", target.getName()))
                                                .replace("$jogador", p.getName())
                                                .replace("$tempo", String.valueOf(invite.getTempo())
                                                        .replace("$valor", custo)));
                                    }
                                }
                                for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("convite-enviado")) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
                                            .replace("$convidado", target.getName()))
                                            .replace("$tempo", String.valueOf(invite.getTempo()))
                                            .replace("$valor", custo));
                                }
                                for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("convite-recebido")) {
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
                                            .replace("$jogador", p.getName()))
                                            .replace("$tempo", String.valueOf(invite.getTempo()))
                                            .replace("$valor", custo));
                                }
                                invite.startTask();
                                chatEventPvP.remove(p);
                                return;
                            } else {
                                p.sendMessage("§cJá há 2 jogadores na fila de duelo. Aguarde que o pedido expire ou seja aceito por um deles para poder enviar novamente.");
                                p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                            }
                        } else {
                            p.sendMessage("§cEste jogador já possui um pedido de duelo pendente.");
                            p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                        }
                    } else {
                        p.sendMessage("§cVocê não pode desafiar a sí mesmo.");
                        p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                    }
                } else {
                    p.sendMessage("§cO jogador especificado não está online.");
                    p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                }
            } else {
                p.sendMessage("§cVocê não tem dinheiro o suficiente para desafiar um jogador.");
                p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
            }
            chatEventPvP.remove(p);
        }
        if (chatEventSumo.contains(p)) {
            e.setCancelled(true);
            final Player target = Bukkit.getPlayer(e.getMessage());
            if (e.getMessage().equals("cancelar")) {
                p.sendMessage("§c§lVocê optou por cancelar a ação convite de duelo.");
                chatEventSumo.remove(p);
                return;
            }
            if (DuelManager.getInstance().hasCoin(p)) {
                if (target != null) {
                    if (target != p) {
                        if (!sumoInvite.hasInvite(target)) {
                            if (sumoInvite.canInvite()) {
                            	sumoInvite.sendInviteTo(p, target);

                                String jogador = p.getName();
                                String convidado = target.getName();
                                String kb = getKbTpe(getDataInfo(p).getKB());
                                String pot = getPotType(getDataInfo(p).getPotLvl()); 
                                String arena = getArenaType(getDataInfo(p).getArena());
                                
                                NumberFormatAPI formatter = new NumberFormatAPI();
                                String custo = formatter.formatNumber(DuelManager.getInstance().getCusto());
                                if (Configs.config.getBoolean("has-sumo-broadcast") == true) {
                                    for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("sumo-duelo-broadcast")) {
                                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg
                                                .replace("$convidado", convidado)
                                                .replace("$jogador", jogador)
                                                .replace("$tempo", "entre 0 e infinito")
                                                .replace("$valor", custo)
                                                .replace("$kb", kb)
                                                .replace("$pot", pot)
                                                .replace("$arena",arena)));
                                    }
                                }
                                for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("convite-enviado-sumo")) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
                                            .replace("$convidado", convidado)
                                            .replace("$tempo", "entre 0 e infinito")
                                            .replace("$valor", custo)
                                            .replace("$kb", kb)
                                            .replace("$pot", pot)
                                            .replace("$arena", arena)));
                                }
                                for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("convite-recebido-sumo")) {
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
                                            .replace("$jogador", jogador)
                                            .replace("$convidado", convidado)
                                            .replace("$tempo", "entre 0 e infinito")
                                            .replace("$valor", custo)
                                            .replace("$kb", kb)
                                            .replace("$pot", pot)
                                            .replace("$arena", arena)));
                                }
                                
                               // invite.startTask();  ADD INVITE DO SUMO
                                chatEventSumo.remove(p);
                                return;
                            } else {
                                p.sendMessage("§cJá há 2 jogadores na fila de duelo. Aguarde que o pedido expire ou seja aceito por um deles para poder enviar novamente.");
                                p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                            }
                        } else {
                            p.sendMessage("§cEste jogador já possui um pedido de duelo pendente.");
                            p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                        }
                    } else {
                        p.sendMessage("§cVocê não pode desafiar a sí mesmo.");
                        p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                    }
                } else {
                    p.sendMessage("§cO jogador especificado não está online.");
                    p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
                }
            } else {
                p.sendMessage("§cVocê não tem dinheiro o suficiente para desafiar um jogador.");
                p.sendMessage("§c§lA ação convite de duelo foi cancelada automaticamente");
            }
            chatEventSumo.remove(p);
        }
    }
    
    public void openModerarMenu(Player p) {
        Inventory inventory = Bukkit.createInventory(null, 4*9, "Visualizando duelos | Moderação");
        SumoDuelManager sumoManager = SumoDuelManager.getInstance();
        DuelManager duel = DuelManager.getInstance();
        Set<Player> addedPlayers = new HashSet<>();
        int slot = 0;
        inventory.setItem(29, getMap(MapType.X1));inventory.setItem(31, getMap(MapType.SUMO_CLASSICA));inventory.setItem(32, getMap(MapType.SUMO_MEDIA));inventory.setItem(33, getMap(MapType.SUMO_GRANDE));
	        if (sumoManager.duelandoHash.isEmpty() && duel.duelandoHash.isEmpty()) {
	            p.openInventory(inventory);
	            inventory.setItem(13, new ItemBuilder(Material.WEB).displayname("§cVazio").lore(new String[] {"§eNenhum duelo ocorrendo."}).build());
	            return;
	        }
	        for (Entry<Player, Player> entry : sumoManager.duelandoHash.entrySet()) {
	            Player displayedPlayer = entry.getKey();
	            Player rival = entry.getValue();
		            if (addedPlayers.contains(displayedPlayer) || addedPlayers.contains(rival)) {
		                continue;
		            }
		            if (slot >= inventory.getSize() || inventory.getItem(slot) != null) {
		                break;
		            }
            addedPlayers.add(displayedPlayer); addedPlayers.add(rival);
            
           final String displayedAccuracy = ChatColor.translateAlternateColorCodes('&', sumoManager.getPercentage(getDataInfo(displayedPlayer).getHits(), getDataInfo(displayedPlayer).getWrongHits())); 
		   final String rivalAccuracy = ChatColor.translateAlternateColorCodes('&', sumoManager.getPercentage(getDataInfo(rival).getHits(), getDataInfo(rival).getWrongHits())); 
            
            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(displayedPlayer.getName());
            meta.setDisplayName("§e" + displayedPlayer.getName());
            meta.setLore(Arrays.asList(new String[]{
                "§7Está enfrentando: §f" + rival.getName(),
                "",
                "§6§lℹ §eDetalhes do duelo:",
                "",
                "    §f◆ Arena §7" + getArenaType(getDataInfo(displayedPlayer).getArena()).toUpperCase(),
                "    §f◆ Efeito §7" + getPotType(getDataInfo(displayedPlayer).getPotLvl()).toUpperCase(),
                "    §f◆ Repulsão §7" + getKbTpe(getDataInfo(displayedPlayer).getKB()).toUpperCase(),
                "",
                "§e⚔ Apuração dos ataques:",
                "  §f" + displayedPlayer.getName() + ": " + displayedAccuracy,
                "  §f" + rival.getName() + ": " + rivalAccuracy,
                "",
                "§eClique para assistir o duelo!"}));
            skull.setItemMeta(meta);
            
    		NBTItemStack nbtItemStack = new NBTItemStack(skull);
    		nbtItemStack.setString("playername", displayedPlayer.getName());
    		
            inventory.setItem(slot, nbtItemStack.getItem());
            slot++;
        } //SUMO CODE
	        for (Entry<Player, Player> entry : duel.duelandoHash.entrySet()) {
	            Player displayedPlayer = entry.getKey();
	            Player rival = entry.getValue();
		            if (addedPlayers.contains(displayedPlayer) || addedPlayers.contains(rival)) {
		                continue;
		            }
		            if (slot >= inventory.getSize() || inventory.getItem(slot) != null) {
		                break;
		            }
            addedPlayers.add(displayedPlayer); addedPlayers.add(rival);
            
            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(displayedPlayer.getName());
            meta.setDisplayName("§e" + displayedPlayer.getName());
            meta.setLore(Arrays.asList(new String[]{
                "§7Está enfrentando: §f" + rival.getName(),
                "",
                "§6§lℹ §eDetalhes do duelo:",
                "",
                "    §f◆ Arena §71V1 PvP",
                "",
                "§eClique para assistir o duelo!"}));
            skull.setItemMeta(meta);
            
    		NBTItemStack nbtItemStack = new NBTItemStack(skull);
    		nbtItemStack.setString("playername", displayedPlayer.getName());
    		
            inventory.setItem(slot, nbtItemStack.getItem());
            slot++;
        }
	    p.playSound(p.getLocation(), Sound.HORSE_SADDLE, 1.0f, 0.5f);
        p.openInventory(inventory);
    }
    

    enum MapType {
    	X1,
        SUMO_CLASSICA, 
        SUMO_MEDIA, 
        SUMO_GRANDE
    }
    
    public ItemStack getMap(MapType mapa) {
    	ItemStack map = null;
    	switch (mapa) {
			case X1:
				map = new ItemBuilder(Material.MAP).glow().displayname("§cArena X1").lore(new String[] {"","§e➢ Ir até"}).build();
				break;
			case SUMO_CLASSICA:
				map = new ItemBuilder(Material.MAP).glow().displayname("§cSumo clássica").lore(new String[] {"","§e➢ Ir até"}).build();
				break;
			case SUMO_MEDIA:
				map = new ItemBuilder(Material.MAP).glow().displayname("§cSumo média").lore(new String[] {"","§e➢ Ir até"}).build();
				break;
			case SUMO_GRANDE:
				map = new ItemBuilder(Material.MAP).glow().displayname("§cSumo grande").lore(new String[] {"","§e➢ Ir até"}).build();
				break;
		default:
			break;
		}
    	ItemMeta itemMeta = map.getItemMeta();
    	itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    	map.setItemMeta(itemMeta);
		return map;
    }
    
   private SumoDuelManager getDataInfo(Player jogador) {
        UUID uuid = jogador.getUniqueId();
        SumoDuelManager jogadorInfo = SumoDuelManager.playerData.get(uuid);
        if (jogadorInfo == null) {
            jogadorInfo = new SumoDuelManager();
            SumoDuelManager.playerData.put(uuid, jogadorInfo);
        }
        return jogadorInfo;
    }
    
    public static ItemStack getSkipItem() {
    	ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) 10);
    	ItemMeta meta = item.getItemMeta();
    	meta.setDisplayName("§aAvançar");
    	meta.setLore(Arrays.asList(new String[] {"§fClique para salvar as configurações"}));
    	item.setItemMeta(meta);
    	return item;
    }
    
    public static ItemStack selectKB() {
    	ItemStack item = new ItemBuilder(Material.GOLD_HELMET).displayname(kbSelection).lore(Arrays.asList(new String[] {
    			"",
    			"  §b➤ §bNenhum §8(Padrão)",
    			" §8● §7Repulsão 1",
    			" §8● §7Repulsão 2",
    			" §8● §7Repulsão 3",
    			"",
    			"§eClique para alterar.",})).build();
    	ItemMeta meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.DURABILITY, 1, true);
   		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
   		item.setItemMeta(meta);
   		return item;
    }
    
    public static ItemStack selectPot() {
    	ItemStack item = new ItemBuilder(Material.FEATHER).displayname("§6Customize os efeitos").lore(Arrays.asList(new String[] {
    			"",
    			"  §b➤ §bNenhum §8(Padrão)",
    			" §8● §7Velocidade 1",
    			" §8● §7Velocidade 2",
    			" §8● §7Velocidade 3",
    			"",
    			"§eClique para alterar.",})).build();
    	ItemMeta meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.DURABILITY, 1, true);
   		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
   		item.setItemMeta(meta);
   		return item;
    }
    
    public static ItemStack selectArena() {
    	ItemStack item = new ItemBuilder(Material.MAP).displayname(arenaSelection).lore(Arrays.asList(new String[] {
    			"",
    			"  §b➤ Arena Clássica §8(Padrão)",
    			" §8● §7Arena Pequena",
    			" §8● §7Arena Grande",
    			"",
    			"§eClique para alterar.",})).build();
    	ItemMeta meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.DURABILITY, 1, true);
   		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
   		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
   		item.setItemMeta(meta);
   		return item;
    }
    
    public void openMenuSelection(Player p) {
    	
    	int kbLevel = getDataInfo(p) == null ? 0 : getDataInfo(p).getKB();
    	int potLvl = getDataInfo(p) == null ? 0 : getDataInfo(p).getPotLvl();
    	int arenaLvl = getDataInfo(p) == null ? 0 : getDataInfo(p).getArena();
    	
    	Inventory inventory = Bukkit.createInventory(null, 3*9, menuSelection);
    	ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)0);
    	for (int i = 0; i < inventory.getSize(); i++) {
    		inventory.setItem(i, vidro);
    	}
    	inventory.setItem(26, getSkipItem());
    	inventory.setItem(11, updateKBselection(kbLevel));
    	inventory.setItem(13, updatePotselection(potLvl));
    	inventory.setItem(15, updateArenaSelection(arenaLvl));
    	p.openInventory(inventory);
    }
    

    @EventHandler
    public void worldChange(PlayerChangedWorldEvent e) {
        final Player p = e.getPlayer();
        SumoDuelManager sumo = SumoDuelManager.getInstance();
        DuelManager duel = DuelManager.getInstance();
        String mundo = Configs.config.getConfig().getString("mundo-do-evento");
        if ((duel.getDuelando().contains(p) || sumo.getDuelando().contains(p)) && 
        		(duel.getManutencaoStatus() == false || sumo.getManutencaoStatus() == false) &&
                	(!p.getWorld().getName().equalsIgnoreCase(mundo))) {
            LocationAPI.getLocation().teleportTo(p, location.POS1);
            p.sendMessage("§cVocê não pode teleportar enquanto estiver duelando.");
        }
    }

   private static ItemStack getSumoItem() {
        String status = "§aLivre.";
        String quantidade = "se pa que sim";
       // if (DuelManager.getInstance().getManutencaoStatus() == true) {
       //     status = "§c§lManutenção";
        //}
    	return new ItemBuilder(Material.LEASH).displayname(sumoDuelItem_name).lore(Arrays.asList(new String[] { 
    			"§fDigite o nome do jogador no chat.", "",
                "  §6" + barrinha + " §fStatus da fila: " + status,
                "  §6" + barrinha + " §fDuelos ocorrendo: §7" + quantidade,
                " ",
                " §fDuelos gratuitos no período de testes!"})).build();
    }
    
    public static void openMenuPrincipal(Player p) throws IOException {
        Inventory inv = Bukkit.createInventory(null, 3 * 9, inventory_name);
        inv.setItem(10, getSkullMenu(p));
        inv.setItem(11, getSumoItem());
        inv.setItem(15, getStorageItem());
        inv.setItem(13, getTopItemStack());
        inv.setItem(14, getCamaroteItemStack());
        p.openInventory(inv);
    }

    private static void openTopMenu(Player p) {
        Inventory inv = Bukkit.createInventory(null, 6 * 9, topmenu_name);
        getTop10(inv);
        inv.setItem(49, getStatisticItem(p.getName()));
        p.openInventory(inv);
    }

    private static void openStorageMenu(Player p) {
        Inventory inv = Bukkit.createInventory(null, 6 * 9, storagemenu_name);
        inv.setItem(49, new ItemBuilder(Material.HOPPER_MINECART).displayname("§aColetar").lore("§7Clique para coletar seus itens.").build());
        if (DuelCache.getCache().hasArmazem(p.getName())) {
            try {
                inv.setContents(InventoryUtils.itemStackArrayFromBase64(DuelCache.getCache().getArmazemFrom(p.getName())));
                inv.setItem(49, new ItemBuilder(Material.STORAGE_MINECART).displayname("§aColetar").lore("§7Clique para coletar seus itens.").build());
            } catch (Exception e) {
                e.printStackTrace();
                p.sendMessage("§cHouve um erro ao consultar seu armazém. Entre em contato com um Administrador!");
                return;
            }
        } else {
            inv.setItem(22, new ItemBuilder(Material.WEB).displayname("§cVazio...").lore("§7Você não possui itens para coletar.").build());
        }
        p.openInventory(inv);
    }

    public static void updateStorageMenu(Player p) {
        int slotsVazio = 0;
        int TotalDeItem = 0;
        ItemStack[] contents = p.getInventory().getContents();
        for (ItemStack item : contents) {
            if (item == null || (item.getType() == Material.AIR)) {
                slotsVazio++;
            }
        }
        if (slotsVazio >= 1) {
            try {
                if (DuelCache.getCache().hasArmazem(p.getName())) {
                    Inventory invStorage = Bukkit.createInventory(null, 6 * 9, p.getName());
                    invStorage.setContents(InventoryUtils.itemStackArrayFromBase64(DuelCache.getCache().getArmazemFrom(p.getName())));

                    for (ItemStack item : invStorage.getContents()) {
                        if (DuelCache.getCache().hasArmazem(p.getName()) && p.getInventory().firstEmpty() != -1) {
                            if (item != null && !(item.getType() == Material.AIR)) {
                                invStorage.remove(item);
                                DuelCache.getCache().setPlayerArmazem(p.getName(), InventoryUtils.toBase64(invStorage));
                                openStorageMenu(p);
                                p.getInventory().addItem(item);
                                p.closeInventory();
                                p.updateInventory();
                                slotsVazio--;
                                TotalDeItem++;

                            }
                        }
                    }
                    if (TotalDeItem == 0) {
                        DuelCache.getCache().setPlayerArmazem(p.getName(), null);
                        p.updateInventory();
                        openStorageMenu(p);
                        p.sendMessage("§cSeu armazém está vázio.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                p.sendMessage("§cHouve um erro ao coletar os itens do seu armazém. Entre em contato com um Administrador!");
            }
        } else {
            p.sendMessage("§cVocê não possui espaço em seu inventário para coletar os itens do seu armazém.");
            p.closeInventory();
            return;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (DuelCache.getCache().hasCache(p.getName())) return;

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
			MysqlMethods.createPlayerData(p.getName());
			DuelCache.getCache().setPlayerArmazem(p.getName(), "");
		});
    }

    public static ItemStack getTop10(Inventory inv) {
        List<String> top = Lists.newArrayList();
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

        int mito = 0;

        for (int stringLine = 0; stringLine < top.size(); stringLine++) {
            mito++;
            String a = top.get(stringLine);
            String[] splitA = a.split(" ");
            //
            String position = splitA[0];
            String playerName = splitA[1];
            String vitorias = splitA[3];
            String derrotas = splitA[5];

            //String prefix = VaultHook.getPlayerPrefix(playerName);

            int vitoriasINT = Integer.parseInt(vitorias.replace("°", " "));
            int derrotasINT = Integer.parseInt(derrotas.replace("°", " "));

            DecimalFormat df = new DecimalFormat("###.##");
            final String KDR = df.format(derrotasINT > 0 ? (double) vitoriasINT / derrotasINT : vitoriasINT);

            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(playerName);
            meta.setDisplayName("§6" + position + " " + playerName);

            meta.setLore(Arrays.asList(new String[]{
                    "",
                    " §e" + barrinha + " §fVitórias: §7" + vitoriasINT,
                    " §e" + barrinha + " §fDerrotas: §7" + derrotasINT,
                    " ",
                    " §e" + barrinha + " §f§lKDR: §7" + KDR,
                    " "}));
            if (mito == 1) {
                List<String> lore = meta.getLore();
                lore.add("   §c⚔ §fEste jogador é o §b§lMVP§f atual!");
                lore.add(" ");
                meta.setLore(lore);
            }
            skull.setItemMeta(meta);

            for (int i = 0; i < inv.getSize(); i++) {
                if (i == 13 || (i >= 21 && i <= 23) || (i >= 29 && i <= 33)) {
                    ItemStack itemSlot = inv.getItem(i);
                    if (itemSlot == null) {
                        inv.setItem(i, skull);
                        break;
                    }
                }
            }
        }
        if (mito <= 9) {
            ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            ItemMeta metaVidro = vidro.getItemMeta();
            metaVidro.setDisplayName("§cVazio...");
            vidro.setItemMeta(metaVidro);
            for (int i = 0; i < inv.getSize(); i++) {
                if (i == 13 || (i >= 21 && i <= 23) || (i >= 29 && i <= 33)) {
                    ItemStack itemSlot = inv.getItem(i);
                    if (itemSlot == null) {
                        inv.setItem(i, vidro);
                    }
                }
            }
        }
        return skull;
    }

    private static ItemStack getStorageItem() {
        ItemStack item = new ItemStack(Material.STORAGE_MINECART);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(storage_name);
        meta.setLore(Arrays.asList(new String[]{
                "§fClique para acessar o seu armazém",
                "§fde itens obtidos durante os duelos.",
                "",
                " §4§l" + barrinha + " §cColete seus itens antes de duelar, ",
                " §4§l" + barrinha + " §ccaso contrário, serão perdidos.",
                " "}));
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getTopItemStack() {
        return new ItemBuilder(Material.NETHER_STAR).displayname(leaderboard_name).lore(Arrays.asList(
                "",
                " §6" + barrinha + " §eConfira os §6§lTOP 10 §emelhores jogadores",
                " §6" + barrinha + " §edos duelos §e§l1v1 §ena arena.",
                "")).build();
    }

    private static ItemStack getStatisticItem(String player) {

        final int vitorias = DuelCache.getCache().getVitoriasFrom(player) == null ? 0 : DuelCache.getCache().getVitoriasFrom(player);
        final int derrotas = DuelCache.getCache().getDerrotasFrom(player) == null ? 0 : DuelCache.getCache().getDerrotasFrom(player);

        DecimalFormat df = new DecimalFormat("###.##");
        final String KDR = df.format(derrotas > 0 ? (double) vitorias / derrotas : vitorias);
        return new ItemBuilder(Material.ARMOR_STAND).displayname("§eSuas estatísticas:").lore(Arrays.asList(
                " ",
                "§c" + barrinha + " §fSuas vitórias:§7 " + vitorias,
                "§c" + barrinha + " §fSuas derrotas:§7 " + derrotas,
                "§c" + barrinha + " §fSeu KDR:§7 " + KDR,
                " ",
                " §7(Aguarde seus dados serem atualizados no top 9)",
                " ")).build();
    }

    public static ItemStack getSkullMenu(Player p) {
        String status = InviteManager.getInstance().canInvite() == true ? "§aLivre." : "§eEm andamento.";
        if (DuelManager.getInstance().getManutencaoStatus() == true) {
            status = "§c§lManutenção";
        }
        final String quantidade = DuelManager.getInstance().getDuelando().size() == 0 ? "§cNenhum." : String.valueOf(DuelManager.getInstance().getDuelando().size() / 2);
        NumberFormatAPI formatter = new NumberFormatAPI();
        String custo = formatter.formatNumber(DuelManager.getInstance().getCusto());
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(p.getName());
        meta.setDisplayName(duelItem_name);
        meta.setLore(Arrays.asList(new String[]{
                "§fDigite o nome do jogador no chat.", "",
                "  §6" + barrinha + " §fStatus da fila: " + status,
                "  §6" + barrinha + " §fDuelos ocorrendo: §7" + quantidade,
                " ",
                " §fEssa ação possui um custo!",
                " §fTaxa de §6⛃ §e" + custo + " §fpara cada oponente."}));
        skull.setItemMeta(meta);
        return skull;
    }
    
    public static ItemStack getCamaroteItemStack() {
        String url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjZGExNjcyYWUxNmE1NzUzOGMxNTgxN2FlYjI4MDBkNTQyMzg3MGRlNGQzMGYxOWNkMjRjMjZkMDZmYjM1NSJ9fX0=";
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        headMeta.setDisplayName(camarote_name);
        headMeta.setLore(Arrays.asList(new String[]{
                " ",
                "§7" + barrinha + " §fClique aqui para acessar ou sair",
                "§7" + barrinha + " §fdo camarote da arena 1v1 quando quiser.",
                " "}));
        head.setItemMeta(headMeta);

        return head;
    }
    
    public ItemStack updateKBselection(int i) {
    	ItemStack item = null;
    	switch (i) {
		case 0:
	    	item = new ItemBuilder(Material.GOLD_HELMET).displayname(kbSelection).lore(Arrays.asList(new String[] {
	    			"",
	    			"   §b➤ §b§nNenhum §8(Padrão)",
	    			" §8● §7Repulsão 1",
	    			" §8● §7Repulsão 2",
	    			" §8● §7Repulsão 3",
	    			"",
	    			"§eClique para alterar.",})).build();
			break;
		case 1:
	    	item = new ItemBuilder(Material.GOLD_HELMET).displayname(kbSelection).lore(Arrays.asList(new String[] {
	    			"",
	    			" §8● §7Nenhum§8 (Padrão)",
	    			"   §b➤ §b§nRepulsão 1",
	    			" §8● §7Repulsão 2",
	    			" §8● §7Repulsão 3",
	    			"",
	    			"§eClique para alterar.",})).build();
			break;
		case 2:
	    	item = new ItemBuilder(Material.GOLD_HELMET).displayname(kbSelection).lore(Arrays.asList(new String[] {
	    			"",
	    			" §8● §7Nenhum §8(Padrão)",
	    			" §8● §7Repulsão 1",
	    			"   §b➤ §b§nRepulsão 2",
	    			" §8● §7Repulsão 3",
	    			"",
	    			"§eClique para alterar.",})).build();
			break;
		case 3:
	    	item = new ItemBuilder(Material.GOLD_HELMET).displayname(kbSelection).lore(Arrays.asList(new String[] {
	    			"",
	    			" §8● §7Nenhum §8(Padrão)",
	    			" §8● §7Repulsão 1",
	    			" §8● §7Repulsão 2",
	    			"   §b➤ §b§nRepulsão 3",
	    			"",
	    			"§eClique para alterar.",})).build();
			break;
		default:
			break;
		}
    	ItemMeta meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.DURABILITY, 1, true);
   		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
   		item.setItemMeta(meta);
		return item;
    }
    
    public ItemStack updatePotselection(int i) {
    	ItemStack item = null;
    	switch (i) {
		case 0:
			 item = new ItemBuilder(Material.FEATHER).displayname("§6Customize os efeitos").lore(Arrays.asList(new String[] {
					"",
					"  §b➤ §nNenhum §8(Padrão)",
					" §8● §7Velocidade 1",
					" §8● §7Velocidade 2",
					" §8● §7Velocidade 3",
					"",
					"§eClique para alterar.",})).build();
			break;
		case 1:
			 item = new ItemBuilder(Material.FEATHER).displayname("§6Customize os efeitos").lore(Arrays.asList(new String[] {
						"",
						" §8● §7Nenhum §8(Padrão)",
						"  §b➤ §nVelocidade 1",
						" §8● §7Velocidade 2",
						" §8● §7Velocidade 3",
						"",
						"§eClique para alterar.",})).build();
			break;
		case 2:
			 item = new ItemBuilder(Material.FEATHER).displayname("§6Customize os efeitos").lore(Arrays.asList(new String[] {
						"",
						" §8● §7Nenhum §8(Padrão)",
						" §8● §7Velocidade 1",
						"  §b➤ §nVelocidade 2",
						" §8● §7Velocidade 3",
						"",
						"§eClique para alterar.",})).build();
			break;
		case 3:
			 item = new ItemBuilder(Material.FEATHER).displayname("§6Customize os efeitos").lore(Arrays.asList(new String[] {
						"",
						" §8● §7Nenhum §8(Padrão)",
						" §8● §7Velocidade 1",
						" §8● §7Velocidade 2",
						"  §b➤ §nVelocidade 3",
						"",
						"§eClique para alterar.",})).build();
			break;
		default:
			break;
		}
    	ItemMeta meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.DURABILITY, 1, true);
   		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
   		item.setItemMeta(meta);
		return item;
    }
    
    public ItemStack updateArenaSelection(int i) {
    	ItemStack item = null;
    	switch (i) {
		case 0:
	    	item = new ItemBuilder(Material.MAP).displayname(arenaSelection).lore(Arrays.asList(new String[] {
	    			"",
	    			"  §b➤ §nArena Clássica§8 (Padrão)",
	    			" §8● §7Arena Pequena",
	    			" §8● §7Arena Grande",
	    			"",
	    			"§eClique para alterar.",})).build();
			break;
		case 1:
	    	item = new ItemBuilder(Material.MAP).displayname(arenaSelection).lore(Arrays.asList(new String[] {
	    			"",
	    			" §8● §7 Arena Clássica §8(Padrão)",
	    			"  §b➤ §nArena Pequena",
	    			" §8● §7Arena Grande",
	    			"",
	    			"§eClique para alterar.",})).build();
			break;
		case 2:
	    	item = new ItemBuilder(Material.MAP).displayname(arenaSelection).lore(Arrays.asList(new String[] {
	    			"",
	    			" §8● §7Arena Clássica §8(Padrão)",
	    			" §8● §7Arena Pequena",
	    			"  §b➤ §nArena Grande",
	    			"",
	    			"§eClique para alterar.",})).build();
			break;
			
		default:
			break;
		}
    	ItemMeta meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.DURABILITY, 1, true);
   		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
   		item.setItemMeta(meta);
		return item;
    }
    
    public String getKbTpe(int getKbHash) {
    	String type = null;
    	switch (getKbHash) {
			case 0:
				type = "Normal";
				break;
			case 1:
				type = "Repulsão 1";
				break;
			case 2:
				type = "Repulsão 2";
				break;
			case 3:
				type = "Repulsão 3";
				break;
			default:
				type = "ERROR: unidentified";
				break;
			}
    	return type;
    }
    
    public String getPotType(int getPotHash) {
    	String type = null;
    	switch (getPotHash) {
			case 0:
				type = "Nenhum";
				break;
			case 1:
				type = "Velocidade 1";
				break;
			case 2:
				type = "Velocidade 2";
				break;
			case 3:
				type = "Velocidade 3";
				break;
			default:
				type = "ERROR: unidentified";
				break;
			}
    	return type;
    }
    
    public String getArenaType(int getArenaHash) {
    	String type = null;
    	switch (getArenaHash) {
			case 0:
				type = "Clássica";
				break;
			case 1:
				type = "Pequena";
				break;
			case 2:
				type = "Grande";
				break;
			default:
				type = "ERROR: unidentified";
				break;
			}
    	return type;
    }
    
   /* public static ItemStack getArrowUP() {
        String url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjFkNGZkZDA5MTg0MGQ5ZTdkZjA2MDE2ODFhZGRlYzYwNTE0ODVhNDg0YmE3ZjUzNmIzNWQ0ZTA1YWE4NmVmOSJ9fX0=";
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        headMeta.setDisplayName(getArrowUP);
        headMeta.setLore(Arrays.asList(new String[]{
                " ",
        		" §6§lAMPLIFICAR §3⬆",
        		" "}));
        head.setItemMeta(headMeta);
        return head;
    }
    
    public static ItemStack getArrowDown() {
        String url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFiODg2NWQ5OTViNjA5YjNmZmVlMGEzZWQyZTY1NDQwZTM1NDQwZDJjNjU1MTRmZWRiMmM1YjJjNjg3Zjg1ZCJ9fX0";
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        headMeta.setDisplayName(getArrowDown);
        headMeta.setLore(Arrays.asList(new String[]{
                " ",
        		" §6§lREDUZIR §3⬇",
        		" "}));
        head.setItemMeta(headMeta);
        return head;
    }
    */
    

    public static String formatar(int n) {
        DecimalFormat df = new DecimalFormat("#.##");
        String format = df.format(n);
        return format;
    }
}
